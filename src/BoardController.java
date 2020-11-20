import java.util.*;
import java.util.List;

/**
 * BoardController receives user input from the BoardView, updates the Board model accordingly, and updates the BoardView to reflect the changes to the model
 */
public class BoardController {

    public static final String HELP_MESSAGE = "\nThe Map: " +
            "\n Territories are adjacent if they share a border or there is an extra line connecting their borders. Alaska and Kamchatka are adjacent, and the line connecting" +
            "\n them extends off the left and right sides of the map. " +
            "\nThe color of a territory when it is not selected indicates which continent it is a part of. The player receives extra armies at the beginning of their turn if they " +
            "\ncontrol entire continents. When selected, the territory will always appear red. " +
            "\nEvery territory has a square drawn above it. The color of the square represents the player that currently controls it, and the number indicates the amount of armies on the territory. " +
            "\nDuring the PLACEMENT turn phase, the square may contain two numbers separated by an addition sign. When this occurs, the number on the right represents armies that the player " +
            "\nhas placed in the territory, but have not been confirmed. The player may still retract these armies before moving to the ATTACK phase. " +
            "\nThe number on the left indicates the amount of armies on the territory at the beginning of the turn, before the player started placing armies. " +
            "\n\nCurrent Player Information: " +
            "\n- The player's name and associated color will be displayed at the top center of the screen as text with a colored square beside it. " +
            "\n- The line labelled Turn Phase indicates the turn phase that the player is currently in. This can be either PLACEMENT, ATTACK, or FORTIFY. " +
            "\n- The line labelled Total Armies indicates the total number of combined armies the player has in all of their controlled territories. " +
            "\n- The line labelled Total Territories indicates the number of territories the player currently controls. " +
            "\n- The line labelled Current Army Bonus indicates how many armies the player will receive at the start of their turn if they do not gain or lose any."+
            "\n- The line labelled Armies to Place indicates the amount of armies that the player has been given during the PLACEMENT turn phase that have not yet been placed " +
            "\non the board. The player must place all of these armies before proceeding to the ATTACK phase. This number will be zero at all other times. " +
            "\n\nSelecting Territories: " +
            "\nTerritories on the game board can be selected or deselected by clicking on them. The specific territories selected indicate the players intentions when they press the ACTION BUTTON" +
            "\n\nButtons: " +
            "\nNew Game " +
            "\n- This button starts a new game of RISK. After being pressed, the user will be prompted to enter the desired number of players and then names for those players." +
            "\nHelp " +
            "\n- This button opens a dialog box containing helpful information about the game. " +
            "\nQuit " +
            "\n- This button terminates the application. When pressed, the game window will close and the current game will be lost. " +
            "\nProceed " +
            "\n- This button is used to move to the next phase of a player's turn, or when in the FORTIFY phase, move to the next player's turn."+
            "\nAction Button (PLACE/RETRACT, ATTACK, FORTIFY) " +
            "\n- This button is used by the player to perform actions during their turn, such as placing armies, attacking, and fortifying. The text of the button will change" +
            "\n to reflect the turn phase that the player is currently in. ";

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
    public void createNewGame(){
        int numPlayers = boardView.getIntInput("Enter the number of players:", Board.MIN_PLAYERS, Board.MAX_PLAYERS);
        List<String> playerNames = new ArrayList<>();
        List<Boolean> IsAi = new ArrayList<>();
        for(int i = 0; i < numPlayers; i++){
            playerNames.add(boardView.getStringInput("Please enter a name for the " + Board.PLAYER_COLOR_FOR_PLAYER_NUM.get(i).getName().toLowerCase() + " player:", Board.PLAYER_COLOR_FOR_PLAYER_NUM.get(i).getName() + " player"));
            Object[] options = {"Human", "AI"};
            IsAi.add(boardView.getOption("Please enter the player type:", options) == 1);
        }
        board = new Board("DEFAULT_MAP.xml", boardView, playerNames, IsAi);
        board.addRiskView(boardView);
        board.nextTurnStage();
    }

    /**
     * Displays a help message to the user
     */
    public void displayHelpMessage(){
        boardView.showMessage(HELP_MESSAGE);
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
