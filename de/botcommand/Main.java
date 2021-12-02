package de.botcommand;

import de.botcommand.commands.Help;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;

public class Main {

    private static final ArrayList<Command> commands = new ArrayList<>();
    private static JDA jda;
    private static Main instance;
    private final String prefix = "!";

    public static Main getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        instance = new Main();

        //Register commands
        registerCommand(new Help());

        try {
            jda = JDABuilder.createDefault("no u").build();
            jda.getPresence().setPresence(OnlineStatus.DO_NOT_DISTURB, Activity.playing("test"));
            jda.addEventListener(new Listener());
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }


    private static void registerCommand(Command command) {
        commands.add(command);
    }

    //Getter
    public String getPrefix() {
        return prefix;
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }

}
