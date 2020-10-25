import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Parser {

    public static final String DIVIDER_CHAR = ",";
    public static final String PROMPT_STRING = ">>> ";
    public static final String INVALID_COMMAND_MESSAGE = "Invalid Command";
    public static Scanner in = new Scanner(System.in);
    public static BoardController boardController;

    public static void begin(BoardController bc){
        boardController = bc;
        while(true){
            parseInputString(getBlankPrompt());
        }
    }

    public static void displayMessage(String message){
        System.out.println(message);
    }

    public static String getPrompt(String promptMessage){
        displayMessage(promptMessage);
        return getBlankPrompt();
    }

    public static String getBlankPrompt(){
        System.out.print(PROMPT_STRING);
        return in.nextLine();
    }

    public static int getIntPrompt(String promptMessage){
        try{
            displayMessage(promptMessage);
            return Integer.parseInt(getBlankPrompt());
        }
        catch(Exception e) {
            return getIntPrompt(promptMessage);
        }
    }

    public static void parseInputString(String input){
        String[] args = input.split(DIVIDER_CHAR);

        int length = args.length;

        if(length >= 1) {
            try{
                CommandWord commandWord = CommandWord.valueOf(args[0].toUpperCase());
                List<String> argsList = new ArrayList<>();
                for(int i = 1; i < length; i++) argsList.add(args[i].toLowerCase());
                boardController.processCommand(new Command(commandWord, argsList));
            }
            catch (Exception e){
                displayMessage("Exception thrown in Parser.parseInputString()");
            }
        }
    }
}