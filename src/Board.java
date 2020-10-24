import java.util.*;

public class Board {
    private List<Continent> continents;
    public static final int MAX_DICE_ROLL = 6;

    public Board(){
        continents = new ArrayList<>();
    }

    public String toString(){
        String string = "";
        for(Continent c: continents){
            string += c + "\n";
        }
        return string;
    }

    public Territory findTerritoryByName(String name){
        for(Territory t: getTerritoryList()){
            if(t.getName().toLowerCase().equals(name.toLowerCase())) return t;
        }
        return null;
    }

    public List<Territory> getTerritoryList(){
        List<Territory> list = new ArrayList<>();
        for(Continent c: continents) {
            list.addAll(c.getTerritoryList());
        }
        return list;
    }

    public void addContinent(Continent continent){
        continents.add(continent);
    }

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
            attackDice.remove(attackDice.indexOf(topAttackDie));
            defendDice.remove(defendDice.indexOf(topDefendDie));
            result += topAttackDie > topDefendDie? 1: -1;
        }

        return result;
    }

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

    public void moveArmies(Territory source, Territory destination, int numArmies){
        destination.addArmies(numArmies);
        source.addArmies(-numArmies);
    }

    public void transferTerritory(Territory territory, Player owner){
        Player prevOwner = territory.getOwner();
        prevOwner.loseTerritory(territory);
        owner.gainTerritory(territory);
        territory.setOwner(owner);
        if(prevOwner.getNumTerritories() == 0) { //prevOwner is eliminated
            System.out.println(prevOwner + " was eliminated!");
        }
    }

    public void fillTerritory(Territory t, Player p){
        p.gainTerritory(t);
        t.addArmies(1);
        t.setOwner(p);
    }

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

