import domainObjectModel.*;
import com.google.gson.Gson;
import common.*;
import inputHandler.TicketAsker;
import inputHandler.UserDataAsker;
import utils.*;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

public class Client {

    private ConnectionReciever connectionReciever;
    private TicketAsker asker;
    private boolean isRunning;
    private Stack<String> callStack;
    private UserInput userInput;
    private SelectionKey selectionKey;
    private User userData;
    private UserDataAsker userDataAsker;

    public Client(String host, int port){
        this.connectionReciever = new ConnectionReciever(host, port);
        this.isRunning = true;
        this.callStack = new Stack();
    }

    public void run(){
        UserOutput.println("Connecting to server...");
        selectionKey = connectionReciever.getConn();
        run("", null);
        return;
    }
    private boolean run(String filepath, User user){
        if(filepath == ""){
            this.userInput = new UserInput(new Scanner(System.in));
            this.asker = new TicketAsker(userInput);
        } else {
            try{
                userInput = new UserInput(new BufferedReader(new FileReader(new File(filepath))));
                this.asker = new TicketAsker(userInput);
            } catch(FileNotFoundException e){
                UserOutput.println("File "+filepath+" not found");
                return true;
            }
        }

        this.userDataAsker = new UserDataAsker(userInput);

        ResponseReader responseReader = new ResponseReader();
        RequestSender requestSender = new RequestSender(new InetSocketAddress(connectionReciever.getHost(), connectionReciever.getPort()));

        Selector selector = selectionKey.selector();
        DatagramChannel datagramChannel = (DatagramChannel) selectionKey.channel();

        Request request = new Request();
        try{
            while(isRunning){
                if(userInput.isInteractive()){
                    UserOutput.println("Waiting for server response...");
                }
                if(selector.select() == 0){
                    continue;
                }
                Set keySet = selector.selectedKeys();
                Iterator it = keySet.iterator();

                while(it.hasNext()){
                    SelectionKey key = (SelectionKey) it.next();
                    it.remove();
                    if(key.isWritable()){
                        if (userData == null){
                            request = userDataAsker.setUser(new Request());
                            this.userData = request.getUser();
                        } else {
                            request.setUser(userData);
                        }
                        if(request.getCommand() == null || request.getCommand().getCommand().isEmpty()){
                            Command command = asker.askValidatedCommand();
                            if(command == null){
                                return true;
                            }
                            request.setCommand(command);
                        }
                        requestSender.sendQuery(request, datagramChannel);
                        datagramChannel.register(selector,SelectionKey.OP_READ);
                    }
                    if(key.isReadable()){
                        Response response = responseReader.getResponse(datagramChannel);
                        if (response == null){
                            return false;
                        }
                        if(response.failed() && userInput.isInteractive()){
                            UserOutput.println("Execution failed");
                        }
                        if (response.getInstruction()!= Instruction.SCRIPT && !response.getContent().isEmpty()){
                            UserOutput.println(response.getContent());
                        }
                        request = handleResponse(response,datagramChannel,selector);
                        if(request == null){
                            return true;
                        }
                        if (!isRunning){
                            if (userInput.isInteractive()) {
                                selector.close();
                                datagramChannel.close();
                            }
                            return false;
                        }
                        datagramChannel.register(selector,SelectionKey.OP_WRITE);
                    }
                }
            }
        } catch (IOException e) {
            UserOutput.println("Selector exception");
            return false;
        }
        return true;
    }

    public Request handleResponse (Response response, DatagramChannel datagramChannel, Selector selector){

        switch (response.getInstruction()){
            case ASK_COMMAND: {
                Request request = new Request();
                Command command = asker.askValidatedCommand();
                if(command!=null) {
                    request.setCommand(command);
                    return request;
                }
                else {
                    return null;
                }
            }
            case USER_NOT_VALIDATED:{
                Request request = userDataAsker.setUser(new Request());
                this.userData = request.getUser();
                return request;
            }
            case ASK_TICKET: {
                try{
                    Request request = ask(asker.askValidatedTicket(userData),response, Ticket.class);
                    return request;
                } catch (NullPointerException e){
                    UserOutput.println("Incorrect input");
                    return null;
                }
            }
            case ASK_COORDINATES: {
                return ask(asker.askValidatedCoordinates(),response,Coordinates.class);
            }
            case ASK_LOCATION: {
                return ask(asker.askValidatedLocation(),response,Location.class);
            }
            case ASK_ADDRESS:{
                return ask(asker.askValidatedAddress(),response, Address.class);
            }
            case ASK_VENUE:{
                return ask(asker.askValidatedVenue(), response, Venue.class);
            }
            case SCRIPT:{
                Request request = new Request();
                boolean recursion =
                        callStack
                                .stream()
                                .anyMatch(filepath -> filepath.equals(response.getContent()));
                if (recursion){
                    UserOutput.println("Failed to execute because of recursion");
                    isRunning = false;
                }
                callStack.push(response.getContent());
                try {
                    datagramChannel.register(selector,SelectionKey.OP_WRITE);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
                TicketAsker saveAsker = asker;
                if(!run(response.getContent(), this.userData)){
                    isRunning = false;
                }
                callStack.pop();
                asker = saveAsker;
                if(isRunning){
                    request.setCommand(asker.askValidatedCommand());
                    return request;
                } else {
                    return null;
                }
            }
            case EXIT:{
                isRunning = false;
                return new Request();
            }
            default: return null;
        }

    }
    private Request ask(Object obj, Response response, Class classToCast){
        Request request = new Request();
        if(obj!=null) {
            try{
                request.setCommand(new Command(response.getCommand().getCommand(),new Gson().toJson(classToCast.cast(obj))));
            } catch (NullPointerException e){
                return null;
            }
            return request;
        }
        else {
            return null;
        }
    }
}