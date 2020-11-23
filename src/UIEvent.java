import java.util.EventObject;

/**
 * Holds information about the RISK game to be given to the views when the game state changes
 */
public class UIEvent extends EventObject {

    /**
     * The current turn stage
     */
    private TurnStage turnStage;
    /**
     * The current player
     */
    private Player currentPlayer;
    /**
     * The army bonus to be earned by the current player at the start of their turn
     */
    private int currentPlayerBonus;
    /**
     * The number of armies that the current player has to place
     */
    private int armiesToPlace;

    public UIEvent(Object source, TurnStage turnStage, Player currentPlayer, int currentPlayerBonus, int armiesToPlace) {
        super(source);
        this.turnStage = turnStage;
        this.currentPlayer = currentPlayer;
        this.currentPlayerBonus = currentPlayerBonus;
        this.armiesToPlace = armiesToPlace;
    }

    /**
     * returns the turn stage
     * @return the turn stage
     */
    public TurnStage getTurnStage() {
        return turnStage;
    }

    /**
     * returns the current player
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * returns the current player bonus
     * @return the current army bonus of the current player
     */
    public int getCurrentPlayerBonus() {
        return currentPlayerBonus;
    }

    /**
     * returns the current number of armies to place
     * @return the current number of armies to place
     */
    public int getArmiesToPlace() {
        return armiesToPlace;
    }
}
