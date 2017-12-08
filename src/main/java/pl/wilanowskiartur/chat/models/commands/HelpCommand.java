package pl.wilanowskiartur.chat.models.commands;

import pl.wilanowskiartur.chat.models.UserModel;

import java.util.List;
import java.util.Map;

public class HelpCommand implements Command{


    @Override
    public void parseCommand(UserModel model, List<UserModel> userList, String... args) {
        model.sendMessage("Lista komend: ");
        for (Map.Entry<String, Command> stringCommandEntry : CommandFactory.getCommandMap().entrySet()) {
            model.sendMessage("--" + stringCommandEntry.getKey() + "-" + stringCommandEntry.getValue().error());
        }
    }

    @Override
    public int argsCount() {
        return 0;
    }

    @Override
    public String error() {
        return "/help";
    }
}
