import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * BoardController receives user input from the BoardView, updates the Board model accordingly, and updates the BoardView to reflect the changes to the model
 */
public class BoardController {

    /**
     * The max number of dice rolls
     */
    public static final int MAX_DICE_ROLL = 6;

    /**
     * The max number of dice that the attacker can roll at once
     */
    public static final int MAX_ATTACK_DICE = 3;

    /**
     * The max number of dice that the defender can roll at once
     */
    public static final int MAX_DEFEND_DICE = 2;

    /**
     * The maximum number of players
     */
    public static final int MAX_PLAYERS = 6;

    /**
     * The minimum number of players
     */
    public static final int MIN_PLAYERS = 2;

    /**
     * A map between any valid number of players and how many armies each player should start the game with
     */
    public static final Map<Integer, Integer> STARTING_ARMIES_FOR_NUM_PLAYERS = Map.of(
            2, 50,
            3, 35,
            4, 30,
            5, 25,
            6, 20
    );

    /**
     * A map between each player number and the corresponding colors
     */
    public static final Map<Integer, Color> PLAYER_COLOR_FOR_PLAYER_NUM = Map.of(
            0,Color.RED,
            1,Color.BLUE,
            2, Color.YELLOW,
            3, Color.GREEN,
            4, Color.MAGENTA,
            5, Color.LIGHT_GRAY
    );

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
     * Conducts a dice battle with the specified number of dice. Positive return values indicate that the attacker
     * won the battle. Negative values indicate the defender has won.
     * @param attackerDiceNum The number of dice the attacker will use
     * @param defenderDiceNum The number of dice the defender will use
     * @return the result of the dice battle (positive indicates attacker won, negative indicates defender won, 0 indicates both players lose 1 army)
     */
    public static int attackResult(int attackerDiceNum, int defenderDiceNum){

        int result = 0;

        //roll dice and collect the results into lists
        Random r = new Random();
        List<Integer> attackDice = new ArrayList<>();
        List<Integer> defendDice = new ArrayList<>();
        for(int i=0; i<attackerDiceNum; i++ ){ attackDice.add(r.nextInt(MAX_DICE_ROLL));}
        for(int i=0; i<defenderDiceNum; i++ ){ defendDice.add(r.nextInt(MAX_DICE_ROLL));}

        //matches up the highest rolls from attacker and defender, modifies the result accordingly, and repeats
        for(int i = Math.min(attackerDiceNum, defenderDiceNum); i > 0; i--){
            int topAttackDie = Collections.max(attackDice);
            int topDefendDie = Collections.max(defendDice);
            attackDice.remove((Integer) topAttackDie);
            defendDice.remove((Integer) topDefendDie);
            //if the attack die is larger than the defend die then add 1 else subtract 1 from result
            result += topAttackDie > topDefendDie? 1: -1;
        }

        return result;
    }

    /**
     * Prompts the user for details when fortifying troops, and calls a method to perform the specified fortification
     */
    private void doFortify(){

        if(board.getSelectedTerritories().size() != 2) {
            //JOptionPane.showMessageDialog(null,"Invalid number of territories selected");
            boardView.showMessage("Invalid number of territories selected");
            return;
        }

        Player currentPlayer = board.getCurrentPlayer();
        Territory t1 = board.getSelectedTerritories().get(0);
        Territory t2 = board.getSelectedTerritories().get(1);

        if(t1.getOwner() != currentPlayer || t2.getOwner() != currentPlayer) {
            //JOptionPane.showMessageDialog(null,"Current player doesn't own both selected territories");
            boardView.showMessage("Current player doesn't own both selected territories");
            return;
        }

        if (!board.areConnected(t1, t2)) {
            //JOptionPane.showMessageDialog(null,"Selected territories are not connected");
            boardView.showMessage("Selected territories are not connected");
            return;
        }

        boolean t1IsDestination;

        if(t1.getNumArmies() == 1){
            t1IsDestination = true;
        }
        else if(t2.getNumArmies() == 1){
            t1IsDestination = false;
        }
        else {
            Object[] options = {t1.getName() + " -> " + t2.getName(), t2.getName() + " -> " + t1.getName()};

            /*t1IsDestination = JOptionPane.showOptionDialog(null, "In which direction do you want to fortify?", "Input",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, options, options[0]) > 0;*/
            t1IsDestination = boardView.getOption("In which direction do you want to fortify?", options) > 0;
        }

        //int armiesToFortify = getValidIntegerInput("How many armies would you like to fortify?", 1, (t1IsDestination? t2.getNumArmies(): t1.getNumArmies()) - 1);
        int armiesToFortify = boardView.getIntInput("How many armies would you like to fortify?", 1, (t1IsDestination ? t2.getNumArmies() : t1.getNumArmies()) - 1);
        fortify((t1IsDestination? t1: t2), (t1IsDestination? t2: t1), armiesToFortify);
    }

    /**
     * Prompts the user for details when attacking, and calls a method to perform the specified attack
     */
    private void doAttack(){

        if(board.getSelectedTerritories().size() != 2) {
            //JOptionPane.showMessageDialog(null,"Invalid number of territories selected");
            boardView.showMessage("Invalid number of territories selected");
            return;
        }

        Player currentPlayer = board.getCurrentPlayer();
        Territory t1 = board.getSelectedTerritories().get(0);
        Territory t2 = board.getSelectedTerritories().get(1);

        if((t1.getOwner() == currentPlayer) == (t2.getOwner() == currentPlayer)) {
            //JOptionPane.showMessageDialog(null,"Current player owns neither or both selected territories");
            boardView.showMessage("Current player owns neither or both selected territories");
            return;
        }

        if((t1.getOwner() == currentPlayer && t1.getNumArmies() < 2) || (t2.getOwner() == currentPlayer && t2.getNumArmies() < 2)) {
            //JOptionPane.showMessageDialog(null,"You don't have enough armies there to attack with");
            boardView.showMessage("You don't have enough armies there to attack with");
            return;
        }

        //int attackDice = getValidIntegerInput("How many armies would "+currentPlayer.getName()+" like to attack with?", 1, Math.min(t1.getNumArmies() - 1, MAX_ATTACK_DICE));
        int attackDice = boardView.getIntInput("How many armies would "+currentPlayer.getName()+" like to attack with?", 1, Math.min(t1.getNumArmies() - 1, MAX_ATTACK_DICE));

        //int defendDice = getValidIntegerInput("How many armies would "+ (t1.getOwner() == currentPlayer? t2.getOwner().getName(): t1.getOwner().getName()) +" like to defend with?", 1, Math.min(t2.getNumArmies(), MAX_DEFEND_DICE));
        int defendDice = boardView.getIntInput("How many armies would "+ (t1.getOwner() == currentPlayer? t2.getOwner().getName(): t1.getOwner().getName()) +" like to defend with?", 1, Math.min(t2.getNumArmies(), MAX_DEFEND_DICE));

        if(t1.getOwner() == currentPlayer) attack(t2, t1, attackDice, defendDice);
        else attack(t1, t2, attackDice, defendDice);

    }

    /**
     * Prompts the user for details when placing/retracting armies, and calls a method to perform the specified army placement/retraction
     */
    private void doPlacement(){
        if(board.getSelectedTerritories().size() != 1) {
            //JOptionPane.showMessageDialog(null,"Invalid number of territories selected");
            boardView.showMessage("Invalid number of territories selected");
            return;
        }

        Territory t = board.getSelectedTerritories().get(0);

        if(t.getOwner() != board.getCurrentPlayer()) {
            //JOptionPane.showMessageDialog(null,"You do not control that territory");
            boardView.showMessage("You do not control that territory");
            return;
        }

        boolean retracting;

        if(t.getTempArmies() == 0){
            if(board.getArmiesToPlace() == 0){
                //JOptionPane.showMessageDialog(null,"You have no more armies to place");
                boardView.showMessage("You have no more armies to place");
                return;
            }
            else{
                retracting = false;
            }
        }
        else if(board.getArmiesToPlace() == 0){
            retracting = true;
        }
        else {
            Object[] options = {"Place", "Retract"};

            /*retracting = JOptionPane.showOptionDialog(null, "Would you like to place or retract armies?", "Input",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, options, options[0]) > 0;*/
            retracting = boardView.getOption("Would you like to place or retract armies?", options) > 0;
        }
        //int armiesToPlace = getValidIntegerInput("How many armies would you like to " + (retracting? "retract from ": "place in ") + t.getName() + "?", 1, (retracting? t.getTempArmies(): board.getArmiesToPlace()));
        int armiesToPlace = boardView.getIntInput("How many armies would you like to " + (retracting? "retract from ": "place in ") + t.getName() + "?", 1, (retracting? t.getTempArmies(): board.getArmiesToPlace()));
        place(t, (retracting? -1: 1) * armiesToPlace);
    }

    /**
     * Places the specified number of armies in the specified territory in the Board model
     * @param t The territory to place armies in
     * @param armies The number of armies to place
     */
    private void place(Territory t, int armies){
        t.addTempArmies(armies);
        board.addArmiesToPlace(-armies);

        int sum = 0;
        for(Territory territory: t.getOwner().getControlledTerritories()){
            sum += territory.getTempArmies();
        }

        InfoPanel infoPanel = boardView.getInfoPanel();
        infoPanel.setArmiesToPlace(board.getArmiesToPlace());
        infoPanel.setNumArmies(t.getOwner().getNumArmies() + sum);

        //BoardPanel mapPanel = boardView.getMapPanel();
        //mapPanel.drawTerritory(t, mapPanel.getGraphics());

        List<Territory> territoriesToUpdate = new ArrayList<>();
        territoriesToUpdate.add(t);
        boardView.updateMap(new MapEvent(this, territoriesToUpdate));

        if(armies > 0) //JOptionPane.showMessageDialog(null,"Placed " + armies + " armies in " + t.getName());
            boardView.showMessage("Placed " + armies + " armies in " + t.getName());
        else if(armies < 0) //JOptionPane.showMessageDialog(null,"Retracted " + (-armies) + " armies from " + t.getName());
            boardView.showMessage("Retracted " + (-armies) + " armies from " + t.getName());
    }

    /**
     * Fortifies the specified number of armies between the specified territories in the Board model
     * @param t1 The territory armies are to be fortified to
     * @param t2 The territory armies are to be fortified from
     * @param armies The number of armies to fortify
     */
    private void fortify(Territory t1, Territory t2, int armies){
        board.moveArmies(t2, t1, armies);

        //BoardPanel mapPanel = boardView.getMapPanel();
        //mapPanel.drawTerritory(t1, mapPanel.getGraphics());
        //mapPanel.drawTerritory(t2, mapPanel.getGraphics());

        List<Territory> territoriesToUpdate = new ArrayList<>();
        territoriesToUpdate.add(t1);
        territoriesToUpdate.add(t2);
        boardView.updateMap(new MapEvent(this, territoriesToUpdate));

        //JOptionPane.showMessageDialog(null,"Fortified " + armies + " armies from " + t2.getName() + " to " + t1.getName());
        boardView.showMessage("Fortified " + armies + " armies from " + t2.getName() + " to " + t1.getName());
        nextTurn();
        nextTurnStage();
    }

    /**
     * Attacks with the specified army numbers between the specified territories in the Board model
     * @param t1 The defending territory
     * @param t2 The attacking territory
     * @param attackDice The number of attacking armies
     * @param defendDice The number of defending armies
     */
    private void attack(Territory t1, Territory t2, int attackDice, int defendDice){

        int result = attackResult(attackDice, defendDice);

        //JOptionPane.showMessageDialog(null, result == 0? "Both players lost an army": (result > 0)? t1.getOwner().getName()+" lost "+result +" armies": t2.getOwner().getName()+" lost "+ (-result) +" armies");
        boardView.showMessage(result == 0? "Both players lost an army": (result > 0)? t1.getOwner().getName()+" lost "+result +" armies": t2.getOwner().getName()+" lost "+ (-result) +" armies");

        if(result == 0){ //both players lose one army
            t2.addArmies(-1);
            t1.addArmies(-1);
        }
        else if(result > 0) { //defender loses armies
            t1.addArmies(-result);
        }
        else { //attacker loses armies
            t2.addArmies(result);
        }

        InfoPanel infoPanel = boardView.getInfoPanel();
        infoPanel.setNumArmies(t2.getOwner().getNumArmies());

        //BoardPanel mapPanel = boardView.getMapPanel();
        //mapPanel.drawTerritory(t1, mapPanel.getGraphics());
        //mapPanel.drawTerritory(t2, mapPanel.getGraphics());

        List<Territory> territoriesToUpdate = new ArrayList<>();
        territoriesToUpdate.add(t1);
        territoriesToUpdate.add(t2);
        boardView.updateMap(new MapEvent(this, territoriesToUpdate));

        if(t1.getNumArmies() <= 0 ) { //defending territory has no armies left
            //JOptionPane.showMessageDialog(null, t1.getName()+" was conquered!");
            boardView.showMessage(t1.getName()+" was conquered!");

            Player prevOwner = t1.getOwner();
            prevOwner.loseTerritory(t1);
            t2.getOwner().gainTerritory(t1);
            t1.setOwner(t2.getOwner());

            infoPanel = boardView.getInfoPanel();
            infoPanel.setNumTerritories(t2.getOwner().getNumTerritories());

            //mapPanel.drawTerritory(t1, mapPanel.getGraphics());

            territoriesToUpdate = new ArrayList<>();
            territoriesToUpdate.add(t1);
            boardView.updateMap(new MapEvent(this, territoriesToUpdate));

            board.toggleTerritorySelection(t2);

            Continent continent = t1.getContinent();
            if(continent.isConquered()) //JOptionPane.showMessageDialog(null, continent.getName()+" was conquered!");
                boardView.showMessage(continent.getName()+" was conquered!");

            if(prevOwner.getNumTerritories() == 0) {

                //prevOwner is eliminated
                board.removePlayer(prevOwner);
                //JOptionPane.showMessageDialog(null, prevOwner.getName() + " was eliminated!");
                boardView.showMessage(prevOwner.getName() + " was eliminated!");

                if(board.getPlayerList().size() > 1){
                    //game is over
                    //JOptionPane.showMessageDialog(null, t2.getOwner().getName() + " has won!");
                    boardView.showMessage(t2.getOwner().getName() + " has won!");
                    board.clearBoard();
                }
            }
            //int armiesToMove = getValidIntegerInput("How many armies would "+t2.getOwner().getName()+" like to move?", attackDice, t2.getNumArmies() - 1);
            int armiesToMove = boardView.getIntInput("How many armies would "+t2.getOwner().getName()+" like to move?", attackDice, t2.getNumArmies() - 1);

            board.moveArmies(t2, t1, armiesToMove);

            //mapPanel.drawTerritory(t1, mapPanel.getGraphics());
            //mapPanel.drawTerritory(t2, mapPanel.getGraphics());

            territoriesToUpdate = new ArrayList<>();
            territoriesToUpdate.add(t1);
            territoriesToUpdate.add(t2);
            boardView.updateMap(new MapEvent(this, territoriesToUpdate));

            //JOptionPane.showMessageDialog(null, "Moved " + armiesToMove + " armies into " + t1.getName());
            boardView.showMessage("Moved " + armiesToMove + " armies into " + t1.getName());
        }
    }

    /**
     * Moves the board to the next player's turn
     */
    private void nextTurn(){
        board.incrementTurn();
        board.getSelectedTerritories().clear();

        InfoPanel infoPanel = boardView.getInfoPanel();
        infoPanel.setCurrentBonus(board.getArmiesToPlace());
        Player player = board.getCurrentPlayer();
        infoPanel.setPlayerName(player.getName());
        infoPanel.setNumArmies(player.getNumArmies());
        infoPanel.setNumTerritories(player.getNumTerritories());
        infoPanel.setArmiesToPlace(board.getArmiesToPlace());
        infoPanel.setPlayerColorPanel(player.getColor());

        infoPanel.repaint();

        //JOptionPane.showMessageDialog(null, "It is now " + player.getName() + "'s turn");
        boardView.showMessage("It is now " + player.getName() + "'s turn");
    }

    /**
     * Moves the board to the next turn phase
     */
    private void nextTurnStage(){
        board.incrementTurnStage();

        String buttonText;
        if(board.getTurnStage() == TurnStage.FORTIFY) buttonText = "Fortify";
        else if(board.getTurnStage() == TurnStage.ATTACK){
            buttonText = "Attack";
            boardView.getMapPanel().repaint();
        }
        else buttonText = "Place / Retract";
        boardView.setActionButtonText(buttonText);

        boardView.getInfoPanel().setTurnStage(board.getTurnStage());

        board.getSelectedTerritories().clear();

        boardView.getMapPanel().repaint();
    }

    /**
     * Calls the correct method when the user presses the ACTION button
     * (labelled as PLACE/RETRACT, ATTACK, or FORTIFY)
     */
    public void doAction(){
        TurnStage turnStage = board.getTurnStage();
        if(turnStage == TurnStage.ATTACK) doAttack();
        else if(turnStage == TurnStage.FORTIFY) doFortify();
        else if(turnStage == TurnStage.PLACEMENT) doPlacement();

        boardView.getMapPanel().repaint();
    }

    /**
     * Constructs a new RISK board from the default map file and registers it with the BoardController and BoardView
     */
    public void createNewGame(){

        BoardConstructor bc = new BoardConstructor();

        board = bc.createMapFromFile("DEFAULT_MAP.xml");

        if(board == null){
            //JOptionPane.showMessageDialog(null, "Error constructing default board. Ensure that the file DEFAULT_MAP.xml is in the source directory.");
            boardView.showMessage("Error constructing default board. Ensure that the file DEFAULT_MAP.xml is in the source directory.");
            return;
        }

        int boardSize = board.getTerritoryList().size();

        //int numPlayers = getValidIntegerInput("Enter the number of players:", MIN_PLAYERS, Math.min(MAX_PLAYERS, boardSize));
        int numPlayers = boardView.getIntInput("Enter the number of players:", MIN_PLAYERS, Math.min(MAX_PLAYERS, boardSize));
        int numArmiesEach = STARTING_ARMIES_FOR_NUM_PLAYERS.get(numPlayers);
        if(numPlayers * numArmiesEach < boardSize) {
            //JOptionPane.showMessageDialog(null,"The selected map has too many territories to fill. Please start a new game with more players");
            boardView.showMessage("The selected map has too many territories to fill. Please start a new game with more players");
            return;
        }

        for(int i=0;i<numPlayers;i++){
            //board.addPlayer(new Player(JOptionPane.showInputDialog("Please enter a name for player " + (i+1) + ":", "Player "+ (i+1)), PLAYER_COLOR_FOR_PLAYER_NUM.get(i)));
            board.addPlayer(new Player(boardView.getStringInput("Please enter a name for player " + (i+1) + ":", "Player "+ (i+1)), PLAYER_COLOR_FOR_PLAYER_NUM.get(i)));
        }

        board.populateBoard(numArmiesEach);

        board.setCurrentPlayer(board.getPlayerList().get(numPlayers - 1));
        board.setTurnStage(TurnStage.FORTIFY);

        nextTurn();
        nextTurnStage();

        boardView.getMapPanel().repaint();
    }

    /**
     * Displays a help message to the user
     */
    public void displayHelpMessage(){

        String message =
                "\nThe Map: " +
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

        /*JOptionPane.showMessageDialog(null, message,
                "Help", JOptionPane.INFORMATION_MESSAGE);*/
        boardView.showMessage(message);
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
        TurnStage currentTurnStage = board.getTurnStage();
        if(currentTurnStage == TurnStage.PLACEMENT){
            if(board.getArmiesToPlace() > 0) {
                //JOptionPane.showMessageDialog(null,"You still have armies to place");
                boardView.showMessage("You still have armies to place");
                return;
            }
            for(Territory t: board.getTerritoryList()){
                if(t.getTempArmies() == 0) continue;
                t.confirmTempArmies();
            }
        }
        else if(currentTurnStage == TurnStage.FORTIFY){
            nextTurn();
        }
        nextTurnStage();
    }
}
