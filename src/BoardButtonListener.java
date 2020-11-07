import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BoardButtonListener implements ActionListener {

    private BoardController boardController;

    public BoardButtonListener(BoardController boardController){
        this.boardController = boardController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if(actionCommand.equals("New Game")) boardController.createNewGame();

        else if(actionCommand.equals("Quit")) boardController.quit();

        else if(actionCommand.equals("Help")) boardController.displayHelpMessage();

        else if(actionCommand.equals("Action")) boardController.doAction();

        else if(actionCommand.equals("Proceed")) boardController.proceed();
    }
}
