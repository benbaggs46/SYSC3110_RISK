import java.util.List;

public class Game {
    private List<Player> players;
    private Player currentPlayer;
    private Board board;



    public static void main(String[] args) {
        BoardConstructor boardConstructor = new BoardConstructor();

        Board board = boardConstructor.createMapFromFile("DEFAULT_MAP.xml");

        System.out.println(board);

        Parser.begin();
    }

    public void eliminatePlayer( String name){
        Player p = null;
        for(int i = 0; i<players.size(); i++){
            if (players.get(i).getName() == name){
                p = players.get(i);
                break;
            }
        }
        if (p != null) {
            players.remove(p);
        }
    }


}
