import java.util.*;

public class Board {

    /**
     * List of continents that are apart of the board
     */
    private List<Continent> continents;//

    /**
     * The constructor for board. An array list is used to implement continents
     */
    public Board(){
        continents = new ArrayList<>();
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
     * Adds a continent to the continent list
     * @param continent the continent to add to the continent list
     */
    public void addContinent(Continent continent){
        continents.add(continent);
    }

    /*
    public int calcDiceNum(Country c){
        int x = c.getArmyNum() / 3;
    }

    */


    /**
     * Generates a number between 1 and 6, as many times as we want.
     * @param numDie is the number od fice being rolled.
     * @return sum, which is the sum of all the dice roll results.
     */
    public  List<Integer> rollDice(Integer numDie) {
        List<Integer> sum = null;
        Random random = new Random();
        for (int i = 0; i < (numDie -1); i++){
            int x = random.nextInt(5) + 1;
            sum.add(x);
        }
        return sum;
    }

    /**
     *
     * @param attackerDiceNum The number of dice the attacker starts with
     * @param defenderDiceNum The number of dice the defender starts with
     * @return
     */
    public int attackResult(int attackerDiceNum, int defenderDiceNum){
        List<Integer> ADice = rollDice(attackerDiceNum);
        List<Integer> DDice = rollDice(defenderDiceNum);
        int Awins = 0;
        int Dwins = 0;
        for (int i = 0; i <= Math.max(attackerDiceNum, defenderDiceNum); i++){
            int x = Math.max(ADice.get(i), DDice.get(i));
            if (x == ADice.get(i)){
                Awins += 1;
            }
            else if (ADice.get(i) == DDice.get(i)){
                //do nothing, delete later
            }
            else if (x == DDice.get(i)){
                Dwins += 1;
            }

        }
        int ADiceSum = 0;
        for (int i = 0; i<= ADice.size(); i++){
            ADiceSum += ADice.get(i);
        }
        int DDiceSum = 0;
        for (int i = 0; i<= DDice.size(); i++){
            DDiceSum += DDice.get(i);
        }

        if(Awins == Dwins){
            return -1;
        }
        else if (Awins > Dwins){
            return (ADiceSum);
        }
        else if (Awins < Dwins){
            return (ADiceSum);
        }
        return 0;

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
        if(prevOwner.getNumTerritories() == 0) { //prevOwner is eliminated
            System.out.println(prevOwner + " was eliminated!");
        }
    }

    /**
     *
     * @param t
     * @param p
     */
    public void fillTerritory(Territory t, Player p){
        p.gainTerritory(t);
        t.addArmies(1);
        t.setOwner(p);
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
                fillTerritory(t, p);
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
                fillTerritory(t,p);
                armiesLeftEach[playerIndex]--;
            }
            donePlacing = true;
            for(int i = 0; i < numPlayers; i++){
                if(armiesLeftEach[i] > 0) donePlacing = false;
            }
        }
    }
}
