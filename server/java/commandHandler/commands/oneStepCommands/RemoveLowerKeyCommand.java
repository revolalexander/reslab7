package commandHandler.commands.oneStepCommands;

import common.User;
import commandHandler.commands.AbstractCommand;
import commandHandler.commands.Executable;
import domainObjectModel.Ticket;
import commandHandler.utils.CollectionManager;
import common.Response;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Removes all elements that are more than specified one
 * **/
public class RemoveLowerKeyCommand extends AbstractCommand {
    private CollectionManager collectionManager;
    public RemoveLowerKeyCommand(CollectionManager collectionManager){
        super("remove_lower_key","removes all elements that have key less than specified");
        this.collectionManager = collectionManager;
    }
    /**
     * @param arg that will be used to perform the remove
     * @see Executable
     * **/
    @Override
    public Response execute(String arg, User user) {
        if(collectionManager.getSize() == 0) {
            return new Response("Collection is empty", true);
        }
        Integer key;
        try{
            key = Integer.parseInt(arg);
        } catch (NumberFormatException e){
            return new Response("Key must be a number", true);
        }
        collectionManager.removeIf(ticket -> ticket.getId() < key && ticket.getCreator().equals(user.getLogin()));
        return new Response();
    }
}