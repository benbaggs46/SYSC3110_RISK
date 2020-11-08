import javax.swing.*;
import java.awt.*;

/**
 * A panel to display information about the current player
 */
public class InfoPanel extends JPanel {

    /**
     * A label to display the current player name
     */
    private JLabel playerName;
    /**
     * A label to display the current turn stage
     */
    private JLabel turnStage;
    /**
     * A label to display the number of armies the current player has
     */
    private JLabel numArmies;
    /**
     * A label to display the number of territories the current player controls
     */
    private JLabel numTerritories;
    /**
     * A label to display the number of armies the current player will receive at the start of their turn
     */
    private JLabel currentBonus;
    /**
     * A label to display the number of armies the current player has to place during the PLACEMENT turn phase
     */
    private JLabel armiesToPlace;
    /**
     * A panel to display the color of the current player
     */
    private JPanel playerColorPanel;
    /**
     * The color of the current player
     */
    private Color playerColor;

    /**
     * Constructor for InfoPanel
     */
    public InfoPanel(){
        super();
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));

        JPanel playerNameBarPanel = new JPanel();
        add(playerNameBarPanel);

        playerColorPanel = new JPanel(){
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                g.setColor(playerColor);
                g.fillRect(0,0,10,10);
            }
        };
        playerNameBarPanel.add(playerColorPanel);

        playerName = new JLabel();
        playerNameBarPanel.add(playerName);

        JPanel playerInfoPanel = new JPanel();
        playerInfoPanel.setLayout(new GridLayout(1,5));
        add(playerInfoPanel);

        turnStage = new JLabel();
        turnStage.setHorizontalAlignment(JLabel.CENTER);
        playerInfoPanel.add(turnStage);
        numArmies = new JLabel();
        numArmies.setHorizontalAlignment(JLabel.CENTER);
        playerInfoPanel.add(numArmies);
        numTerritories = new JLabel();
        numTerritories.setHorizontalAlignment(JLabel.CENTER);
        playerInfoPanel.add(numTerritories);
        currentBonus = new JLabel();
        currentBonus.setHorizontalAlignment(JLabel.CENTER);
        playerInfoPanel.add(currentBonus);
        armiesToPlace = new JLabel();
        armiesToPlace.setHorizontalAlignment(JLabel.CENTER);
        playerInfoPanel.add(armiesToPlace);
    }

    /**
     * Sets the player name label
     * @param string The new player name
     */
    public void setPlayerName(String string){
        playerName.setText(string);
    }
    /**
     * Sets the turn stage label
     * @param turnStage The new turnStage
     */
    public void setTurnStage(TurnStage turnStage){
        this.turnStage.setText("Turn Phase: " + String.valueOf(turnStage));
    }
    /**
     * Sets the army number label
     * @param num The new army number
     */
    public void setNumArmies(int num){
        numArmies.setText("Total Armies: " + num);
    }
    /**
     * Sets the territory number label
     * @param num The new territory number
     */
    public void setNumTerritories(int num){
        numTerritories.setText("Total Territories: " + num);
    }
    /**
     * Sets the current bonus label
     * @param num The new current bonus
     */
    public void setCurrentBonus(int num){
        currentBonus.setText("Current Army Bonus: " + num + " / turn");
    }
    /**
     * Sets the armies to place label
     * @param num The new number of armies to place
     */
    public void setArmiesToPlace(int num){
        armiesToPlace.setText("Armies To Place: " + num);
    }
    /**
     * Sets the player color
     * @param color The new player color
     */
    public void setPlayerColorPanel(Color color){
        this.playerColor = color;
    }
}
