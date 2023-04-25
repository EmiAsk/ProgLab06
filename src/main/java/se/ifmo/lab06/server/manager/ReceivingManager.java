package se.ifmo.lab06.server.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.ifmo.lab06.shared.dto.request.Request;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ReceivingManager {
    private static final Logger logger = LoggerFactory.getLogger(SendingManager.class);
    private static final int BATCH = 1024;

    private final DatagramSocket connection;
    private final Map<SocketAddress, byte[]> clientData;

    public ReceivingManager(DatagramSocket connection) {
        this.connection = connection;
        this.clientData = new HashMap<>();
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
        var request = Map.entry(pair.getKey(), (Request) objectStream.readObject());
        logger.info("Request <{}> received from {}", request.getValue().getClass().getSimpleName(), request.getKey());
        return request;
    }
}
