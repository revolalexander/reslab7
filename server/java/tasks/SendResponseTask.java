package tasks;

import common.Response;
import responseSender.ResponseSender;

import java.net.InetAddress;

public class SendResponseTask implements Runnable{
    private ResponseSender responseSender;
    private Response response;
    private InetAddress address;
    private int port;
    public SendResponseTask(ResponseSender responseSender, Response response, InetAddress address, int port){
        this.responseSender = responseSender;
        this.response = response;
        this.address = address;
        this.port = port;
    }
    @Override
    public void run() {
        responseSender.sendResponse(response,address, port);
    }
}
