import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

/**
 * Holds information about the RISK map to be given to a view when the map changes
 */
public class MapEvent extends EventObject {

    /**
     * The territories that have changes
     */
    private List<Territory> territoryList;

    public MapEvent(Object source, List<Territory> territoryList) {
        super(source);
        this.territoryList = new ArrayList<>();
        for(Territory t: territoryList){
            this.territoryList.add(t);
        }
    }

    /**
     * Returns the territories that have changed
     * @return The territories that have changed
     */
    public List<Territory> getTerritoryList(){
        return territoryList;
    }
}
