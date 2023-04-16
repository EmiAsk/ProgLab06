package se.ifmo.lab06.server;

import se.ifmo.lab06.dto.Request;
import se.ifmo.lab06.dto.Response;
import se.ifmo.lab06.util.CLIPrinter;
import se.ifmo.lab06.util.Printer;

import javax.management.ObjectName;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private final DatagramSocket connection;
    private final Printer printer;
    private static final int PORT = 5050;
    private static final int BATCH = 1024;
    private final Map<SocketAddress, byte[]> clientData;

    public Server() throws SocketException {
        this.connection = new DatagramSocket(PORT);
        this.clientData = new HashMap<>();
        this.printer = new CLIPrinter();
    }


    public void send(byte[] data, SocketAddress address) throws IOException {
        int n = (int) Math.ceil((double) data.length / BATCH);

        for (int i = 0; i < n; i++) {
            byte[] batch = new byte[BATCH];
            System.arraycopy(data, i * BATCH, batch, 0, BATCH);
            batch[BATCH - 1] = (byte) ((i + 1 == n) ? 1 : 0);
            DatagramPacket dp = new DatagramPacket(batch, batch.length, address);
            connection.send(dp);
            printer.printf("Batch %s/%s has been sent\n", i + 1, n);
        }
    }

    public void send(Response response, SocketAddress address) throws IOException {
        var byteStream = new ByteArrayOutputStream();
        var objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(response);
        send(byteStream.toByteArray(), address);
    }

    public Map.Entry<SocketAddress, byte[]> receive() throws IOException {
        var batch = new byte[BATCH];
        var dp = new DatagramPacket(batch, batch.length);

        while (true) {
            connection.receive(dp);
            var address = dp.getSocketAddress();

            var value = clientData.getOrDefault(address, new byte[]{});
            var data = new ByteArrayOutputStream();
            data.write(value);
            data.write(dp.getData());
            clientData.put(address, data.toByteArray());
            if (dp.getData()[BATCH - 1] == (byte) 1) {
                return Map.entry(address, clientData.get(address));
            }
        }
    }

    public Map.Entry<SocketAddress, Request> receiveRequest() throws IOException, ClassNotFoundException {
        var pair = receive();
        var byteStream = new ByteArrayInputStream(pair.getValue());
        var objectStream = new ObjectInputStream(byteStream);

        return Map.entry(pair.getKey(), (Request) objectStream.readObject());

    }

    public void run() throws IOException {

    }

}