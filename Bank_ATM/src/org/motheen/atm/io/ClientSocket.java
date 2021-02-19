package org.motheen.atm.io;

import org.motheen.atm.ClientGUI;
import org.motheen.server.ThreadHandler;

import javax.xml.crypto.Data;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Motheen Baig
 */
public class ClientSocket {

    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

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
        // send client public key
        // get server public key
    }

}
