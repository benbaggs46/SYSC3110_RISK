import java.util.ArrayList;
import java.util.List;

public class BoardController {
    private Board board;
    private List<Player> players;
    private int armiesToPlace;
    private Player currentPlayer;
    private TurnStage turnStage;

    public BoardController(Board board){
        this.board = board;
        players = new ArrayList<>();
    }

    public String processCommand(Command c){
        CommandWord word = c.getCommandWord();
        List<String> args = c.getArgs();
        try {
            switch (word) {
                case PLAY -> {
                    int numPlayers = Integer.parseInt(args.get(0));
                    board = BoardConstructor.createMapFromFile("DEFAULT_MAP.xml");

                    for(int i=0;i<numPlayers;i++){
                        players.add(new Player(Parser.getPrompt("Enter a name for player "+ (i+1))));
                    }

                    currentPlayer = players.get(0);
                    turnStage = TurnStage.PLACEMENT;

                    board.populateBoard(players, 30);
                    return "New board created with " + numPlayers + " players";
                }

                case PRINT -> {
                    return board.toString();
                }

                case QUIT -> {
                    System.out.println("Thank you for playing!");
                    System.exit(1);
                }

                case ATTACK -> {
                    if(turnStage != TurnStage.ATTACK) return "You cannot attack during the "+ turnStage +" phase of your turn";
                    Territory t1 = board.findTerritoryByName(args.get(0));
                    Territory t2 = board.findTerritoryByName(args.get(1));
                    int attackDice = Integer.parseInt(args.get(2));

                    if(t2.getOwner() != currentPlayer) return "You cannot attack from that territory. You do not control it";
                    if(t1.getOwner() == currentPlayer) return "You cannot attack your own territory";
                    if(attackDice <= 0) return "You must attack with a positive number of armies";
                    if(t2.getNumArmies() <= attackDice) return "There are not enough armies in " + t2.getName();

                    int defendDice = Parser.getIntPrompt("How many armies would "+t1.getOwner().getName()+" like to defend with?");
                    while(defendDice <= 0 || defendDice > t1.getNumArmies()){
                        String message = defendDice <= 0? "You must defend with a positive number of armies":"There are not enough armies in "+t1.getName();
                        defendDice = Parser.getIntPrompt(message);
                    }

                    board.attack(t2, t1, attackDice, defendDice);
                    return "Attacked " + t1.getName() +" from "+t2.getName()+ " with "+attackDice+" armies. " +defendDice+" armies defended";
                }

                case RETRACT -> {
                    if(turnStage != TurnStage.PLACEMENT) return "You cannot retract armies during the "+ turnStage +" phase of your turn";
                    Territory t = board.findTerritoryByName(args.get(0));
                    int armies = Integer.parseInt(args.get(1));

                    if(t.getOwner() != currentPlayer) return "You do not control that territory";
                    if(t.getTempArmies() < armies) return "You cannot retract that many armies";
                    if(armies <= 0) return "You cannot retract a negative number of armies";

                    t.addTempArmies(-armies);
                    armiesToPlace += armies;
                }

                case PLACE -> {
                    if(turnStage != TurnStage.PLACEMENT) return "You cannot place armies during the "+ turnStage +" phase of your turn";
                    Territory t = board.findTerritoryByName(args.get(0));
                    int armies = Integer.parseInt(args.get(1));

                    if(armiesToPlace <= 0) return "You have no more armies to place";
                    if(t.getOwner() != currentPlayer) return "You do not control that territory";
                    if(armiesToPlace < armies) return "You cannot place that many armies";
                    if(armies <= 0) return "You cannot place a negative number of armies";

                    t.addTempArmies(armies);
                    armiesToPlace -= armies;
                }

                case FORTIFY -> {
                        if(turnStage != TurnStage.FORTIFY) return "You cannot fortify during the "+ turnStage +" phase of your turn";
                        Territory t1 = board.findTerritoryByName(args.get(0));
                        Territory t2 = board.findTerritoryByName(args.get(1));
                        int armies = Integer.parseInt(args.get(2));

                        if (t1.getOwner() != currentPlayer || t2.getOwner() != currentPlayer)
                            return "You do not control both territories";
                        if (!board.areConnected(t1, t2)) return "Specified territories are not connected";
                        if (t2.getNumArmies() <= armies)
                            return "You cannot move that many armies from "+t2.getName();
                        board.moveArmies(t2, t1, armies);
                        return "Fortified " + armies + " armies from " + t2.getName() + " to " + t1.getName();
                }

                case PROCEED -> {
                    String string = "";
                    if(turnStage == TurnStage.PLACEMENT){
                        if(armiesToPlace > 0) return "You still have armies to place";
                        for(Territory t: board.getTerritoryList()){
                            t.confirmTempArmies();
                        }
                    }
                    else if(turnStage == TurnStage.FORTIFY){
                        goToNextTurn();
                        string = "It is now "+currentPlayer +"'s turn\n";
                    }
                    goToNextTurnStage();
                    string += "You are in the " + turnStage +" phase";
                    return string;
                }
            }
        }
        catch(Exception e){return "Exception thrown in BoardController.processCommand()";}
        return Parser.INVALID_COMMAND_MESSAGE;
    }

    private void goToNextTurn(){
        currentPlayer = players.get((players.indexOf(currentPlayer) + 1) % players.size());
    }

    private void goToNextTurnStage(){
        turnStage = TurnStage.values()[(turnStage.ordinal() + 1) % TurnStage.values().length];
    }
}
