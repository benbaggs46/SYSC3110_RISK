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

    public void processCommand(Command c){
        CommandWord word = c.getCommandWord();
        List<String> args = c.getArgs();
        try {
            switch (word) {
                case PLAY -> {
                    int numPlayers = Integer.parseInt(args.get(0));
                    if(numPlayers <= 0 || numPlayers > 6) {Parser.displayMessage("Invalid number of players"); return;}
                    board = BoardConstructor.createMapFromFile("DEFAULT_MAP.xml");

                    for(int i=0;i<numPlayers;i++){
                        players.add(new Player(Parser.getPrompt("Enter a name for player "+ (i+1))));
                    }

                    currentPlayer = players.get(0);
                    turnStage = TurnStage.PLACEMENT;

                    board.populateBoard(players, 30);
                    Parser.displayMessage("New board created with " + numPlayers + " players");
                }

                case PRINT -> {
                    Parser.displayMessage( board.toString());
                }

                case QUIT -> {
                    Parser.displayMessage("Thank you for playing!");
                    System.exit(1);
                }

                case ATTACK -> {
                    if(turnStage != TurnStage.ATTACK) {Parser.displayMessage("You cannot attack during the "+ turnStage +" phase of your turn"); return;}
                    Territory t1 = board.findTerritoryByName(args.get(0));
                    Territory t2 = board.findTerritoryByName(args.get(1));
                    int attackDice = Integer.parseInt(args.get(2));

                    if(t2.getOwner() != currentPlayer) {Parser.displayMessage("You cannot attack from that territory. You do not control it"); return;}
                    if(t1.getOwner() == currentPlayer) {Parser.displayMessage("You cannot attack your own territory"); return;}
                    if(attackDice <= 0) {Parser.displayMessage("You must attack with a positive number of armies"); return;}
                    if(t2.getNumArmies() <= attackDice) {Parser.displayMessage("There are not enough armies in " + t2.getName()); return;}

                    int defendDice = Parser.getIntPrompt("How many armies would "+t1.getOwner().getName()+" like to defend with?");
                    while(defendDice <= 0 || defendDice > t1.getNumArmies()){
                        String message = defendDice <= 0? "You must defend with a positive number of armies":"There are not enough armies in "+t1.getName();
                        defendDice = Parser.getIntPrompt(message);
                    }

                    Parser.displayMessage("Attacked " + t1.getName() +" from "+t2.getName()+ " with "+attackDice+" armies. " +defendDice+" armies defended");
                    board.attack(t2, t1, attackDice, defendDice);
                }

                case RETRACT -> {
                    if(turnStage != TurnStage.PLACEMENT) {Parser.displayMessage("You cannot retract armies during the "+ turnStage +" phase of your turn"); return;}
                    Territory t = board.findTerritoryByName(args.get(0));
                    int armies = Integer.parseInt(args.get(1));

                    if(t.getOwner() != currentPlayer) {Parser.displayMessage("You do not control that territory"); return;}
                    if(t.getTempArmies() < armies) {Parser.displayMessage("You cannot retract that many armies"); return;}
                    if(armies <= 0) {Parser.displayMessage("You cannot retract a negative number of armies"); return;}

                    t.addTempArmies(-armies);
                    armiesToPlace += armies;
                }

                case PLACE -> {
                    if(turnStage != TurnStage.PLACEMENT) {Parser.displayMessage("You cannot place armies during the "+ turnStage +" phase of your turn"); return;}
                    Territory t = board.findTerritoryByName(args.get(0));
                    int armies = Integer.parseInt(args.get(1));

                    if(armiesToPlace <= 0) {Parser.displayMessage("You have no more armies to place"); return;}
                    if(t.getOwner() != currentPlayer) {Parser.displayMessage("You do not control that territory"); return;}
                    if(armiesToPlace < armies) {Parser.displayMessage("You cannot place that many armies"); return;}
                    if(armies <= 0) {Parser.displayMessage("You cannot place a negative number of armies"); return;}

                    t.addTempArmies(armies);
                    armiesToPlace -= armies;
                }

                case FORTIFY -> {
                        if(turnStage != TurnStage.FORTIFY) {Parser.displayMessage("You cannot fortify during the "+ turnStage +" phase of your turn"); return;}
                        Territory t1 = board.findTerritoryByName(args.get(0));
                        Territory t2 = board.findTerritoryByName(args.get(1));
                        int armies = Integer.parseInt(args.get(2));

                        if (t1.getOwner() != currentPlayer || t2.getOwner() != currentPlayer) {Parser.displayMessage("You do not control both territories"); return;}
                        if (!board.areConnected(t1, t2)) {Parser.displayMessage("Specified territories are not connected"); return;}
                        if (t2.getNumArmies() <= armies) {Parser.displayMessage("You cannot move that many armies from "+t2.getName()); return;}

                        board.moveArmies(t2, t1, armies);
                        Parser.displayMessage("Fortified " + armies + " armies from " + t2.getName() + " to " + t1.getName());
                }

                case PROCEED -> {
                    String string = "";
                    if(turnStage == TurnStage.PLACEMENT){
                        if(armiesToPlace > 0) {Parser.displayMessage("You still have armies to place"); return;}
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
                    Parser.displayMessage(string);
                }
            }
        }
        catch(Exception e){Parser.displayMessage("Exception thrown in BoardController.processCommand()");}
    }

    private void goToNextTurn(){
        currentPlayer = players.get((players.indexOf(currentPlayer) + 1) % players.size());
    }

    private void goToNextTurnStage(){
        turnStage = TurnStage.values()[(turnStage.ordinal() + 1) % TurnStage.values().length];
    }
}
