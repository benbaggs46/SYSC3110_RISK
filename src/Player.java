import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Territory> controlledTerritories;
    private String name;

    public Player(String name){
        controlledTerritories = new ArrayList<>();
        this.name = name;
    }

    public String toString(){
        return name;
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

    public String getName(){
        return name;
    }
}