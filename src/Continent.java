/**
 * Models a continent in RISK.
 * Each continent is comprised of territories and gives an army per turn bonus to any player who controls all of its territories.
 */

import java.awt.*;
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
     * The color used to indicate the continent when drawn on board
     */
    private Color color;

    /**
     * constructor for Continent sets the name and army bonus and creates an empty list of territories
     * @param name name of the continent
     * @param armyBonus number of bonus armies the continent gives
     */
    public Continent(String name, int armyBonus, Color color){
        this.name = name;
        this.armyBonus = armyBonus;
        territories = new ArrayList<>();
        this.color = color;
    }

    /**
     * gets the territories within the  continent
     * @return A list of the territories of the continent
     */
    public List<Territory> getTerritoryList(){
        return territories;
    }

    /**
     * Tests whether the specified continent is controlled by a single player
     * @return A boolean indicating whether the continent is controlled by a single player
     */
    public boolean isConquered(){
        Player p = territories.get(0).getOwner();
        for(Territory t: territories){
            if(t.getOwner() != p) return false;
        }
        return true;
    }

    /**
     * creates a string representation of the continent
     * @return the name of the continent and a list of territories in the continent
     */
    public String toString(){
        String string = name + "\nTerritories:\n";
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