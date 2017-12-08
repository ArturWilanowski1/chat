package pl.wilanowskiartur.chat.models.commands;

import pl.wilanowskiartur.chat.models.UserModel;

import java.util.List;

public interface Command {

    public void parseCommand(UserModel model, List<UserModel> userList, String ... args);
    int argsCount();
    String error();



}
