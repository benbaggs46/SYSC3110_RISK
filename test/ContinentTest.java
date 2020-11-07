import org.junit.Test;

import java.awt.*;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class ContinentTest<p1> {
    Color col = new Color(255, 204, 0); // Yellow
    Color col2 = new Color(0,225,0); //Green
    BoardConstructor BC = new BoardConstructor();
    Continent continent = new Continent("NA", 5, col);
    Player player =new Player("John Doe", col2);
    Player p2 = new Player("Jane Doe", col2);
    // Ontario points
    int[] xp1 = {190, 260, 236, 184};
    int[] yp1 = {82, 106, 160, 152};
    Polygon poly1 = new Polygon(xp1, yp1, 4);
    Territory terr1 = new Territory("Ontario", continent, poly1);

    int[] xp2 = {270, 322, 286, 236};
    int[] yp2 = {80, 140, 168, 160};
    Polygon poly2 = new Polygon(xp2, yp2, 4);
    Territory terr2 = new Territory("Quebec", continent, poly2);



    @Test
    public void getColor() {
        continent = new Continent("NA", 5, col );
        assertEquals(col, continent.getColor());
        assertNotEquals(col2, continent.getColor());
    }

    @Test
    public void getTerritoryList() {
        terr1.setOwner(player);
        terr2.setOwner(p2);
        assertEquals ("[" + terr1 + "," , continent.getTerritoryList().toString().substring(0,35));
        assertEquals( terr2 + "]" , continent.getTerritoryList().toString().substring(36));
        assertNotEquals("[" + terr2 + "," , continent.getTerritoryList().toString().substring(0,35));
    }

    @Test
    public void isConquered() {
        terr1.setOwner(player);
        terr2.setOwner(p2);
        assertFalse( continent.isConquered());
        terr2.setOwner(player);
        assertTrue ( continent.isConquered());
    }

    @Test
    public void testToString() {
        terr1.setOwner(player);
        terr2.setOwner(p2);
        //System.out.println(continent.toString());
        String test1 = new String();
        test1 = "NA\n" +  //2
                "Territories:\n" + //11
                "Ontario\n" + //7                     82
                "Owner: John Doe\n" + //15
                "Armies: 0\n" + //9
                "Quebec\n" + //6
                "Owner: Jane Doe\n" + //15
                "Armies: 0" ;//9
        String test2 = new String();
        test2 = "SA\n" +  //2
                "Territories:\n" + //11
                "Ontario\n" + //7
                "Owner: John 2 Doe\n" + //17
                "Armies: 0\n" + //9
                "Quebec\n" +    //6
                "Owner: Jane 2 Doe\n" + //17        78
                "Armies: 0"; //9
        assertEquals("", test1, continent.toString().substring(0,82));
        assertNotEquals("", test2, continent.toString().substring(0,82));
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
        terr1.setOwner(player);
        terr2.setOwner(p2);
        System.out.println(continent.getTerritoryList().toString());
        String test1 = new String();
        test1 = "[Ontario\n" +
                "Owner: John Doe\n" +
                "Armies: 0, Quebec\n" +
                "Owner: Jane Doe\n" +
                "Armies: 0, Alberta\n" +
                "Owner: Jane Doe\n" +
                "Armies: 0]";
        String test2 = new String();
        test2 = "[Ontario\n" +
                "Owner: John Doe\n" +
                "Armies: 0, Quebec\n" +
                "Owner: Jane Doe\n" +
                "Armies: 0, Alberta\n" +
                "Owner: Jane Doe\n" +
                "Armies: 0, Alberta\n" +
                "Owner: Jane Doe\n" +
                "Armies: 0]";


        Territory test = new Territory("Alberta", continent, poly2);
        test.setOwner(p2);
        assertEquals(test1, continent.getTerritoryList().toString());
        continent.addTerritory(test);
        assertEquals(test2, continent.getTerritoryList().toString());

    }
}
