package commandHandler.commands;

import common.User;
import common.Response;

/**
 * Interface for commands
 * **/
public interface Executable {
    /**
     * Executes the command
     * @return exit status of command
     * **/
    Response execute(String arg, User user);
    /**
     * @return the name of command
     * **/
    String getName();
    /**
     * @return the description of command
     * **/
    String getDescription();
}