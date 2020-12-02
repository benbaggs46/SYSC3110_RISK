import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

/**
 * Creates a frame for the user to play RISK
 */
public class BoardView extends JFrame implements RiskView, RiskInput{

    /**
     * Message containing some helpfully information for the player
     */
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

    /**
     * Show the user a message containing the help information
     */
    public void showHelp(){
        showMessage(HELP_MESSAGE);
    }

    public int getNumPlayers(){
        return getIntInput("Enter the number of players:", Board.MIN_PLAYERS, Board.MAX_PLAYERS);
    }

    public String getPlayerName(int i){
        return getStringInput("Please enter a name for the " +
                Board.PLAYER_COLOR_FOR_PLAYER_NUM.get(i).getName().toLowerCase() +
                " player:", Board.PLAYER_COLOR_FOR_PLAYER_NUM.get(i).getName() + " player");
    }

    public String getMapPath(){
        return getStringInput("Please enter a path for the map file",
                "DEFAULT_MAP.xml");
    }

    public boolean isPlayerAI(){
        Object[] options = {"Human", "AI"};
        return getOption("Please enter the player type:", options) == 1;
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
