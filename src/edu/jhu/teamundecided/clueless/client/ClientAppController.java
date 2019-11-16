package edu.jhu.teamundecided.clueless.client;

import edu.jhu.teamundecided.clueless.client.startscreen.StartScreen;
import edu.jhu.teamundecided.clueless.client.startscreen.UserName;
import edu.jhu.teamundecided.clueless.database.Database;
import edu.jhu.teamundecided.clueless.server.Server;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;

public class ClientAppController
{
    private ClientApp _clientApp;

    private Socket _socket;
    private BufferedReader _reader; // Input from Server
    private BufferedWriter _writer; // Output to Server

    private JFrame _frame;

    private String _userName;

    private boolean _clientRunning = true;

    public ClientAppController()
    {
        this._clientApp = new ClientApp(this);
        this._frame = new JFrame("ClueLess");
    }

    public JFrame getFrame() { return this._frame; }

    public boolean connect(String ipaddress, int port)
    {
        try
        {
            _socket = new Socket(ipaddress, port);
            _reader = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
            _writer = new BufferedWriter(new OutputStreamWriter(_socket.getOutputStream()));

            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public void startServer()
    {
        // Start Server
        Server server = new Server(8818);
        server.start();
    }

    public void startClient()
    {
        startReadMessageThread(this);

        _frame.setContentPane(_clientApp.returnMainPanel());
        _frame.pack();
        _frame.setLocationRelativeTo(null);
        _frame.setVisible(true);
        _clientApp.writeToScreen("Welcome " + _userName + "!...");
    }

    public void askForUserName()
    {
        _frame.setContentPane(new UserName(this).getMainPanel());
        _frame.pack();
        _frame.setLocationRelativeTo(null);
        _frame.setVisible(true);
    }

    public void startReadMessageThread(ClientAppController controller)
    {
        Thread readMessage = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                String msg = "";
                while (_clientRunning)
                {
                    try
                    {
                        if(!_socket.isClosed())
                        {
                            msg = controller.getReader().readLine();

                            controller.handleMessage(msg);
                        }
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });

        readMessage.start();
    }

    public void handleMessage(String message) throws IOException
    {
        switch(message)
        {
            case "serverclose":
                Database.getInstance().setRunning(false);
                _reader.close();
                _writer.close();
                _socket.close();
                getClientApp().writeToScreen("Server Closed!");
                break;
            default:
                getClientApp().writeToScreen(message);
        }
    }

    public void clientCloseOperation(JFrame frame)
    {
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                try
                {
                    System.out.println("Closing Window...");
                    // Close connection to server
                    if(_socket != null)
                    {
                        System.out.println("Closing stream and socket...");
                        _clientRunning = false;
                        _socket.close();
                        System.out.println("Closed streams and socket.");
                    }
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }

                System.out.println("Exiting...");
                // Close window
                System.exit(0);
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void writeToServer(String message)
    {
        try
        {
            _writer.write(message);
            _writer.newLine();
            _writer.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public BufferedReader getReader()
    {
        return _reader;
    }

    public ClientApp getClientApp() { return _clientApp; }

    public void setUserName(String name)
    {
        _userName = name;
    }

    public String getUserName()
    {
        return _userName;
    }
}
