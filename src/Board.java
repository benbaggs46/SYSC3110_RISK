import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Models the RISK game board, including all players, continents, territories, armies, and turn phases.
 */
public class Board {

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
    public static final Map<Integer, RiskColor> PLAYER_COLOR_FOR_PLAYER_NUM = Map.of(
            0,RiskColor.RED,
            1,RiskColor.BLUE,
            2, RiskColor.YELLOW,
            3, RiskColor.GREEN,
            4, RiskColor.MAGENTA,
            5, RiskColor.GRAY
    );
    /**
     * Indicates whether the game has been started
     */
    private boolean gameHasStarted;

    /**
     * A list of continents that belong to the board
     */
    private List<Continent> continents;
    /**
     * A list of players that are playing on the board
     */
    private List<Player> players;

    /**
     * A list of extra lines to be drawn on the game board.
     * Each line is stored as a List of 4 integers (x1, y1, x2, y2)
     */
    private List<List<Integer>> lines;

    /**
     * The player who's turn it is currently
     */
    private Player currentPlayer;
    /**
     * Number of armies the current player can place
     */
    private int armiesToPlace;

    /**
     * The stage of the turn the current player is on
     */
    private TurnStage turnStage;

    /**
     * A list containing all territories that the user is currently selecting
     */
    private List<Territory> selectedTerritories;

    /**
     * All views to be notified when the game state changes
     */
    private List<RiskView> views;

    /**
     * Returns the user input source of the Board
     * @return The user input source of the Board
     */
    public RiskInput getUserInputSource() {
        return userInputSource;
    }

    /**
     * The source of user input the board will ask for while performing actions (attack, fortify, etc.)
     */
    private RiskInput userInputSource;

    /**
     * Indicates whether the game has been won
     */
    private boolean gameIsWon;

    /**
     * Constructor for the board
     */
    public Board(String filename, RiskInput userInputSource, List<String> playerNames, List<Boolean> isAi){
        continents = new ArrayList<>();
        players = new ArrayList<>();
        selectedTerritories = new ArrayList<>();
        lines = new ArrayList<>();
        views = new ArrayList<>();
        this.userInputSource = userInputSource;
        gameHasStarted = false;
        gameIsWon = false;

        BoardConstructor boardConstructor = new BoardConstructor();
        boardConstructor.loadBoardFromFile(filename, this);

        int numPlayers = playerNames.size();

        for(int i = 0; i < numPlayers; i++){
            Player player = new Player(playerNames.get(i), PLAYER_COLOR_FOR_PLAYER_NUM.get(i).getColor(), isAi.get(i));
            players.add(player);
        }

        shuffle(players);

        populateBoard(STARTING_ARMIES_FOR_NUM_PLAYERS.get(numPlayers));

        currentPlayer = players.get(numPlayers - 1);
        turnStage = TurnStage.FORTIFY;
    }

    /**
     * Randomly arranges a list of elements
     * @param list The list to be shuffled
     * @param <E> The type of the list elements
     */
    public static <E> void shuffle(List<E> list){
        Random r = new Random();
        Stack<E> stack = new Stack<>();
        for(int i = 0; i < list.size(); i++){
            int index = r.nextInt(list.size());
            stack.push(list.get(index));
            list.remove(index);
        }
        while(!stack.isEmpty()) list.add(stack.pop());
    }

    /**
     * Sends a string message to all views of the model
     * @param message The string message to be sent
     */
    public void sendMessageToViews(String message){
        for(RiskView boardView: views) {
            boardView.showMessage(message);
        }
    }

    /**
     * Adds a view to the RISK model
     * @param view The view to be added
     */
    public void addRiskView(RiskView view){
        views.add(view);
    }

    /**
     * Adds an extra line to the board
     * @param line The line to be added
     */
    public void addLine(List<Integer> line){
        if(line.size() == 4) lines.add(line);
    }

    /**
     * Returns a list of the extra lines to be drawn on the game board
     * @return A List of all extra lines
     */
    public List<List<Integer>> getLines(){
        return lines;
    }

    /**
     * Returns a list of all territories on the Board
     * @return A List containing all territories on the Board
     */
    public List<Territory> getSelectedTerritories(){
        return selectedTerritories;
    }

    /**
     * Selects the specified territory if it is unselected.
     * Deselects the specified territory if it is selected.
     * @param t The territory whose selection is to be toggled
     */
    public void toggleTerritorySelection(Territory t){

        if(currentPlayer.isAi()) return; //the user cannot select or deselect territories when it is an AI player's turn

        if(selectedTerritories.contains(t)) selectedTerritories.remove(t);
        else selectedTerritories.add(t);
    }

    /**
     * Tells the user how many Armies they have yet to place
     * @return the number of armies to place
     */
    public int getArmiesToPlace() {
        return armiesToPlace;
    }

    /**
     * Calculates how many armies to give to the specified player at the start of their turn
     * @param player the player to calculate for
     * @return the number of armies that player gets at the start of their turn
     */
    private int getArmyBonusForPlayer(Player player){
        return Math.max(player.getNumTerritories() / 3, 3) + getContinentBonusForPlayer(player);
    }

    /**
     * Checks to see if the player controls any of the continents and returns the bonus' they get from that
     * @param player the player to check
     * @return the number of bonus armies the player gets
     */
    private int getContinentBonusForPlayer(Player player){
        int sum = 0;
        for(Continent c: continents){
            boolean controlsContinent = true;
            for(Territory t: c.getTerritoryList()){
                if(t.getOwner() != player) {
                    controlsContinent = false;
                    break;
                }
            }
            if(controlsContinent) sum += c.getArmyBonus();
        }
        return sum;
    }

    /**
     * Searches for a territory with the same name as the one given
     * @param name The territory to look for
     * @return the territory object corresponding to the name. Null if territory not found
     */
    public Territory findTerritoryByName(String name){
        for(Territory t: getTerritoryList()){
            if(t.getName().toLowerCase().equals(name.toLowerCase())) return t;
        }
        return null;
    }

    /**
     * Goes through the continents list finding all the Territory objects in
     * each continent.
     * @return List of all Territory objects
     */
    public List<Territory> getTerritoryList(){
        List<Territory> list = new ArrayList<>();
        for(Continent c: continents) {
            list.addAll(c.getTerritoryList());
        }
        return list;
    }

    /**
     * Add a continent to the continent list
     * @param continent the continent to add to the list
     */
    public void addContinent(Continent continent){
        continents.add(continent);
    }

    /**
     * Tests if the two input territories are connected through territories all owned by the same player
     * @param t1 The first territory
     * @param t2 The second territory
     * @return A boolean indicating whether the two territories are connected
     */
    public static boolean areConnected(Territory t1, Territory t2){
        Set<Territory> visited = new HashSet<>();
        Queue<Territory> queue = new LinkedList<>();
        queue.add(t1);
        visited.add(t1);
        while(!queue.isEmpty()) {
            Territory t = queue.remove();
            if(t == t2) return true;
            for(Territory neighbour: t.getNeighbours()) {
                if(t.getOwner() != neighbour.getOwner()) continue;
                if(!visited.contains(neighbour)){
                    visited.add(neighbour);
                    queue.add(neighbour);
                }
            }
        }
        return false;
    }

    /**
     * Moves the specified number of armies from one territory to another
     * @param source The territory where armies are to be moved from
     * @param destination The territory where armies are to be moved to
     * @param numArmies The number of armies to move
     */
    private void moveArmies(Territory source, Territory destination, int numArmies){
        destination.addArmies(numArmies);
        source.addArmies(-numArmies);
    }

    /**
     * Fills the board with the specified players and armies, making it ready for play.
     * Each player is given an equal amount of territories, distributed randomly around the board.
     * Each player is given an equal number of armies, distributed randomly throughout their territories, with a minimum of 1 on any territory.
     * @param numArmiesEach The number of armies given to each player
     */
    private void populateBoard(int numArmiesEach){
        Random r = new Random();
        int numPlayers = players.size();
        int[] armiesLeftEach = new int[numPlayers];
        for(int i = 0; i < numPlayers; i++){
            armiesLeftEach[i] = numArmiesEach;
        }
        List<Territory> unfilledTerritories = getTerritoryList();
        while(!unfilledTerritories.isEmpty()) {
            for (int playerIndex = 0; playerIndex < numPlayers; playerIndex++) {
                if(unfilledTerritories.isEmpty()) break;
                int territoryIndex = r.nextInt(unfilledTerritories.size());
                Territory t = unfilledTerritories.remove(territoryIndex);
                Player p = players.get(playerIndex);
                p.gainTerritory(t);
                t.addArmies(1);
                t.setOwner(p);
                armiesLeftEach[playerIndex]--;
            }
        }

        boolean donePlacing = false;
        while(!donePlacing){
            for(int playerIndex = 0; playerIndex < numPlayers; playerIndex++){
                if(armiesLeftEach[playerIndex] <= 0) continue;
                Player p = players.get(playerIndex);
                int territoryIndex = r.nextInt(p.getNumTerritories());
                Territory t = p.getControlledTerritories().get(territoryIndex);
                t.addArmies(1);
                t.setOwner(p);
                armiesLeftEach[playerIndex]--;
            }
            donePlacing = true;
            for(int i = 0; i < numPlayers; i++){
                if(armiesLeftEach[i] > 0) donePlacing = false;
            }
        }
    }

    /**
     * Prompts the user for details when placing/retracting armies, and calls a method to perform the specified army placement/retraction
     */
    private void doPlacement(){
        if(selectedTerritories.size() != 1) {
            sendMessageToViews("Invalid number of territories selected (must be 1)");
            return;
        }

        Territory t = selectedTerritories.get(0);

        if(t.getOwner() != currentPlayer) {
            sendMessageToViews("You do not control that territory");
            return;
        }

        boolean retracting;

        if(t.getTempArmies() == 0){
            if(armiesToPlace == 0){
                sendMessageToViews("You have no more armies to place");
                return;
            }
            else{
                retracting = false;
            }
        }
        else if(armiesToPlace == 0){
            retracting = true;
        }
        else {
            Object[] options = {"Place", "Retract"};
            retracting = userInputSource.getOption("Would you like to place or retract armies?", options) > 0;
        }
        int armiesToPlace = userInputSource.getIntInput("How many armies would you like to " + (retracting? "retract from ": "place in ") + t.getName() + "?", 1, (retracting? t.getTempArmies(): getArmiesToPlace()));
        place(t, (retracting? -1: 1) * armiesToPlace);
    }

    /**
     * Places the specified number of armies in the specified territory in the Board model
     * @param t The territory to place armies in
     * @param armies The number of armies to place
     */
    public void place(Territory t, int armies){
        t.addTempArmies(armies);
        armiesToPlace -= armies;

        for(RiskView boardView: views) {
            boardView.updateUI(new UIEvent(this, turnStage, currentPlayer, getArmyBonusForPlayer(currentPlayer), armiesToPlace));
        }

        List<Territory> territoriesToUpdate = new ArrayList<>();
        territoriesToUpdate.add(t);
        for(RiskView boardView: views) {
            boardView.updateMap(new MapEvent(this, territoriesToUpdate));
        }

        if(!currentPlayer.isAi()) {
            if (armies > 0)
                sendMessageToViews("Placed " + armies + " armies in " + t.getName());
            else if (armies < 0)
                sendMessageToViews("Retracted " + (-armies) + " armies from " + t.getName());
        }
    }

    /**
     * Moves the board to the next player's turn
     */
    private void nextTurn(){

        currentPlayer = players.get((players.indexOf(currentPlayer) + 1) % players.size());
        armiesToPlace = getArmyBonusForPlayer(currentPlayer);

        if(!currentPlayer.isAi())
            sendMessageToViews("It is now " + currentPlayer.getName() + "'s turn");

        else{
            AIPlayer.takeTurn(this, currentPlayer);
        }
    }

    /**
     * Moves the board to the next turn phase
     */
    public void nextTurnStage(){

        if(gameIsWon) return;

        turnStage = TurnStage.values()[(turnStage.ordinal() + 1) % TurnStage.values().length];
        selectedTerritories.clear();

        if(turnStage == TurnStage.PLACEMENT) {
            nextTurn();
        }

        else if(turnStage == TurnStage.ATTACK){
            if(armiesToPlace > 0) {
                turnStage = TurnStage.PLACEMENT;
                sendMessageToViews("You still have armies to place");
                return;
            }
            for(Territory t: getTerritoryList()){
                if(t.getTempArmies() == 0) continue;
                t.confirmTempArmies();
            }
        }

        for(RiskView boardView: views) {
            boardView.updateUI(new UIEvent(this, turnStage, currentPlayer, getArmyBonusForPlayer(currentPlayer), armiesToPlace));
            boardView.updateMap(new MapEvent(this, getTerritoryList()));
        }
    }

    /**
     * Prompts the user for details when fortifying troops, and calls a method to perform the specified fortification
     */
    private void doFortify(){

        if(selectedTerritories.size() != 2) {
            sendMessageToViews("Invalid number of territories selected (must be 2)");
            return;
        }

        Territory t1 = selectedTerritories.get(0);
        Territory t2 = selectedTerritories.get(1);

        if(t1.getOwner() != currentPlayer || t2.getOwner() != currentPlayer) {
            sendMessageToViews("Current player doesn't own both selected territories");
            return;
        }

        if (!areConnected(t1, t2)) {
            sendMessageToViews("Selected territories are not connected");
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
            t1IsDestination = userInputSource.getOption("In which direction do you want to fortify?", options) > 0;
        }

        int armiesToFortify = userInputSource.getIntInput("How many armies would you like to fortify?", 1, (t1IsDestination ? t2.getNumArmies() : t1.getNumArmies()) - 1);
        fortify((t1IsDestination? t1: t2), (t1IsDestination? t2: t1), armiesToFortify);
    }

    /**
     * Fortifies the specified number of armies between the specified territories in the Board model
     * @param t1 The territory armies are to be fortified to
     * @param t2 The territory armies are to be fortified from
     * @param armies The number of armies to fortify
     */
    public void fortify(Territory t1, Territory t2, int armies){
        moveArmies(t2, t1, armies);

        List<Territory> territoriesToUpdate = new ArrayList<>();
        territoriesToUpdate.add(t1);
        territoriesToUpdate.add(t2);

        for(RiskView boardView: views) {
            boardView.updateMap(new MapEvent(this, territoriesToUpdate));
        }

        if(!currentPlayer.isAi())
            sendMessageToViews("Fortified " + armies + " armies from " + t2.getName() + " to " + t1.getName());

        nextTurnStage();
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
     * Prompts the user for details when attacking, and calls a method to perform the specified attack
     */
    private void doAttack(){

        if(getSelectedTerritories().size() != 2) {
            sendMessageToViews("Invalid number of territories selected (must be 2)");
            return;
        }

        Territory t1 = selectedTerritories.get(0);
        Territory t2 = selectedTerritories.get(1);

        if((t1.getOwner() == currentPlayer) == (t2.getOwner() == currentPlayer)) {
            sendMessageToViews("Current player owns neither or both selected territories");
            return;
        }

        if(!(t1.getNeighbours().contains(t2))) {
            sendMessageToViews("The selected territories do not border each other");
            return;
        }

        Territory attackingTerritory;
        Territory defendingTerritory;
        if(t1.getOwner() == currentPlayer){
            attackingTerritory = t1;
            defendingTerritory = t2;
        }
        else{
            attackingTerritory = t2;
            defendingTerritory = t1;
        }

        if(attackingTerritory.getNumArmies() < 2) {
            sendMessageToViews("You don't have enough armies there to attack with");
            return;
        }

        int attackDice = currentPlayer.isAi()? Math.min(attackingTerritory.getNumArmies() - 1, MAX_ATTACK_DICE):
                userInputSource.getIntInput("How many armies would "+currentPlayer.getName()+" like to attack with?", 1, Math.min(attackingTerritory.getNumArmies() - 1, MAX_ATTACK_DICE));

        int defendDice = defendingTerritory.getOwner().isAi()? Math.min(defendingTerritory.getNumArmies(), MAX_DEFEND_DICE):
                userInputSource.getIntInput("How many armies would "+ defendingTerritory.getOwner().getName() +" like to defend with?", 1, Math.min(defendingTerritory.getNumArmies(), MAX_DEFEND_DICE));

        attack(defendingTerritory, attackingTerritory, attackDice, defendDice);
    }

    /**
     * Attacks with the specified army numbers between the specified territories in the Board model
     * @param t1 The defending territory
     * @param t2 The attacking territory
     * @param attackDice The number of attacking armies
     * @param defendDice The number of defending armies
     */
    public void attack(Territory t1, Territory t2, int attackDice, int defendDice){

        int result = attackResult(attackDice, defendDice);

        if(!t1.getOwner().isAi() || !t2.getOwner().isAi())
        sendMessageToViews(result == 0 ? "Both players lost an army" : (result > 0) ? t1.getOwner().getName() + " lost " + result + " armies" : t2.getOwner().getName() + " lost " + (-result) + " armies");

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

        for(RiskView boardView: views) {
            boardView.updateUI(new UIEvent(this, turnStage, currentPlayer, getArmyBonusForPlayer(currentPlayer), armiesToPlace));
        }

        List<Territory> territoriesToUpdate = new ArrayList<>();
        territoriesToUpdate.add(t1);
        territoriesToUpdate.add(t2);

        for(RiskView boardView: views) {
            boardView.updateMap(new MapEvent(this, territoriesToUpdate));
        }

        if(t1.getNumArmies() <= 0 ) { //defending territory has no armies left
            if(!currentPlayer.isAi()) {
                sendMessageToViews(t1.getName() + " was conquered!");
            }

            Player prevOwner = t1.getOwner();
            prevOwner.loseTerritory(t1);
            t2.getOwner().gainTerritory(t1);
            t1.setOwner(t2.getOwner());

            territoriesToUpdate = new ArrayList<>();
            territoriesToUpdate.add(t1);

            toggleTerritorySelection(t2);

            for(RiskView boardView: views) {
                boardView.updateUI(new UIEvent(this, turnStage, currentPlayer, getArmyBonusForPlayer(currentPlayer), armiesToPlace));
                boardView.updateMap(new MapEvent(this, territoriesToUpdate));
            }

            Continent continent = t1.getContinent();
            if(continent.isConquered()) {
                if (!currentPlayer.isAi()) {
                    sendMessageToViews(continent.getName() + " was conquered!");
                }
            }

            if(prevOwner.getNumTerritories() == 0) {

                //prevOwner is eliminated
                players.remove(prevOwner);

                sendMessageToViews(prevOwner.getName() + " was eliminated!");

                if(players.size() == 1){
                    sendMessageToViews(t2.getOwner().getName() + " has won!");
                    gameIsWon = true;
                    return;
                }
            }

            int armiesToMove = currentPlayer.isAi()? t2.getNumArmies() - 1:
                    userInputSource.getIntInput("How many armies would "+t2.getOwner().getName()+" like to move?", attackDice, t2.getNumArmies() - 1);

            moveArmies(t2, t1, armiesToMove);

            territoriesToUpdate = new ArrayList<>();
            territoriesToUpdate.add(t1);
            territoriesToUpdate.add(t2);

            for(RiskView boardView: views) {
                boardView.updateMap(new MapEvent(this, territoriesToUpdate));
            }
           if(!currentPlayer.isAi())
                sendMessageToViews("Moved " + armiesToMove + " armies into " + t1.getName());
        }
    }

    /**
     * Manipulates the board accordingly when the user presses the PROCEED button
     */
    public void proceed(){
        if(currentPlayer.isAi()) return; //the user cannot move to next turn stage when it is an AI player's turn
        nextTurnStage();
    }

    /**
     * Manipulates the board accordingly when the user presses the ACTION button
     */
    public void doAction(){
        if(currentPlayer.isAi()) return; //the user cannot perform an action when it is an AI player's turn
        if(turnStage == TurnStage.ATTACK) doAttack();
        else if(turnStage == TurnStage.FORTIFY) doFortify();
        else if(turnStage == TurnStage.PLACEMENT) doPlacement();
    }
}

