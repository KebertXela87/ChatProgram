package edu.jhu.teamundecided.clueless.client;

import edu.jhu.teamundecided.clueless.client.dialogs.DialogController;
import edu.jhu.teamundecided.clueless.client.startscreen.UserName;
import edu.jhu.teamundecided.clueless.client.startscreen.characterSelect;
import edu.jhu.teamundecided.clueless.database.Database;
import edu.jhu.teamundecided.clueless.server.Server;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class ClientAppController
{
    private ClientApp _clientApp;
    private characterSelect _characterSelect;

    private Socket _socket;
    private BufferedReader _reader; // Input from Server
    private BufferedWriter _writer; // Output to Server

    private JFrame _frame;

    private String _userName;
    private boolean _isHost = false;

    private boolean _clientRunning = true;

    public ClientAppController()
    {
        this._clientApp = new ClientApp(this);
        this._characterSelect = new characterSelect(this);
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

            startReadMessageThread(this);
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
        this._isHost =  true;
        Server server = new Server(8818);
        server.start();
    }

    public void startClient()
    {
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

    public void askForCharacterSelect()
    {
        writeToServer("getDisabledCharacters");
        _frame.setContentPane(new characterSelect(this).getSelectionPanel());
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
                while (_clientRunning)
                {
                    try
                    {
                        if(!_socket.isClosed())
                        {
                            controller.handleMessage(controller.getReader().readLine());
                        }
                    }
                    catch (IOException e)
                    {
                        System.out.println("Client Closed.");
                    }
                }
            }
        });

        readMessage.start();
    }

    public void handleMessage(String message) throws IOException
    {
        String[] tokens = message.split(":", 2);

        switch(tokens[0])
        {
            case "serverclose":
                _reader.close();
                _writer.close();
                _socket.close();
                getClientApp().writeToScreen("The Server has closed! The host has logged off!");
                break;
            case "disableCharacter":
                addToClientDisabledCharacterList(tokens[1]);
                break;
            case "moveDialog":
                DialogController.getInstance().createMoveDialog(this, tokens[1]);
                break;
            case "suggestDialog":
                DialogController.getInstance().createSuggestDialog(this, tokens[1]);
                break;
            case "playerHandDialog":
                DialogController.getInstance().createPlayerHandDialog(this, tokens[1]);
                break;
            case "moveSprites":
                _clientApp.getClientGameBoard().handleSpriteMovement(tokens[1]);
                break;
            default:
                getClientApp().writeToScreen(message);
        }
    }

    private void addToClientDisabledCharacterList(String list)
    {
        Database.getInstance().getLock().lock();
        if (!list.equals(""))
        {
            String[] tokens = list.split(":");
            for (String token : tokens)
            {
                Database.getInstance().getDisabledCharacters().add(token);
            }
        }
        Database.getInstance().getLock().unlock();
    }

    public void logout()
    {
        try
        {
            // Close connection to server
            if (!_socket.isClosed())
            {
                if(_isHost)
                {
                    writeToServer("shutdownServer");
                }
                else
                {
                    writeToServer("logoff:" + _userName);
                }
                _clientRunning = false;
                _socket.close();
            }
        }
        catch (IOException ex)
        {
            System.out.println("Socket Exception");
            ex.printStackTrace();
        }

        System.out.println("Exiting...");
        // Close window
        System.exit(0);
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

    public characterSelect getCharacterSelect() { return _characterSelect; }

    public void setUserName(String name)
    {
        _userName = name;
    }

    public String getUserName()
    {
        return _userName;
    }
}
