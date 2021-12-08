package connectionReciever;

import commandHandler.utils.Logger;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;

public class ConnectionReciever {
    private int port;
    private DatagramSocket datagramSocket;
    public ConnectionReciever(int port){
        this.port = port;
    }
    public DatagramSocket getConn(){
        if(this.datagramSocket != null){
            return this.datagramSocket;
        }
        SocketAddress socketAddress = new InetSocketAddress(port);
        try {
            DatagramSocket datagramSocket = new DatagramSocket(socketAddress);
            return datagramSocket;
        } catch (SocketException e) {
            Logger.error("datagramSocket exception in ConnectionReciever");
            e.printStackTrace();
            return null;
        }
    }
}
