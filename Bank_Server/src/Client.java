import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Motheen Baig
 */
public class Client implements Runnable {

    private final int clientID;
    private final Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public Client(final int clientID, final Socket socket) {
        this.clientID = clientID;
        this.socket = socket;
    }

    @Override
    public void run() {

    }

}
