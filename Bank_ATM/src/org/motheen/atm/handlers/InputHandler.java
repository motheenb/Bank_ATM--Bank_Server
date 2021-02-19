package org.motheen.atm.handlers;

import org.motheen.atm.ClientGUI;
import org.motheen.atm.State;

import java.awt.event.*;

public class InputHandler implements MouseListener, MouseMotionListener, KeyListener {

    private final ClientGUI clientGUI;

    public InputHandler(final ClientGUI clientGUI) {
        this.clientGUI = clientGUI;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        char keyChar = e.getKeyChar();
        ClientGUI.log("KeyPressed: " + keyCode);
        if (keyCode > 47 && keyCode < 58) {
            if (ClientGUI.ClientState.equals(State.EnterCard)) {
                if (clientGUI.getCardNumber().length() < 16) {
                    clientGUI.setCardNumber(clientGUI.getCardNumber() + keyChar);
                }
            } else if (ClientGUI.ClientState.equals(State.EnterPIN)) {
                if (clientGUI.getCardPIN().length() < 4) {
                    clientGUI.setCardPIN(clientGUI.getCardPIN() + keyChar);
                }
            } else if (ClientGUI.ClientState.equals(State.Withdraw)) {
                if (clientGUI.getWithdraw().length() < 9000) {
                    clientGUI.setWithdraw(clientGUI.getWithdraw() + keyChar);
                }
            }
        } else if (keyCode == 8) {
            if (ClientGUI.ClientState.equals(State.EnterCard)) {
                clientGUI.setCardNumber(removeLastChar(clientGUI.getCardNumber()));
            } else if (ClientGUI.ClientState.equals(State.EnterPIN)) {
                clientGUI.setCardPIN(removeLastChar(clientGUI.getCardPIN()));
            } else if (ClientGUI.ClientState.equals(State.Withdraw)) {
                clientGUI.setWithdraw(removeLastChar(clientGUI.getWithdraw()));
            }
        } else if (keyCode == 10) {
            if (ClientGUI.ClientState.equals(State.EnterCard)) {

            }
        }
        if (ClientGUI.ClientState.equals(State.MainMenu)) {
            if (keyCode == 49) { // withdraw
                ClientGUI.ClientState = State.Withdraw;
            } else if (keyCode == 50) { // deposit
                ClientGUI.ClientState = State.Deposit;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public String removeLastChar(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

}
