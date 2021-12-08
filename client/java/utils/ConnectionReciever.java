package utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

public class ConnectionReciever {
    private String host;
    private int port;
    public ConnectionReciever(String host, int port){
        this.host = host;
        this.port = port;
    }
    public SelectionKey getConn(){
        try {
            SocketAddress socketAddress = new InetSocketAddress(host, port);
            DatagramChannel channel = DatagramChannel.open();
            channel.connect(socketAddress);
            channel.configureBlocking(false);
            Selector selector = Selector.open();
            SelectionKey key = channel.register(selector, SelectionKey.OP_WRITE);
            return key;
        } catch (IOException e){
            Logger.error("Connection exception");
            e.printStackTrace();
            return null;
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
