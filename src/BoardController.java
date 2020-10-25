/**
 * Receives user commands from the Parser class and modifies the game board.
 * The class will determine if commands make sense given the current game state, and if so, make changes to the board model.
 */

import java.util.*;

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
     * The Board being controlled by the BoardController
     */
    private Board board;

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
     * Performs an attack between two territories
     * @param attackingTerritory The territory being attacked from
     * @param defendingTerritory The territory being attacked
     * @param attackerDiceNum The number of dice the attacker is rolling
     * @param defenderDiceNum The number of dice the defender is rolling
     */
    private void attack(Territory attackingTerritory, Territory defendingTerritory, int attackerDiceNum, int defenderDiceNum){
        int result = attackResult(attackerDiceNum, defenderDiceNum);

        Parser.displayMessage(result == 0? "Both players lost an army": (result > 0)? defendingTerritory.getOwner().getName()+" lost "+result +" armies": attackingTerritory.getOwner().getName()+" lost "+ (-result) +" armies");

        if(result == 0){ //both players lose one army
            attackingTerritory.addArmies(-1);
            defendingTerritory.addArmies(-1);
        }
        else if(result > 0) { //defender loses armies
            defendingTerritory.addArmies(-result);
        }
        else { //attacker loses armies
            attackingTerritory.addArmies(result);
        }
        if(defendingTerritory.getNumArmies() <= 0 ) { //defending territory has no armies left
            Parser.displayMessage(defendingTerritory.getName()+" was conquered!");

            Player prevOwner = defendingTerritory.getOwner();
            prevOwner.loseTerritory(defendingTerritory);
            attackingTerritory.getOwner().gainTerritory(defendingTerritory);
            defendingTerritory.setOwner(attackingTerritory.getOwner());

            Continent continent = defendingTerritory.getContinent();
            if(continent.isConquered()) Parser.displayMessage(continent.getName()+" was conquered!");

            if(prevOwner.getNumTerritories() == 0) {

                //prevOwner is eliminated
                board.removePlayer(prevOwner);
                Parser.displayMessage(prevOwner.getName() + " was eliminated!");

                if(board.getPlayerList().size() > 1){
                    //game is over
                    Parser.displayMessage(attackingTerritory.getOwner().getName() + " has won!");
                    board.clearBoard();
                    Parser.displayMessage("Enter PLAY,<number_of_players> to start a new game");
                }
            }

            //ask owner how many armies they want to move
            int armiesToMove = Parser.getIntPrompt("How many armies would "+attackingTerritory.getOwner().getName()+" like to move?");
            while(armiesToMove < attackerDiceNum || armiesToMove > attackingTerritory.getNumArmies() - 1){
                String message = armiesToMove < attackerDiceNum? "You must move at least "+attackerDiceNum+" armies": "There are not enough armies in "+ attackingTerritory.getName();
                armiesToMove = Parser.getIntPrompt(message);
            }

            board.moveArmies(attackingTerritory, defendingTerritory, armiesToMove);
            Parser.displayMessage("Moved " + armiesToMove + " armies into " + defendingTerritory.getName());
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
     * Processes a command given by the Parser, tests if it makes sense given the current game state
     * If the command is appropriate, modifies the Board accordingly
     * If the command is inappropriate, displays a message telling the user what the problem is
     * @param c The Command given by the Parser
     */
    public void processCommand(Command c){
        CommandWord word = c.getCommandWord();
        List<String> args = c.getArgs();

        if(word == CommandWord.PLAY){
            int numPlayers = Integer.parseInt(args.get(0));
            if(numPlayers > MAX_PLAYERS || numPlayers < MIN_PLAYERS) {Parser.displayMessage("Number of players must be between 2 and 6"); return;}
            int numArmiesEach = STARTING_ARMIES_FOR_NUM_PLAYERS.get(numPlayers);
            BoardConstructor bc = new BoardConstructor();
            board = bc.createMapFromFile("DEFAULT_MAP.xml");

            if(board == null) {Parser.displayMessage("Error encountered constructing board, please try again"); return;}

            for(int i=0;i<numPlayers;i++){
                board.addPlayer(new Player(Parser.getPrompt("Enter a name for player "+ (i+1))));
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
        else if(word == CommandWord.HELP){
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
        else if(word == CommandWord.QUIT){
            Parser.displayMessage("Thank you for playing!");
            System.exit(1);
        }
        else{
            if(board == null) {Parser.displayMessage("You have to start a new game first!");}

            else if(word == CommandWord.PRINT){
                Parser.displayMessage( board.toString());
            }
            else if(word == CommandWord.INFO){
                String name = args.get(0);
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
            else {

                TurnStage currentTurnStage = board.getTurnStage();
                Player currentPlayer = board.getCurrentPlayer();

                if(word == CommandWord.PROCEED){

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
                else if(word == CommandWord.PLACE){

                    int armiesToPlace = board.getArmiesToPlace();
                    if(currentTurnStage != TurnStage.PLACEMENT) {Parser.displayMessage("You cannot place armies during the "+ currentTurnStage +" phase of your turn"); return;}
                    Territory t = board.findTerritoryByName(args.get(0));
                    int armies = Integer.parseInt(args.get(1));

                    if(armiesToPlace <= 0) {Parser.displayMessage("You have no more armies to place"); return;}
                    if(t.getOwner() != board.getCurrentPlayer()) {Parser.displayMessage("You do not control that territory"); return;}
                    if(armiesToPlace < armies) {Parser.displayMessage("You cannot place that many armies"); return;}
                    if(armies <= 0) {Parser.displayMessage("You cannot place a negative number of armies"); return;}

                    t.addTempArmies(armies);
                    board.addArmiesToPlace(-armies);

                    Parser.displayMessage("Placed " + armies + " armies in " + t.getName());
                }
                else if(word == CommandWord.RETRACT){

                    if(currentTurnStage != TurnStage.PLACEMENT) {Parser.displayMessage("You cannot retract armies during the "+ currentTurnStage +" phase of your turn"); return;}
                    Territory t = board.findTerritoryByName(args.get(0));
                    int armies = Integer.parseInt(args.get(1));

                    if(t.getOwner() != board.getCurrentPlayer()) {Parser.displayMessage("You do not control that territory"); return;}
                    if(t.getTempArmies() < armies) {Parser.displayMessage("You cannot retract that many armies"); return;}
                    if(armies <= 0) {Parser.displayMessage("You cannot retract a negative number of armies"); return;}

                    t.addTempArmies(-armies);
                    board.addArmiesToPlace(armies);

                    Parser.displayMessage("Retracted " + armies + " armies from " + t.getName());
                }
                else if(word == CommandWord.ATTACK){

                    if(currentTurnStage != TurnStage.ATTACK) {Parser.displayMessage("You cannot attack during the "+ currentTurnStage +" phase of your turn"); return;}
                    Territory t1 = board.findTerritoryByName(args.get(0));
                    Territory t2 = board.findTerritoryByName(args.get(1));
                    int attackDice = Integer.parseInt(args.get(2));

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
                    attack(t2, t1, attackDice, defendDice);
                }
                else if(word == CommandWord.FORTIFY){

                    if(currentTurnStage != TurnStage.FORTIFY) {Parser.displayMessage("You cannot fortify during the "+ currentTurnStage +" phase of your turn"); return;}
                    Territory t1 = board.findTerritoryByName(args.get(0));
                    Territory t2 = board.findTerritoryByName(args.get(1));
                    int armies = Integer.parseInt(args.get(2));

                    if (t1.getOwner() != currentPlayer || t2.getOwner() != currentPlayer) {Parser.displayMessage("You do not control both territories"); return;}
                    if (!board.areConnected(t1, t2)) {Parser.displayMessage("Specified territories are not connected"); return;}
                    if (t2.getNumArmies() <= armies) {Parser.displayMessage("You cannot move that many armies from "+t2.getName()); return;}

                    board.moveArmies(t2, t1, armies);
                    Parser.displayMessage("Fortified " + armies + " armies from " + t2.getName() + " to " + t1.getName());
                    nextTurn();
                    nextTurnStage();
                }
            }
        }
    }
}
