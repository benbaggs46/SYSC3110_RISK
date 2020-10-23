import java.util.ArrayList;
import java.util.List;

public class Territory {
    private List<Territory> neighbours;
    private Player owner;
    private int numArmies;

    public Territory(){
        neighbours = new ArrayList<>();
    }

    public List<Territory> getNeighbours() {
        return neighbours;
    }

    public void addNeighbour(Territory neighbour) {
        this.neighbours.add(neighbour);
    }

    public void removeNeighbour(Territory neighbour) {
        this.neighbours.remove(neighbour);
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
}
