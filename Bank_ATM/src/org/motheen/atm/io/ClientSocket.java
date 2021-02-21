package org.motheen.atm.io;

import org.motheen.atm.ClientGUI;
import org.motheen.atm.State;
import org.motheen.server.handlers.ThreadHandler;
import org.motheen.server.io.Client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.concurrent.ExecutionException;
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
    private final ClientGUI clientGUI;

    public ClientSocket(final ClientGUI clientGUI) {
        this.clientGUI = clientGUI;
        try {
            socket = new Socket("127.0.0.1", 43594);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            ClientGUI.log("Connected to server.");
            ClientGUI.ClientState = State.NewKey;
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        String message;
        byte encryptedData[], plainData[];
        while (!socket.isClosed()) {
            switch (ClientGUI.ClientState) {
                case NewKey -> {
                    final Future<Boolean> keyExchange = ThreadHandler.get().submit(this::doKeyExchange);
                    while (!keyExchange.isDone()) {
                     // wait
                    }
                    try {
                        if (keyExchange.get()) {
                            ClientGUI.ClientState = State.EnterCard;
                        }
                    } catch (final InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                case EnterCard -> {
                    encryptedData = session.getData(inputStream);
                    plainData = session.decryptData(encryptedData);
                    message = new String(plainData);
                    String cardStatus = message.split(":")[2].split("=")[1];
                    if (cardStatus.equalsIgnoreCase("ok")) {
                        ClientGUI.ClientState = State.EnterPIN;
                    }
                }
                case EnterPIN -> {
                    encryptedData = session.getData(inputStream);
                    plainData = session.decryptData(encryptedData);
                    message = new String(plainData);
                    String pinStatus = message.split(":")[2].split("=")[1];
                    ClientGUI.log("Pin Status: " + pinStatus);
                    if (pinStatus.equalsIgnoreCase("ok")) {
                        encryptedData = session.getData(inputStream);
                        byte clientData[] = session.decryptData(encryptedData);
                        String clientInfo = new String(clientData);
                        ClientGUI.log("Client Info: " + clientInfo);
                        // request client info (full name, balance, 5 transactions)
                        // wait for server response
                        // if valid response, display main menu with client info
                    }
                }
            }
       }
    }

    public void sendPIN() {
        String data = "0:type=ATM:card_pin=" + clientGUI.getCardPIN();
        byte encrypted[] = session.encryptData(data);
        session.sendData(encrypted, outputStream);
    }

    public void sendCardNumber() {
        String data = "0:type=ATM:card_number=" + clientGUI.getCardNumber();
        byte encrypted[] = session.encryptData(data);
        session.sendData(encrypted, outputStream);
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

    public Session getSession() {
        return session;
    }

}
