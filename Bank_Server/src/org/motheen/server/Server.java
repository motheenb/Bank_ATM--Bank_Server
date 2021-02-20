package org.motheen.server;

import org.motheen.server.handlers.ClientHandler;
import org.motheen.server.handlers.ThreadHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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

    public void listen() {
        Socket socket;
        while (!serverSocket.isClosed()) {
            try {
                socket = serverSocket.accept();
                clientHandler.acceptConnection(socket);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void log(final Object o) {
        System.out.println(o);
    }

    public static void main(final String...args) {
        final Server server = new Server(43594);
        ThreadHandler.get().execute(server::listen);
    }

}
