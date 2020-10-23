import java.util.ArrayList;
import java.util.List;

public class Game {
    private List<Player> players;
    private Player currentPlayer;
    private Board board;

    public Game() {
        players = new ArrayList<>();
    }

    public void addPlayer(Player player){
        players.add(player);
    }

    public void removePlayer(Player player){
        players.remove(player);
    }

    public void play(){
        while(true){
            for(Player i: players){
                new Turn(i);
            }
        }
    }

    public static void main(String args[]){

        Game game = new Game();
        game.addPlayer(new Player());
        game.addPlayer(new Player());
        game.play();
    }
}