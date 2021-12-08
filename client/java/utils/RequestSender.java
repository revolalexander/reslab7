
package utils;

import common.Request;
import common.Serializer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class RequestSender {
    private InetSocketAddress address;
    public RequestSender(InetSocketAddress addr){
        this.address = addr;
    }
    public boolean sendQuery(Request request, DatagramChannel channel){
        try{
            byte[] bytes = Serializer.serialize(request);
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            channel.send(byteBuffer, address);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}