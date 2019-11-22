package edu.jhu.teamundecided.clueless.client.dialogs;

import edu.jhu.teamundecided.clueless.client.ClientApp;
import edu.jhu.teamundecided.clueless.client.ClientAppController;
import edu.jhu.teamundecided.clueless.database.Database;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DialogController
{
    private String filepath = "./src/images/cards/rooms/200w/";

    private static DialogController _dialogController = null;

    public static DialogController getInstance()
    {
        if (_dialogController == null)
        {
            _dialogController = new DialogController();
        }

        return _dialogController;
    }

    private String _moveOptions;
    private ArrayList<JRadioButton> moveList;

    private String _suggestionRoom;

    private playerHandDialog _playerDialog = null;
    private String _playerHand;

    public DialogController()
    {}

    /// MOVE DIALOG LOGIC
    public void createMoveDialog(ClientAppController controller, String moveOptions)
    {
        setMoveOptions(moveOptions);
        moveDialog dialog = new moveDialog(controller);
        dialog.setLocationRelativeTo(controller.getFrame());
        dialog.setTitle("Make an Move!");
        dialog.setResizable(false);
        dialog.pack();
        dialog.setVisible(true);
    }

    public String getMoveOptions()
    {
        return _moveOptions;
    }

    public void setMoveOptions(String _moveOptions)
    {
        this._moveOptions = _moveOptions;
    }

    public JPanel getMovePanel()
    {
        String[] options = getMoveOptions().split(":");

        JPanel movePanel = new JPanel();

        ButtonGroup moveButtonGroup = new ButtonGroup();

        resetMoveList();

        for (String option : options)
        {
            JRadioButton temp = new JRadioButton(new ImageIcon(filepath + option + ".jpg"));
            temp.setSelectedIcon(new ImageIcon(filepath + option + "Selected.jpg"));
            temp.setName(option);
            moveButtonGroup.add(temp);
            moveList.add(temp);
            movePanel.add(temp);
        }

        return movePanel;
    }

    public ArrayList<JRadioButton> getMoveList()
    {
        return moveList;
    }

    public void resetMoveList()
    {
        if(moveList == null)
        {
            moveList = new ArrayList<>();
        }
        else
        {
            moveList.clear();
        }
    }

    /// SUGGEST DIALOG LOGIC
    public void createSuggestDialog(ClientAppController controller, String room)
    {
        _suggestionRoom = room;
        suggestDialog dialog = new suggestDialog(controller);
        dialog.setLocationRelativeTo(controller.getFrame());
        dialog.setTitle("Make an Suggestion!");
        dialog.setResizable(false);
        dialog.pack();
        dialog.setVisible(true);
    }

    public String getSuggestionRoom() { return _suggestionRoom; }

    /// ACCUSE DIALOG LOGIC
    public void createAccuseDialog(ClientAppController controller)
    {
        accuseDialog dialog = new accuseDialog(controller);
        dialog.setLocationRelativeTo(controller.getFrame());
        dialog.setTitle("Make an Accusation!");
        dialog.setResizable(false);
        dialog.pack();
        dialog.setVisible(true);
    }

    /// PLAYER HAND PANEL
    public void createPlayerHandDialog(ClientAppController controller, String hand)
    {
        // Check to see if hand has changed
        if(_playerDialog != null)
        {
            if (!_playerHand.equals(hand))
            {
                _playerDialog.dispose();
            }
            else
            {
                _playerDialog.toFront();
                return;
            }
        }

        setPlayerHandCards(hand);
        _playerDialog = new playerHandDialog();
        _playerDialog.setLocationRelativeTo(controller.getFrame());
        _playerDialog.setModal(false);
        _playerDialog.setTitle("Player Hand");
        _playerDialog.setResizable(false);
        _playerDialog.pack();
        _playerDialog.setVisible(true);
    }

    public JPanel getPlayerHandPanel()
    {
        String[] cards = getPlayerHandCards().split(":");

        JPanel playerHandPanel = new JPanel();

        for (String card : cards)
        {
            String localFilePath = "./src/images/cards/";
            if(Database.getInstance().getCharacterNames().keySet().contains(card))
            {
                localFilePath += "suspects/250w/";
            }
            else if(Database.getInstance().getWeaponNames().keySet().contains(card))
            {
                localFilePath += "weapons/250w/";
            }
            else if(Database.getInstance().getRoomNames().keySet().contains(card))
            {
                localFilePath += "rooms/200w/";
            }

            JLabel temp = new JLabel(new ImageIcon(localFilePath + card + ".jpg"));
            playerHandPanel.add(temp);
        }

        return playerHandPanel;
    }

    private String getPlayerHandCards() { return _playerHand; }

    public void setPlayerHandCards(String cards)
    {
        _playerHand = cards;
    }

    public void setPlayerDialogNull()
    {
        _playerDialog = null;
    }
}
