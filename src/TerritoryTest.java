import java.awt.*;

import static org.junit.Assert.*;

public class TerritoryTest {

    Player p1 = new Player("John Doe", Color.RED);
    Player p2 = new Player("Jane Doe", Color.BLUE);


    // Alaska points
    int[] xp = {22, 82, 98, 110};
    int[] yp = {60, 34, 82, 124};
    // Ontario points
    int[] xp1 = {190, 260, 236, 184};
    int[] yp1 = {82, 106, 160, 152};
    // Quebec points
    int[] xp2 = {270, 322, 286, 236};
    int[] yp2 = {80, 140, 168, 160};

    // Alaska Polygon
    Polygon poly = new Polygon(xp, yp, 4);
    // Ontario Polygon
    Polygon poly1 = new Polygon(xp1, yp1, 4);
    // Quebec Polygon
    Polygon poly2 = new Polygon(xp2, yp2, 4);

    // Continent - North America
    Continent cont = new Continent("North America", 5, Color.RED);
    Continent cont1 = new Continent("North America", 5, Color.GREEN);
    // 2 variables for North America to check that cont and cont1 are not equal due
    // to different colours.

    // Continent - Asia
    Continent cont2 = new Continent("Asia", 6, Color.BLUE);

    Territory terr = new Territory("Alaska", cont, poly);
    Territory terr1 = new Territory("Ontario", cont, poly1);
    Territory terr2 = new Territory("Quebec", cont, poly2);

    @org.junit.Test
    public void getPolygon() {
        assertEquals(poly1, terr1.getPolygon());
        assertEquals(poly2, terr2.getPolygon());
        assertNotEquals(poly, terr1.getPolygon());
        assertNotEquals(poly, terr2.getPolygon());
    }

    @org.junit.Test
    public void getContinent() {
        assertEquals(cont, terr1.getContinent());
        assertNotEquals(cont1, terr1.getContinent());
        assertNotEquals(cont2, terr1.getContinent());
    }

    @org.junit.Test
    public void getTempArmies() {
        assertEquals(0, terr1.getTempArmies());
        terr1.addTempArmies(3);
        assertEquals(3, terr1.getTempArmies());
        terr2.addTempArmies(2);
        assertEquals(2, terr2.getTempArmies());
        assertNotEquals(2, terr1.getTempArmies());
    }

    @org.junit.Test
    public void addTempArmies() {
        // Tested in getTempArmies()
    }

    @org.junit.Test
    public void confirmTempArmies() {
        terr.confirmTempArmies();
        assertEquals(0, terr1.getNumArmies());
        terr.addTempArmies(3);
        assertEquals(0, terr1.getNumArmies());
        terr.confirmTempArmies();
        assertEquals(3, terr1.getNumArmies());
        terr.addTempArmies(2);
        assertEquals(3, terr1.getNumArmies());
        terr.confirmTempArmies();
        assertEquals(5, terr1.getNumArmies());
    }

    @org.junit.Test
    public void testToString() {
        terr1.setOwner(p1);
        p1.gainTerritory(terr1);
        String string1 = "Ontario" + "\nOwner: " + "John Doe" + "\nArmies: " + 0;
        assertEquals(string1, terr1.toString());
        terr.setOwner(p2);
        p2.gainTerritory(terr);
        String string = "Alaska" + "\nOwner: " + "Jane Doe" + "\nArmies: " + 0;
        assertEquals(string, terr.toString());
        assertNotEquals(string1, terr.toString());
        terr.addTempArmies(3);
        terr1.addTempArmies(4);
        assertNotEquals(string1, terr1.toString());
        assertNotEquals(string, terr.toString());
        string1 = "Ontario" + "\nOwner: " + "John Doe" + "\nArmies: " + 0
                + "\nUnconfirmed armies: "+ terr1.getTempArmies();
        string = "Alaska" + "\nOwner: " + "Jane Doe" + "\nArmies: " + 0
                + "\nUnconfirmed armies: "+ terr.getTempArmies();
        assertEquals(string1, terr1.toString());
        assertEquals(string, terr.toString());
        terr.confirmTempArmies();
        terr1.confirmTempArmies();
        assertNotEquals(string1, terr1.toString());
        assertNotEquals(string, terr.toString());
        string1 = "Ontario" + "\nOwner: " + "John Doe" + "\nArmies: " + 4;
        string = "Alaska" + "\nOwner: " + "Jane Doe" + "\nArmies: " + 3;
        assertEquals(string1, terr1.toString());
        assertEquals(string, terr.toString());
    }

    @org.junit.Test
    public void getNeighbours() {
        assertEquals(0, terr1.getNeighbours().size());
        terr1.addNeighbour(terr);
        assertEquals(1, terr1.getNeighbours().size());
        terr1.addNeighbour(terr2);
        assertEquals(2, terr1.getNeighbours().size());
    }

    @org.junit.Test
    public void addNeighbour() {
        // Tested in getNeighbours()
    }

    @org.junit.Test
    public void getOwner() {
        assertNull(terr.getOwner());
        terr.setOwner(p1);
        p1.gainTerritory(terr);
        assertEquals(p1, terr.getOwner());
        terr1.setOwner(p2);
        p2.gainTerritory(terr1);
        assertEquals(p2, terr1.getOwner());
        terr.setOwner(p2);
        p2.gainTerritory(terr);
        assertEquals(p2, terr.getOwner());
        p1.gainTerritory(terr1);
        assertNotEquals(p1, terr.getOwner());
    }

    @org.junit.Test
    public void setOwner() {
        // Tested in getOwner()
    }

    @org.junit.Test
    public void getNumArmies() {
        assertEquals(0, terr1.getNumArmies());
        terr1.addArmies(3);
        assertEquals(3, terr1.getNumArmies());
        terr1.addArmies(2);
        assertEquals(5, terr1.getNumArmies());
        terr1.addTempArmies(2);
        assertEquals(5, terr1.getNumArmies());
        terr1.confirmTempArmies();
        assertEquals(7, terr1.getNumArmies());

    }

    @org.junit.Test
    public void addArmies() {
        // Tested in getNumArmies()
    }

    @org.junit.Test
    public void getName() {
        assertEquals("Alaska", terr.getName());
        assertEquals("Ontario", terr1.getName());
        assertEquals("Quebec", terr2.getName());
        assertNotEquals("Japan", terr.getName());
    }
}