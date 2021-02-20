package org.motheen.server.io;

import org.motheen.server.Server;
import org.motheen.server.handlers.ThreadHandler;

import java.io.*;
import java.net.Socket;
import java.security.PublicKey;
import java.util.concurrent.Future;

/**
 *
 * @author Motheen Baig
 */
public class Client {

    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private final Session session = new Session();

    public Client(final Socket socket) {
        this.socket = socket;
        try {
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        final Future<Boolean> keyExchange = ThreadHandler.get().submit(this::doKeyExchange);
        while (!keyExchange.isDone()) {
            // wait
        }
        while (!socket.isClosed()) {

        }
    }

    public boolean doKeyExchange() {
        session.generateKeyPair();
        try {
            objectInputStream = new ObjectInputStream(inputStream);
            session.setOtherPublicKey((PublicKey) objectInputStream.readObject());
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(session.getPublicKey());
            session.generateSecretKey();
            Server.log("Secret Key: " + new String(session.getSecretKey()));
        } catch (final IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return session.getSecretKey().length > 0;
    }

}
