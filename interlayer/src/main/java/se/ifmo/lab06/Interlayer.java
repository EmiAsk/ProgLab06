package se.ifmo.lab06;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.ifmo.lab06.client.Client;
import se.ifmo.lab06.dto.request.PingRequest;
import se.ifmo.lab06.server.Server;

import javax.naming.TimeLimitExceededException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

public class Interlayer {

    private static final Logger logger = LoggerFactory.getLogger(Interlayer.class);

    private final List<SocketAddress> servers = new ArrayList<>();

    public Interlayer() {
        servers.add(new InetSocketAddress("127.0.0.1", 4139));
        servers.add(new InetSocketAddress("127.0.0.1", 4513));
        servers.add(new InetSocketAddress("127.0.0.1", 12312));
    }

    public void run() {
        try (var client = Client.connect();
             var server = new Server(7777)) {

            while (true) {
                var pair = server.receiveRequest();
                var clientAddr = pair.getKey();
                var request = pair.getValue();

                for (SocketAddress address : servers) {
                    try {
                        client.setTimeout(100_000_000); // 1 ms
                        client.sendAndReceive(address, new PingRequest());
                    } catch (TimeLimitExceededException e) {
                        continue;
                    }
                    client.setTimeout(2_000_000_000);
                    var response = client.sendAndReceive(address, request);
                    server.send(clientAddr, response);
                    break;
                }
            }
        } catch (IOException e) {
            logger.error("Error occurred while I/O.\n{}", e.getMessage());
        } catch (ClassNotFoundException e) {
            logger.error("Error. Invalid response format from server");
        } catch (TimeLimitExceededException e) {
            logger.error("Error. Server not available");
        }
    }
}
