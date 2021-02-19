package org.motheen.server;

import javax.xml.crypto.Data;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Motheen Baig
 */
public class Client {

    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

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
        // get client public key
        // send server public key
    }

}
