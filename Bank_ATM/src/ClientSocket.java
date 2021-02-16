import java.io.*;
import java.net.Socket;

/**
 *
 * @author Motheen Baig
 */
public class ClientSocket implements Runnable {

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    //
    private final ClientGUI clientGUI;

    public ClientSocket(final ClientGUI clientGUI) {
        this.clientGUI = clientGUI;
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {
            // test
        }
     }

     public ClientSocket openStreams() {
         try {
             this.objectInputStream = new ObjectInputStream(socket.getInputStream());
             this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
         } catch (final IOException e) {
             e.printStackTrace();
         }
         return this;
     }

    public ClientSocket connectToServer() {
        try {
            this.socket = new Socket("127.0.0.1", 43594);
        } catch (final IOException e) {
            //e.printStackTrace();
        }
        return this;
    }

    public Socket getSocket() {
        return socket;
    }

}
