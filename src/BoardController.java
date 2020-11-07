

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

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
            5, Color.GRAY
    );

    /**
     * The Board being controlled by the BoardController
     */
    private Board board;

    private BoardView boardView;

    public BoardController(BoardView boardView){
        this.boardView = boardView;
    }

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

    private void doFortify(){

        if(board.getSelectedTerritories().size() != 2) {
            JOptionPane.showMessageDialog(null,"Invalid number of territories selected");
            return;
        }

        Player currentPlayer = board.getCurrentPlayer();
        Territory t1 = board.getSelectedTerritories().get(0);
        Territory t2 = board.getSelectedTerritories().get(1);

        if(t1.getOwner() != currentPlayer || t2.getOwner() != currentPlayer) {
            JOptionPane.showMessageDialog(null,"Current player doesn't own both selected territories");
            return;
        }

        if (!board.areConnected(t1, t2)) {
            JOptionPane.showMessageDialog(null,"Selected territories are not connected");
            return;
        }

        if(t1.getNumArmies() == 1){
            int armiesToFortify = getValidIntegerInput("How many armies would you like to fortify?", 1, t2.getNumArmies() - 1);
            fortify(t1, t2, armiesToFortify);
        }
        else if(t2.getNumArmies() == 1){
            int armiesToFortify = getValidIntegerInput("How many armies would you like to fortify?", 1, t1.getNumArmies() - 1);
            fortify(t2, t1, armiesToFortify);
        }
        else {
            Object[] options = {t1.getName() + " -> " + t2.getName(), t2.getName() + " -> " + t1.getName()};
            if (JOptionPane.showOptionDialog(null, "In which direction do you want to fortify?", "Input",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, options, options[0]) == 0) {
                int armiesToFortify = getValidIntegerInput("How many armies would you like to fortify?", 1, t1.getNumArmies() - 1);
                fortify(t2, t1, armiesToFortify);
            } else {
                int armiesToFortify = getValidIntegerInput("How many armies would you like to fortify?", 1, t2.getNumArmies() - 1);
                fortify(t1, t2, armiesToFortify);
            }
        }
    }

    private void doAttack(){

        if(board.getSelectedTerritories().size() != 2) {
            JOptionPane.showMessageDialog(null,"Invalid number of territories selected");
            return;
        }

        Player currentPlayer = board.getCurrentPlayer();
        Territory t1 = board.getSelectedTerritories().get(0);
        Territory t2 = board.getSelectedTerritories().get(1);

        if((t1.getOwner() == currentPlayer) == (t2.getOwner() == currentPlayer)) {
            JOptionPane.showMessageDialog(null,"Current player owns neither or both selected territories");
            return;
        }

        int attackDice = getValidIntegerInput("How many armies would "+currentPlayer.getName()+" like to attack with?", 1, Math.min(t1.getNumArmies() - 1, MAX_ATTACK_DICE));

        int defendDice = getValidIntegerInput("How many armies would "+ (t1.getOwner() == currentPlayer? t2.getOwner().getName(): t1.getOwner().getName()) +" like to defend with?", 1, Math.min(t2.getNumArmies(), MAX_DEFEND_DICE));

        if(t1.getOwner() == currentPlayer) attack(t2, t1, attackDice, defendDice);
        else attack(t1, t2, attackDice, defendDice);

    }

    private void doPlacement(){
        if(board.getSelectedTerritories().size() != 1) {
            JOptionPane.showMessageDialog(null,"Invalid number of territories selected");
            return;
        }

        Territory t = board.getSelectedTerritories().get(0);

        if(t.getOwner() != board.getCurrentPlayer()) {
            JOptionPane.showMessageDialog(null,"You do not control that territory");
            return;
        }

        if(t.getTempArmies() == 0){
            if(board.getArmiesToPlace() == 0){
                JOptionPane.showMessageDialog(null,"You have no more armies to place");
                return;
            }
            else{
                int armiesToPlace = getValidIntegerInput("How many armies would you like to place in " + t.getName() + "?", 0, board.getArmiesToPlace());
                place(t, armiesToPlace);
            }
        }
        else if(board.getArmiesToPlace() == 0){
            int armiesToPlace = getValidIntegerInput("How many armies would you like to retract from " + t.getName() + "?", 0, t.getTempArmies());
            place(t, -armiesToPlace);
        }
        else {
            Object[] options = {"Place", "Retract"};
            if (JOptionPane.showOptionDialog(null, "Would you like to place or retract armies?", "Input",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, options, options[0]) == 0) {
                int armiesToPlace = getValidIntegerInput("How many armies would you like to place in " + t.getName() + "?", 0, board.getArmiesToPlace());
                place(t, armiesToPlace);
            }
            else{
                int armiesToPlace = getValidIntegerInput("How many armies would you like to retract from " + t.getName() + "?", 0, t.getTempArmies());
                place(t, -armiesToPlace);
            }
        }
    }

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

        BoardPanel mapPanel = boardView.getMapPanel();
        mapPanel.drawTerritoryInfo(t, mapPanel.getGraphics());

        if(armies > 0) JOptionPane.showMessageDialog(null,"Placed " + armies + " armies in " + t.getName());
        else JOptionPane.showMessageDialog(null,"Retracted " + (-armies) + " armies from " + t.getName());
    }

    private void fortify(Territory t1, Territory t2, int armies){
        board.moveArmies(t2, t1, armies);

        BoardPanel mapPanel = boardView.getMapPanel();
        mapPanel.drawTerritoryInfo(t1, mapPanel.getGraphics());
        mapPanel.drawTerritoryInfo(t2, mapPanel.getGraphics());

        JOptionPane.showMessageDialog(null,"Fortified " + armies + " armies from " + t2.getName() + " to " + t1.getName());
        nextTurn();
        nextTurnStage();
    }

    private void attack(Territory t1, Territory t2, int attackDice, int defendDice){

        int result = attackResult(attackDice, defendDice);

        JOptionPane.showMessageDialog(null, result == 0? "Both players lost an army": (result > 0)? t1.getOwner().getName()+" lost "+result +" armies": t2.getOwner().getName()+" lost "+ (-result) +" armies");

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

        BoardPanel mapPanel = boardView.getMapPanel();
        mapPanel.drawTerritoryInfo(t1, mapPanel.getGraphics());
        mapPanel.drawTerritoryInfo(t2, mapPanel.getGraphics());

        if(t1.getNumArmies() <= 0 ) { //defending territory has no armies left
            JOptionPane.showMessageDialog(null, t1.getName()+" was conquered!");

            Player prevOwner = t1.getOwner();
            prevOwner.loseTerritory(t1);
            t2.getOwner().gainTerritory(t1);
            t1.setOwner(t2.getOwner());

            infoPanel = boardView.getInfoPanel();
            infoPanel.setNumTerritories(t2.getOwner().getNumTerritories());

            mapPanel.drawTerritoryInfo(t1, mapPanel.getGraphics());

            board.toggleTerritorySelection(t2);

            Continent continent = t1.getContinent();
            if(continent.isConquered()) JOptionPane.showMessageDialog(null, continent.getName()+" was conquered!");

            if(prevOwner.getNumTerritories() == 0) {

                //prevOwner is eliminated
                board.removePlayer(prevOwner);
                JOptionPane.showMessageDialog(null, prevOwner.getName() + " was eliminated!");

                if(board.getPlayerList().size() > 1){
                    //game is over
                    JOptionPane.showMessageDialog(null, t2.getOwner().getName() + " has won!");
                    board.clearBoard();
                }
            }
            int armiesToMove = getValidIntegerInput("How many armies would "+t2.getOwner().getName()+" like to move?", attackDice, t2.getNumArmies() - 1);

            board.moveArmies(t2, t1, armiesToMove);

            mapPanel.drawTerritoryInfo(t1, mapPanel.getGraphics());
            mapPanel.drawTerritoryInfo(t2, mapPanel.getGraphics());

            JOptionPane.showMessageDialog(null, "Moved " + armiesToMove + " armies into " + t1.getName());
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

        boardView.getMapPanel().drawTerritorySelection(boardView.getMapPanel().getGraphics());
    }

    public void doAction(){
        TurnStage turnStage = board.getTurnStage();
        if(turnStage == TurnStage.ATTACK) doAttack();
        else if(turnStage == TurnStage.FORTIFY) doFortify();
        else if(turnStage == TurnStage.PLACEMENT) doPlacement();

        boardView.getMapPanel().drawTerritorySelection(boardView.getMapPanel().getGraphics());
    }

    public static boolean isValidIntegerInput(String input, int min, int max){
        if(input == null) return false;
        if(input.isEmpty()) return false;
        for(char ch: input.toCharArray()){
            if(!Character.isDigit(ch)) return false;
        }
        int value = Integer.parseInt(input);
        if(value < min || value > max) return false;
        return true;
    }

    public static int getValidIntegerInput(String message, int min, int max){
        if(min == max) return min;
        String input = "";
        while(!isValidIntegerInput(input, min, max)) input = JOptionPane.showInputDialog(message + " (" + min + " - " + max + ")");
        return Integer.parseInt(input);
    }

    public void createNewGame(){

        BoardConstructor bc = new BoardConstructor();

        board = bc.createMapFromFile("DEFAULT_MAP.xml");

        if(board == null){
            JOptionPane.showMessageDialog(null, "Error constructing default board. Ensure that the file DEFAULT_MAP.xml is in the source directory.");
            return;
        }

        int boardSize = board.getTerritoryList().size();

        int numPlayers = getValidIntegerInput("Enter the number of players:", MIN_PLAYERS, Math.min(MAX_PLAYERS, boardSize));
        int numArmiesEach = STARTING_ARMIES_FOR_NUM_PLAYERS.get(numPlayers);
        if(numPlayers * numArmiesEach < boardSize) {
            JOptionPane.showMessageDialog(null,"The selected map has too many territories to fill. Please start a new game with more players");
            return;
        }

        for(int i=0;i<numPlayers;i++){
            board.addPlayer(new Player(JOptionPane.showInputDialog("Please enter a name for player " + (i+1) + ":", "Player "+ (i+1)), PLAYER_COLOR_FOR_PLAYER_NUM.get(i)));
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

        String message = "- Separate all command words and arguments with commas only (',')\n" +
                "- All names are case insensitive\n" +
                "- Do not use commas in Continent, Territory, or Player names\n" +
                "- Extra arguments after commands will be ignored\n" +
                "- <argument:int> signifies an integer argument\n" +
                "- <argument:String> signifies a name as a string argument\n";

        for(CommandWord commandWord: CommandWord.values()) {
            message += commandWord.getSignature();
            message += commandWord.getDescription();
        }

        JOptionPane.showMessageDialog(null, message,
                "Help", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Quits the application
     */
    public void quit(){
        System.exit(1);
    }

    /**
     * Ends the current phase of the current turn
     */
    public void proceed(){
        TurnStage currentTurnStage = board.getTurnStage();
        if(currentTurnStage == TurnStage.PLACEMENT){
            if(board.getArmiesToPlace() > 0) {
                JOptionPane.showMessageDialog(null,"You still have armies to place");
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
