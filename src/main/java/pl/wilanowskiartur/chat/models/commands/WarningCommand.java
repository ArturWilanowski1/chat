package pl.wilanowskiartur.chat.models.commands;

import pl.wilanowskiartur.chat.models.UserModel;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class WarningCommand implements Command{
    @Override
    public void parseCommand(UserModel model, List<UserModel> userModelList, String... args) {

        String tempString = "";
        if(!args[0].isEmpty()) tempString = args[0];
        String nick = tempString;

        Optional<UserModel> userModel = userModelList.stream()
                .filter(s -> s.getNickname().equals(nick))
                .findAny();

        if(userModel.isPresent()){
            userModel.get().sendMessagePacket("Otrzymałeś ostrzeżenie od "+model.getNickname()+" \n");
        }
    }

    @Override
    public int argsCount() {
        return 1;
    }

    @Override
    public String error() {
        return "Użycie komendy to: /warning nickname";
    }
}