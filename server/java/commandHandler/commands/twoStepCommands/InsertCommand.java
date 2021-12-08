package commandHandler.commands.twoStepCommands;

import common.User;
import commandHandler.commands.AbstractCommand;
import commandHandler.commands.Executable;
import domainObjectModel.Ticket;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import commandHandler.utils.CollectionManager;
import commandHandler.utils.Logger;
import common.Command;
import common.Instruction;
import common.Response;

import java.util.Map;
import java.util.TreeMap;

/**
 * Inserts new element with specified key into collection
 * **/
public class InsertCommand extends AbstractCommand {

    private CollectionManager collectionManager;
    private Map<String, Integer> keys;
    public InsertCommand(CollectionManager collectionManager){
        super("insert", "insert new element with specified key");
        this.collectionManager = collectionManager;
        this.keys = new TreeMap<>();
    }
    /**
     * @see Executable
     * @param key that will be used to perform the insert
     * **/
    @Override
    public Response execute(String key, User user){
        if (key == null || key.isEmpty()){
            Logger.error("Empty key in InsertCommand");
            return new Response("Empty key in InsertCommand",true);
        }
        System.out.println("InsertCommand " + key);
        if (!this.keys.containsKey(user.getLogin())){
            try{
                Integer ticketId = Integer.parseInt(key);
                if (collectionManager.contains(ticketId)){
                    return new Response("Ticket with key " + ticketId + " already exists", false);
                }
                if (this.keys.containsValue(ticketId)){
                    return new Response("This id is being processed",false);
                }
                this.keys.put(user.getLogin(),ticketId);
                return new Response("",false,Instruction.ASK_TICKET, new Command("insert", ""+ticketId));
            } catch (NumberFormatException e){
                return new Response("Key must be a number",true);
            }
        } else {
            try{
                if (collectionManager.contains(this.keys.get(user.getLogin()))){
                    Integer existingKey = this.keys.get(user.getLogin());
                    this.keys.remove(user.getLogin());
                    return new Response("Ticket with key " + existingKey + " already exists", false);
                }
                Ticket ticket = new Gson().fromJson(key,Ticket.class);
                ticket.setId(this.keys.get(user.getLogin()));
                collectionManager.create(ticket.getId(), ticket);
                this.keys.remove(user.getLogin());
                return new Response();
            } catch (JsonSyntaxException | ClassCastException e){
                Logger.error("Invalid JSON ticket in InsertCommand");
                e.printStackTrace();
                return new Response("Invalid JSON ticket in InsertCommand", true);
            }
        }
    }

}