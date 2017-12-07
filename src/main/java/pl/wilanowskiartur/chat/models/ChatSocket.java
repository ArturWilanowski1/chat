package pl.wilanowskiartur.chat.models;

import org.apache.catalina.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSocket
public class ChatSocket extends TextWebSocketHandler implements WebSocketConfigurer{

    private List<UserModel> userList;

    public ChatSocket(){
        userList = new ArrayList<>();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(this, "/chat").setAllowedOrigins("*");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        UserModel sender = findUserModel(session);
        if (sender.getNickname() == null){
            sender.setNickname(message.getPayload());
            System.out.println("Ustawiono Twój nick na: " + message.getPayload());
            return;
        }


        sendMessageToAll(generatePrefix(sender) + message.getPayload());
    }

    private String generatePrefix(UserModel userModel) {
        return "<" + getTime() + ">" + " " + userModel.getNickname() +": ";
    }

    private String getTime() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss"));
    }

    private UserModel findUserModel(WebSocketSession session) {
        return userList.stream()
                .filter(s -> s.getSession().equals(session))
                .findAny()
                .get();
    }

    private void sendMessageToAll(String message) {
        userList.forEach(s -> s.sendMessage(message));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        UserModel sender = new UserModel(session);
        userList.add(new UserModel(session));

        sender.sendMessage("Witaj w komunikatorze!");
        sender.sendMessage("Twoja pierwsza wiadomość będzie Twoim nickiem!");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        userList.remove(findUserModel(session));
    }
}
