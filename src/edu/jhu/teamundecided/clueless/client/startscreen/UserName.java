package edu.jhu.teamundecided.clueless.client.startscreen;

import edu.jhu.teamundecided.clueless.client.ClientAppController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class UserName
{
    private JPanel _mainPanel;
    private JPanel logo;
    private JTextField _userName;
    private JButton _sendButton;

    public UserName(ClientAppController cac)
    {
        _sendButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                cac.setUserName(_userName.getText());
                cac.writeToServer("user:" + cac.getUserName());

                cac.askForCharacterSelect();
            }
        });

        _userName.addKeyListener(new KeyAdapter()
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
    }

    public JPanel getMainPanel()
    {
        return _mainPanel;
    }

    private void createUIComponents()
    {
        logo = new Logo();
    }
}
