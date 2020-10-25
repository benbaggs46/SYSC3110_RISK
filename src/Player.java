import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Territory> controlledTerritories;
    private String name;

    public Player(String name){
        controlledTerritories = new ArrayList<>();
        this.name = name;
    }

    public void gainTerritory(Territory territory){
        controlledTerritories.add(territory);
    }

    public void loseTerritory(Territory territory){
        controlledTerritories.remove(territory);
    }

    public int getNumTerritories(){
        return controlledTerritories.size();
    }

    public int getNumArmies(){
        int sum = 0;
        for(Territory t: controlledTerritories) sum += t.getNumArmies();
        return sum;
    }

    public List<Territory> getControlledTerritories(){
        return controlledTerritories;
    }

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