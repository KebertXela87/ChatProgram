package edu.jhu.teamundecided.clueless.client;

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

    public ClientAppController()
    {
        this._clientApp = new ClientApp(this);
    }


    public static void main(String[] args)
    {
        ClientAppController controller = new ClientAppController();

        if(!controller.connect())
        {
            // connection failed
            System.exit(0);
        }

        JFrame frame = new JFrame("ClueLess");

        controller.clientCloseOperation(frame);

        frame.setContentPane(controller.getClientApp().returnMainPanel());
        frame.pack();
        frame.setVisible(true);

        controller.getClientApp().writeToScreen("Welcome User...");

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

    private ClientApp getClientApp() { return _clientApp; }
}
