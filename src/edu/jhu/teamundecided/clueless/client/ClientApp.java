package edu.jhu.teamundecided.clueless.client;

import edu.jhu.teamundecided.clueless.client.dialogs.DialogController;
import edu.jhu.teamundecided.clueless.client.gameboard.ClientGameBoard;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;

public class ClientApp
{
    private JTextArea _messageCenter;
    private JTextField _messageField;
    private JButton _sendButton;
    private JPanel _chatPanel;
    private JPanel _gameBoard;
    private JPanel _controls;
    private JButton _moveButton;
    private JButton _suggestButton;
    private JButton _accusationButton;
    private JButton _notebookButton;
    private JPanel _mainPanel;
    private JButton _endTurnButton;
    private JButton _logoutButton;
    private JButton _handButton;
    private JButton _readyButton;

    public ClientApp(ClientAppController controller)
    {
        _sendButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                controller.writeToServer("chat:" + controller.getUserName() + " > " + _messageField.getText());
                _messageField.setText(""); // clear text field
            }
        });

        _messageField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    _sendButton.doClick();
                }
            }
        });

        // MOVE BUTTON
        _moveButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // send message to server to get possible room locations
                controller.writeToServer("getmoves:" + controller.getUserName());
            }
        });

        // SUGGEST BUTTON
        _suggestButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                controller.writeToServer("getSuggestRoom:" + controller.getUserName());
            }
        });

        // ACCUSE BUTTON
        _accusationButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                DialogController.getInstance().createAccuseDialog(controller);
            }
        });

        // PLAYER HAND BUTTON
        _handButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                controller.writeToServer("getPlayerHand:" + controller.getUserName());
            }
        });

        // NOTEBOOK BUTTON
        _notebookButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                DialogController.getInstance().createNotebookDialog(controller);
            }
        });

        // END TURN BUTTON
        _endTurnButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                _moveButton.setEnabled(false);
                _suggestButton.setEnabled(false);
                _accusationButton.setEnabled(false);
                _endTurnButton.setEnabled(false);

                _messageField.grabFocus(); // set focus to the chat text field

                controller.writeToServer("endturn:" + controller.getUserName());
            }
        });

        // LOGOUT BUTTON
        _logoutButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                controller.getFrame().dispatchEvent(new WindowEvent(controller.getFrame(), WindowEvent.WINDOW_CLOSING));
            }
        });

        _readyButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                controller.writeToServer("readytoplay");
                _readyButton.setEnabled(false);
            }
        });
    }

    public void writeToScreen(String message)
    {
        _messageCenter.setText(_messageCenter.getText().trim() + "\n" + message);
        DefaultCaret caret = (DefaultCaret)_messageCenter.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    }

    public JPanel returnMainPanel() { return _mainPanel; }

    public Component[] getControlButtons() { return _controls.getComponents(); }

    public JButton getMoveButton()
    {
        return _moveButton;
    }

    public JButton getSuggestButton()
    {
        return _suggestButton;
    }

    public JButton getAccusationButton()
    {
        return _accusationButton;
    }

    public JButton getEndTurnButton()
    {
        return _endTurnButton;
    }

    public JButton getPlayerHandButton() { return _handButton; }

    public JButton getNotebookButton() { return _notebookButton; }

    public JTextField getMessageField() { return _messageField; }

    public ClientGameBoard getClientGameBoard() { return (ClientGameBoard) _gameBoard; }

    private void createUIComponents()
    {
        _gameBoard = new ClientGameBoard();
    }
}
