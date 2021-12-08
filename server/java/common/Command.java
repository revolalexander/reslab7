package common;

import java.io.Serializable;

public class Command implements Serializable {
    private String command;
    private String argument;

    public Command(String command, String argument) {
        this.command = command;
        this.argument = argument;
    }

    public Command(){
        this.command = "";
        this.argument = "";
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }
}
