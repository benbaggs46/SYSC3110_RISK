import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class MapEvent extends EventObject {

    private List<Territory> territoryList;

    public MapEvent(Object source, List<Territory> territoryList) {
        super(source);
        this.territoryList = new ArrayList<>();
        for(Territory t: territoryList){
            this.territoryList.add(t);
        }
    }

    public List<Territory> getTerritoryList(){
        return territoryList;
    }
}
