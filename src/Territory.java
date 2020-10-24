import java.util.ArrayList;
import java.util.List;

public class Territory {
    /**
     * List of territories that are beside this territory
     */
    private List<Territory> neighbours;

    /**
     * The player who owns the territory
     */
    private Player owner;
    private int numArmies;
    private String name;

    public Territory(String name){
        neighbours = new ArrayList<>();
        this.name = name;
        numArmies = 0;
    }

    public String toString(){
        return name + "\nOwner: " + owner + "\nArmies: " + numArmies;
    }

    public List<Territory> getNeighbours() {
        return neighbours;
    }

    public void addNeighbour(Territory neighbour) {
        neighbours.add(neighbour);
    }

    public void removeNeighbour(Territory neighbour) {
        neighbours.remove(neighbour);
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public int getNumArmies() {
        return numArmies;
    }

    public void setNumArmies(int numArmies) {
        this.numArmies = numArmies;
    }

    public void addArmies(int numArmies) {
        this.numArmies += numArmies;
    }

    public String getName(){
        return name;
    }
}
