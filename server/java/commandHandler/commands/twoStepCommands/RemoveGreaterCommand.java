package commandHandler.commands.twoStepCommands;

import common.User;
import commandHandler.commands.AbstractCommand;
import commandHandler.commands.Executable;
import domainObjectModel.Ticket;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import commandHandler.utils.CollectionManager;
import common.Command;
import common.Instruction;
import common.Response;

import java.util.Iterator;
import java.util.Map;

/**
 * Removes all elements that are more than specified one
 * **/
public class RemoveGreaterCommand extends AbstractCommand {
    private CollectionManager collectionManager;
    public RemoveGreaterCommand(CollectionManager collectionManager){
        super("remove_greater","removes all elements that more than specified");
        this.collectionManager = collectionManager;
    }
    /**
     * @see Executable
     * **/
    @Override
    public Response execute(String arg, User user) {
        if(collectionManager.getSize() == 0) {
            return new Response("Collection is empty",true);
        }
        Ticket ticket;
        try{
            ticket = new Gson().fromJson(arg,Ticket.class);
        } catch (JsonSyntaxException e){
            return new Response("Unknown argument", true);
        }
        if (ticket == null){
            return new Response("",false, Instruction.ASK_TICKET, new Command("remove_greater",""));
        }
        collectionManager.removeIf(existingTicket -> existingTicket.getName().length() > ticket.getName().length() && existingTicket.getCreator().equals(user.getLogin()));
        return new Response();
    }
}