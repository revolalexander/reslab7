
package commandHandler.commands.twoStepCommands;


import common.Command;
import common.User;
import commandHandler.commands.AbstractCommand;
import commandHandler.commands.Executable;
import domainObjectModel.Ticket;
import com.google.gson.Gson;
import commandHandler.utils.CollectionManager;
import common.Instruction;
import common.Response;

import java.util.Map;
import java.util.TreeMap;

/**
 * Replaces one element with another if first element is less than specified
 * **/
public class ReplaceIfLowerCommand extends AbstractCommand {
    private CollectionManager collectionManager;
    private Map<String, Integer> keys;
    public ReplaceIfLowerCommand(CollectionManager collectionManager){
        super("replace_if_lower","replace element by key if new element is less than old one");
        this.collectionManager = collectionManager;
        this.keys = new TreeMap<>();
    }
    /**
     * @see Executable
     * @param arg that will be used to perform the remove
     * **/

    @Override
    public Response execute(String arg, User user) {
        if (collectionManager.getSize() == 0){
            return new Response("Collection is empty", true);
        }
        if (arg == null || arg.isEmpty()){
            return new Response("Key is empty or null", true);
        }
        if (!this.keys.containsKey(user.getLogin())){
            try{
                Integer ticketId = Integer.parseInt(arg);
                if (!this.collectionManager.contains(ticketId)){
                    return new Response("No ticket with id " + ticketId + " exists", false);
                }
                Ticket oldTicket = collectionManager.read(ticketId);
                if (!oldTicket.getCreator().equals(user.getLogin())){
                    return new Response("You don't have a permission to do it",true);
                }
                if (this.keys.containsValue(ticketId)){
                    return new Response("This id is being processed", false);
                }
                keys.put(user.getLogin(),ticketId);
                return new Response("",false,Instruction.ASK_TICKET,new Command("replace_if_lower",arg));
            } catch (NumberFormatException e){
                return new Response("Key must be a number", true);
            }
        } else {
            if (!this.collectionManager.contains(this.keys.get(user.getLogin()))){
                Integer existingKey = this.keys.get(user.getLogin());
                this.keys.remove(user.getLogin());
                return new Response("No ticket with id " + existingKey + " exists", false);
            }
            Ticket newTicket = new Gson().fromJson(arg,Ticket.class);
            Integer ticketId = this.keys.get(user.getLogin());
            Ticket oldTicket = collectionManager.read(ticketId);
            newTicket.setId(ticketId);
            if (newTicket.compareTo(oldTicket) < 0) {
                    collectionManager.update(ticketId, newTicket);
            }
            this.keys.remove(user.getLogin());
            return new Response();
        }
    }
}