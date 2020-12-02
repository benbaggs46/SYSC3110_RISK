import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Performs AI controlled turns for RISK players
 */
public class AIPlayer extends Player{

    /**
     * The current player who the AI is making decisions for
     */
    private Player player;

    /**
     * The board that the AI Player is playing on
     */
    private static Board board;

    /**
     * The minimum utility score for a possible attack required for the AI Player to do it
     */
    public static int UTILITY_THRESHOLD = 1;

    /**
     * Constructor for the Player object
     *
     * @param name  sets the players name to this parameter
     * @param color
     * @param isAi
     */
    public AIPlayer(String name, Color color, Boolean isAi) {
        super(name, color, isAi);
    }

    /**
     * Gets the best territory to attack based on the current board state
     * @return The attackable territory with the highest utility score, or null if there are no options good enough
     */
    private Territory getTargetTerritory(){

        //create list of all possible territories to attack and an array of their corresponding utility scores
        List<Territory> attackableTerritories = getAttackableTerritories(player.getControlledTerritories());

        if(attackableTerritories.isEmpty()) return null;

        float[] utilityScores = new float[attackableTerritories.size()];

        //assign utility scores
        int maxIndex = -1;
        for(int i = 0; i < attackableTerritories.size(); i++){
            if(maxIndex == -1) maxIndex = i;
            Territory t = attackableTerritories.get(i);
            float utilityScore = 0;
            utilityScore += getAmountOfBordersRemoved(t, player.getControlledTerritories());
            utilityScore += percentOfOpponentTerritoriesRemoved(t);
            utilityScore += percentOfUnconqueredContinentRemoved(t) * 10;
            utilityScore += opponentContinentBonusPrevented(t);
            utilityScore += (getArmyRatio(t, true) - t.getNumArmies());
            utilityScores[i] = utilityScore;
            if(utilityScores[maxIndex] < utilityScore) maxIndex = i;
        }
        return utilityScores[maxIndex] < UTILITY_THRESHOLD? null: attackableTerritories.get(maxIndex);
    }

    /**
     * Returns how many fewer border territories the AI player would have if the took the specified territory
     * @param t The territory that the player is considering attacking
     * @param territoryGroup The territories currently controlled by the player
     * @return The number of border territories they would eliminate if they took the territory
     */
    private int getAmountOfBordersRemoved(Territory t, List<Territory> territoryGroup){
        int currentBorders = getBorderTerritories(territoryGroup).size();
        territoryGroup.add(t);
        int futureBorders = getBorderTerritories(territoryGroup).size();
        territoryGroup.remove(t);
        return currentBorders - futureBorders;
    }

    /**
     * Returns the amount of armies from continent bonuses that the AI player would deprive an opponent if they took the specified territory
     * @param t The territory that the player is considering attacking
     * @return The total continent bonus for the opponent that would be prevented
     */
    private int opponentContinentBonusPrevented(Territory t){
        Player opponent = t.getOwner();
        Continent c = t.getContinent();
        for(Territory t2: c.getTerritoryList()){
            if(opponent != t2.getOwner()) return 0;
        }
        return c.getArmyBonus();
    }

    /**
     * Returns the percentage of the uncontrolled portion of the continent that the AI player would conquer if they took the specified territory
     * @param t The territory that the player is considering attacking
     * @return The percent of uncontrolled continent that would be conquered
     */
    private float percentOfUnconqueredContinentRemoved(Territory t){
        List<Territory> territoriesInContinent = t.getContinent().getTerritoryList();
        float unconqueredContinentSize = 0;
        for(Territory t2: territoriesInContinent){
            if(t2.getOwner() != player) unconqueredContinentSize++;
        }
        return 1 / unconqueredContinentSize;
    }

    /**
     * Returns the percentage of an opponent's territory that the AI player would conquer if they took the specified territory
     * @param t The territory that the player is considering attacking
     * @return The percent of the opponent's territories that would be conquered
     */
    private float percentOfOpponentTerritoriesRemoved(Territory t){
        return 1 / (float) t.getOwner().getNumTerritories();
    }

    /**
     * Performs a RISK turn for the current player on the current board state
     * @param currentBoard The current board
     * @param currentPlayer The player who the AI is acting for
     */
    public void takeTurn(Board currentBoard, Player currentPlayer){
        player = currentPlayer;
        board = currentBoard;
        AIPlacement();
        AIAttack();
        AIFortify();
    }

    /**
     * Performs the PLACEMENT phase of the AI turn
     */
    private void AIPlacement(){
        Territory target = getTargetTerritory();
        Territory attackingTerritory;
        if(target != null) {
            attackingTerritory = getControlledNeighbourWithMostArmies(target);
        }
        else{
            attackingTerritory = getTerritoryByArmyRatio(null, false);
        }
        board.place(attackingTerritory, board.getArmiesToPlace());
        board.nextTurnStage();
    }

    /**
     * Performs the ATTACK phase of the AI turn
     */
    private void AIAttack(){
        Territory target = getTargetTerritory();
        while(target !=  null){
            Territory attackingTerritory = getControlledNeighbourWithMostArmies(target);
            int defendDice = target.getOwner().isAi()? Math.min(target.getNumArmies(), Board.MAX_DEFEND_DICE):
                    board.getUserInputSource().getIntInput("How many armies would "+ target.getOwner().getName() +" like to defend " + target.getName() + " with?", 1, Math.min(target.getNumArmies(), Board.MAX_DEFEND_DICE));
            board.attack(target, attackingTerritory, Math.min(attackingTerritory.getNumArmies() - 1, Board.MAX_ATTACK_DICE), defendDice);
            target = getTargetTerritory();
        }
        board.nextTurnStage();
    }

    /**
     * Performs the FORTIFY phase of the AI turn
     */
    private void AIFortify(){
        Territory source = getNonBorderTerritoryWithMostArmies();
        if(source == null) source = getTerritoryByArmyRatio(null, true);
        Territory destination = getTerritoryByArmyRatio(source, false);
        if(destination != null && source.getNumArmies() > 1){
            board.fortify(destination, source, source.getNumArmies() / 2);
        }
        else board.nextTurnStage();
    }

    /**
     * Returns the territory with the most armies that is:
     * - controlled by the player
     * - not a border territory
     * - has at least 2 armies
     * @return Returns the non-border territory of the current player with the most moveable armies, null if no such territory exists
     */
    private Territory getNonBorderTerritoryWithMostArmies(){
        List<Territory> nonBorderTerritories = player.getControlledTerritories();
        List<Territory> borderTerritories = getBorderTerritories(nonBorderTerritories);

        int maxIndex = -1;
        for(int i = 0; i < nonBorderTerritories.size(); i++){
            Territory t = nonBorderTerritories.get(i);
            if(!borderTerritories.contains(t)) {
                if (t.getNumArmies() > 1) {
                    if (maxIndex == -1) maxIndex = i;
                    else if (t.getNumArmies() > nonBorderTerritories.get(maxIndex).getNumArmies()) maxIndex = i;
                }
            }
        }
        return maxIndex == -1? null: nonBorderTerritories.get(maxIndex);
    }

    /**
     * Returns the territory controlled by the current player with either the highest or lowest army ratio.
     * The territory must be connected to the specified territory through only other player controlled territories
     * @param connectedTerritory The territory that must be connected to
     * @param lookingForMax Determines if the territory with the highest(true) or lowest(false) army ratio is selected
     * @return Returns the territory controlled by the current player with either the highest or lowest army ratio, that is connected to
     * the specified territory
     */
    private Territory getTerritoryByArmyRatio(Territory connectedTerritory, boolean lookingForMax){
        List<Territory> borderTerritories = player.getControlledTerritories();
        int bestIndex = -1;
        float[] armyRatios = new float[borderTerritories.size()];
        for(int i = 0; i < borderTerritories.size(); i++){
            Territory t = borderTerritories.get(i);
            armyRatios[i] = getArmyRatio(t, false);
            if(connectedTerritory != null){
                if(board.areConnected(t, connectedTerritory)){
                    if(bestIndex == -1) bestIndex = i;
                    else if((armyRatios[i] > armyRatios[bestIndex]) == lookingForMax) bestIndex = i;
                }
            }
            else{
                 if(bestIndex == -1) bestIndex = i;
                 else if((armyRatios[i] > armyRatios[bestIndex]) == lookingForMax) bestIndex = i;
            }
        }
        return bestIndex == -1? null: borderTerritories.get(bestIndex);
    }

    /**
     * Returns either the difference between the number of armies in a current player controlled territory and the sum of armies in uncontrolled neighbour territories (1),
     * or the sum of armies in current player controlled neighbour territories and the number of armies in an uncontrolled territory (2)
     * @param t The single territory used in the calculation
     * @param attacking Determines which result is calculated. (2) if true, (1) if false
     * @return The calculated army difference
     */
    private float getArmyRatio(Territory t, boolean attacking){
        float availableAttackingArmies = 0;
        for(Territory t2: t.getNeighbours()){
            if((t2.getOwner() != player) == attacking) continue;
            availableAttackingArmies += t2.getNumArmies() - 1;
        }
        float armyRatio = availableAttackingArmies - t.getNumArmies();
        return (attacking? 1: -1) * armyRatio;
    }

    /**
     * Returns the current player controlled neighbour of the specified territory with the most armies
     * @param t The territory that the result must neighbour
     * @return The neighbour territory with the most armies
     */
    private Territory getControlledNeighbourWithMostArmies(Territory t){
        Territory neighbourWithMostArmies = null;
        for(Territory t2: t.getNeighbours()){
            if(t2.getOwner() != player) continue;
            if(neighbourWithMostArmies == null) neighbourWithMostArmies = t2;
            if(neighbourWithMostArmies.getNumArmies() < t2.getNumArmies()) neighbourWithMostArmies = t2;
        }
        return neighbourWithMostArmies;
    }

    /**
     * Returns the border territories of a set of territories
     * @param territoryGroup The list of territories
     * @return A list of border territories from the provided territory group
     */
    private List<Territory> getBorderTerritories(List<Territory> territoryGroup){
        List<Territory> borderTerritories = new ArrayList<>();
        for(Territory t: territoryGroup){
            for(Territory neighbour: t.getNeighbours()){
                if(territoryGroup.contains(neighbour)) continue;
                if(borderTerritories.contains(neighbour)) continue;
                borderTerritories.add(t);
                break;
            }
        }
        return borderTerritories;
    }

    /**
     * Returns all territories possible for the current player to attack
     * @param territoryGroup The current player's territories
     * @return A list of all attackable territories
     */
    private List<Territory> getAttackableTerritories(List<Territory> territoryGroup){
        List<Territory> borderTerritories = new ArrayList<>();
        for(Territory t: territoryGroup){
            if(t.getNumArmies() < 2) continue;
            for(Territory neighbour: t.getNeighbours()){
                if(territoryGroup.contains(neighbour)) continue;
                if(borderTerritories.contains(neighbour)) continue;
                borderTerritories.add(neighbour);
            }
        }
        return borderTerritories;
    }
}
