import java.util.List;
import java.util.Scanner;

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

    public void isGameOver () {
        Territory t;
        if (players.size() == 1) {
            if (players.get(0).getNumTerritories() == 42){
                endGame();
            }
        }
    }

    public void endGame(){
        System.out.println("Congratulations!! " + players.get(0).getName() + " has come out on top!");
        System.out.println("Would you like to start another match or quit?");
        Parser p = new Parser();
        p.begin();
    }

}

