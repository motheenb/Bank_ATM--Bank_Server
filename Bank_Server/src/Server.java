import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    private final int port;
    private ServerSocket serverSocket;
    private final ClientHandler clientHandler = new ClientHandler(this);

    public Server(final int port) {
        this.port = port;
    }

    public Server start() {
        try {
            this.serverSocket = new ServerSocket(this.port);
            Server.log("Server listening on port: " + this.port);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Server listen() {
        ThreadManager.get().execute(() -> {
            while (!serverSocket.isClosed()) {
                clientHandler.acceptConnection(serverSocket);
            }
        });
        return this;
    }

    public static void log(final Object o) {
        System.out.println(o);
    }

    public static void main(final String...args) {
        new Server(43594).start().listen();
    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }
}
