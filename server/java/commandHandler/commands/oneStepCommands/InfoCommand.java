package commandHandler.commands.oneStepCommands;

import common.User;
import commandHandler.commands.AbstractCommand;
import commandHandler.commands.Executable;
import commandHandler.utils.CollectionManager;
import common.Response;

/**
 * Prints the information about the collection
 * **/
public class InfoCommand extends AbstractCommand {
    private CollectionManager collectionManager;
    public InfoCommand(CollectionManager collectionManager){
        super("info", "prints the information about the collection");
        this.collectionManager = collectionManager;
    }
    /**
     * @see Executable
     * **/
    @Override
    public Response execute(String arg, User user) {
        String content =
        "\nInformation about the collection: " +
        "\nType: " + collectionManager.getCollectionClass() +
        "\nCreated: " + collectionManager.getCreationDate() +
        "\nNumber of elements: " + collectionManager.getSize();
        return new Response(content,false);
    }
}