import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.Assert.*;

public class BoardTest {
    Board board;
    Continent c;
    Territory t;
    Player p, p2;
    List l;

    @Before
    public void SetUp(){
        board = new Board();
        c =  new Continent("Continent 1", 5, Color.BLACK);
        t =  new Territory("territory 1",c, new Polygon());
        p = new Player("p1", Color.BLUE);
        p2 = new Player("p2", Color.CYAN);
        t.setOwner(p);
        l = new ArrayList<>();
    }

    @Test
    public void TestgetSelectedTerritories() {
        board.addContinent(c);
        assertEquals(new ArrayList<>(), board.getSelectedTerritories());
        board.toggleTerritorySelection(t);
        l.add(t);
        assertEquals(l , board.getSelectedTerritories());
        board.toggleTerritorySelection(t);
        assertEquals(new ArrayList<>(), board.getSelectedTerritories());
    }

    @Test
    public void TestgetTurnStage() {
        assertEquals(null, board.getTurnStage());
        board.setTurnStage(TurnStage.PLACEMENT);
        assertEquals(TurnStage.PLACEMENT, board.getTurnStage());
        board.incrementTurnStage();
        assertEquals(TurnStage.ATTACK, board.getTurnStage());
    }

    @Test
    public void TestgetArmiesToPlace() {
        assertEquals(0, board.getArmiesToPlace());
        board.addArmiesToPlace(10);
        assertEquals(10, board.getArmiesToPlace());
        board.clearBoard();
        assertEquals(0, board.getArmiesToPlace());
    }

    @Test
    public void TestgetCurrentPlayer() {
        assertEquals(null, board.getCurrentPlayer());
        board.setCurrentPlayer(p);
        assertEquals(p, board.getCurrentPlayer());
        board.clearBoard();
        assertEquals(null, board.getCurrentPlayer());
    }

    @Test
    public void TestgetArmyBonusForPlayer() {
        board.addContinent(c);
        assertEquals(3, board.getArmyBonusForPlayer(p2));
        t.setOwner(p2);
        assertEquals(5, board.getArmyBonusForPlayer(p2));
    }

    @Test
    public void findTerritoryByName() {
        assertEquals(null, board.findTerritoryByName("territory 1"));
        board.addContinent(c);
        assertEquals(t, board.findTerritoryByName("territory 1"));
    }

    @Test
    public void findContinentByName() {
        assertEquals(null, board.findContinentByName("Continent 1"));
        board.addContinent(c);
        assertEquals(c, board.findContinentByName("Continent 1"));
    }

    @Test
    public void findPlayerByName() {
        assertEquals(null, board.findPlayerByName("p1"));
        board.addPlayer(p);
        assertEquals(p, board.findPlayerByName("p1"));
    }

    @Test
    public void getTerritoryList() {
        assertEquals(new ArrayList<>(), board.getTerritoryList());
        board.addContinent(c);
        l.add(t);
        assertEquals(l, board.getTerritoryList());
    }

    @Test
    public void getPlayerList() {
        assertEquals(new ArrayList<>(), board.getPlayerList());
        board.addPlayer(p);
        l.add(p);
        assertEquals(l, board.getPlayerList());
    }

    @Test
    public void areConnected() {
        Territory t2 = new Territory("territory 2", c, new Polygon());
        t2.setOwner(p);
        assertFalse(board.areConnected(t, t2));
        t2.addNeighbour(t);
        t.addNeighbour(t2);
        assertTrue(board.areConnected(t, t2));
    }
}