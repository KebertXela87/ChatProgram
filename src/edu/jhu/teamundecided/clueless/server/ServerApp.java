package edu.jhu.teamundecided.clueless.server;

import javax.swing.*;

public class ServerApp extends JFrame
{
    private JTextArea _messageCenter;
    private JPanel _serverPanel;

    public ServerApp()
    {
        JFrame frame = new JFrame("ServerApp");
        frame.setContentPane(_serverPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void writeToWindow(String message)
    {
        _messageCenter.setText(_messageCenter.getText().trim() + "\n" + message);
    }
}
