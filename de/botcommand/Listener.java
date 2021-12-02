package de.botcommand;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Listener extends ListenerAdapter {

    private final Main instance = Main.getInstance();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        for (Command command : this.instance.getCommands()) {
            String rightCommand = command.isIgnoreCase() ? command.getRightCommand().toLowerCase() : command.getRightCommand();
            String possibleCommand = command.isIgnoreCase() ? event.getMessage().getContentRaw().toLowerCase() : event.getMessage().getContentRaw();
            if (possibleCommand.startsWith(rightCommand)) {
                command.processCommand(possibleCommand, event.getMember(), event.getChannel());
            }
        }
    }
}
