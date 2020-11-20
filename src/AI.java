import java.util.ArrayList;

/**
 * AI implements the view so that it can be notified about when it is its turn and when it needs to do things
 * */
public class AI implements RiskView {

    private Board board;
    private Player player;

    public AI(Board board, Player player){
        this.board = board;
        this.player = player;
    }

    @Override
    public void updateMap(MapEvent me){}

    /**
     * UpdateUI for the AI class controls the logic for what the AI needs to do when it receives a UI event
     * @param uie
     */
    @Override
    public void updateUI(UIEvent uie) {
        if(!uie.getCurrentPlayer().equals(player)){
            return;
        }
        switch(uie.getTurnStage()){
            case PLACEMENT: {
                //TODO: implement what the AI should do during the placement stage
            }
            case ATTACK: {
                ArrayList<Object> i;
                do {
                    i = Utility();
                } while(i  != null);
            }
            case FORTIFY: {
                //TODO: implement what the AI should do during the fortify stage
            }
        }
    }

    @Override
    public void showMessage(String message) {}

    /**
     * Utility function will return the best option for where to attack, from where, and with how many troops
     * @return ArrayList in the format [Territory attacking, Territory defending, Int attacking armies]
     */
    public ArrayList<Object> Utility(){

        //Determine state of territories around it based on: numArmies, ...            Prefer not attacking border territories
       // for (int i = 0; i <  ; i++) {

       // }


        //Can it conquer a continent?


        //Where to fortify via inverse of A? offensively or defensively?  Place the armies @ the start of a round


        return null;

    }
}
