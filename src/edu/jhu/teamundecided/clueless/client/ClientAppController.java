package edu.jhu.teamundecided.clueless.client;

import edu.jhu.teamundecided.clueless.client.startscreen.StartScreen;

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

    public ClientAppController()
    {
        this._clientApp = new ClientApp(this);
        this._frame = new JFrame("ClueLess");
    }

    public JFrame getFrame() { return this._frame; }

    public static void main(String[] args)
    {
        ClientAppController controller = new ClientAppController();

        //TODO move this to a listener due to addition of StartScreen
        if(!controller.connect())
        {
            // connection failed
            System.exit(0);
        }

        controller.clientCloseOperation(controller.getFrame());

        // Set Start Screen
        controller.getFrame().setContentPane(new StartScreen(controller).getMainPanel());

        controller.getFrame().pack();
        controller.getFrame().setVisible(true);

        //TODO move this to a listener with the Connect logic
        Thread readMessage = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (true)
                {
                    try
                    {
                        String msg = controller.getReader().readLine();
                        controller.getClientApp().writeToScreen(msg);
                        /*
                        TODO
                        Add method here that will take the String input and pass it to a
                        command table.
                         */
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

    private boolean connect()
    {
        try
        {
            _socket = new Socket("localhost", 8818);
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

    private void clientCloseOperation(JFrame frame)
    {
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                writeToServer("logoff");
                System.exit(0);
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    void writeToServer(String message)
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
}
