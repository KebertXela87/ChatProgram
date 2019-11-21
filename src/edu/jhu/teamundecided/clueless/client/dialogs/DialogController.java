package edu.jhu.teamundecided.clueless.client.dialogs;

import edu.jhu.teamundecided.clueless.client.ClientAppController;

import javax.swing.*;
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

    public DialogController()
    {

    }

    /// MOVE DIALOG LOGIC
    public void createMoveDialog(ClientAppController controller, String moveOptions)
    {
        setMoveOptions(moveOptions);
        moveDialog dialog = new moveDialog(controller);
        dialog.setLocationRelativeTo(controller.getFrame());
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

    /// ACCUSE DIALOG LOGIC
}
