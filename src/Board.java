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
    public static final Map<Integer, Color> PLAYER_COLOR_FOR_PLAYER_NUM = Map.of(
            0,Color.RED,
            1,Color.BLUE,
            2, Color.YELLOW,
            3, Color.GREEN,
            4, Color.MAGENTA,
            5, Color.LIGHT_GRAY
    );
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

    private List<RiskView> views;

    private RiskInput userInputSource;

    /**
     * Constructor for the board
     */
    public Board(RiskInput userInputSource, List<String> playerNames){
        continents = new ArrayList<>();
        players = new ArrayList<>();
        selectedTerritories = new ArrayList<>();
        lines = new ArrayList<>();
        views = new ArrayList<>();
        this.userInputSource = userInputSource;

        BoardConstructor boardConstructor = new BoardConstructor();
        boardConstructor.loadBoardFromFile("DEFAULT_MAP.xml", this);

        int numPlayers = playerNames.size();

        for(int i = 0; i < numPlayers; i++){
            addPlayer(new Player(playerNames.get(i), PLAYER_COLOR_FOR_PLAYER_NUM.get(i)));
        }

        populateBoard(STARTING_ARMIES_FOR_NUM_PLAYERS.get(numPlayers));

        currentPlayer = players.get(numPlayers - 1);
        turnStage = TurnStage.FORTIFY;
        nextTurnStage();
    }

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
        if(selectedTerritories.contains(t)) selectedTerritories.remove(t);
        else selectedTerritories.add(t);
    }

    /**
     * Get the current players turnstage
     * @return current turnstage
     */
    public TurnStage getTurnStage() {
        return turnStage;
    }

    /**
     * Goes to the next players turn and gives them armies
     */
    public void incrementTurn(){
        currentPlayer = players.get((players.indexOf(currentPlayer) + 1) % players.size());
        armiesToPlace = getArmyBonusForPlayer(currentPlayer);
    }

    /**
     * Sets turnStage to the next turn stage
     */
    public void incrementTurnStage(){
        turnStage = TurnStage.values()[(turnStage.ordinal() + 1) % TurnStage.values().length];
    }

    /**
     * Tells the user how many Armies they have yet to place
     * @return the number of armies to place
     */
    public int getArmiesToPlace() {
        return armiesToPlace;
    }

    /**
     * Increases the number of armies left to place by the input
     * @param armiesToPlace number to add to the current number of armies to place
     */
    public void addArmiesToPlace(int armiesToPlace) {
        this.armiesToPlace += armiesToPlace;
    }

    /**
     * Give the player currently stored in currentPlayer
     * @return the currentPlayer
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets the current player to the one given
     * @param currentPlayer player to set as currentPlayer
     */
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Calculates how many armies to give to the specified player at the start of their turn
     * @param player the player to calculate for
     * @return the number of armies that player gets at the start of their turn
     */
    public int getArmyBonusForPlayer(Player player){
        return Math.max((player.getNumTerritories() / 3) + getContinentBonusForPlayer(player), 3);
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
     * Gives a string that contains the output of calling toString on each of
     * the continents in the continents list
     * @return a string containing the return from each continent.toString()
     */
    public String toString(){
        String string = "";
        for(Continent c: continents){
            string += c + "\n";
        }
        return string;
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
     * Gives the continent with the same name as the one specified
     * @param name the name of the continent to search for
     * @return the continent object that corresponds to the name given (null if name doesn't belong to a continent)
     */
    public Continent findContinentByName(String name){
        for(Continent c: continents){
            if(c.getName().toLowerCase().equals(name.toLowerCase())) return c;
        }
        return null;
    }

    /**
     * Give the player with the name equal to one given
     * @param name the name to look for in the player list
     * @return the player object with the same name (null if there is no player with that name)
     */
    public Player findPlayerByName(String name){
        for(Player p: players){
            if(p.getName().toLowerCase().equals(name.toLowerCase())) return p;
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
     * Gives the list of players
     * @return the list of players
     */
    public List<Player> getPlayerList(){
        return players;
    }

    /**
     * Add a player to the player list
     * @param player the player to add to the list
     */
    public void addPlayer(Player player) {players.add(player);}

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
    public boolean areConnected(Territory t1, Territory t2){
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
    public void moveArmies(Territory source, Territory destination, int numArmies){
        destination.addArmies(numArmies);
        source.addArmies(-numArmies);
    }

    /**
     * Removes a player from the board
     * @param player The player to be removed
     */
    public void removePlayer(Player player){
        players.remove(player);
    }

    /**
     * Clears the board, resetting it to its default state after construction
     */
    public void clearBoard(){ //empties the current board
        continents.clear();
        players.clear();
        currentPlayer = null;
        armiesToPlace = 0;
        turnStage = TurnStage.PLACEMENT;

        /*the territories and players still reference each other, but their memory
        should still be freed. Java's garbage collection handles cyclic references*/
    }

    /**
     * Fills the board with the specified players and armies, making it ready for play.
     * Each player is given an equal amount of territories, distributed randomly around the board.
     * Each player is given an equal number of armies, distributed randomly throughout their territories, with a minimum of 1 on any territory.
     * @param numArmiesEach The number of armies given to each player
     */
    public void populateBoard(int numArmiesEach){
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
    public void doPlacement(){
        if(selectedTerritories.size() != 1) {
            for(RiskView boardView: views) {
                boardView.showMessage("Invalid number of territories selected");
            }
            return;
        }

        Territory t = selectedTerritories.get(0);

        if(t.getOwner() != currentPlayer) {
            for(RiskView boardView: views) {
                boardView.showMessage("You do not control that territory");
            }
            return;
        }

        boolean retracting;

        if(t.getTempArmies() == 0){
            if(armiesToPlace == 0){
                for(RiskView boardView: views) {
                    boardView.showMessage("You have no more armies to place");
                }
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
    private void place(Territory t, int armies){
        t.addTempArmies(armies);
        addArmiesToPlace(-armies);

        for(RiskView boardView: views) {
            boardView.updateUI(new UIEvent(this, turnStage, currentPlayer, getArmyBonusForPlayer(currentPlayer), armiesToPlace));
        }

        List<Territory> territoriesToUpdate = new ArrayList<>();
        territoriesToUpdate.add(t);
        for(RiskView boardView: views) {
            boardView.updateMap(new MapEvent(this, territoriesToUpdate));
        }

        if(armies > 0)
            for(RiskView boardView: views) {
                boardView.showMessage("Placed " + armies + " armies in " + t.getName());
            }
        else if(armies < 0)
            for(RiskView boardView: views) {
                boardView.showMessage("Retracted " + (-armies) + " armies from " + t.getName());
            }
    }

    /**
     * Moves the board to the next player's turn
     */
    public void nextTurn(){
        currentPlayer = players.get((players.indexOf(currentPlayer) + 1) % players.size());
        armiesToPlace = getArmyBonusForPlayer(currentPlayer);

        for(RiskView boardView: views) {
            boardView.showMessage("It is now " + currentPlayer.getName() + "'s turn");
        }
    }

    /**
     * Moves the board to the next turn phase
     */
    public void nextTurnStage(){

        if(turnStage == TurnStage.FORTIFY) nextTurn();

        else if(turnStage == TurnStage.PLACEMENT){
            if(armiesToPlace > 0) {
                for(RiskView boardView: views) {
                    boardView.showMessage("You still have armies to place");
                }
                return;
            }
            for(Territory t: getTerritoryList()){
                if(t.getTempArmies() == 0) continue;
                t.confirmTempArmies();
            }
        }

        turnStage = TurnStage.values()[(turnStage.ordinal() + 1) % TurnStage.values().length];

        selectedTerritories.clear();

        for(RiskView boardView: views) {
            boardView.updateUI(new UIEvent(this, turnStage, currentPlayer, getArmyBonusForPlayer(currentPlayer), armiesToPlace));
            boardView.updateMap(new MapEvent(this, getTerritoryList()));
        }
    }
}

