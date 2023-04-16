package se.ifmo.lab06.client;

import se.ifmo.lab06.client.dto.Request;
import se.ifmo.lab06.client.dto.Response;
import se.ifmo.lab06.client.util.CLIPrinter;
import se.ifmo.lab06.client.util.Printer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class Client implements AutoCloseable {
    private final DatagramChannel connection;
    private final InetSocketAddress address;
    private final Printer printer;

    private static final int BATCH = 1024;

    private Client(DatagramChannel connection, InetSocketAddress address) {
        this.connection = connection;
        this.address = address;
        this.printer = new CLIPrinter();
    }

    public static Client connect(String host, int port) throws IOException {
        var dc = DatagramChannel.open();
        dc.configureBlocking(false);
        return new Client(dc, new InetSocketAddress(host, port));
    }

    public void send(byte[] data) throws IOException {
        int n = (int) Math.ceil((double) data.length / BATCH);

        for (int i = 0; i < n; i++) {
            byte[] batch = new byte[BATCH];
            System.arraycopy(data, i * BATCH, batch, 0, BATCH);
            batch[BATCH - 1] = (byte) ((i + 1 == n) ? 1 : 0);
            connection.send(ByteBuffer.wrap(batch), address);
            printer.printf("Batch %s/%s has been sent\n", i + 1, n);
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
            data.write(buffer.array());
            if (buffer.array()[BATCH - 1] == (byte) 1) {
                return data.toByteArray();
            }
        }
    }

    public Response sendAndReceive(Request request) {
        try {
            send(request);
            return receiveResponse();
        } catch (ClassNotFoundException e) {
            return new Response("Error. Invalid response format from server", Response.Status.ERROR);
        } catch (IOException e) {
            return new Response("Error. Unable to send or receive data", Response.Status.ERROR);
        }
    }

    public void close() throws IOException {
        connection.close();
    }

}