
package commandHandler.commands.oneStepCommands;

import common.User;
import commandHandler.commands.AbstractCommand;
import commandHandler.commands.Executable;
import commandHandler.utils.CollectionManager;
import common.Response;

/**
 * Prints all elements in collection
 * **/
public class ShowCommand extends AbstractCommand {
    private CollectionManager collectionManager;
    private String content = "";
    public ShowCommand(CollectionManager collectionManager){
        super("show","prints all elements");
        this.collectionManager = collectionManager;
    }

    /**
     * @see Executable
     * **/
    @Override
    public Response execute(String arg, User user) {
        if (collectionManager.getSize() == 0){
            return new Response("Collection is empty", false);
        }
        collectionManager.getStream()
                .forEach(ticket -> content+=ticket.toString());
        String content = this.content;
        this.content = "";
        return new Response(content,false);
    }
}