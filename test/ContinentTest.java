import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class ContinentTest {
    Color col = new Color(255, 204, 0); // Yellow
    Color col2 = new Color(0,225,0); //Green
    Board board;
    Continent continent;
    Territory territory;


    @Test
    public void getColor() {
        continent = new Continent("NA", 5, col );
        assertEquals(col, continent.getColor());
        assertNotEquals(col2, continent.getColor());
    }

    @Test
    public void getTerritoryList() {
        //TODO
        continent = new Continent("NA", 2, col2);
        //territory = new Territory("OntariO",continent, ?????? );
        //continent.addTerritory(OntariO);
        //assertEquals("OntariO", continent.getTerritoryList() );
        assertNotEquals("Ontario", continent.getTerritoryList() );

    }

    @Test
    public void isConquered() {
        //TODO
    }

    @Test
    public void testToString() {
        //TODO
    }

    @Test
    public void getArmyBonus() {
        continent = new Continent("NA",5,col);
        Continent c2 = new Continent("SA",3,col2);
        assertEquals(5, continent.getArmyBonus());
        assertEquals(3, c2.getArmyBonus());
        assertNotEquals(5, c2.getArmyBonus());
        assertNotEquals("", c2.getArmyBonus());
        assertNotNull(c2.getArmyBonus());
    }

    @Test
    public void getName() {
        continent = new Continent("NA",5,col);
        Continent c2 = new Continent("SA",3,col2);
        assertEquals("NA", continent.getName());
        assertEquals("SA", c2.getName());
        assertNotEquals(5, c2.getName());
        assertNotEquals("", c2.getName());
        assertNotNull(c2.getName());
    }

    @Test
    public void addTerritory() {
        //TODO
    }
}