package edu.jhu.teamundecided.clueless.client.startscreen;

import edu.jhu.teamundecided.clueless.client.ClientAppController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class StartScreen
{
    private JTextField _ipAddress;
    private JButton _hostButton;
    private JButton _joinButton;
    private JTextField _port;
    private JPanel logo;
    private JPanel _mainPanel;


    public StartScreen(ClientAppController cac)
    {
        _hostButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // Start Server
                cac.startServer();

                if(!cac.connect("localhost", 8818))
                {
                    // connection failed
                    System.exit(0);
                }

                cac.askForUserName();
            }
        });

        _joinButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(!cac.connect(_ipAddress.getText(), Integer.parseInt(_port.getText())))
                {
                    // connection failed
                    System.exit(0);
                }

                cac.askForUserName();
            }
        });

        _joinButton.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    _joinButton.doClick();
                }
            }
        });

        _port.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    _joinButton.doClick();
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
