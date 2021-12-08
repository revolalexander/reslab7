package commandHandler.commands.twoStepCommands;

import common.Command;
import common.User;
import commandHandler.commands.AbstractCommand;
import commandHandler.commands.Executable;
import commandHandler.utils.Logger;
import domainObjectModel.Ticket;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import commandHandler.utils.CollectionManager;
import common.Instruction;
import common.Response;

import java.util.Map;
import java.util.TreeMap;

/**
 * Updates the element by its key
 * **/
public class UpdateCommand extends AbstractCommand {
    private CollectionManager collectionManager;
    private Map<String,Integer> keys;
    public UpdateCommand(CollectionManager collectionManager){
        super("update","updates the element with specified id");
        this.collectionManager = collectionManager;
        this.keys = new TreeMap<>();
    }

    /**
     * @see Executable
     * @param arg that will be used to perform the update
     * **/
    @Override
    public Response execute(String arg, User user){
        if(arg == null || arg.isEmpty()){
            return new Response("Key is empty", true);
        }
        if (!this.keys.containsKey(user.getLogin())){
            try{
                Integer ticketId = Integer.parseInt(arg);
                if (!collectionManager.contains(ticketId)){
                    return new Response("Ticket with id " + ticketId + " doesn't exist", false);
                }
                Ticket oldTicket = collectionManager.read(ticketId);
                if (!oldTicket.getCreator().equals(user.getLogin())){
                    return new Response("You don't have a permission to do this", true);
                }
                if (this.keys.containsValue(ticketId)){
                    return new Response("This id is being processed", false);
                }
                keys.put(user.getLogin(), ticketId);
                return new Response("",false, Instruction.ASK_TICKET, new Command("update",arg) );
            } catch (NumberFormatException e){
                return new Response("Key must be a number",true);
            }
        } else {
            try{
                if (!this.collectionManager.contains(this.keys.get(user.getLogin()))){
                    Integer existingKey = this.keys.get(user.getLogin());
                    this.keys.remove(user.getLogin());
                    return new Response("No ticket with id " + existingKey + " exists", false);
                }
                Ticket ticket = new Gson().fromJson(arg,Ticket.class);
                Ticket oldTicket = collectionManager.read(this.keys.get(user.getLogin()));
                ticket.setId(this.keys.get(user.getLogin()));
                collectionManager.update(this.keys.get(user.getLogin()), ticket);
                this.keys.remove(user.getLogin());
                return new Response();
            } catch (JsonSyntaxException | ClassCastException e){
                Logger.error("Invalid JSON Ticket in UpdateCommand");
                return new Response("Invalid JSON Ticket in UpdateCommand",true);
            }
        }
    }
}