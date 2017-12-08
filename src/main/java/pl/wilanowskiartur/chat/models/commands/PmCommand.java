package pl.wilanowskiartur.chat.models.commands;

import pl.wilanowskiartur.chat.models.UserModel;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PmCommand implements Command {


    @Override
    public void parseCommand(UserModel sender, List<UserModel> userList, String... args) {
        Optional<UserModel> toWho = userList.stream()
                .filter(s -> s.getNickname().equals(args[0]))
                .findAny();

        args[0] = "";

        if (toWho.isPresent()){
            toWho.get().sendMessage("---> (" + sender.getNickname() + "): "
                    + Arrays.stream(args).collect(Collectors.joining(" ")));

            sender.addPrivateMessage();
        }else {
            sender.sendMessage("Taki user nie istnieje!");
        }
    }

    @Override
    public int argsCount() {
        return -1;
    }

    @Override
    public String error() {
        return "/pm <nick> <wiadomosc jakas tutaj>";
    }
}
