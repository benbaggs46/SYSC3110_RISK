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

    /**
     * The number of armies that are on the territory
     */
    private int numArmies;

    /**
     * The name of the territory
     */
    private String name;
    private int tempArmies;
    private Continent continent;

    /**
     * The constructor of territory sets neighbours to an empty array list, name to the value of the parameter and numArmies to 0
     * @param name the name of the territory
     */
    public Territory(String name, Continent continent){
        neighbours = new ArrayList<>();
        this.name = name;
        numArmies = 0;
        tempArmies = 0;
        this.continent = continent;
        continent.addTerritory(this);
    }

    /**
     * Returns the continent that the territory is a part of
     * @return The continent that the territory is a part of
     */
    public Continent getContinent(){
        return continent;
    }

    /**
     * Gives the number of tempArmies the territory has
     * @return number of temp armies
     */
    public int getTempArmies(){
        return tempArmies;
    }

    /**
     * Adds temp armies to the territory
     * @param amount number of temp armies to add
     */
    public void addTempArmies(int amount){
        tempArmies += amount;
    }

    /**
     * Adds the temp armies to the total number of armies, then clears the tempArmies
     */
    public void confirmTempArmies(){
        addArmies(tempArmies);
        tempArmies = 0;
    }

    /**
     * prints a string representation of the Territory
     * @return the name of the territory with its owner and number of armies
     */
    public String toString(){
        String string = name + "\nOwner: " + owner.getName() + "\nArmies: " + numArmies;
        if(tempArmies > 0) string += "\nUnconfirmed armies: "+ tempArmies;
        return string;
    }

    /**
     * Gets the neighbors of the territory
     * @return the list of neighbors
     */
    public List<Territory> getNeighbours() {
        return neighbours;
    }

    /**
     * adds a neighbor to the territory
     * @param neighbour the neighbor which is added as a neighbor to the territory
     */
    public void addNeighbour(Territory neighbour) {
        neighbours.add(neighbour);
    }

    /**
     * gets the owner of the territory
     * @return the player who owns the territory
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * sets the owner of the territory
     * @param owner the player who will own the territory
     */
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    /**
     * gets the number of armies that are on the territory
     * @return the number of armies on the territory
     */
    public int getNumArmies() {
        return numArmies;
    }

    /**
     * adds armies to the territory
     * @param numArmies the number of armies to be added to the territory
     */
    public void addArmies(int numArmies) {
        this.numArmies += numArmies;
    }

    /**
     * gets the name of the territory
     * @return the name of the territory
     */
    public String getName(){
        return name;
    }
}
