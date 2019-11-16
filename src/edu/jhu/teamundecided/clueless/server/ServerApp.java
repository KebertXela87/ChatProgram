package edu.jhu.teamundecided.clueless.server;

import javax.swing.*;

public class ServerApp extends JFrame
{
    private JTextArea _messageCenter;
    private JPanel _serverPanel;

    private JFrame _frame;

    public ServerApp()
    {
        _frame = new JFrame("ServerApp");
        _frame.setContentPane(_serverPanel);
        _frame.pack();
        _frame.setVisible(true);
    }

    public JFrame getServerFrame()
    {
        return _frame;
    }

    public void writeToWindow(String message)
    {
        _messageCenter.setText(_messageCenter.getText().trim() + "\n" + message);
    }
}
