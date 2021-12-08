package commandHandler.commands.oneStepCommands;

import common.User;
import commandHandler.commands.AbstractCommand;
import commandHandler.commands.Executable;
import commandHandler.utils.CollectionManager;
import common.Response;

/**
 * Removes the element from collections by its key
 * **/
public class RemoveKeyCommand extends AbstractCommand {
    private CollectionManager collectionManager;
    public RemoveKeyCommand(CollectionManager collectionManager){
        super("remove_key","remove the element from collection by its key");
        this.collectionManager = collectionManager;
    }
    /**
     * @see Executable
     * @param key that will be used to perform the remove
     * **/
    @Override
    public Response execute(String key, User user) {
        try{
            if(key.isEmpty()) {
                return new Response("Key is empty", true);
            }
            Integer resultKey = Integer.parseInt(key);
            if (collectionManager.getSize() <= 0){
                return new Response("Collection is already empty", true);
            }
            if (!collectionManager.contains(resultKey)){
                return new Response("Ticket with such key doesn't exist",true);
            }
            if (!collectionManager.read(resultKey).getCreator().equals(user.getLogin())){
                return new Response("You don't have a permission to remove this", true);
            }
            collectionManager.delete(resultKey);
            return new Response();
        } catch (NumberFormatException e){
            return new Response("Key must be a number", true);
        }

    }
}