package pl.wilanowskiartur.chat.models.commands;

import pl.wilanowskiartur.chat.models.UserModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandFactory {

    private static Map<String, Command> stringCommandMap;

    static {
        stringCommandMap = new HashMap<>();
        stringCommandMap.put("kick", new KickCommand());
        stringCommandMap.put("pm", new PmCommand());
    }

    private List<UserModel> userList;

    public CommandFactory(List<UserModel> list){
        userList = list;
    }

    public boolean parseCommand(UserModel userModel, String s){
        if (!s.startsWith("/")){
            return false;
        }
        String[] parts = s.split(" ");
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);
        String commandAlone = parts[0].substring(1, parts[0].length());
        if (!stringCommandMap.containsKey(commandAlone)){
            userModel.sendMessage("Taka komenda nie istnieje!");
            return true;
        }

        Command command = stringCommandMap.get(commandAlone);
        if (command.argsCount() != -1){
            if (command.argsCount() != args.length){
                userModel.sendMessage(command.error());
                return true;
            }
        }

        command.parseCommand(userModel, userList, args);
        return true;

    }
}
