import java.awt.*;

import static org.junit.Assert.*;

public class PlayerTest {

    @org.junit.Test
    public void getColor() {
        Player p = new Player("John", Color.BLUE);
        assertEquals(p.getColor(), Color.BLUE);
    }

    @org.junit.Test
    public void gainTerritory() {
        Player p = new Player("John", Color.BLUE);
        Continent c = new Continent("North America", 10, Color.YELLOW);
        Polygon pol = new Polygon();
        Territory t = new Territory("Ontario", c, pol);
        p.gainTerritory(t);
        assertEquals(p.getNumTerritories(), 1);
        assertEquals(p.getControlledTerritories().get(0), t);
    }

    @org.junit.Test
    public void loseTerritory() {
        Player p = new Player("John", Color.BLUE);
        Continent c = new Continent("North America", 10, Color.YELLOW);
        Polygon pol = new Polygon();
        Territory t = new Territory("Ontario", c, pol);
        p.loseTerritory(t);
        assertEquals(p.getNumTerritories(), 0);
        Territory t1 = new Territory("Quebec", c, pol);
        p.gainTerritory(t);
        p.gainTerritory(t1);
        p.loseTerritory(t);
        assertEquals(p.getNumTerritories(), 1);
        assertEquals(p.getControlledTerritories().get(0), t1);
    }

    @org.junit.Test
    public void getNumTerritories() {
        Player p = new Player("John", Color.BLUE);
        Continent c = new Continent("North America", 10, Color.YELLOW);
        Polygon pol = new Polygon();
        Territory t = new Territory("Ontario", c, pol);
        assertEquals(p.getNumTerritories(), 0);
        p.gainTerritory(t);
        assertEquals(p.getNumTerritories(), 1);
        Territory t1 = new Territory("Quebec", c, pol);
        p.gainTerritory(t1);
        assertEquals(p.getNumTerritories(), 2);
        p.loseTerritory(t);
        assertEquals(p.getNumTerritories(), 1);
        p.loseTerritory(t1);
        assertEquals(p.getNumTerritories(), 0);
    }

    @org.junit.Test
    public void getNumArmies() {
        Player p = new Player("John", Color.BLUE);
        Continent c = new Continent("North America", 10, Color.YELLOW);
        Polygon pol = new Polygon();
        Territory t = new Territory("Ontario", c, pol);
        t.addArmies(5);
        assertEquals(p.getNumArmies(), 0);
        p.gainTerritory(t);
        assertEquals(p.getNumArmies(), 5);
        Territory t1 = new Territory("Quebec", c, pol);
        t1.addArmies(2);
        p.gainTerritory(t1);
        assertEquals(p.getNumArmies(), 7);
        p.loseTerritory(t1);
        assertEquals(p.getNumArmies(), 5);
        p.loseTerritory(t);
        assertEquals(p.getNumArmies(), 0);
    }

    @org.junit.Test
    public void getControlledTerritories() {
        Player p = new Player("John", Color.BLUE);
        Continent c = new Continent("North America", 10, Color.YELLOW);
        Polygon pol = new Polygon();
        Territory t = new Territory("Ontario", c, pol);
        assertTrue(p.getControlledTerritories().isEmpty());
        p.gainTerritory(t);
        assertEquals(p.getNumTerritories(), 1);
        assertEquals(p.getControlledTerritories().get(0), t);
        Territory t1 = new Territory("Quebec", c, pol);
        p.gainTerritory(t1);
        assertEquals(p.getNumTerritories(), 2);
        assertEquals(p.getControlledTerritories().get(0), t);
        assertEquals(p.getControlledTerritories().get(1), t1);
        p.loseTerritory(t1);
        assertEquals(p.getNumTerritories(), 1);
        assertEquals(p.getControlledTerritories().get(0), t);
        p.loseTerritory(t);
        assertTrue(p.getControlledTerritories().isEmpty());
    }

    @org.junit.Test
    public void getName() {
        Player p = new Player("John", Color.BLUE);
        assertEquals(p.getName(), "John");
    }

    @org.junit.Test
    public void testToString() {
        Player p = new Player("John", Color.BLUE);
        Continent c = new Continent("North America", 10, Color.YELLOW);
        Polygon pol = new Polygon();
        Territory t = new Territory("Ontario", c, pol);
        t.addArmies(5);
        Territory t1 = new Territory("Quebec", c, pol);
        t1.addArmies(2);
        assertEquals(p.toString(), "John controls 0 territories with 0 armies");
        p.gainTerritory(t);
        assertEquals(p.toString(), "John controls 1 territories with 5 armies\n" +
                "Ontario\n" +
                "Armies: 5");
        p.gainTerritory(t1);
        assertEquals(p.toString(), "John controls 2 territories with 7 armies\n" +
                "Ontario\n" +
                "Armies: 5\n" +
                "Quebec\n" +
                "Armies: 2");
        p.loseTerritory(t1);
        assertEquals(p.toString(), "John controls 1 territories with 5 armies\n" +
                "Ontario\n" +
                "Armies: 5");
        p.loseTerritory(t);
        assertEquals(p.toString(), "John controls 0 territories with 0 armies");;
    }
}
