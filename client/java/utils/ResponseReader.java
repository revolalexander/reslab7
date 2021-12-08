package utils;

import common.Response;
import common.Serializer;
import utils.Logger;

import java.io.IOException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;


public class ResponseReader {
    private final int MAX_READING_ATTEMPTS = 10;
    private final int BUFFER_SIZE = 10240;
    public Response getResponse(DatagramChannel datagramChannel){

        byte[] bytes = new byte[BUFFER_SIZE];
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        Response response = null;
        try {
            datagramChannel.receive(byteBuffer);
            try {
                response = (Response)Serializer.deserialize(bytes);
                return response;
            } catch (IOException | ClassNotFoundException | ClassCastException e) {
                Logger.error("Server message is too big or class cast exception response reader");
            }
        } catch (SocketException e) {
            Logger.error("Connection with server disrupted");
        } catch (IOException e){
            Logger.error("Connection disrupted");
        }
        return response;
    }

}