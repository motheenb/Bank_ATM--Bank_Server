import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHandler {

    private final Client clients[] = new Client[10];
    private final Server server;

    public ClientHandler(final Server server) {
        this.server = server;
    }

    public void acceptConnection(final ServerSocket serverSocket) {
        Socket socket;
        try {
            socket = serverSocket.accept();
            Server.log("Accepted connection from: " + socket.getInetAddress());
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

}
