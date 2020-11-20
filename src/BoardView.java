import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

/**
 * Creates a frame for the user to play RISK
 */
public class BoardView extends JFrame implements RiskView, RiskInput{

    /**
     * The panel to draw the risk board on
     */
    private BoardPanel mapPanel;

    /**
     * The panel to display player specific information
     * (player name, turn phase, number of territories, etc.)
     */
    private InfoPanel infoPanel;

    /**
     * The button the user presses to place/retract armies, attack, or fortify
     */
    private JButton actionButton;

    /**
     * The BoardController which updates the Board model according to user input and updates the BoardView to reflect those changes
     */
    private BoardController bc;

    /**
     * Constructor for BoardView
     */
    public BoardView(){

        super("RISK");
        bc = new BoardController(this);
        mapPanel = new BoardPanel(this);
        add(mapPanel, BorderLayout.CENTER);
        MouseAdapter ma = new BoardMouseListener(this);
        mapPanel.addMouseListener(ma);

        infoPanel = new InfoPanel();
        add(infoPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        JButton button = new JButton("New Game");
        button.addActionListener(new BoardButtonListener(this));
        buttonPanel.add(button);

        button = new JButton("Help");
        button.addActionListener(new BoardButtonListener(this));
        buttonPanel.add(button);

        button = new JButton("Quit");
        button.addActionListener(new BoardButtonListener(this));
        buttonPanel.add(button);

        button = new JButton("Proceed");
        button.setActionCommand("Proceed");
        button.addActionListener(new BoardButtonListener(this));
        buttonPanel.add(button);

        actionButton = new JButton("Action");
        actionButton.setActionCommand("Action");
        actionButton.addActionListener(new BoardButtonListener(this));
        buttonPanel.add(actionButton);

        setSize(1000,725);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    /**
     * Returns the associated BoardController
     * @return The associated BoardController
     */
    public BoardController getBoardController(){
        return bc;
    }

    /**
     * Returns the map panel of this BoardView
     * @return The map panel of this BoardView
     */
    public BoardPanel getMapPanel(){
        return mapPanel;
    }

    /**
     * Sets the text of the ACTION button to the specified text
     * @param text The text to be displayed by the ACTION button
     */
    public void setActionButtonText(String text){
        actionButton.setText(text);
    }
    
    public static void main(String[] args){
        new BoardView();
    }

    @Override
    public void updateMap(MapEvent me) {
        for(Territory t: me.getTerritoryList()){
            mapPanel.drawTerritory(t, mapPanel.getGraphics());
        }
    }

    @Override
    public void updateUI(UIEvent uie) {

        Player player = uie.getCurrentPlayer();

        /**
         * TODO: Need to implement a method for the UI when it is an AI turn
         */
        if(player.isAi()){
            return;
        }
        TurnStage turnStage = uie.getTurnStage();

        infoPanel.setPlayerColorPanel(player.getColor());
        infoPanel.setNumArmies(player.getNumArmies());
        infoPanel.setArmiesToPlace(uie.getArmiesToPlace());
        infoPanel.setNumTerritories(player.getNumTerritories());
        infoPanel.setTurnStage(turnStage);
        infoPanel.setPlayerName(player.getName());
        infoPanel.setCurrentBonus(uie.getCurrentPlayerBonus());

        infoPanel.repaint();

        String buttonText;
        if(turnStage == TurnStage.FORTIFY) buttonText = "Fortify";
        else if(turnStage == TurnStage.ATTACK) buttonText = "Attack";
        else buttonText = "Place / Retract";
        actionButton.setText(buttonText);

    }

    @Override
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    @Override
    public String getStringInput(String prompt, String defaultValue) {
        return JOptionPane.showInputDialog(prompt, defaultValue);
    }

    @Override
    public int getIntInput(String prompt, int min, int max) {
        if(min == max) return min;
        String input = "";
        while(!isValidIntegerInput(input, min, max)) input = JOptionPane.showInputDialog(prompt + " (" + min + " - " + max + ")");
        return Integer.parseInt(input);
    }

    public Boolean getBooleanInput(String prompt, Boolean defaultValue) {
        int x = JOptionPane.showConfirmDialog(this, prompt);
        return x != 1;
    }

    /**
     * Determines if the provided string input is an integer between the specified minimum and maximum (both inclusive)
     * @param input The input string
     * @param min The inclusive minimum value
     * @param max The inclusive maximum value
     * @return A boolean indicating whether the input represents an integer between the minimum and maximum values
     */
    public static boolean isValidIntegerInput(String input, int min, int max){
        if(input == null) return false;
        if(input.isEmpty()) return false;
        for(char ch: input.toCharArray()){
            if(!Character.isDigit(ch)) return false;
        }
        int value = Integer.parseInt(input);

        return !(value < min || value > max);
    }

    @Override
    public int getOption(String prompt, Object[] options){
        return JOptionPane.showOptionDialog(null, prompt, "Input",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);
    }
}
