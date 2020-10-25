import java.util.ArrayList;
import java.util.List;

public class Player {
    /**
     * The list of territories the player owns
     */
    private List<Territory> controlledTerritories;

    /**
     * The player's name
     */
    private String name;

    /**
     * Constructor for the Player object
     * @param name sets the players name to this parameter
     */
    public Player(String name){
        controlledTerritories = new ArrayList<>();
        this.name = name;
    }

    /**
     * Adds a territory to the controlledTerritories list once a player conquers that territory
     * @param territory the territory which the player conquers
     */
    public void gainTerritory(Territory territory){
        controlledTerritories.add(territory);
    }

    /**
     * Removes the specified territory from the controlledTerritories when a player loses ownership of the territory
     * @param territory the territory which the player loses ownership of
     */
    public void loseTerritory(Territory territory){
        controlledTerritories.remove(territory);
    }

    /**
     * Gets the number of territories the player own by getting the size of the controlledTerritories list.
     * @return the size of the controlledTerritories list
     */
    public int getNumTerritories(){
        return controlledTerritories.size();
    }

    public int getNumArmies(){
        int sum = 0;
        for(Territory t: controlledTerritories) sum += t.getNumArmies();
        return sum;
    }

    /**
     * get the list of territories the player owns
     * @return the controlledTerritories list
     */
    public List<Territory> getControlledTerritories(){
        return controlledTerritories;
    }

    /**
     * Returns the name of the player
     * @return the player's name
     */
    public String getName(){
        return name;
    }

    public String toString(){
        String string = name+ " controls " + getNumTerritories() + " territories with " + getNumArmies() + " armies";
        for (Territory t: controlledTerritories){
            string += "\n" + t.getName() + "\nArmies: " + t.getNumArmies();
        }
        return string;
    }

}