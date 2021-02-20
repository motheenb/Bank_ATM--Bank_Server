package org.motheen.server.handlers;

import org.motheen.server.io.Client;
import org.motheen.server.Server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Motheen Baig
 */
public class ClientHandler {

    private final List<Client> clients;

    public ClientHandler() {
        clients = new ArrayList<>();
    }

    public void acceptConnection(final Socket socket) {
        Server.log("Accepted connection from: " + socket.getInetAddress());
        final Client client = new Client(socket);
        ThreadHandler.get().execute(client::run);
        clients.add(client);
    }

}
