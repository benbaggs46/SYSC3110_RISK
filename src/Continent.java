import java.util.ArrayList;
import java.util.List;

public class Continent {
    /**
     * list of territories in the continent
     */
    private List<Territory> territories;

    /**
     * Number of bonus armies the continent gives
     */
    private int armyBonus;

    /**
     * the name of the continent
     */
    private String name;

    /**
     * constructor for Continent sets the name and army bonus and creates an empty list of territories
     * @param name name of the continent
     * @param armyBonus number of bonus armies the continent gives
     */
    public Continent(String name, int armyBonus){
        this.name = name;
        this.armyBonus = armyBonus;
        territories = new ArrayList<>();
    }

    /**
     * gets the territories within the  continent
     * @return
     */
    public List<Territory> getTerritoryList(){
        return territories;
    }

    /**
     * creates a string representation of the continent
     * @return the name of the continent and a list of territories in the continent
     */
    public String toString(){
        String string = name + "\n";
        for(Territory t: territories){
            string += t + "\n";
        }
        return string;
    }

    /**
     * gets the army bonus of the continent
     * @return the army bonus
     */
    public int getArmyBonus(){
        return armyBonus;
    }

    /**
     * gets the name of the continent
     * @return the name of the continent
     */
    public String getName(){
        return name;
    }

    /**
     * adds a territory to the continent
     * @param territory the territory which is added to the continent
     */
    public void addTerritory(Territory territory){
        territories.add(territory);
    }
}