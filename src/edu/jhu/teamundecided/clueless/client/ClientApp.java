package edu.jhu.teamundecided.clueless.client;

import edu.jhu.teamundecided.clueless.client.gameboard.ClientGameBoard;
import edu.jhu.teamundecided.clueless.client.gameboard.PlayerSprite;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ClientApp
{
    private JTextArea _messageCenter;
    private JTextField _messageField;
    private JButton _sendButton;
    private JPanel _chatPanel;
    private JPanel _playerHand;
    private JPanel _gameBoard;
    private JPanel _controls;
    private JButton _moveButton;
    private JButton _suggestButton;
    private JButton _accusationButton;
    private JButton _notebookButton;
    private JPanel _mainPanel;

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

        _moveButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                for (PlayerSprite sprite : ((ClientGameBoard) _gameBoard).getSprites())
                {
                    sprite.setDestX(sprite.getX() + 100);
                }
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

    private void createUIComponents()
    {
        _gameBoard = new ClientGameBoard();
    }
}
