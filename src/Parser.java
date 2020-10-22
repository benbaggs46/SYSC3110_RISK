import java.util.ArrayList;
import java.util.Scanner;

public class Parser {

    private CommandWords commands;
    private Scanner reader;

    public Parser(ArrayList<String> commands) {
        this.commands = new CommandWords(commands);
        reader = new Scanner(System.in);
    }

    public ArrayList<String> getCommand()
    {
        String inputLine;   // will hold the full input line
        ArrayList<String> words = new ArrayList<>(); //holds all the words

        System.out.print("> ");     // print prompt

        inputLine = reader.nextLine();

        // Find all the words on the line.
        Scanner tokenizer = new Scanner(inputLine);
        while(tokenizer.hasNext()){
            words.add(tokenizer.next());
        }

        // Now check whether this word is known. If so, create a command
        // with it. If not, create a "null" command (for unknown command).
        for(String i: words){
            if(commands.isCommand(i)){
                return words;
            }
        }
        System.out.println("Invalid command. Please Try again");
        return getCommand(); //recursive call to try to get the player to input a new command
    }
}
