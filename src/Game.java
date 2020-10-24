import java.util.List;

public class Game {
    private List<Player> players;
    private Player currentPlayer;
    private Board board;

    public static void main(String[] args) {
        BoardConstructor boardConstructor = new BoardConstructor();

        Board board = boardConstructor.createMapFromFile("DEFAULT_MAP.xml");

        System.out.println(board);
    }
}
