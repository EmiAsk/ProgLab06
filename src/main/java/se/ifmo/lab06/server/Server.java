package se.ifmo.lab06.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.ifmo.lab06.shared.dto.request.Request;
import se.ifmo.lab06.shared.dto.response.Response;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class Server implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private final DatagramSocket connection;
    private static final int PORT = 4445;
    private static final int BATCH = 1024;
    private final Map<SocketAddress, byte[]> clientData;

    public Server() throws SocketException {
        this.connection = new DatagramSocket(PORT);
        this.clientData = new HashMap<>();
    }


    public void send(SocketAddress address, byte[] data) throws IOException {
        int n = (int) Math.ceil((double) data.length / (BATCH - 1));

        for (int i = 0; i < n; i++) {
            byte[] batch = new byte[BATCH];
            System.arraycopy(data, i * (BATCH - 1), batch, 0, Math.min(data.length - i * (BATCH - 1), BATCH - 1));
            batch[BATCH - 1] = (byte) ((i + 1 == n) ? 1 : 0);
            DatagramPacket dp = new DatagramPacket(batch, batch.length, address);
            connection.send(dp);
            logger.info("Batch {}/{} has been sent\n", i + 1, n);
        }
    }

    public void send(SocketAddress address, Response response) throws IOException {
        var byteStream = new ByteArrayOutputStream();
        var objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(response);
        send(address, byteStream.toByteArray());
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
            data.write(Arrays.copyOf(batch, BATCH - 1));
            clientData.put(address, data.toByteArray());
            if (dp.getData()[BATCH - 1] == (byte) 1) {
                return Map.entry(address, clientData.remove(address));
            }
        }
    }

    public Map.Entry<SocketAddress, Request> receiveRequest() throws IOException, ClassNotFoundException {
        var pair = receive();
        var byteStream = new ByteArrayInputStream(pair.getValue());
        var objectStream = new ObjectInputStream(byteStream);
        return Map.entry(pair.getKey(), (Request) objectStream.readObject());
    }

    @Override
    public void close() throws Exception {
        this.connection.close();
    }
}