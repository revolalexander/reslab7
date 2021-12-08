
package common;

import java.io.Serializable;

public class Response implements Serializable {
    private String content;
    private boolean executionFailed;
    private Instruction instruction;
    private Command command;
    final long serialVersionUID = 1337L;
    public Response(){
        this.content = "";
        this.executionFailed = false;
        this.instruction = Instruction.ASK_COMMAND;
    }
    public Response(String content, boolean failed, Instruction instruction){
        this.content = content;
        this.executionFailed = failed;
        this.instruction = instruction;
    }
    public Response(String content, boolean failed, Instruction instruction, Command command){
        this.content = content;
        this.executionFailed = failed;
        this.instruction = instruction;
        this.command = command;
    }
    public Response(String content, boolean failed){
        this.content = content;
        this.executionFailed = failed;
        this.instruction = Instruction.ASK_COMMAND;
    }
    public Instruction getInstruction() {
        return instruction;
    }

    public String getContent() {
        return content;
    }

    public boolean failed() {
        return executionFailed;
    }

    public Response setContent(String content){
        this.content = content;
        return this;
    }
    public Response setExecutionFailed(boolean flag){
        this.executionFailed = flag;
        return this;
    }
    public Response setInstruction(Instruction instruction){
        this.instruction = instruction;
        return this;
    }
    public Command getCommand(){
        return this.command;
    }
    public Response setCommand(Command command){
        this.command = command;
        return this;
    }
}