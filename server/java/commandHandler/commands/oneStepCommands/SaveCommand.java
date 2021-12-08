
package commandHandler.commands.oneStepCommands;

import common.User;
import commandHandler.commands.AbstractCommand;
import commandHandler.commands.Executable;
import common.Response;

/**
 * Saves the collection to file
 * **/
public class SaveCommand extends AbstractCommand {
    public SaveCommand(){
        super("save","save collection");
    }
    /**
     * @see Executable
     * **/
    public Response execute(String arg, User user){
        return new Response();
    }
}