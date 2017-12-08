package pl.wilanowskiartur.chat.models.commands;

import pl.wilanowskiartur.chat.models.UserModel;

import java.util.List;

public class UserCommand implements Command {
    @Override
    public void parseCommand(UserModel model, List<UserModel> userList, String... args) {
        userList.forEach(s -> model.sendMessagePacket("~ " + s.getNickname()));
    }

    @Override
    public int argsCount() {
        return 0;
    }

    @Override
    public String error() {
        return "/user";
    }
}
