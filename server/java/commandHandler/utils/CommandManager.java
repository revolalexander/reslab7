package commandHandler.utils;

import common.User;
import commandHandler.commands.AbstractCommand;
import commandHandler.commands.Executable;
import common.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Stores and manages commands
 * **/
public class CommandManager {
    private Map<String, Executable> commands = new HashMap<String,Executable>();
    private Stack<String> scriptStack = new Stack<>();

    public CommandManager(Executable... commands){
        for (Executable c : commands){
            this.commands.put(c.getName(), c);
        }
    }
    /**
     * Add new command to command manager
     * @param command Executable command
     * @see Executable
     * **/
    public void addCommand(Executable command){
        commands.put(command.getName(),command);
    }
    /**
     * Checks if command manager contains command with specified name
     * @return true if command manager contains command with specified name, else returns false
     * @param commandName name of command
     * **/
    public boolean contains(String commandName){
        return commands.containsKey(commandName);
    }
    /**
     * Executes the command with specified name
     * @param command name of command
     * @param key argument for command
     * @return exit status of executable command
     * **/
    public Response executeCommand(String command, String key, User user){
        if(contains(command)){
            return commands.get(command).execute(key.trim(), user);
        } else{
            return new Response("Command " + command + " doesn't exist", true);
        }
    }
    /**
     * @return all commands
     * **/
    public AbstractCommand[] getCommands(){
        return this.commands.values().toArray(new AbstractCommand[this.commands.values().size()]);
    }

}