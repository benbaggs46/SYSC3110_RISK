import java.util.ArrayList;
import java.util.List;

public class AIPlayer {

    private static Player player;
    private static Board board;

    public static int UTILITY_THRESHOLD = 1;

    private static Territory getTargetTerritory(){

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

        /*JOptionPane.showMessageDialog(null, attackableTerritories.get(maxIndex).getName() + "\n"
                + "Utility Score = " + utilityScores[maxIndex] + "\n"
                + "borders removed = " + (getAmountOfBordersRemoved(attackableTerritories.get(maxIndex), player.getControlledTerritories())) + "\n"
                + "% of opponent territories removed = " + (percentOfOpponentTerritoriesRemoved(attackableTerritories.get(maxIndex))) + "\n"
                + "% of unconquered continent removed = " + (percentOfUnconqueredContinentRemoved(attackableTerritories.get(maxIndex))) + "\n"
                + "opponent continent bonus prevented = " + (opponentContinentBonusPrevented(attackableTerritories.get(maxIndex))) + "\n"
                + "army ratio = " + (getArmyRatio(attackableTerritories.get(maxIndex), true)));*/

        //return territory with the highest utility score
        return utilityScores[maxIndex] < UTILITY_THRESHOLD? null: attackableTerritories.get(maxIndex);
    }

    private static int getAmountOfBordersRemoved(Territory t, List<Territory> territoryGroup){
        int currentBorders = getBorderTerritories(territoryGroup).size();
        territoryGroup.add(t);
        int futureBorders = getBorderTerritories(territoryGroup).size();
        territoryGroup.remove(t);
        return currentBorders - futureBorders;
    }

    private static int opponentContinentBonusPrevented(Territory t){
        Player opponent = t.getOwner();
        Continent c = t.getContinent();
        for(Territory t2: c.getTerritoryList()){
            if(opponent != t2.getOwner()) return 0;
        }
        return c.getArmyBonus();
    }

    private static float percentOfUnconqueredContinentRemoved(Territory t){
        List<Territory> territoriesInContinent = t.getContinent().getTerritoryList();
        float unconqueredContinentSize = 0;
        for(Territory t2: territoriesInContinent){
            if(t2.getOwner() != player) unconqueredContinentSize++;
        }
        return 1 / unconqueredContinentSize;
    }

    private static float percentOfOpponentTerritoriesRemoved(Territory t){
        return 1 / (float) t.getOwner().getNumTerritories();
    }

    public static void takeTurn(Board currentBoard, Player currentPlayer){
        player = currentPlayer;
        board = currentBoard;
        AIPlacement();
        AIAttack();
        AIFortify();
    }

    private static void AIPlacement(){
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

    private static void AIAttack(){
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

    private static void AIFortify(){
        Territory source = getNonBorderTerritoryWithMostArmies();
        if(source == null) source = getTerritoryByArmyRatio(null, true);
        Territory destination = getTerritoryByArmyRatio(source, false);
        if(destination != null && source.getNumArmies() > 1){
            board.fortify(destination, source, source.getNumArmies() / 2);
        }
        else board.nextTurnStage();
    }

    private static Territory getNonBorderTerritoryWithMostArmies(){
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

    private static Territory getTerritoryByArmyRatio(Territory connectedTerritory, boolean lookingForMax){
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

    private static float getArmyRatio(Territory t, boolean attacking){
        float availableAttackingArmies = 0;
        for(Territory t2: t.getNeighbours()){
            if((t2.getOwner() != player) == attacking) continue;
            availableAttackingArmies += t2.getNumArmies() - 1;
        }
        float armyRatio = availableAttackingArmies - t.getNumArmies();
        return (attacking? 1: -1) * armyRatio;
    }

    private static Territory getControlledNeighbourWithMostArmies(Territory t){
        Territory neighbourWithMostArmies = null;
        for(Territory t2: t.getNeighbours()){
            if(t2.getOwner() != player) continue;
            if(neighbourWithMostArmies == null) neighbourWithMostArmies = t2;
            if(neighbourWithMostArmies.getNumArmies() < t2.getNumArmies()) neighbourWithMostArmies = t2;
        }
        return neighbourWithMostArmies;
    }

    private static List<Territory> getBorderTerritories(List<Territory> territoryGroup){
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

    private static List<Territory> getAttackableTerritories(List<Territory> territoryGroup){
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
