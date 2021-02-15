import handlers.ThreadHandler;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 * @author Motheen Baig
 */
public class ClientGUI extends JFrame {

    private Image image;
    private Graphics graphics;
    private final int clientW = 500, clientH = 500;
    //
    private final ScreenHandler screenHandler = new ScreenHandler(this);
    //
    private final InputHandler inputHandler = new InputHandler(this);
    //
    public static State ClientState = State.ConnectingToServer;
    //
    private String cardNumber = "", cardPIN = "", withdraw = "0.00", deposit = "", accountBalance = "1419.32";
    //
    private ClientSocket clientSocket;

    public ClientGUI() {
        if (ClientState.equals(State.ConnectingToServer)) {
            Future<Boolean> connectToServer = ThreadHandler.get().submit(() -> {
                clientSocket = new ClientSocket(this).connectToServer();
                // TimeUnit.SECONDS.sleep(1);
                return clientSocket.getSocket() != null;
            });
            while (!connectToServer.isDone()) {
                if (!isVisible()) {
                    setTitle("WorldBank ATM - Motheen Baig");
                    setSize(clientW, clientH);
                    addKeyListener(inputHandler);
                    addMouseListener(inputHandler);
                    addMouseMotionListener(inputHandler);
                    setResizable(false);
                    setVisible(true);
                    image = createImage(clientW, clientH);
                    graphics = image.getGraphics();
                } else
                    continue;
            }
            try {
                if (connectToServer.get()) {
                    ClientGUI.log("Connected to server.");
                    ClientState = State.EnterCard;
                    ThreadHandler.get().execute(() -> clientSocket.openStreams());
                } else {
                    ClientGUI.log("Client could not to server.");
                    screenHandler.setErrorMessage("Error!"); // strike through 'connecting to server'
                }
            } catch (final InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public void paint(final Graphics g) {
        if (image == null) {
            return;
        }
        graphics.clearRect(0, 0, clientW, clientH);
        //
        screenHandler.paint(graphics);
        //
        g.drawImage(image, 0, 0, this);
        repaint();
    }

    public static void log(final Object o) {
        System.out.println(o);
    }

    public int getClientW() {
        return clientW;
    }

    public int getClientH() {
        return clientH;
    }

    public static void main(final String...args) {
        new ClientGUI();
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(final String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardPIN() {
        return cardPIN;
    }

    public void setCardPIN(final String cardPIN) {
        this.cardPIN = cardPIN;
    }

    public String getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(final String withdraw) {
        this.withdraw = withdraw;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(final String deposit) {
        this.deposit = deposit;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(final String accountBalance) {
        this.accountBalance = accountBalance;
    }
}
