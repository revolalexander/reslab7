package commandHandler.commands.oneStepCommands;

import common.User;
import commandHandler.commands.AbstractCommand;
import commandHandler.commands.Executable;
import common.Instruction;
import common.Response;

import java.util.Stack;

/**
 * Execute the script from file
 * **/
public class ExecuteScriptCommand extends AbstractCommand {
    private Stack<String> scriptStack = new Stack<>();

    public ExecuteScriptCommand(){
        super("execute_script","read and execute script from specified file. " +
                "Script contains same commands as user uses in interactive mode");
    }
    /**
     * @see Executable
     * @param filePath Путь к файлу
     * **/
    @Override
    public Response execute(String filePath, User user) {
        return new Response(filePath, false, Instruction.SCRIPT);
    }
}