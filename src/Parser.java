import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Parser {

    /**
     * The character used to divide arguments of a command
     */
    public static final String DIVIDER_CHAR = ",";

    /**
     * The string to be displayed before the user is prompted for input
     */
    public static final String PROMPT_STRING = ">>> ";

    /**
     * The Scanner object that reads console input from the user
     */
    public static Scanner in = new Scanner(System.in);

    /**
     * The board controller to be notified when a valid command is entered
     */
    public static BoardController boardController;

    /**
     * The main method of Parser, and the entire program
     * @param args ---
     */
    public static void main(String[] args){
        boardController = new BoardController();
        Parser.displayMessage("Welcome to RISK!\n" +
                "Made by Ben Baggs, Imran Latif, Liam Ballantyne, and Vijay Ramalingom for SYSC 3110 A" +
                "\nEnter HELP for a list of commands");
        while(true){
            parseInputString(getBlankPrompt());
        }
    }

    /**
     * Displays a message to the console
     * @param message The message to be displayed to the user
     */
    public static void displayMessage(String message){
        System.out.println(message);
    }

    /**
     * Receives an input string from the user through the console, with a prompt message
     * @param promptMessage The message to be displayed before prompting the user for input
     * @return The string input by the user
     */
    public static String getPrompt(String promptMessage){
        displayMessage(promptMessage);
        return getBlankPrompt();
    }

    /**
     * Receives an input string from the user, with no prompt message
     * @return The string input by the user
     */
    public static String getBlankPrompt(){
        System.out.print(PROMPT_STRING);
        return in.nextLine();
    }

    /**
     * Receives an integer input by the user, and keeps asking until the user inputs a valid integer
     * @param promptMessage The prompt message to be displayed before prompting for input
     * @return The integer input by the user
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
     * Processes the string input by the user, and executes the command in the BoardController if it is a valid command
     * @param input The command string input by the user
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
                displayMessage("Invalid Command");
            }
        }
    }
}