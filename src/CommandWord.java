/**
 * Represents all possible user commands and contains description information about each.
 */
public enum CommandWord {
    PLAY("PLAY,<player_number:int>","- Starts a new game of RISK with the specified number of players\n" +
            "- The number of players must be between 2 and 6\n"),
    PROCEED("PROCEED","- Moves to the next phase of the player's turn, or to the next player's turn\n"),
    PLACE("PLACE,<territory:String>,<army_number:int>","- During the FORTIFY phase, places new armies in the specified territory\n" +
            "- This is not a permanent action, and can be undone by the RETRACT command\n" +
            "- Army placements become confirmed once the player ends the fortify phase\n"),
    RETRACT("RETRACT,<territory:String>,<army_number:int>","- Removes armies placed using the PLACE command, allowing them to be placed elsewhere\n"),
    ATTACK("ATTACK,<defending_territory:String>,<attacking_territory:String>,<attacker_number:int>","- During the ATTACK phase, performs an attack between the specified territories\n" +
            "- The attacker will attempt to roll with the specified number of dice (between 1 and 3)\n"),
    FORTIFY("FORTIFY,<to_territory:String>,<from_territory:String>,<army_number:int>","- During the FORTIFY phase, moves armies between the specified territories\n" +
            "- Once a successful fortification is completed, the players turn will end automatically\n"),
    PRINT("PRINT","- Prints a description of the entire board\n"),
    INFO("INFO,<object_name:String>","- Shows more information about the specified object (Continent, Territory, or Player)\n"),
    HELP("HELP","- Displays a help message detailing all commands\n"),
    QUIT("QUIT","- Quits the game\n");

    /**
     * The command signature of the command type, to be displayed by the HELP command
     */
    private String description;

    /**
     * A description of the command's functionality, to be displayed by the HELP command
     */
    private String signature;

    /**
     * Constructor for CommandWord
     * @param signature The command signature of the command type, to be displayed by the HELP command
     * @param description A description of the command's functionality, to be displayed by the HELP command
     */
    CommandWord(final String signature, final String description){
        this.signature = signature;
        this.description = description;
    }

    /**
     * Returns the signature of the command word
     * @return A String containing a signature of the command
     */
    public String getSignature(){return signature;}

    /**
     * Returns the description of the command word
     * @return A String containing a description of the command
     */
    public String getDescription(){return description;}
}
