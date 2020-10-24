import java.util.List;

public class Game {
    private List<Player> players;
    private Player currentPlayer;
    private Board board;

    public static void main(String[] args) {

        Parser.begin(new BoardController(new Board()));

    }
}
