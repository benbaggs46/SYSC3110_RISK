import java.util.List;

public class BoardController {
    private Board board;
    private List<Player> players;
    private int armiesToFortify;
    private Player currentPlayer;

    public BoardController(Board board){
        this.board = board;
        currentPlayer = null;
    }

    public String processCommand(Command c){
        CommandWord word = c.getCommandWord();
        List<String> args = c.getArgs();
        try {
            switch (word) {
                case PLAY -> {
                        int numPlayers = Integer.parseInt(args.get(0));
                        return "new game with " + numPlayers + " players";
                }

                case ATTACK -> {
                    Territory t1 = board.findTerritoryByName(args.get(0));
                    Territory t2 = board.findTerritoryByName(args.get(1));
                    int attackDice = Integer.parseInt(args.get(2));

                    if(t2.getOwner() != currentPlayer) return "You cannot attack from that territory. You do not control it";
                    if(t1.getOwner() == currentPlayer) return "You cannot attack your own territory";
                    if(attackDice <= 0) return "You must attack with a positive number of armies";
                    if(t2.getNumArmies() <= attackDice) return "There are not enough armies in " + t2;

                    int defendDice = Parser.getIntPrompt("How many armies would " + t1.getOwner() + " like to defend with?");
                    while(defendDice > t1.getNumArmies() || defendDice <= 0){
                        String message = defendDice <= 0? "You must defend with a positive number of armies": "There are not that many armies in " + t1;
                        defendDice = Parser.getIntPrompt(message);
                    }
                    board.attack(t2, t1, attackDice, defendDice);
                    return "Attacked " + t1 +" from "+t2+ " with "+attackDice+" armies. " +defendDice+" armies defended";
                }

                case FORTIFY -> {
                        Territory t1 = board.findTerritoryByName(args.get(0));
                        Territory t2 = board.findTerritoryByName(args.get(1));
                        int armies = Integer.parseInt(args.get(2));

                        if (t1.getOwner() != currentPlayer || t2.getOwner() != currentPlayer)
                            return "You do not control both territories";
                        if (!board.areConnected(t1, t2)) return "Specified territories are not connected";
                        if (t2.getNumArmies() <= armies)
                            return "You cannot move that many armies from the specified territory";
                        board.moveArmies(t2, t1, armies);
                        return "Fortified " + armies + " from " + t2 + " to " + t1;
                }
            }
        }
        catch(Exception e){return "Arguments are invalid";}
        return Parser.INVALID_COMMAND_MESSAGE;
    }
}
