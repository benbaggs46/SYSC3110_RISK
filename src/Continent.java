import java.util.ArrayList;
import java.util.List;

public class Continent {
    private List<Territory> territories;
    private int armyBonus;
    private String name;

    public Continent(String name, int armyBonus){
        this.name = name;
        this.armyBonus = armyBonus;
        territories = new ArrayList<>();
    }

    public List<Territory> getTerritoryList(){
        return territories;
    }

    public String toString(){
        String string = name + "\n";
        for(Territory t: territories){
            string += t + "\n";
        }
        return string;
    }

    public int getArmyBonus(){
        return armyBonus;
    }

    public String getName(){
        return name;
    }

    public void addTerritory(Territory territory){
        territories.add(territory);
    }
}