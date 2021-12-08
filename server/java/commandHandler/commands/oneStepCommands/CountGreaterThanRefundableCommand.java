package commandHandler.commands.oneStepCommands;

import common.User;
import commandHandler.commands.AbstractCommand;
import commandHandler.commands.Executable;
import commandHandler.utils.CollectionManager;
import commandHandler.utils.Logger;
import common.Response;

/**
 * Shows quantitiy of elements that have refundable bigger than specified
 * **/
public class CountGreaterThanRefundableCommand extends AbstractCommand {
        private CollectionManager collectionManager;

        public CountGreaterThanRefundableCommand(CollectionManager collectionManager){
            super("count_greater_than_refundable", "show quantity of elements that have refundable bigger than specified");
            this.collectionManager = collectionManager;
        }

        /**
         * @see Executable
         **/
        @Override
        public Response execute(String arg, User user){
            if (arg == null || arg.isEmpty() || (!arg.equals("true") && !arg.equals("false"))){
                Logger.error("Arg is empty in CountGreaterThanRefundableCommand");
                return new Response("Unknown argument of command count greater than refundable",true);
            }
            boolean refundable = Boolean.parseBoolean(arg);
            if (refundable){
                return new Response("0 elements have bigger refundable than " + arg,false);
            } else {
                return new Response(collectionManager.getSize() + " elements have bigger refundable than " + arg,false);
            }
        }


}
