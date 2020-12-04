import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Responds when the user presses a button on screen, and invokes the corresponding method in the BoardController
 */
public class BoardButtonListener implements ActionListener {

    /**
     * The BoardView that the BoardButtonListener is listening to
     */
    private BoardView boardView;

    /**
     * Constructor for BoardButtonListener
     * @param boardView The BoardView that the BoardButtonListener is listening to
     */
    public BoardButtonListener(BoardView boardView){
        this.boardView = boardView;
    }

    /**
     * Invokes the correct method in BoardController when the user presses a button
     * @param e An ActionEvent generated by a button when it is pressed
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        String actionCommand = e.getActionCommand();

        BoardController boardController = boardView.getBoardController();

        if(actionCommand.equals("New Game")) boardController.createNewGame();

        else if(actionCommand.equals("Quit")) boardController.quit();

        else if(actionCommand.equals("Help")) boardController.displayHelpMessage();

        else if(actionCommand.equals("Action")) boardController.doAction();

        else if(actionCommand.equals("Proceed")) boardController.proceed();

        else if(actionCommand.equals("Save Game")) boardController.save();

        else if(actionCommand.equals("Load Game")) boardController.load();
    }
}
