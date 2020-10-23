import java.util.Random;
import java.lang.Math;
public class Dice {

    /**
     *Generates a number between 1 and 6, as many times as we want.
     * @param numDie is the number od fice being rolled. 
     * @return sum, which is the sum of all the dice roll results.
     */
    public int rollDice(Integer numDie) {
        int sum=0;
        Random random = new Random();
        for (int i = 0; i < (numDie -1); i++){
            int x = random.nextInt(5) + 1;
            sum += x;
        }
        return sum;
    }


    /**
     * This function takes in two dice rolls, and checks them against each other
     * in order to determine a winner based on who rolled the higher number.
     * @param diceRoll1
     * @param diceRoll2
     * @return
     */
    public String compareDice (int diceRoll1, int diceRoll2){
        int x = Math.max(diceRoll1, diceRoll2);
        if (x == diceRoll1){
            return "Attackers";
        }
        else if (x == diceRoll2){
            return "Defenders";
        }
        else{
            return "Errpr";
        }

    }

    /*
    public int calcDiceNum(Country c){
        int x = c.getArmyNum() / 3;
    }

    */


}
