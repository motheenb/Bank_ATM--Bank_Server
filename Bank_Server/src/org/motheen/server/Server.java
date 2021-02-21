package org.motheen.server;

import org.motheen.server.handlers.ClientHandler;
import org.motheen.server.handlers.ThreadHandler;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author Motheen Baig
 */
public class Server {

    private ServerSocket serverSocket;
    private final ClientHandler clientHandler = new ClientHandler();

    public Server(final int port) {
        try {
            serverSocket = new ServerSocket(port);
            Server.log("Server is listening on port: " + port);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public Server listen() {
        ThreadHandler.get().execute(() -> {
            while (!serverSocket.isClosed()) {
                try {
                    clientHandler.acceptConnection(serverSocket.accept());
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return this;
    }

    public static void log(final Object o) {
        System.out.println(o);
    }

    public static void main(final String...args) {
        new Server(43594).listen();
    }

}
