public class AIPlayer {

    public static void takeTurn(Board board, Player player){
        int armiesToPlace = board.getArmiesToPlace();

        Territory t = player.getControlledTerritories().get(0);

        board.place(t, armiesToPlace);

        board.nextTurnStage();
        board.nextTurnStage();
        board.nextTurnStage();
    }
}
