# Simple-JDA-Command-Util

[![CodeFactor](https://www.codefactor.io/repository/github/nichtfurkan/simple-jda-command-util/badge)](https://www.codefactor.io/repository/github/nichtfurkan/simple-jda-command-util)
A simple way to create Discord Bot Commands in Java

To understand the code you need to be familiar with the **JDA** Library.
Its good if you have already created some Bots.

JDA - https://github.com/DV8FromTheWorld/JDA

*Beware that my English is terrible*

# Creating a command
To create a command you need to create a Class which extends the Command.java class:

```Java

public class HelpCommand extends Command {

    public HelpCommand(String command, int neededSubCommandsSize, boolean ignoreSubCommands, boolean ignoreCase, boolean admin, String neededSubCommands) {
        super(command, neededSubCommandsSize, ignoreSubCommands, ignoreCase, admin, neededSubCommands);
    }
    
}

```
After the constructor has been created you need to fill some informations about your command.</br>
Delete all Parameters from the Constructor and refill everything in the super method like: 

```Java

public class HelpCommand extends Command {

    public HelpCommand() {
        //Command, neededSubCommandsSize, ignoreSubCommands, ignoreCase, admin, neededSubCommands
        super("help", 0, false, true, false, "");
    }
    
}

```
Let me explain what these Parameters are:
* **Command** - The command **WITHOUT** the Prefix
* **neededSubCommandsSize** - The number of sub commands (!Help SUBCOMMAND_1 SUBCOMMAND_2) that will be needed for this Command.
If there are too many or not enoug sub commands it will send a Message that you can edit. Check **Configuration**.

* **ignoreSubCommands** - A boolean that if true will ignore the sub commands totally.
* **ignoreCase** - A boolean that if ture will ignore the case sensitive in the Command.
* **admin** - A boolean that if true will send a Message to thoes who dont have Administration rights on the Discord Server. Check **Configuration**
* **neededSubCommands** - A String where you give the needed sub commands. Example: "USERNAME, ID", "(USERNAME, ID)" etc. This is needed for the Message that will be send if there are not enough sub commands

After you've set the Parameters you can now create he **processCommand** metho

```Java

public class HelpCommand extends Command {

    public HelpCommand() {
        //Command, neededSubCommandsSize, ignoreSubCommands, ignoreCase, admin, neededSubCommands
        super("help", 0, false, true, false, "");
    }
    
    @Override
    public void processCommand(String command, Member member, TextChannel channel) {
        super.processCommand(command, member, channel);
    }
    
}

```
In this method you can write your Code but before doing that we need one more thing.</br>
### **DONT DELETE THE SUPER METHOD IT INITS THINGS**</br>
We need a check that the Command has been typed successfully and with right sub commands.</br>
We need:

```Java
    
    @Override
    public void processCommand(String command, Member member, TextChannel channel) {
       super.processCommand(command, member, channel);
       if(!this.checkIntegrity()) {
            return;
        }
    }

```

The **checkIntegrity** method checks if the Command has enoug sub commands and checks the Permissions.

## Registering the Command
Now we need to register the Command. The best way to do this is to create a list in your Main Class and add the command in the list.</br>
**Also dont forget to make the list accessible for every class**


```Java
    
public class Main {

    private static final ArrayList<Command> commands = new ArrayList<>();

    public static void main(String[] args) {
        //Register commands
        commands.add(new HelpCommand());
    }
    
    //Getter
    public static ArrayList<Command> getCommands() {
        return commands;
    }

}

```

Here i am adding the Command to a list the is accessbile through the **getCommands()** method.</br>
After we have put our Commands in a list we need to create a Listener.</br>

## Creating the Listener
To create the listener we can create a new Class or put the Listener in the Main class.</br>
In my case i will create a new **Listener** Class for this example.

```Java

public class Listener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

    }
}
```
In this class we extend the ListenerAdapter that comes with **JDA**</br>
after extending we create a new method called **onGuildMessageReceived** that also comes from **JDA**</br>
Now we need to give the commands a "trigger point"</br>


```Java

@Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        for (Command command : Main.getCommands()) {

            String rightCommand = command.isIgnoreCase() ? command.getRightCommand().toLowerCase() : command.getRightCommand();

            String possibleCommand = command.isIgnoreCase() ? event.getMessage().getContentRaw().toLowerCase() : event.getMessage().getContentRaw();

            if (possibleCommand.startsWith(rightCommand)) {
              command.processCommand(possibleCommand, event.getMember(), event.getChannel());
            }
        }
    }
    
```
In the **onGuildMessageReceived** method we create a for loop that loops through the command list that we've created earlier. </br>
In the loop we are creating two strings **rightCommand** and **possibleCommand**</br>
where **rightCommand** is the first Parameter in our **HelpCommand** class</br>
**possibleCommand** is the Message that the user typed after the event has been fired.</br>
Now we are checking if the **ignoreCase** boolean is true and if it is we **.lowerCase()** both Strings so they match.</br>
After the String has been created we will check if the **possibleCommand** is starting with **rightCommand** and if it is we call the **.processCommand()** method from the element that is being looped.

All we need is to register the Listener with **JDA** in our Main class.


```Java

    public static void main(String[] args) {
        jda.addEventListener(new Listener());
    }
    
``` 

And we are done. Now you can create your Command and register it!

# Usefull methods
For faster development i've created some getters that can be usefull in the **.processCommand()** method.

For example we say that the user typed "!help Test1 Test2"
* **getOnlyCommand** - Gets just the command in our case its "!help"
* **getRawCommand** - Gets the full command in our case its "!help Test1 Test2"
* **getOnlySubCommands** - Gets only the sub commands in our case its "Test1 Test2"
* **getCommandArray** - Gets an array that splits spaces of the **rawCommand**.
* **getTextChannel** - Gets the Text Channel where the user typed the command in.

# Configuration
At the top of the **Command** class you will see

```Java

    //Configuration
    private final String noPermissions = "Sorry but you don't have Permissions for that!";
    private final String notEnoughArgs = "Please use %COMMAND% **%ARGS%**";
    private final String tooManyArgs = "Please use only %COMMAND%";
    private final String prefix = "!"; //Replace this with your Prefix in your Main or Utility class
    
``` 
Here you can specify the Messages that will be popping up if theres something wrong.</br>
**PLEASE MAKE SURE TO EDIT THE PREFIX TO YOURS WITHOUT IT EVERYTHING WILL EXPLODE**


