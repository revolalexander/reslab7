package tasks;

import authentication.UserValidator;
import commandHandler.utils.CommandManager;
import common.Instruction;
import common.Request;
import common.Response;
import responseSender.ResponseSender;

import java.net.InetAddress;
import java.util.concurrent.ExecutorService;

public class HandleRequestTask implements Runnable {
    private CommandManager commandManager;
    private UserValidator userValidator;
    private Request request;
    private ExecutorService writingPool;
    private ResponseSender responseSender;
    private InetAddress address;
    private int port;
    public HandleRequestTask(UserValidator userValidator, CommandManager commandManager, Request request, ExecutorService writingPool, ResponseSender responseSender, InetAddress address, int port){
        this.commandManager = commandManager;
        this.userValidator = userValidator;
        this.request = request;
        this.writingPool = writingPool;
        this.responseSender = responseSender;
        this.address = address;
        this.port = port;
    }

    @Override
    public void run() {
        if (
                !userValidator.validate(request.getUser()) &&
                !request.getCommand().getCommand().equals("sign_up")
        ){
            writingPool.execute(new SendResponseTask(
                    responseSender,
                    new Response("User didn't pass the validation", false, Instruction.USER_NOT_VALIDATED),
                    address,
                    port
                    ));
        } else {
            Response response = commandManager.executeCommand(request.getCommand().getCommand(), request.getCommand().getArgument(), request.getUser());
            writingPool.execute(new SendResponseTask(
                    responseSender,
                    response,
                    address,
                    port
            ));
        }
    }
}
