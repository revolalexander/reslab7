package requestReader;

import commandHandler.utils.Logger;
import common.Request;
import common.Serializer;

import javax.management.Query;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class RequestReader {
    private DatagramSocket datagramSocket;
    private final int MAX_READING_ATTEMPTS = 10;
    private final int BUFFER_SIZE = 4096;
    public RequestReader(DatagramSocket datagramSocket){
        this.datagramSocket = datagramSocket;
    }

    public Request getRequest(){
        byte[] bytes = new byte[BUFFER_SIZE];
        byte[] resultBytes = new byte[0];
        Request request;
        for(int i = 0; i<MAX_READING_ATTEMPTS; i++){
            try{
                DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
                this.datagramSocket.receive(packet);
                byte[] tempBytes = new byte[resultBytes.length + BUFFER_SIZE];
                System.arraycopy(resultBytes, 0, tempBytes, 0, resultBytes.length);
                System.arraycopy(bytes, 0, tempBytes, resultBytes.length, BUFFER_SIZE);
                resultBytes = tempBytes;
                try{
                    request = (Request) Serializer.deserialize(resultBytes);
                    request.setAddress(packet.getAddress());
                    request.setPort(packet.getPort());
                    if(request.getCommand()!=null){
                        Logger.info("Recieved new query " + request.getCommand().getCommand());
                    } else {
                        Logger.info("Received new query");
                    }
                    return request;
                } catch (IOException | ClassNotFoundException | ClassCastException e){
                    try{
                        Thread.sleep(100);
                    } catch (InterruptedException interruptedException){
                        interruptedException.printStackTrace();
                    } finally {
                        continue;
                    }
                }
            } catch (IOException e){
                Logger.error("Reading request error");
                e.printStackTrace();
                return new Request();
            }
        }
        Logger.error("Reading request error");
        return new Request();
    }

}
