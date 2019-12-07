package edu.jhu.teamundecided.clueless.client.dialogs;

import edu.jhu.teamundecided.clueless.client.ClientAppController;
import edu.jhu.teamundecided.clueless.database.Database;

import javax.swing.*;
import java.util.ArrayList;

public class DialogController
{
    private String _cardFilepath = "./src/images/cards/";

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

    private notebookDialog _notebookDialog = null;

    private String _suggestOptions;
    private ButtonGroup disproveButtonGroup;

    private String _revealedCard;

    private String _characterName;

    private String _accusationResponse;
    private String _casefileSuspect;
    private String _casefileWeapon;
    private String _casefileRoom;

    public DialogController()
    {}

    /// MOVE DIALOG LOGIC
    public void createMoveDialog(ClientAppController controller, String moveOptions)
    {
        setMoveOptions(moveOptions);
        moveDialog dialog = new moveDialog(controller);
        dialog.setTitle("Make an Move!");
        dialog.setResizable(false);
        dialog.pack();
        dialog.setLocationRelativeTo(controller.getFrame());
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
        String[] options;
        String[] hallwayDir = new String[0];
        JPanel movePanel = new JPanel();
        ButtonGroup moveButtonGroup = new ButtonGroup();
        resetMoveList();

        // CHECK FOR HALLWAYS
        if(getMoveOptions().contains("#"))
        {
            // SPLIT OUT DIRECTIONS
            String[] temp = getMoveOptions().split("#");

            options = temp[0].split(":"); // room names
            hallwayDir = temp[1].split(":"); // hallway directions
        }
        else
        {
            // NO HALLWAYS
            options = getMoveOptions().split(":");
        }

        for (String option : options)
        {
            JRadioButton temp;

            // OPTION IS A HALLWAY, GET DIRECTION
            if(option.contains("hallway_"))
            {
                String hallwayDirection = "";
                for(String dir : hallwayDir)
                {
                    if(dir.startsWith(option))
                    {
                        hallwayDirection = dir.split(",")[1];
                    }
                }

                temp = new JRadioButton(new ImageIcon(_cardFilepath + "rooms/" + hallwayDirection + ".jpg"));
                temp.setSelectedIcon(new ImageIcon(_cardFilepath + "rooms/selected/" + hallwayDirection + "Selected.jpg"));
            }
            // NOT A HALLWAY
            else
            {
                temp = new JRadioButton(new ImageIcon(_cardFilepath + "rooms/" + option + ".jpg"));
                temp.setSelectedIcon(new ImageIcon(_cardFilepath + "rooms/selected/" + option + "Selected.jpg"));
            }

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
        dialog.setTitle("Make a Suggestion!");
        dialog.setResizable(false);
        dialog.pack();
        dialog.setLocationRelativeTo(controller.getFrame());
        dialog.setVisible(true);
    }

    public String getSuggestionRoom() { return _suggestionRoom; }

    /// ACCUSE DIALOG LOGIC
    public void createAccuseDialog(ClientAppController controller)
    {
        accuseDialog dialog = new accuseDialog(controller);
        dialog.setTitle("Make an Accusation!");
        dialog.setResizable(false);
        dialog.pack();
        dialog.setLocationRelativeTo(controller.getFrame());
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
        _playerDialog.setModal(false);
        _playerDialog.setTitle("Player Hand");
        _playerDialog.setResizable(false);
        _playerDialog.pack();
        _playerDialog.setLocationRelativeTo(controller.getFrame());
        _playerDialog.setVisible(true);
    }

    public JPanel getPlayerHandPanel()
    {
        String[] cards = getPlayerHandCards().split(":");

        JPanel playerHandPanel = new JPanel();

        for (String card : cards)
        {
            String subdir = "";
            if(Database.getInstance().getCharacterNames().keySet().contains(card))
            {
                subdir += "suspects/";
            }
            else if(Database.getInstance().getWeaponNames().keySet().contains(card))
            {
                subdir += "weapons/";
            }
            else if(Database.getInstance().getRoomNames().keySet().contains(card))
            {
                subdir += "rooms/";
            }

            JLabel temp = new JLabel(new ImageIcon(_cardFilepath + subdir + card + ".jpg"));
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

    /// NOTEBOOK PANEL
    public void createNotebookDialog(ClientAppController controller)
    {
        if(_notebookDialog == null)
        {
            _notebookDialog =  new notebookDialog();
        }

        _notebookDialog.setModal(false);
        _notebookDialog.setTitle("Notebook");
        _notebookDialog.setResizable(false);
        _notebookDialog.pack();
        _notebookDialog.setLocationRelativeTo(controller.getFrame());
        _notebookDialog.setVisible(true);
    }

    // DISPROVE PANEL
    public void createDisproveDialog(ClientAppController controller, String suggestOptions)
    {
        setSuggestOptions(suggestOptions);
        disproveDialog dialog = new disproveDialog(controller);
        dialog.setTitle("Disprove the Suggestion!");
        dialog.setResizable(false);
        dialog.pack();
        dialog.setLocationRelativeTo(controller.getFrame());
        dialog.setVisible(true);
    }

    public String getSuggestOptions()
    {
        return _suggestOptions;
    }

    public void setSuggestOptions(String suggestOptions)
    {
        this._suggestOptions = suggestOptions;
    }

    public ButtonGroup getDisproveButtonGroup()
    {
        return disproveButtonGroup;
    }

    public JPanel getDisprovePanel()
    {
        String[] options;
        JPanel disprovePanel = new JPanel();
        disproveButtonGroup = new ButtonGroup();

        options = getSuggestOptions().split(":");

        for (String option : options)
        {
            JRadioButton temp;
            String subdir = "";
            if(Database.getInstance().getCharacterNames().containsKey(option))
            {
                subdir = "suspects/";
            }
            if(Database.getInstance().getWeaponNames().containsKey(option))
            {
                subdir = "weapons/";
            }
            if(Database.getInstance().getRoomNames().containsKey(option))
            {
                subdir = "rooms/";
            }

            temp = new JRadioButton(new ImageIcon(_cardFilepath + subdir + option + ".jpg"));
            temp.setSelectedIcon(new ImageIcon(_cardFilepath + subdir + "selected/" + option + "Selected.jpg"));

            temp.setName(option);
            disproveButtonGroup.add(temp);
            disprovePanel.add(temp);
        }

        return disprovePanel;
    }

    // REVEALED PANEL
    public void createRevealedDialog(ClientAppController controller, String revealedCard)
    {
        setRevealedCard(revealedCard);
        revealedDialog dialog = new revealedDialog(controller);
        dialog.setTitle("Revealed Card!");
        dialog.setResizable(false);
        dialog.pack();
        dialog.setLocationRelativeTo(controller.getFrame());
        dialog.setVisible(true);
    }

    public String getRevealedCard()
    {
        StringBuilder revealedCardDir = new StringBuilder(_cardFilepath);

        if(Database.getInstance().getCharacterNames().containsKey(_revealedCard))
        {
            revealedCardDir.append("suspects/");
        }
        if(Database.getInstance().getWeaponNames().containsKey(_revealedCard))
        {
            revealedCardDir.append("weapons/");
        }
        if(Database.getInstance().getRoomNames().containsKey(_revealedCard))
        {
            revealedCardDir.append("rooms/");
        }

        revealedCardDir.append(_revealedCard).append(".jpg");

        return revealedCardDir.toString();
    }

    public void setRevealedCard(String revealedCard)
    {
        this._revealedCard = revealedCard;
    }

    // START TURN PANEL
    public void createStartTurnDialog(ClientAppController controller, String startTurnMsg)
    {
        String[] tokens = startTurnMsg.split(":", 2);
        setCharacterName(tokens[0]);
        startTurnDialog dialog = new startTurnDialog(controller, tokens[1]);
        dialog.setTitle("Start Turn!");
        dialog.setResizable(false);
        dialog.pack();
        dialog.setLocationRelativeTo(controller.getFrame());
        dialog.setVisible(true);
    }

    public String getCharacterIcon()
    {
        return "./src/images/gameboard/sprites/characterSelect/" + _characterName + "_cs.png";
    }

    public void setCharacterName(String name) { this._characterName = name; }

    // ACCUSE RESPONSE PANEL
    public void createAccusationResponsePanel(ClientAppController controller, String response)
    {
        parseResponse(response);
        accusationResponseDialog dialog = new accusationResponseDialog(controller);
        dialog.setTitle("Accusation Response");
        dialog.setResizable(false);
        dialog.pack();
        dialog.setLocationRelativeTo(controller.getFrame());
        dialog.setVisible(true);
    }

    private void parseResponse(String response)
    {
        String[] tokens = response.split(":");
        String responseType = tokens[0];
        String username = tokens[1];

        _casefileSuspect = tokens[2];
        _casefileWeapon = tokens[3];
        _casefileRoom = tokens[4];

        if(responseType.equals("gameover"))
        {
            _accusationResponse = username + " has won the game!";
        }
        else // "lost"
        {
            _accusationResponse = "You have guessed incorrectly...";
        }
    }

    public String getAccusationResponseParts(String part)
    {
        if(part.equals("suspect"))
        {
            return _casefileSuspect;
        }
        else if(part.equals("weapon"))
        {
            return  _casefileWeapon;
        }
        else if(part.equals("room"))
        {
            return _casefileRoom;
        }
        else if(part.equals("response"))
        {
            return _accusationResponse;
        }
        return "";
    }

    // NO REVEAL DIALOG
    public void createNoRevealDialog(ClientAppController controller)
    {
        norevealDialog dialog = new norevealDialog(controller);
        dialog.setTitle("Suggestion Not Disproven!");
        dialog.setResizable(false);
        dialog.pack();
        dialog.setLocationRelativeTo(controller.getFrame());
        dialog.setVisible(true);
    }

    public void createStartGameDialog(ClientAppController controller)
    {
        gameStartedDialog dialog = new gameStartedDialog(controller);
        dialog.setTitle("The Game Has Started!");
        dialog.setResizable(false);
        dialog.pack();
        dialog.setLocationRelativeTo(controller.getFrame());
        dialog.setVisible(true);
    }
}
