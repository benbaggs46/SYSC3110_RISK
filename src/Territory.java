import java.util.ArrayList;
import java.util.List;

public class Territory {
    private List<Territory> neighbours;
    private Player owner;
    private int numArmies;
    private String name;
    private int tempArmies;

    public Territory(String name){
        neighbours = new ArrayList<>();
        this.name = name;
        numArmies = 0;
        tempArmies = 0;
    }

    public int getTempArmies(){
        return tempArmies;
    }

    public void addTempArmies(int amount){
        tempArmies += amount;
    }

    public void confirmTempArmies(){
        addArmies(tempArmies);
        tempArmies = 0;
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
