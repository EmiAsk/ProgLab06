package se.ifmo.lab06.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.ifmo.lab06.dto.request.Request;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import static java.util.Map.Entry;
import java.util.Map;

public class ReceivingManager {
    private static final Logger logger = LoggerFactory.getLogger(SendingManager.class);
    private static final int BATCH = 1024;
    private static final int ADDRESS_SIZE = 6;
    private static final int END_SIZE = 1;

    private final DatagramSocket connection;
    private final Map<SocketAddress, byte[]> clientData;

    public ReceivingManager(DatagramSocket connection) {
        this.connection = connection;
        this.clientData = new HashMap<>();
    }

    public Entry<SocketAddress, byte[]> receive() throws IOException {
        var batch = new byte[BATCH];
        var dp = new DatagramPacket(batch, batch.length);

        while (true) {
            connection.receive(dp);
            var address = getAddress(Arrays.copyOfRange(dp.getData(), BATCH - ADDRESS_SIZE, BATCH));
            var value = clientData.getOrDefault(address, new byte[]{});
            var data = new ByteArrayOutputStream();
            data.write(value);
            data.write(Arrays.copyOf(batch, BATCH - (ADDRESS_SIZE + END_SIZE)));
            clientData.put(address, data.toByteArray());
            if (dp.getData()[BATCH - (ADDRESS_SIZE + END_SIZE)] == (byte) 1) {
                return Map.entry(address, clientData.remove(address));
            }
        }
    }

    public Entry<SocketAddress, Request> receiveRequest() throws IOException, ClassNotFoundException {
        var pair = receive();
        var byteStream = new ByteArrayInputStream(pair.getValue());
        var objectStream = new ObjectInputStream(byteStream);
        var request = Map.entry(pair.getKey(), (Request) objectStream.readObject());
        logger.info("Request <{}> received from {}", request.getValue().getClass().getSimpleName(), request.getKey());
        return request;
    }

    private SocketAddress getAddress(byte[] data) {
        var buffer = ByteBuffer.wrap(data);

        var o1 = buffer.get() + 128;
        var o2 = buffer.get() + 128;
        var o3 = buffer.get() + 128;
        var o4 = buffer.get() + 128;

        var host = "%s.%s.%s.%s".formatted(o1, o2, o3, o4);
        var port = buffer.getChar();

        return new InetSocketAddress(host, port);
    }
}
