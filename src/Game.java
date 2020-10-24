import java.util.ArrayList;
import java.util.List;

public class Game {
    private List<Player> players;
    private Player currentPlayer;
    private Board board;

    public static void main(String[] args) {
        BoardConstructor boardConstructor = new BoardConstructor();
        Board board = boardConstructor.createMapFromFile("DEFAULT_MAP.xml");

        List<Player> playerList = new ArrayList<>();
        playerList.add(new Player("p1"));
        playerList.add(new Player("p2"));
        playerList.add(new Player("p3"));
        playerList.add(new Player("p4"));

        board.populateBoard(playerList, 30);

        System.out.println(board);

    }
}
