import java.util.*;

public class Board {
    /**
     * A list of continents that belong to the board
     */
    private List<Continent> continents;
    /**
     * A list of players that are playing on the board
     */
    private List<Player> players;
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
     * The max number of dice rolls
     */
    public static final int MAX_DICE_ROLL = 6;

    /**
     * Constructor for the board
     */
    public Board(){
        continents = new ArrayList<>();
        players = new ArrayList<>();
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
    public void goToNextTurn(){
        currentPlayer = players.get((players.indexOf(currentPlayer) + 1) % players.size());
        armiesToPlace = getArmyBonusForPlayer(currentPlayer);
    }

    /**
     * Sets turnStage to the next turn stage
     */
    public void goToNextTurnStage(){
        turnStage = TurnStage.values()[(turnStage.ordinal() + 1) % TurnStage.values().length];
    }

    /**
     * Sets the objects turnStage to what is given
     * @param turnStage the turnStage to go to
     */
    public void setTurnStage(TurnStage turnStage) {
        this.turnStage = turnStage;
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
     * Conducts a dice battle with the specified number of dice. Positive return values indicate that the attacker
     * won the battle. Negative values indicate the defender has won.
     * @param attackerDiceNum The number of dice the attacker will use
     * @param defenderDiceNum The number of dice the defender will use
     * @return the result of the dice battle (positive indicates attacker won, negative indicates defender won)
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
     *
     * @param attackingTerritory
     * @param defendingTerritory
     * @param attackerDiceNum
     * @param defenderDiceNum
     */
    public void attack(Territory attackingTerritory, Territory defendingTerritory, int attackerDiceNum, int defenderDiceNum){
        int result = attackResult(attackerDiceNum, defenderDiceNum);

        Parser.displayMessage(result == 0? "Both players lost an army": (result > 0)? defendingTerritory.getOwner()+" lost "+result +" armies": attackingTerritory.getOwner()+" lost "+ (-result) +" armies");

        if(result == 0){ //both players lose one army
            attackingTerritory.addArmies(-1);
            defendingTerritory.addArmies(-1);
        }
        else if(result > 0) { //defender loses armies
            defendingTerritory.addArmies(-result);
        }
        else { //attacker loses armies
            attackingTerritory.addArmies(result);
        }
        if(defendingTerritory.getNumArmies() <= 0 ) { //defending territory has no armies left
            Parser.displayMessage(defendingTerritory.getName()+" was conquered!");

            transferTerritory(defendingTerritory, attackingTerritory.getOwner());
            moveArmies(attackingTerritory, defendingTerritory, attackerDiceNum);
        }
    }

    /**
     *
     * @param t1
     * @param t2
     * @return
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
     *
     * @param source
     * @param destination
     * @param numArmies
     */
    public void moveArmies(Territory source, Territory destination, int numArmies){
        destination.addArmies(numArmies);
        source.addArmies(-numArmies);
    }

    /**
     *
     * @param territory
     * @param owner
     */
    public void transferTerritory(Territory territory, Player owner){
        Player prevOwner = territory.getOwner();
        prevOwner.loseTerritory(territory);
        owner.gainTerritory(territory);
        territory.setOwner(owner);
        if(prevOwner.getNumTerritories() == 0) {

            //prevOwner is eliminated
            players.remove(prevOwner);
            Parser.displayMessage(prevOwner.getName() + " was eliminated!");

            if(players.size() > 1){
                //game is over
                Parser.displayMessage(owner.getName() + " has won!");
                clearBoard();
            }
        }

        //ask owner how many armies they want to move
    }

    public void clearBoard(){ //empties the current board
        continents.clear();
        players.clear();
        currentPlayer = null;
        armiesToPlace = 0;
        turnStage = TurnStage.PLACEMENT;

        /*the territories and players still reference each other, but their memory
        should still be freed. Java's garbage collection handles cyclic references*/
    }

    public boolean isEmpty(){
        return continents.isEmpty();
    }

    /**
     *
     * @param players
     * @param numArmiesEach
     */
    public void populateBoard(List<Player> players, int numArmiesEach){
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
}

