/**
 * This class represents a command from the user.
 * Every command contains a command word and a list of String arguments.
 */

import java.util.List;

public class Command {
    /**
     * The command word that is passed in during construction
     */
    private CommandWord commandWord;
    /**
     * The rest of the arguments that are a part of the command
     */
    private List<String> args;

    /**
     * Constructor for commands so that they are formatted well for further use
     * @param commandWord the word that belongs to the commandWord enum
     * @param args the rest of the words included in the command
     */
    public Command(CommandWord commandWord, List<String> args){
        this.commandWord = commandWord;
        this.args = args;
    }

    /**
     * Gives the specific word that belongs to the enum of commandWords
     * @return the command word that is an enum of commandWords
     */
    public CommandWord getCommandWord(){
        return commandWord;
    }

    /**
     * Gives the list holding the rest of the arguments in the command
     * @return list holding the rest of the words in the command
     */
    public List<String> getArgs(){
        return args;
    }
}
