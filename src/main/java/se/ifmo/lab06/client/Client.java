package se.ifmo.lab06.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.ifmo.lab06.shared.dto.request.Request;
import se.ifmo.lab06.shared.dto.response.Response;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;

public class Client implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private final DatagramChannel connection;
    private final InetSocketAddress address;

    private static final int BATCH = 1024;

    private Client(DatagramChannel connection, InetSocketAddress address) {
        this.connection = connection;
        this.address = address;
    }

    public static Client connect(String host, int port) throws IOException {
        var dc = DatagramChannel.open();
        return new Client(dc, new InetSocketAddress(host, port));
    }

    public void send(byte[] data) throws IOException {
        int n = (int) Math.ceil((double) data.length / (BATCH - 1));

        for (int i = 0; i < n; i++) {
            byte[] batch = new byte[BATCH];
            System.arraycopy(data, i * (BATCH - 1), batch, 0, Math.min(data.length - i * (BATCH - 1), BATCH - 1));
            batch[BATCH - 1] = (byte) ((i + 1 == n) ? 1 : 0);
            connection.send(ByteBuffer.wrap(batch), address);
            logger.info("Batch {}/{} has been sent", i + 1, n);
        }
    }

    public void send(Request request) throws IOException {
        var byteStream = new ByteArrayOutputStream();
        var objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(request);
        send(byteStream.toByteArray());
    }

    public Response receiveResponse() throws IOException, ClassNotFoundException {
        byte[] data = receive();
        var byteStream = new ByteArrayInputStream(data);
        var objectStream = new ObjectInputStream(byteStream);

        return (Response) objectStream.readObject();
    }

    public byte[] receive() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(BATCH);
        SocketAddress address = null;
        var data = new ByteArrayOutputStream();

        while (true) {
            while (address == null) {
                address = connection.receive(buffer);
            }

            data.write(Arrays.copyOf(buffer.array(), BATCH - 1));
            if (buffer.array()[BATCH - 1] == (byte) 1) {
                return data.toByteArray();
            }
            address = null;
            buffer.clear();
        }
    }

    public Response sendAndReceive(Request request) {
        try {
            send(request);
            return receiveResponse();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
//            return new Response("Error. Invalid response format from server", Response.Status.ERROR);
        } catch (IOException e) {
            throw new RuntimeException(e);
//            return new Response("Error. Unable to send or receive data", Response.Status.ERROR);
        }
    }

    public void close() throws IOException {
        connection.close();
    }

}