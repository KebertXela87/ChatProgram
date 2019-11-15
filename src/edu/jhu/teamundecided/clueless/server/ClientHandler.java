package edu.jhu.teamundecided.clueless.server;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread
{
    private BufferedReader _reader; // Input from Client App
    private BufferedWriter _writer; // Output to Client App

    private Socket _clientSocket;
    private Server _server;
    private ServerApp _serverApp;

    public ClientHandler(Socket socket, Server server)
    {
        try
        {
            _reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            _writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            _clientSocket = socket;
            _server = server;
            _serverApp = server.getServerApp();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        String received;
        while (true)
        {
            try
            {
                received = _reader.readLine();

                _serverApp.writeToWindow(received);


                //TODO send message to method to process for different actions...


                if(received.equals("logoff"))
                {
                    _server.removeHandler(this);
                    _clientSocket.close();
                    break;
                }

                // broadcast
                for (ClientHandler client : _server.getCients())
                {
                    if(!client.equals(this))
                    {
                        client.writeToClient(received);
                    }
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public BufferedWriter getWriter()
    {
        return _writer;
    }

    private void writeToClient(String message)
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
}
