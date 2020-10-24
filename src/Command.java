import java.util.List;

public class Command {
    private CommandWord commandWord;
    private List<String> args;

    public Command(CommandWord commandWord, List<String> args){
        this.commandWord = commandWord;
        this.args = args;
    }

    public CommandWord getCommandWord(){
        return commandWord;
    }

    public List<String> getArgs(){
        return args;
    }
}
