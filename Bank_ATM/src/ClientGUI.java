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

    public ClientGUI() {
        setTitle("WorldBank ATM - Motheen Baig");
        setSize(clientW, clientH);
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
        // Draw
        g.drawImage(image, 0, 0, this);
    }

    public static void log(final Object o) {
        System.out.println(o);
    }

    public static void main(final String...args) {
        new ClientGUI();
    }

}
