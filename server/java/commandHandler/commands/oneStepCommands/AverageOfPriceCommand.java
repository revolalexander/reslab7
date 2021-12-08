package commandHandler.commands.oneStepCommands;

import common.User;
import commandHandler.commands.AbstractCommand;
import commandHandler.commands.Executable;
import domainObjectModel.Ticket;
import commandHandler.utils.CollectionManager;
import common.Response;

/**
 * Command that show average price of all elements
 * **/
public class AverageOfPriceCommand extends AbstractCommand {

    private CollectionManager collectionManager;

    public AverageOfPriceCommand(CollectionManager collectionManager){
        super("average_of_price", "show average price");
        this.collectionManager = collectionManager;
    }

    /**
     * @see Executable
     * **/
    @Override
    public Response execute(String arg, User user){
        if(collectionManager.getSize() == 0){
            return new Response("Collection is empty", true);
        }
        double avgPrice = collectionManager.getStream().mapToDouble(Ticket::getPrice).sum();
        avgPrice /= collectionManager.getSize();
        return new Response("Average price is " + avgPrice, false);
    }

}
