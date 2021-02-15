import javax.swing.*;
import java.awt.*;

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
    public static State ClientState = State.EnterCard;
    //
    private String cardNumber = "", cardPIN = "", withdraw = "0.00", deposit = "", accountBalance = "1419.32";

    public ClientGUI() {
        setTitle("WorldBank ATM - Motheen Baig");
        setSize(clientW, clientH);
        addKeyListener(inputHandler);
        addMouseListener(inputHandler);
        addMouseMotionListener(inputHandler);
        setResizable(false);
        setVisible(true);
        image = createImage(clientW, clientH);
        graphics = image.getGraphics();
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
