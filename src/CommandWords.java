import java.util.ArrayList;
import java.util.List;

public class CommandWords {

    private List<String> validCommands;

    public CommandWords(ArrayList<String> validCommands) {
        this.validCommands = validCommands;
    }

    public boolean isCommand(String command){
        for(String i: validCommands){
            if(i.equals(command)){
                return true;
            }
        }
        return false;
    }
}
