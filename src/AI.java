public class AI implements RiskView {

    private Board board;

    public AI(Board board){
        this.board=board;
    }

    @Override
    public void updateMap(MapEvent me) {

    }

    @Override
    public void updateUI(UIEvent uie) {

    }

    @Override
    public void showMessage(String message) {

    }

    /**public int Utility(Board board){

        //Determine state of territories around it based on: numArmies, ...            Prefer not attacking border territories
       // for (int i = 0; i <  ; i++) {

       // }


        //Can it conquer a continent?


        //Where to fortify via inverse of A? offensively or defensively?  Place the armies @ the start of a round


        return 0;

    }) **/
}
