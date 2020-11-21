import java.util.ArrayList;
import java.util.List;

public class AIPlayer {

    public static void takeTurn(Board board, Player player){
        //TEST TURN - AI JUST PLACES ARMIES AND ENDS TURN
        /*int armiesToPlace = board.getArmiesToPlace();

        Territory t = player.getControlledTerritories().get(0);

        board.place(t, armiesToPlace);

        board.nextTurnStage();
        board.nextTurnStage();
        board.nextTurnStage();*/

        List<Territory> borderTerritories = getBorderTerritories(player.getControlledTerritories());

        int[] utilityScores = new int[borderTerritories.size()];

        List<Territory> controlledTerritories = player.getControlledTerritories();

        for(int i = 0; i < borderTerritories.size(); i++){
            controlledTerritories.add(borderTerritories.get(i));
            utilityScores[i] = getBorderTerritories(controlledTerritories).size();
            controlledTerritories.remove(borderTerritories.get(i));
        }
        int minIndex = -1;
        for(int i = 0; i < utilityScores.length; i++){
            if(minIndex == -1) minIndex = i;
            else if(utilityScores[minIndex] > utilityScores[i]) minIndex = i;
        }
        Territory target = borderTerritories.get(minIndex);

        Territory controlledNeighbour = controlledTerritories.get(0);
        for(Territory t: target.getNeighbours()){
            if(t.getOwner() == player){
                controlledNeighbour = t;
                break;
            }
        }

        int armiesToPlace = board.getArmiesToPlace();

        board.place(controlledNeighbour, armiesToPlace);

        board.nextTurnStage();

        while(target.getOwner() != player && controlledNeighbour.getNumArmies() > 1){
            int attackDice = Math.min(3, controlledNeighbour.getNumArmies() - 1);
            int defendDice = board.getUserInputSource().getIntInput("How many armies would "+ target.getOwner().getName() +" like to defend " + target.getName() + " with?", 1, Math.min(target.getNumArmies(), Board.MAX_DEFEND_DICE));
            board.attack(target, controlledNeighbour, attackDice,defendDice);
        }

        board.nextTurnStage();
        board.nextTurnStage();
    }

    public static List<Territory> getBorderTerritories(List<Territory> territoryGroup){
        List<Territory> borderTerritories = new ArrayList<>();
        for(Territory t: territoryGroup){
            for(Territory neighbour: t.getNeighbours()){
                if(territoryGroup.contains(neighbour)) continue;
                if(borderTerritories.contains(neighbour)) continue;
                borderTerritories.add(neighbour);
            }
        }
        return borderTerritories;
    }
}
