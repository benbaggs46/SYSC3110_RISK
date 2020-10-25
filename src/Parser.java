import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Parser {
    /**
     * The character that will be used to split inputted strings
     */
    public static final String DIVIDER_CHAR = ",";
    /**
     * The string that will be printed to prompt the user to type
     */
    public static final String PROMPT_STRING = ">>> ";
    /**
     * The string to be printed if the command isn't valid
     */
    public static final String INVALID_COMMAND_MESSAGE = "Invalid Command";
    /**
     * A scanner to get the input
     */
    public static Scanner in = new Scanner(System.in);
    /**
     * The boardController that will be used to process commands
     */
    public static BoardController boardController;

    /**
     * Start looking for user input
     * @param bc the boardController that will be used to process commands
     */
    public static void begin(BoardController bc){
        boardController = bc;
        while(true){
            parseInputString(getBlankPrompt());
        }
    }

    /**
     * Print a message for the user
     * @param message The message to be printed
     */
    public static void displayMessage(String message){
        System.out.println(message);
    }

    /**
     * Displays a message then prompts the user for input
     * @param promptMessage the message to prompt with
     * @return the user input to the prompt
     */
    public static String getPrompt(String promptMessage){
        displayMessage(promptMessage);
        return getBlankPrompt();
    }

    /**
     * Prompts the user for input
     * @return the user input to the prompt
     */
    public static String getBlankPrompt(){
        System.out.print(PROMPT_STRING);
        return in.nextLine();
    }

    /**
     * Prompts the user for an integer input
     * @param promptMessage Message to prompt user with
     * @return the integer the user input
     */
    public static int getIntPrompt(String promptMessage){
        try{
            displayMessage(promptMessage);
            return Integer.parseInt(getBlankPrompt());
        }
        catch(Exception e) {
            return getIntPrompt(promptMessage);
        }
    }

    /**
     * Splits the input on ever comma then checks to see if there is a commandWord present. If there
     * is one, a Command is built with all the words from the input and sent to the boardController for further
     * processing and testing.
     * @param input the string to parse
     */
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