import authentication.*;
import commandHandler.commands.oneStepCommands.*;
import commandHandler.commands.twoStepCommands.InsertCommand;
import commandHandler.commands.twoStepCommands.RemoveGreaterCommand;
import commandHandler.commands.twoStepCommands.ReplaceIfLowerCommand;
import commandHandler.commands.twoStepCommands.UpdateCommand;
import commandHandler.utils.CollectionManager;
import commandHandler.utils.CommandManager;
import common.Request;
import connectionReciever.ConnectionReciever;
import dataAccessObjects.AbstractDAO;
import dataAccessObjects.UserDAO;
import requestReader.RequestReader;
import responseSender.ResponseSender;
import tasks.HandleRequestTask;
import tasks.ReadRequestTask;

import java.net.DatagramSocket;
import java.sql.Connection;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

public class Server {
    private CollectionManager collectionManager;
    public Server(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }
    public void run(ExecutorService executingPool, ExecutorService readingPool, ExecutorService writingPool, Connection connection, AbstractDAO dao){
        Scanner scanner = new Scanner(System.in);
        DatagramSocket socket = new ConnectionReciever(14413).getConn();
        RequestReader requestReader = new RequestReader(socket);
        ResponseSender responseSender = new ResponseSender(socket);
        UserDAO userDAO = new UserDAO(connection);
        Encryptor encryptor = new MD2Encryptor();

        CommandManager commandManager = new CommandManager(
                new InfoCommand(collectionManager),
                new ShowCommand(collectionManager),
                new InsertCommand(collectionManager),
                new UpdateCommand(collectionManager),
                new RemoveKeyCommand(collectionManager),
                new ClearCommand(collectionManager),
                new RemoveGreaterCommand(collectionManager),
                new ReplaceIfLowerCommand(collectionManager),
                new ExitCommand(),
                new SaveCommand(),
                new ExecuteScriptCommand(),
                new RemoveLowerKeyCommand(collectionManager),
                new AverageOfPriceCommand(collectionManager),
                new CountGreaterThanRefundableCommand(collectionManager),
                new PrintFieldAscendingRefundable(collectionManager),
                new SignUpCommand(userDAO, encryptor),
                new SignInCommand(userDAO, encryptor)
        );
        HelpCommand helpCommand = new HelpCommand();
        helpCommand.setCommandManager(commandManager);
        commandManager.addCommand(helpCommand);
        SaltGenerator saltGenerator = new SaltGenerator();
        UserValidator userValidator = new UserValidator(userDAO,encryptor);

        while (true){
            Request request = ((ForkJoinPool) readingPool).invoke(new ReadRequestTask(requestReader));
            executingPool.execute(new HandleRequestTask(userValidator,commandManager,request,writingPool,responseSender, request.getAddress(), request.getPort()));
            if(request.getCommand().getCommand().equals("exit")){
                System.exit(0);
            }
        }
    }
}
