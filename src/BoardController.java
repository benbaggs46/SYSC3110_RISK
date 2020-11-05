/**
 * Receives user commands from the Parser class and modifies the game board.
 * The class will determine if commands make sense given the current game state, and if so, make changes to the board model.
 */

import java.awt.*;
import java.util.*;
import java.util.List;

public class BoardController {

    /**
     * The max number of dice rolls
     */
    public static final int MAX_DICE_ROLL = 6;

    /**
     * The max number of dice that the attacker can roll at once
     */
    public static final int MAX_ATTACK_DICE = 3;

    /**
     * The max number of dice that the defender can roll at once
     */
    public static final int MAX_DEFEND_DICE = 2;

    /**
     * The maximum number of players
     */
    public static final int MAX_PLAYERS = 6;

    /**
     * The minimum number of players
     */
    public static final int MIN_PLAYERS = 2;

    /**
     * A map between any valid number of players and how many armies each player should start the game with
     */
    public static final Map<Integer, Integer> STARTING_ARMIES_FOR_NUM_PLAYERS = Map.of(
            2, 50,
            3, 35,
            4, 30,
            5, 25,
            6, 20
    );

    /**
     * A map between each player number and the corresponding colors
     */
    public static final Map<Integer, Color> PLAYER_COLOR_FOR_PLAYER_NUM = Map.of(
            0,Color.RED,
            1,Color.BLUE,
            2, Color.YELLOW,
            3, Color.GREEN,
            4, Color.MAGENTA,
            5, Color.GRAY
    );

    /**
     * The Board being controlled by the BoardController
     */
    private Board board;

    private BoardView boardView;

    public BoardController(BoardView boardView){
        this.boardView = boardView;
    }

    public Board getBoard(){
        return board;
    }

    /**
     * Conducts a dice battle with the specified number of dice. Positive return values indicate that the attacker
     * won the battle. Negative values indicate the defender has won.
     * @param attackerDiceNum The number of dice the attacker will use
     * @param defenderDiceNum The number of dice the defender will use
     * @return the result of the dice battle (positive indicates attacker won, negative indicates defender won, 0 indicates both players lose 1 army)
     */
    public static int attackResult(int attackerDiceNum, int defenderDiceNum){

        int result = 0;

        //roll dice and collect the results into lists
        Random r = new Random();
        List<Integer> attackDice = new ArrayList<>();
        List<Integer> defendDice = new ArrayList<>();
        for(int i=0; i<attackerDiceNum; i++ ){ attackDice.add(r.nextInt(MAX_DICE_ROLL));}
        for(int i=0; i<defenderDiceNum; i++ ){ defendDice.add(r.nextInt(MAX_DICE_ROLL));}

        //matches up the highest rolls from attacker and defender, modifies the result accordingly, and repeats
        for(int i = Math.min(attackerDiceNum, defenderDiceNum); i > 0; i--){
            int topAttackDie = Collections.max(attackDice);
            int topDefendDie = Collections.max(defendDice);
            attackDice.remove((Integer) topAttackDie);
            defendDice.remove((Integer) topDefendDie);
            //if the attack die is larger than the defend die then add 1 else subtract 1 from result
            result += topAttackDie > topDefendDie? 1: -1;
        }

        return result;
    }

    /**
     * Performs an attack between the specified territories with the specified number of armies
     * @param defendingTerritoryName The name of the territory being attacked
     * @param attackingTerritoryName The name of the territory being attacked from
     * @param attackDiceString The number of armies the attacker wishes to attack with as a string
     */
    private void attack(String defendingTerritoryName, String attackingTerritoryName, String attackDiceString){
        TurnStage currentTurnStage = board.getTurnStage();
        Player currentPlayer = board.getCurrentPlayer();
        if(currentTurnStage != TurnStage.ATTACK) {Parser.displayMessage("You cannot attack during the "+ currentTurnStage +" phase of your turn"); return;}
        Territory t1 = board.findTerritoryByName(defendingTerritoryName);
        Territory t2 = board.findTerritoryByName(attackingTerritoryName);
        int attackDice = Integer.parseInt(attackDiceString);

        if(!t1.getNeighbours().contains(t2)) {Parser.displayMessage("Those two territories don't border each other"); return;}
        if(t2.getOwner() != currentPlayer) {Parser.displayMessage("You cannot attack from that territory. You do not control it"); return;}
        if(t1.getOwner() == currentPlayer) {Parser.displayMessage("You cannot attack your own territory"); return;}
        if(attackDice <= 0) {Parser.displayMessage("You must attack with a positive number of armies"); return;}
        if(attackDice > MAX_ATTACK_DICE) {Parser.displayMessage("You can't attack with more than "+MAX_ATTACK_DICE+" armies"); return;}
        if(t2.getNumArmies() <= attackDice) {Parser.displayMessage("There are not enough armies in " + t2.getName()); return;}

        int defendDice = Parser.getIntPrompt("How many armies would "+t1.getOwner().getName()+" like to defend with?");
        while(defendDice <= 0 || defendDice > Math.min(t1.getNumArmies(), MAX_DEFEND_DICE)){
            String message = defendDice <= 0? "You must defend with a positive number of armies":(defendDice > MAX_DEFEND_DICE? "You can't defend with more than "+MAX_DEFEND_DICE+" armies":"There are not enough armies in "+t1.getName());
            defendDice = Parser.getIntPrompt(message);
        }

        Parser.displayMessage("Attacked " + t1.getName() +" from "+t2.getName()+ " with "+attackDice+" armies. " +defendDice+" armies defended");

        int result = attackResult(attackDice, defendDice);

        Parser.displayMessage(result == 0? "Both players lost an army": (result > 0)? t1.getOwner().getName()+" lost "+result +" armies": t2.getOwner().getName()+" lost "+ (-result) +" armies");

        if(result == 0){ //both players lose one army
            t2.addArmies(-1);
            t1.addArmies(-1);
        }
        else if(result > 0) { //defender loses armies
            t1.addArmies(-result);
        }
        else { //attacker loses armies
            t2.addArmies(result);
        }
        if(t1.getNumArmies() <= 0 ) { //defending territory has no armies left
            Parser.displayMessage(t1.getName()+" was conquered!");

            Player prevOwner = t1.getOwner();
            prevOwner.loseTerritory(t1);
            t2.getOwner().gainTerritory(t1);
            t1.setOwner(t2.getOwner());

            Continent continent = t1.getContinent();
            if(continent.isConquered()) Parser.displayMessage(continent.getName()+" was conquered!");

            if(prevOwner.getNumTerritories() == 0) {

                //prevOwner is eliminated
                board.removePlayer(prevOwner);
                Parser.displayMessage(prevOwner.getName() + " was eliminated!");

                if(board.getPlayerList().size() > 1){
                    //game is over
                    Parser.displayMessage(t2.getOwner().getName() + " has won!");
                    board.clearBoard();
                    Parser.displayMessage("Enter PLAY,<number_of_players> to start a new game");
                }
            }

            //ask owner how many armies they want to move
            int armiesToMove = Parser.getIntPrompt("How many armies would "+t2.getOwner().getName()+" like to move?");
            while(armiesToMove < attackDice || armiesToMove > t2.getNumArmies() - 1){
                String message = armiesToMove < attackDice? "You must move at least "+attackDice+" armies": "There are not enough armies in "+ t2.getName();
                armiesToMove = Parser.getIntPrompt(message);
            }

            board.moveArmies(t2, t1, armiesToMove);
            Parser.displayMessage("Moved " + armiesToMove + " armies into " + t1.getName());
        }
    }

    /**
     * Moves the board to the next player's turn
     */
    public void nextTurn(){
        board.incrementTurn();
        Parser.displayMessage("It is now "+board.getCurrentPlayer().getName() +"'s turn");
    }

    /**
     * Moves the board to the next turn phase
     */
    public void nextTurnStage(){
        board.incrementTurnStage();
        Parser.displayMessage("You are in the " + board.getTurnStage() +" phase");
        if(board.getTurnStage() == TurnStage.PLACEMENT) Parser.displayMessage("You have "+ board.getArmiesToPlace() +" new armies to place");
    }

    /**
     * Starts a new game of RISK with the specified number of players
     * @param numPlayers The desired number of players
     */
    private void startNewGame(int numPlayers){
        if(numPlayers > MAX_PLAYERS || numPlayers < MIN_PLAYERS) {Parser.displayMessage("Number of players must be between 2 and 6"); return;}
        int numArmiesEach = STARTING_ARMIES_FOR_NUM_PLAYERS.get(numPlayers);
        BoardConstructor bc = new BoardConstructor();
        board = bc.createMapFromFile("DEFAULT_MAP.xml");

        //MAYBE REMOVE THIS
        boardView.getMapPanel().setBoard(board);

        if(board == null) {Parser.displayMessage("Error encountered constructing board, please try again"); return;}

        for(int i=0;i<numPlayers;i++){
            board.addPlayer(new Player(Parser.getPrompt("Enter a name for player "+ (i+1)), PLAYER_COLOR_FOR_PLAYER_NUM.get(i)));
        }

        int boardSize = board.getTerritoryList().size();
        if(numPlayers > boardSize) {Parser.displayMessage("The selected map doesn't have enough territories for "+numPlayers+" players"); return;}
        if(numPlayers * numArmiesEach < boardSize) {Parser.displayMessage("The selected map has too many territories"); return;}

        board.populateBoard(numArmiesEach);

        board.setCurrentPlayer(board.getPlayerList().get(numPlayers - 1));
        board.setTurnStage(TurnStage.FORTIFY);

        Parser.displayMessage("New board created with " + numPlayers + " players");
        nextTurn();
        nextTurnStage();
    }

    /**
     * Displays a help message to the user
     */
    private void displayHelpMessage(){
        Parser.displayMessage("- Separate all command words and arguments with commas only (',')\n" +
                "- All names are case insensitive\n" +
                "- Do not use commas in Continent, Territory, or Player names\n" +
                "- Extra arguments after commands will be ignored\n" +
                "- <argument:int> signifies an integer argument\n" +
                "- <argument:String> signifies a name as a string argument\n");

        for(CommandWord commandWord: CommandWord.values()) {
            Parser.displayMessage(commandWord.getSignature());
            Parser.displayMessage(commandWord.getDescription());
        }
    }

    /**
     * Quits the application
     */
    private void quit(){
        Parser.displayMessage("Thank you for playing!");
        System.exit(1);
    }

    /**
     * Prints the complete state of the game board
     */
    private void printBoard(){
        Parser.displayMessage( board.toString());
    }

    /**
     * Displays information about the object with the specified name
     * @param name The name of the Continent, Territory, or Player
     */
    private void displayInfo(String name){
        Continent continent = board.findContinentByName(name);
        if(continent != null) {
            Parser.displayMessage(continent.toString());
            return;
        }
        Territory territory = board.findTerritoryByName(name);
        if(territory != null) {
            Parser.displayMessage(territory.toString());
            Parser.displayMessage("Neighbours:");
            for(Territory neighbour: territory.getNeighbours()){
                Parser.displayMessage(neighbour.toString());
            }
            return;
        }
        Player player = board.findPlayerByName(name);
        if(player != null) {
            Parser.displayMessage(player.toString());
            return;
        }
        Parser.displayMessage("No objects with that name exist");
    }

    /**
     * Ends the current phase of the current turn
     */
    private void proceed(){
        TurnStage currentTurnStage = board.getTurnStage();
        if(currentTurnStage == TurnStage.PLACEMENT){
            if(board.getArmiesToPlace() > 0) {Parser.displayMessage("You still have armies to place"); return;}
            for(Territory t: board.getTerritoryList()){
                t.confirmTempArmies();
            }
        }
        else if(currentTurnStage == TurnStage.FORTIFY){
            nextTurn();
        }
        nextTurnStage();
    }

    /**
     * Places temporary armies in a territory
     * @param territoryName The name of the territory in which armies are to be placed
     * @param numArmiesString The number of armies as a string
     */
    private void place(String territoryName, String numArmiesString){
        TurnStage currentTurnStage = board.getTurnStage();
        int armiesToPlace = board.getArmiesToPlace();
        if(currentTurnStage != TurnStage.PLACEMENT) {Parser.displayMessage("You cannot place armies during the "+ currentTurnStage +" phase of your turn"); return;}
        Territory t = board.findTerritoryByName(territoryName);
        int armies = Integer.parseInt(numArmiesString);

        if(armiesToPlace <= 0) {Parser.displayMessage("You have no more armies to place"); return;}
        if(t.getOwner() != board.getCurrentPlayer()) {Parser.displayMessage("You do not control that territory"); return;}
        if(armiesToPlace < armies) {Parser.displayMessage("You cannot place that many armies"); return;}
        if(armies <= 0) {Parser.displayMessage("You cannot place a negative number of armies"); return;}

        t.addTempArmies(armies);
        board.addArmiesToPlace(-armies);

        Parser.displayMessage("Placed " + armies + " armies in " + t.getName());
    }

    /**
     * Retracts a specified number of temporarily placed armies from a territory
     * @param territoryName The name of the territory armies are to be retracted from
     * @param numArmiesString the number of armies as a string
     */
    private void retract(String territoryName, String numArmiesString){
        TurnStage currentTurnStage = board.getTurnStage();
        if(currentTurnStage != TurnStage.PLACEMENT) {Parser.displayMessage("You cannot retract armies during the "+ currentTurnStage +" phase of your turn"); return;}
        Territory t = board.findTerritoryByName(territoryName);
        int armies = Integer.parseInt(numArmiesString);

        if(t.getOwner() != board.getCurrentPlayer()) {Parser.displayMessage("You do not control that territory"); return;}
        if(t.getTempArmies() < armies) {Parser.displayMessage("You cannot retract that many armies"); return;}
        if(armies <= 0) {Parser.displayMessage("You cannot retract a negative number of armies"); return;}

        t.addTempArmies(-armies);
        board.addArmiesToPlace(armies);

        Parser.displayMessage("Retracted " + armies + " armies from " + t.getName());
    }

    /**
     * Fortifies armies according to the arguments given by the user
     * @param destTerritoryName The name of the territories armies are to be fortified to
     * @param sourceTerritoryName The name of the territories armies are to be fortified from
     * @param numArmiesString The number of armies to fortify as a string
     */
    private void fortify(String destTerritoryName, String sourceTerritoryName, String numArmiesString){
        TurnStage currentTurnStage = board.getTurnStage();
        Player currentPlayer = board.getCurrentPlayer();
        if(currentTurnStage != TurnStage.FORTIFY) {Parser.displayMessage("You cannot fortify during the "+ currentTurnStage +" phase of your turn"); return;}
        Territory t1 = board.findTerritoryByName(destTerritoryName);
        Territory t2 = board.findTerritoryByName(sourceTerritoryName);
        int armies = Integer.parseInt(numArmiesString);

        if (t1.getOwner() != currentPlayer || t2.getOwner() != currentPlayer) {Parser.displayMessage("You do not control both territories"); return;}
        if (!board.areConnected(t1, t2)) {Parser.displayMessage("Specified territories are not connected"); return;}
        if (t2.getNumArmies() <= armies) {Parser.displayMessage("You cannot move that many armies from "+t2.getName()); return;}

        board.moveArmies(t2, t1, armies);
        Parser.displayMessage("Fortified " + armies + " armies from " + t2.getName() + " to " + t1.getName());
        nextTurn();
        nextTurnStage();
    }

    /**
     * Processes a command given by the Parser, tests if it makes sense given the current game state
     * If the command is appropriate, modifies the Board accordingly
     * If the command is inappropriate, displays a message telling the user what the problem is
     * @param c The Command given by the Parser
     */
    public void processCommand(Command c){
        CommandWord word = c.getCommandWord();
        List<String> args = c.getArgs();

        if(word == CommandWord.PLAY){
           startNewGame(Integer.parseInt(args.get(0)));
        }
        else if(word == CommandWord.HELP){
            displayHelpMessage();
        }
        else if(word == CommandWord.QUIT){
            quit();
        }
        else{
            if(board == null) {Parser.displayMessage("You have to start a new game first!");}

            else if(word == CommandWord.PRINT){
                printBoard();
            }
            else if(word == CommandWord.INFO){
                displayInfo(args.get(0));
            }
            else {
                if(word == CommandWord.PROCEED){
                    proceed();
                }
                else if(word == CommandWord.PLACE){
                    place(args.get(0), args.get(1));
                }
                else if(word == CommandWord.RETRACT){
                    retract(args.get(0), args.get(1));
                }
                else if(word == CommandWord.ATTACK){
                   attack(args.get(0), args.get(1), args.get(2));
                }
                else if(word == CommandWord.FORTIFY){
                    fortify(args.get(0), args.get(1), args.get(2));
                }
            }
        }
    }
}
