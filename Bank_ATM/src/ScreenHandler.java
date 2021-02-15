import java.awt.*;
import java.text.DecimalFormat;

public class ScreenHandler {

    private final ClientGUI clientGUI;
    //
    private Graphics2D graphics2D;
    private String errorMessage = "";

    public ScreenHandler(final ClientGUI clientGUI) {
        this.clientGUI = clientGUI;
    }

    public void paint(final Graphics g) {
        graphics2D = (Graphics2D) g.create();
        graphics2D.fillRect(0, 0, clientGUI.getClientW(), clientGUI.getClientH());
        if (errorMessage != "") {
            graphics2D.setColor(Color.RED);
            drawCenteredString("" + errorMessage, clientGUI.getClientW(), clientGUI.getClientH(), -25, graphics2D);
        }
        switch (ClientGUI.ClientState) {
            case ConnectingToServer -> {
                graphics2D.setColor(Color.GREEN);
                drawCenteredString("Connecting to server.", clientGUI.getClientW(), clientGUI.getClientH(), 0, graphics2D);
            }
            case EnterCard -> {
                graphics2D.setColor(Color.GREEN);
                drawCenteredString("Enter 16-digit Card Number: ", clientGUI.getClientW(), clientGUI.getClientH(), 0, graphics2D);
                graphics2D.setColor(Color.YELLOW);
                drawCenteredString(clientGUI.getCardNumber() + "*", clientGUI.getClientW(), clientGUI.getClientH(), 25, graphics2D);
            }
            case EnterPIN -> {
                graphics2D.setColor(Color.GREEN);
                drawCenteredString("Enter 4-digit PIN Number: ", clientGUI.getClientW(), clientGUI.getClientH(), 0, graphics2D);
                graphics2D.setColor(Color.YELLOW);
                drawCenteredString(getBlockedPIN() + "*", clientGUI.getClientW(), clientGUI.getClientH(), 25, graphics2D);
            }
            case MainMenu -> {
                graphics2D.setColor(Color.YELLOW);
                drawCenteredString("Hi, Motheen Baig!", clientGUI.getClientW(), clientGUI.getClientH(), 0, graphics2D);
                double balance = 3142344.91;
                drawCenteredString("Account Balance: " + formatDollars(balance), clientGUI.getClientW(), clientGUI.getClientH(), 25, graphics2D);
                drawCenteredString("Press 1. to Withdraw", clientGUI.getClientW(), clientGUI.getClientH(), 50, graphics2D);
                drawCenteredString("Press 2. to Deposit", clientGUI.getClientW(), clientGUI.getClientH(), 75, graphics2D);
            }
            case Withdraw -> {
                graphics2D.setColor(Color.GREEN);
                drawCenteredString("Balance: " + formatDollars(Double.parseDouble(clientGUI.getAccountBalance())), clientGUI.getClientW(), clientGUI.getClientH(), 0, graphics2D);
                graphics2D.setColor(Color.YELLOW);
                drawCenteredString("Enter Amount You'd Like To Withdraw: ", clientGUI.getClientW(), clientGUI.getClientH(), 25, graphics2D);
                drawCenteredString(formatDollars(Double.parseDouble(clientGUI.getWithdraw())), clientGUI.getClientW(), clientGUI.getClientH(), 50, graphics2D);
            }
            case Deposit -> {
                graphics2D.setColor(Color.GREEN);
                drawCenteredString("Enter Amount You'd Like To Deposit: ", clientGUI.getClientW(), clientGUI.getClientH(), 0, graphics2D);
                graphics2D.setColor(Color.YELLOW);
                drawCenteredString("*", clientGUI.getClientW(), clientGUI.getClientH(), 25, graphics2D);
            }
            case Transactions -> {
                // Display last 10 transactions
            }
        }
    }

    public String formatDollars(final double amount) {
        String result = new DecimalFormat("#,###.00").format(amount);
        if (result.startsWith(".")) {
            result = "0" + result;
        }
        return result;
    }

    public String getBlockedPIN() {
        String asteriskPIN = "";
        for (int i = 0; i < clientGUI.getCardPIN().length(); i++) {
            asteriskPIN += "*";
        }
        return asteriskPIN;
    }

    public void drawCenteredString(final String s, final int w, final int h, final int yOffset, final Graphics g) {
        final FontMetrics fm = g.getFontMetrics();
        final int x = (w - fm.stringWidth(s)) / 2;
        final int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2) + yOffset;
        g.drawString(s, x, y);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
