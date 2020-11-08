import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {

    private JLabel playerName;
    private JLabel turnStage;
    private JLabel numArmies;
    private JLabel numTerritories;
    private JLabel currentBonus;
    private JLabel armiesToPlace;

    private JPanel playerColorPanel;
    private Color playerColor;

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

        turnStage = new JLabel();
        add(turnStage);
        numArmies = new JLabel();
        add(numArmies);
        numTerritories = new JLabel();
        add(numTerritories);
        currentBonus = new JLabel();
        add(currentBonus);
        armiesToPlace = new JLabel();
        add(armiesToPlace);
    }

    public void setPlayerName(String string){
        playerName.setText(string);
    }
    public void setTurnStage(TurnStage turnStage){
        this.turnStage.setText("Turn Phase: " + String.valueOf(turnStage));
    }
    public void setNumArmies(int num){
        numArmies.setText("Total Armies: " + num);
    }
    public void setNumTerritories(int num){
        numTerritories.setText("Total Territories: " + num);
    }
    public void setCurrentBonus(int num){
        currentBonus.setText("Current Army Bonus: " + num + " / turn");
    }
    public void setArmiesToPlace(int num){
        armiesToPlace.setText("Armies To Place: " + num);
    }

    public void setPlayerColorPanel(Color color){
        this.playerColor = color;
    }
}
