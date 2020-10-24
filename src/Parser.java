import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Parser {

    public static final String DIVIDER_CHAR = " ";

    public Parser(){
        Scanner in = new Scanner(System.in);
        while(true){
            System.out.println(">>> ");
            parseInputString(in.nextLine());
        }
    }

    public static void parseInputString(String input){
        String[] args = input.split(DIVIDER_CHAR);

        int length = args.length;

        if(length >= 1) {
            try{
                CommandWord commandWord = CommandWord.valueOf(args[0].toUpperCase());
                List<String> argsList = new ArrayList<>();
                for(int i = 1; i < length; i++) argsList.add(args[i]);
                System.out.println("process command - " + commandWord);
            }
            catch (Exception e){
                System.out.println("Invalid Command");
            }
        }
    }
}