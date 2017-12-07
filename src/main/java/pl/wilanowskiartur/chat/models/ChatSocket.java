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
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSocket
public class ChatSocket extends TextWebSocketHandler implements WebSocketConfigurer{

    List<UserModel> userList;

    public ChatSocket(){
        userList = new ArrayList<>();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(this, "/chat").setAllowedOrigins("*");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        sendMessageToAll(message.getPayload() + "\n");
    }

    private void sendMessageToAll(String message) {
        userList.forEach(s -> s.sendMessage(message));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        userList.add(new UserModel(session));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        userList.remove(userList.stream()
                .filter(s -> s.getSession().equals(session))
                .findAny()
                .get());
    }
}
