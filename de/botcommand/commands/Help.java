package de.botcommand.commands;

import de.botcommand.Command;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class Help extends Command {

    public Help() {
        super("help", 0, false, true, false, "");
    }

    @Override
    public void processCommand(String command, Member member, TextChannel channel) {
        super.processCommand(command, member, channel);
        if (!this.checkIntegrity()) {
            return;
        }
        this.sendMessageToChannel("Help is here");
    }
}
