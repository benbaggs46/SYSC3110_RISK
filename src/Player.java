import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Territory> controlledTerritories;

    public Player(){
        controlledTerritories = new ArrayList<>();
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
}
