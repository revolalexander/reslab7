package commandHandler.commands.oneStepCommands;

import common.User;
import commandHandler.commands.AbstractCommand;
import commandHandler.commands.Executable;
import commandHandler.utils.CollectionManager;
import common.Response;

/**
 * Show all refundable values in ascending order
 * **/
public class PrintFieldAscendingRefundable extends AbstractCommand {
    private CollectionManager collectionManager;
    private int count;

    public PrintFieldAscendingRefundable(CollectionManager collectionManager){
        super("print_field_ascending_refundable", "show all refundable values in in ascending order");
        this.collectionManager = collectionManager;
    }

    /**
     * @see Executable
     * **/
    @Override
    public Response execute(String arg, User user){
        collectionManager.getStream().forEach(ticket->{
            if (ticket.isRefundable()){
                count++;
            }
        });
        String content = "";
        for (int i = 0; i<collectionManager.getSize() - count; i++){
            content+="false\n";
        }
        for (int i = 0; i<count; i++){
            content+="true\n";
        }
        count = 0;
        return new Response(content,false);
    }
}
