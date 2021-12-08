package commandHandler.commands.oneStepCommands;


import common.User;
import commandHandler.commands.AbstractCommand;
import commandHandler.commands.Executable;
import common.Instruction;
import common.Response;

/**
 * Interrupt the programm
 * **/
public class ExitCommand extends AbstractCommand {
    public ExitCommand(){
        super("exit","stop the programm");
    }
    /**
     * @see Executable
     * **/
    @Override
    public Response execute(String arg, User user) {
        return new Response("",false, Instruction.EXIT);
    }
}