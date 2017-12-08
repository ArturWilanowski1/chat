package pl.wilanowskiartur.chat.models.commands;

import org.apache.catalina.User;
import pl.wilanowskiartur.chat.models.UserModel;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class KickCommand implements Command {

    @Override
    public void parseCommand(UserModel sender, List<UserModel> userModelList, String ... args){
        String nickToKick = args[0];
        Optional<UserModel> userModel = userModelList.stream()
                .filter(s -> s.getNickname().equals(nickToKick))
                .findAny();

        if (userModel.isPresent()){
            try {
                userModel.get().getSession().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            sender.sendMessage("Taki user nie istnieje!");
        }
    }

    @Override
    public int argsCount() {
        return 1;
    }

    @Override
    public String error() {
        return "Użycie komendy to: /kick tutaj_nick";
    }




}
