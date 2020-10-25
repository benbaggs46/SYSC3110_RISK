import java.util.List;

public class BoardController {
    /**
     * The board that the controller belongs too
     */
    private Board board;

    /**
     * Constructor for the board controller
     * @param board the board that the constructor will belong too
     */
    public BoardController(Board board){
        this.board = board;
    }

    /**
     * Determines how many armies the players start with based of how many players there are
     * @param numPlayers the number of players playing
     * @return the number of armies each player will start with
     */
    public static int getNumArmiesEachForNumPlayers(int numPlayers){
        if(numPlayers == 2) return 50;
        else if(numPlayers == 3) return 35;
        else if(numPlayers == 4) return 30;
        else if(numPlayers == 5) return 25;
        else if(numPlayers == 6) return 20;
        else return -1;
    }

    /**
     * Determines if the command that is being passed in is completely valid and then if it is preforms that action
     * @param c the command to be executed
     */
    public void processCommand(Command c){
        CommandWord word = c.getCommandWord();
        List<String> args = c.getArgs();
        try {
            switch (word) {
                case PLAY -> {
                    int numPlayers = Integer.parseInt(args.get(0));
                    int numArmiesEach = BoardController.getNumArmiesEachForNumPlayers(numPlayers);
                    if(numArmiesEach < 0) {Parser.displayMessage("Number of players must be between 2 and 6"); return;}
                    board = BoardConstructor.createMapFromFile("DEFAULT_MAP.xml");

                    for(int i=0;i<numPlayers;i++){
                        board.addPlayer(new Player(Parser.getPrompt("Enter a name for player "+ (i+1))));
                    }

                    if(numPlayers > board.getTerritoryList().size()) {Parser.displayMessage("The selected map doesn't have enough territories for "+numPlayers+" players"); return;}
                    if(numPlayers * numArmiesEach < board.getTerritoryList().size()) {Parser.displayMessage("The selected map has too many territories"); return;}

                    board.populateBoard(numArmiesEach);

                    board.setCurrentPlayer(board.getPlayerList().get(0));
                    board.setTurnStage(TurnStage.PLACEMENT);

                    Parser.displayMessage("New board created with " + numPlayers + " players");
                }

                case PRINT -> {
                    if(board.isEmpty()) {Parser.displayMessage("You have to start a new game first!"); return;}
                    Parser.displayMessage( board.toString());
                }

                case QUIT -> {
                    Parser.displayMessage("Thank you for playing!");
                    System.exit(1);
                }

                case ATTACK -> {
                    if(board.isEmpty()) {Parser.displayMessage("You have to start a new game first!"); return;}
                    if(board.getTurnStage() != TurnStage.ATTACK) {Parser.displayMessage("You cannot attack during the "+ board.getTurnStage() +" phase of your turn"); return;}
                    Territory t1 = board.findTerritoryByName(args.get(0));
                    Territory t2 = board.findTerritoryByName(args.get(1));
                    int attackDice = Integer.parseInt(args.get(2));

                    if(!t1.getNeighbours().contains(t2)) {Parser.displayMessage("Those two territories don't border each other"); return;}
                    if(t2.getOwner() != board.getCurrentPlayer()) {Parser.displayMessage("You cannot attack from that territory. You do not control it"); return;}
                    if(t1.getOwner() == board.getCurrentPlayer()) {Parser.displayMessage("You cannot attack your own territory"); return;}
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
                    if(board.isEmpty()) {Parser.displayMessage("You have to start a new game first!"); return;}
                    if(board.getTurnStage() != TurnStage.PLACEMENT) {Parser.displayMessage("You cannot retract armies during the "+ board.getTurnStage() +" phase of your turn"); return;}
                    Territory t = board.findTerritoryByName(args.get(0));
                    int armies = Integer.parseInt(args.get(1));

                    if(t.getOwner() != board.getCurrentPlayer()) {Parser.displayMessage("You do not control that territory"); return;}
                    if(t.getTempArmies() < armies) {Parser.displayMessage("You cannot retract that many armies"); return;}
                    if(armies <= 0) {Parser.displayMessage("You cannot retract a negative number of armies"); return;}

                    t.addTempArmies(-armies);
                    board.addArmiesToPlace(armies);
                }

                case PLACE -> {
                    if(board.isEmpty()) {Parser.displayMessage("You have to start a new game first!"); return;}
                    if(board.getTurnStage() != TurnStage.PLACEMENT) {Parser.displayMessage("You cannot place armies during the "+ board.getTurnStage() +" phase of your turn"); return;}
                    Territory t = board.findTerritoryByName(args.get(0));
                    int armies = Integer.parseInt(args.get(1));

                    if(board.getArmiesToPlace() <= 0) {Parser.displayMessage("You have no more armies to place"); return;}
                    if(t.getOwner() != board.getCurrentPlayer()) {Parser.displayMessage("You do not control that territory"); return;}
                    if(board.getArmiesToPlace() < armies) {Parser.displayMessage("You cannot place that many armies"); return;}
                    if(armies <= 0) {Parser.displayMessage("You cannot place a negative number of armies"); return;}

                    t.addTempArmies(armies);
                    board.addArmiesToPlace(-armies);
                }

                case FORTIFY -> {
                    if(board.isEmpty()) {Parser.displayMessage("You have to start a new game first!"); return;}
                        if(board.getTurnStage() != TurnStage.FORTIFY) {Parser.displayMessage("You cannot fortify during the "+ board.getTurnStage() +" phase of your turn"); return;}
                        Territory t1 = board.findTerritoryByName(args.get(0));
                        Territory t2 = board.findTerritoryByName(args.get(1));
                        int armies = Integer.parseInt(args.get(2));

                        if (t1.getOwner() != board.getCurrentPlayer() || t2.getOwner() != board.getCurrentPlayer()) {Parser.displayMessage("You do not control both territories"); return;}
                        if (!board.areConnected(t1, t2)) {Parser.displayMessage("Specified territories are not connected"); return;}
                        if (t2.getNumArmies() <= armies) {Parser.displayMessage("You cannot move that many armies from "+t2.getName()); return;}

                        board.moveArmies(t2, t1, armies);
                        Parser.displayMessage("Fortified " + armies + " armies from " + t2.getName() + " to " + t1.getName());
                        board.goToNextTurn();
                        board.goToNextTurnStage();
                        Parser.displayMessage("It is now "+board.getCurrentPlayer().getName() +"'s turn\nYou are in the " + board.getTurnStage() +" phase");
                }

                case PROCEED -> {
                    if(board.isEmpty()) {Parser.displayMessage("You have to start a new game first!"); return;}
                    String string = "";
                    if(board.getTurnStage() == TurnStage.PLACEMENT){
                        if(board.getArmiesToPlace() > 0) {Parser.displayMessage("You still have armies to place"); return;}
                        for(Territory t: board.getTerritoryList()){
                            t.confirmTempArmies();
                        }
                    }
                    else if(board.getTurnStage() == TurnStage.FORTIFY){
                        board.goToNextTurn();
                        string = "It is now "+board.getCurrentPlayer().getName() +"'s turn\n";
                    }
                    board.goToNextTurnStage();
                    string += "You are in the " + board.getTurnStage() +" phase";
                    Parser.displayMessage(string);
                }

                case INFO -> {
                    if(board.isEmpty()) {Parser.displayMessage("You have to start a new game first!"); return;}
                    String name = args.get(0);
                    Continent continent = board.findContinentByName(name);
                    if(continent != null) {Parser.displayMessage(continent.toString()); return;}
                    Territory territory = board.findTerritoryByName(name);
                    if(territory != null) {Parser.displayMessage(territory.toString()); return;}
                    Player player = board.findPlayerByName(name);
                    if(player != null) {Parser.displayMessage(player.toString()); return;}

                    Parser.displayMessage("No objects with that name exist");
                }
            }
        }
        catch(Exception e){Parser.displayMessage("Command arguments were invalid");}
    }
}
