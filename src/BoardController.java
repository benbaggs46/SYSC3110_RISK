/**
 * BoardController receives user input from the BoardView, updates the Board model accordingly, and updates the BoardView to reflect the changes to the model
 */
public class BoardController {

    /**
     * The Board being controlled by the BoardController
     */
    private Board board;

    /**
     * The BoardView displaying the Board model
     */
    private BoardView boardView;

    /**
     * Constructor of type BoardController
     * @param boardView The view responsible for sending input to the BoardController
     */
    public BoardController(BoardView boardView){
        this.boardView = boardView;
    }

    /**
     * Returns the Board model controlled by the BoardController
     * @return The Board model controlled by the BoardController
     */
    public Board getBoard(){
        return board;
    }

    /**
     * Calls the correct method when the user presses the ACTION button
     * (labelled as PLACE/RETRACT, ATTACK, or FORTIFY)
     */
    public void doAction(){
        board.doAction();
    }

    /**
     * Constructs a new RISK board from the default map file and registers it with the BoardController and BoardView
     */
    public void createNewGame(boolean gameIsNew){
        do {
            board = Board.newBoard(boardView, gameIsNew);
        } while(!board.isValid());

        board.addRiskView(boardView);
        boardView.repaint();
        board.nextTurnStage();
    }

    /**
     * Saves the current game state
     */
    public void save(){ if(board != null) board.saveGame();}

    /**
     * Displays a help message to the user
     */
    public void displayHelpMessage(){
        boardView.showHelp();
    }

    /**
     * Quits the application
     */
    public void quit(){
        System.exit(1);
    }

    /**
     * Ends the current phase of the current turn.
     * Called when the user presses the PROCEED button
     */
    public void proceed(){
        board.proceed();
    }
}