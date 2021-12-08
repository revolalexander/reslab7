package commandHandler.commands.oneStepCommands;

import common.User;
import commandHandler.commands.AbstractCommand;
import commandHandler.commands.Executable;
import commandHandler.utils.CommandManager;
import commandHandler.utils.Logger;
import common.Response;

/**
 * Prints the information about available commands that command manager contains
 * **/
public class HelpCommand extends AbstractCommand {

    private CommandManager commandManager;
    public HelpCommand(){
        super("help","prints the information about available commands");
    }
    /**
     * @see Executable
     * **/
    public Response execute(String arg, User user){
        if(commandManager == null){
            Logger.error("CommandManager isn't set in HelpCommand");
            return new Response("CommandManager isn't set",true);
        }
        AbstractCommand[] commands = commandManager.getCommands();
        String content = "";
        for (AbstractCommand command : commands){
            if (command.getName().equals("sign_up") || command.getName().equals("sign_in")){
                continue;
            }
            content += command.getName() + " - " + command.getDescription() + "\n";
        }
        return new Response(content,false);
    }

    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }
}