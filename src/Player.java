import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Models a RISK player.
 * Each player controls a list of territories.
 */
public class Player {
    /**
     * The list of territories the player owns
     */
    private List<Territory> controlledTerritories;

    /**
     * The player's name
     */
    private String name;

    /**
     * The color representing the player on the game board
     */
    private Color color;

    /**
     * Indicates whether the player is AI controlled
     */
    private Boolean ai;

    /**
     * Constructor for the Player object
     * @param name sets the players name to this parameter
     */
    public Player(String name, Color color, Boolean isAi){
        controlledTerritories = new ArrayList<>();
        this.name = name;
        this.color = color;
        this.ai = isAi;
    }

    /**
     * Creates a new Player
     * @param name The player name
     * @param color The player color
     * @param isAi Indicates whether the player is AI controlled
     * @return
     */
    public static Player newPlayer(String name, Color color, Boolean isAi){
        if(isAi) return new AIPlayer(name, color);
        return new Player(name, color, false);
    }

    /**
     * Converts the player to an XML string
     * @return The XML representation of the player
     */
    public String toXML(){
        String xml = "<player>\n";
        xml += "<name>" + name + "</name>";
        xml += "<isAi>" + ai + "</isAi>\n";
        xml += "<color>" + BoardConstructor.colorToHex(color) + "</color>\n";
        for(Territory t: controlledTerritories){
            xml += "<territory>";
            xml += "<name>" + t.getName() + "</name>";
            xml += "<numArmies>" + t.getNumArmies() + "</numArmies>";
            xml += "<tempArmies>" + t.getTempArmies() + "</tempArmies>";
            xml += "</territory>\n";
        }
        xml += "</player>\n";
        return xml;
    }

    /**
     * Returns the color of the player
     * @return The color of the player
     */
    public Color getColor(){
        return color;
    }

    /**
     * Adds a territory to the controlledTerritories list once a player conquers that territory
     * @param territory the territory which the player conquers
     */
    public void gainTerritory(Territory territory){
        controlledTerritories.add(territory);
    }

    /**
     * Removes the specified territory from the controlledTerritories when a player loses ownership of the territory
     * @param territory the territory which the player loses ownership of
     */
    public void loseTerritory(Territory territory){
        controlledTerritories.remove(territory);
    }

    /**
     * Gets the number of territories the player own by getting the size of the controlledTerritories list.
     * @return the size of the controlledTerritories list
     */
    public int getNumTerritories(){
        return controlledTerritories.size();
    }

    /**
     * Gets the total number of armies that the player has
     * @return the total number of armies the player has
     */
    public int getNumArmies(){
        int sum = 0;
        for(Territory t: controlledTerritories) sum += t.getNumArmies() + t.getTempArmies();
        return sum;
    }

    /**
     * get the list of territories the player owns
     * @return the controlledTerritories list
     */
    public List<Territory> getControlledTerritories(){
        return controlledTerritories;
    }

    /**
     * Returns the name of the player
     * @return the player's name
     */
    public String getName(){
        return name;
    }

    /**
     * Gives a string describing all the territories the player controls including how many armies each has
     * @return string containing the status of the player territories and armies
     */
    public String toString(){
        String string = name+ " controls " + getNumTerritories() + " territories with " + getNumArmies() + " armies";
        for (Territory t: controlledTerritories){
            string += "\n" + t.getName() + "\nArmies: " + t.getNumArmies();
        }
        return string;
    }

    /**
     * Indicates whether the player is AI controlled
     * @return True if the player is AI, false otherwise
     */
    public boolean isAi() {
        return ai;
    }
}