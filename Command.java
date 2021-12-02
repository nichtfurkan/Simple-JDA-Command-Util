package de.furkan.bot.commands;

import de.furkan.bot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.internal.utils.PermissionUtil;

public class Command {

    //Configuration
    private final String noPermissions = "Sorry but you don't have Permissions for that!";
    private final String notEnoughArgs = "Please use %COMMAND% **%ARGS%**";
    private final String tooManyArgs = "Please use only %COMMAND%";
    private final String prefix = "!"; //Replace this with your Prefix in your Main or Utility class


    //Booleans
    private final boolean admin, ignoreSubCommands, ignoreCase;

    //Ints
    private final int neededSubCommandsSize;

    //Strings
    private String rawCommand,onlySubCommands,onlyCommand,command;
    private final String neededSubCommands,rightCommand;
    private String[] commandArray;

    //Others
    private Member member;
    private TextChannel textChannel;
    private final Main instance = Main.getInstance();


    public Command(String command, int neededSubCommandsSize, boolean ignoreSubCommands, boolean ignoreCase, boolean admin, String neededSubCommands) {
        //Inits general stuff
        this.admin = admin;
        this.neededSubCommands = neededSubCommands;
        this.ignoreSubCommands = ignoreSubCommands;
        this.neededSubCommandsSize = neededSubCommandsSize;
        this.ignoreCase = ignoreCase;
        this.rightCommand = prefix + command;
    }

    //Getter
    public String getOnlyCommand() {
        return onlyCommand;
    }

    public String getRawCommand() {
        return rawCommand;
    }

    public String getOnlySubCommands() {
        return onlySubCommands;
    }

    public String[] getCommandArray() {
        return commandArray;
    }

    public TextChannel getTextChannel() {
        return textChannel;
    }

    public Main getInstance() {
        return instance;
    }

    public String getRightCommand() {
        return rightCommand;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }


    //Checks integrity if command is typed in right form and with enough args.
    public boolean checkIntegrity() {
        //Checks permissions
        if (this.admin && !PermissionUtil.checkPermission(this.member, Permission.ADMINISTRATOR)) {
            this.sendNoPermission();
            return false;
        }

        //Checks subCommands
        if (!this.ignoreSubCommands && this.neededSubCommandsSize != this.commandArray.length) {
            if(this.neededSubCommandsSize > this.commandArray.length) {
                this.sendMissingSubCommands(this.neededSubCommands);
            } else {
                this.sendTooMuchSubCommands();
            }

            return false;
        }
        return true;
    }

    //Inits a few things and generates multiple command variants for better development.
    public void processCommand(String command, Member member, TextChannel channel) {
        this.command = command;
        this.member = member;
        this.textChannel = channel;

        //Create command variants
        this.rawCommand = this.command;
        this.onlyCommand = this.rawCommand.contains(" ") ? this.rawCommand.split(" ")[0] : this.rawCommand;
        this.onlySubCommands = this.rawCommand.contains(" ") ? this.rawCommand.replaceAll(this.onlyCommand + " ", "") : null;
        if (this.onlySubCommands != null) {
            if (this.onlySubCommands.contains(" ")) {
                this.commandArray = this.onlySubCommands.split(" ");
            } else {
                this.commandArray = new String[]{this.onlySubCommands};
            }
        } else {
            this.commandArray = new String[]{};
        }



    }

    private void sendNoPermission() {
        this.textChannel.sendMessage(this.noPermissions).queue();
    }

    private void sendTooMuchSubCommands() {
        String args = this.tooManyArgs.replaceAll("%COMMAND%", this.onlyCommand);
        this.textChannel.sendMessage(args).queue();
    }

    private void sendMissingSubCommands(String subCommands) {
        String args = this.notEnoughArgs.replaceAll("%COMMAND%", this.onlyCommand).replaceAll("%ARGS%",subCommands);
        System.out.println(this.notEnoughArgs.replaceAll("%COMMAND%", this.onlyCommand).replaceAll("%ARGS%",subCommands));
        this.textChannel.sendMessage(args).queue();
    }


    //For better development
    public void sendMessageToChannel(String message) {
        this.textChannel.sendMessage(message).queue();
    }

    public void sendEmbedToChannel(EmbedBuilder builder) {
        this.textChannel.sendMessage(builder.build()).queue();
    }

}
