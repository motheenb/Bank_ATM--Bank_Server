package org.motheen.atm.io;

import org.motheen.atm.ClientGUI;
import org.motheen.server.handlers.ThreadHandler;

import java.io.*;
import java.net.Socket;
import java.security.PublicKey;
import java.util.concurrent.Future;

/**
 *
 * @author Motheen Baig
 */
public class ClientSocket {

    private Socket socket;
    private Session session = new Session();
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public ClientSocket() {
        try {
            socket = new Socket("127.0.0.1", 43594);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            ClientGUI.log("Connected to server.");

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
            // handle incoming
       }
    }

    public boolean doKeyExchange() {
        session.generateKeyPair();
        try {
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(session.getPublicKey());
            objectOutputStream.flush();
            objectInputStream = new ObjectInputStream(inputStream);
            session.setOtherPublicKey((PublicKey) objectInputStream.readObject());
            session.generateSecretKey();
            ClientGUI.log("Secret Key: " + new String(session.getSecretKey()));
        } catch (final IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return session.getSecretKey().length > 0;
    }

}
