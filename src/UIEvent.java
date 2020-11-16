import java.util.EventObject;

public class UIEvent extends EventObject {

    private TurnStage turnStage;
    private Player currentPlayer;
    private int currentPlayerBonus;
    private int armiesToPlace;

    public UIEvent(Object source, TurnStage turnStage, Player currentPlayer, int currentPlayerBonus, int armiesToPlace) {
        super(source);
        this.turnStage = turnStage;
        this.currentPlayer = currentPlayer;
        this.currentPlayerBonus = currentPlayerBonus;
        this.armiesToPlace = armiesToPlace;
    }

    public TurnStage getTurnStage() {
        return turnStage;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public int getCurrentPlayerBonus() {
        return currentPlayerBonus;
    }

    public int getArmiesToPlace() {
        return armiesToPlace;
    }
}
