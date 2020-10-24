import java.util.*;

public class Board {
    private List<Continent> continents;

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

    public List<Territory> getTerritoryList(){
        List<Territory> list = new ArrayList<>();
        for(Continent c: continents) {
            for(Territory t: c.getTerritoryList()) {
                list.addAll(c.getTerritoryList());
            }
        }
        return list;
    }

    public void addContinent(Continent continent){
        continents.add(continent);
    }

    /*
    public int calcDiceNum(Country c){
        int x = c.getArmyNum() / 3;
    }

    */


    /**
     *Generates a number between 1 and 6, as many times as we want.
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

    public void joinTerritories(Territory t1, Territory t2){
        t1.addNeighbour(t2);
        t2.addNeighbour(t1);
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
}

