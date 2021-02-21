package org.motheen.server.io;

import org.motheen.server.Server;
import org.motheen.server.handlers.ThreadHandler;

import java.io.*;
import java.net.Socket;
import java.security.PublicKey;
import java.util.concurrent.ExecutionException;
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
    private Status ClientStatus = Status.NewKey;
    enum Status {
        WaitingForCard, WaitingForPIN, MainMenu, NewKey
    }

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
        byte encryptedData[];
        while (!socket.isClosed()) {
            switch (ClientStatus) {
                case NewKey -> {
                    Future<Boolean> keyExchange = ThreadHandler.get().submit(this::doKeyExchange);
                    while (!keyExchange.isDone()) {
                        // wait/do another task
                    }
                    try {
                        if (keyExchange.get()) {
                            ClientStatus = Status.WaitingForCard;
                        }
                    } catch (final InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                case WaitingForCard -> {
                    encryptedData = session.getData(inputStream);
                    byte plainData[] = session.decryptData(encryptedData);
                    // Server.log("PlainData: " + new String(plainData));
                    String cardNumber = new String(plainData).split(":")[2].split("=")[1];
                    Server.log("Card Number: " + cardNumber);
                    // check if card # ok
                    String message = "0:type=ATM:card_number=ok";
                    encryptedData = session.encryptData(message);
                    session.sendData(encryptedData, outputStream);
                    ClientStatus = Status.WaitingForPIN;
                }
                case WaitingForPIN -> {
                    encryptedData = session.getData(inputStream);
                    byte plainData[] = session.decryptData(encryptedData);
                    String pinNumber = new String(plainData).split(":")[2].split("=")[1];
                    Server.log("Card PIN: " + pinNumber);
                    // check if PIN ok/bad?
                    String message = "0:type=ATM:card_pin=ok";
                    encryptedData = session.encryptData(message);
                    session.sendData(encryptedData, outputStream);
                    // if ok pull customer info
                    message = "0:type=ATM:full_name=andy swingz:balance=432431.25";
                    encryptedData = session.encryptData(message);
                    session.sendData(encryptedData, outputStream);
                }
                case MainMenu -> {
                    encryptedData = session.getData(inputStream);
                    byte plainData[] = session.decryptData(encryptedData);
                    String actionRequest = new String(plainData);
                    // withdraw or deposit
                }
            }
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
