package pl.wilanowskiartur.chat.models.sockets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import pl.wilanowskiartur.chat.models.LogModel;
import pl.wilanowskiartur.chat.models.MessageModel;
import pl.wilanowskiartur.chat.models.UserModel;
import pl.wilanowskiartur.chat.models.commands.CommandFactory;
import pl.wilanowskiartur.chat.models.repositories.LogRepository;
import sun.rmi.runtime.Log;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Configuration
@EnableWebSocket
public class ChatSocket extends TextWebSocketHandler implements WebSocketConfigurer{

    private List<UserModel> userList;
    private CommandFactory commandFactory;
    public static final Gson GSON = new GsonBuilder().create();

    private LogRepository logRepository;

    @Autowired
    public ChatSocket(LogRepository logRepository){
        userList = new ArrayList<>();
        commandFactory = new CommandFactory(userList);
        this.logRepository = logRepository;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(this, "/chat").setAllowedOrigins("*");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        UserModel sender = findUserModel(session);

        MessageModel messageModel = GSON.fromJson(message.getPayload(), MessageModel.class);

        switch (messageModel.getMessageType()){
            case MESSAGE: {
                parseMessagePacket(sender, messageModel);
                break;
            }

        }
    }

    private void parseMessagePacket(UserModel sender, MessageModel messageModel) {
        if (sender.getNickname() == null){
            if (checkBusyNick(sender, messageModel)) return;
            if (checkNickByRegex(sender, messageModel)) return;

            sender.setNickname(messageModel.getContext());
            sender.sendMessagePacket("Ustawiono Twój nick na: " + messageModel.getContext());
            sendMessageToAllWithoutMe(sender,"Użytkownik " + messageModel.getContext() + " dołączył");
            return;
        }
        if (commandFactory.parseCommand(sender, messageModel.getContext() )) {
            return;
        }
        sendMessageToAll(generatePrefix(sender) + messageModel.getContext());

        saveLogToDatabase(sender, messageModel.getContext());

        sender.addGlobalMessage();
    }

    @Async
    void saveLogToDatabase(UserModel sender, String context) {
        LogModel logModel = new LogModel();
        logModel.setMessage(context);
        logModel.setSender(sender.getNickname());
        logModel.setDate(LocalDateTime.now());

        logRepository.save(logModel);
    }


    private boolean checkBusyNick(UserModel sender, MessageModel messageModel) {
        for (UserModel userModel : userList){
            if (userModel.getNickname() != null && userModel.getNickname().equals(messageModel.getContext())){
                sender.sendMessagePacket("~~~ Nick jest zajęty, spróbuj innego!");
                return true;
            }
        }
        return false;
    }

    private boolean checkNickByRegex(UserModel sender, MessageModel messageModel) {
        if (!Pattern.matches("\\w{4,20}", messageModel.getContext())){
            sender.sendMessagePacket("Nick nie spełnia wymogów. Same litery i cyfry min 4 znaki!");
            return true;
        }
        return false;
    }


    private void sendMessageToAllWithoutMe(UserModel sender, String s) {
        userList.stream()
                .filter(user -> !user.equals(sender))
                .forEach(user -> user.sendMessagePacket(s));
    }

    private String generatePrefix(UserModel userModel) {
        return "<" + getTime() + ">" + " " + userModel.getNickname() +": ";
    }

    private String getTime() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    private UserModel findUserModel(WebSocketSession session) {
        return userList.stream()
                .filter(s -> s.getSession().equals(session))
                .findAny()
                .get();
    }

    private void sendMessageToAll(String message) {
        userList.forEach(s -> s.sendMessagePacket(message));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        UserModel sender = new UserModel(session);
        userList.add(new UserModel(session));

        sender.sendMessagePacket("Witaj w komunikatorze!");
        sender.sendMessagePacket("Twoja pierwsza wiadomość będzie Twoim nickiem!");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        UserModel userModel = findUserModel(session);
        if (userModel.getNickname() != null) {
            sendMessageToAllWithoutMe(userModel, "Użytkownik " + userModel.getNickname() + " opuścił chat!");
            }
        userList.remove(findUserModel(session));
    }
}
