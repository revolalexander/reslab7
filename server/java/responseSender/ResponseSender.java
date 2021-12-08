package responseSender;


import commandHandler.utils.Logger;
import common.Response;
import common.Serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ResponseSender {

    private DatagramSocket datagramSocket;
    public ResponseSender(DatagramSocket datagramSocket){
        this.datagramSocket = datagramSocket;
    }

    public boolean sendResponse(Response response, InetAddress address, int port){
        try{
            byte[] bytes = Serializer.serialize(response);
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
            datagramSocket.send(packet);
            Logger.info("Sent new response");
            return true;
        }catch(IOException e){
            Logger.error("Failed to serialize response");
            e.printStackTrace();
            return false;
        }
    }
}