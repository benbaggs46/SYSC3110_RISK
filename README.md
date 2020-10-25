# SYSC3110 RISK
### Version: 1.0.0
### Authors:
- Ben Baggs: benbaggs46
- Liam Ballantyne: ljjb97
- Imran Latif: imran1503
- Vijay Ramalingom: vijayramalingom12

### Changelog:
No changes as of now

### Quick start:
To play risk run the main function in Parser.java or if running from the jar file type
```
java -jar risk.jar
```

The game will then start! To start playing type: 
```
play,<number of player you want>
```
The game will then prompt you for the names of the players and then the game begins!

### Deliverables:
SYSC3110_RISK_UML.png gives a detailed uml diagram
The src directory contains all the source files for the project


### Issues:
There are currently no know issues.

### Complete User manual:
To play risk run the main function in Parser.java or if running from the jar file type
```
java -jar risk.jar
```

The game will then start! 
#### Valid commands and syntax:

- Separate all command words and arguments with commas only (',')
- All names are case insensitive
- Do not use commas in Continent, Territory, or Player names
- Extra arguments after commands will be ignored
- <argument:int> signifies an integer argument
- <argument:String> signifies a name as a string argument

PLAY,<player_number:int>
- Starts a new game of RISK with the specified number of players
- The number of players must be between 2 and 6

PROCEED
- Moves to the next phase of the player's turn, or to the next player's turn

PLACE,<territory:String>,<army_number:int>
- During the FORTIFY phase, places new armies in the specified territory
- This is not a permanent action, and can be undone by the RETRACT command
- Army placements become confirmed once the player ends the fortify phase

RETRACT,<territory:String>,<army_number:int>
- Removes armies placed using the PLACE command, allowing them to be placed elsewhere

ATTACK,<defending_territory:String>,<attacking_territory:String>,<attacker_number:int>
- During the ATTACK phase, performs an attack between the specified territories
- The attacker will attempt to roll with the specified number of dice (between 1 and 3)

FORTIFY,<to_territory:String>,<from_territory:String>,<army_number:int>
- During the FORTIFY phase, moves armies between the specified territories
- Once a successful fortification is completed, the players turn will end automatically

PRINT
- Prints a description of the entire board

INFO,<object_name:String>
- Shows more information about the specified object (Continent, Territory, or Player)

HELP
- Displays a help message detailing all commands

QUIT
- Quits the game

#### Playing the game
1. To start playing type:
    ```
    play,<number of players>
    ```
    Where number of players is an integer from 2-6
2. Next type in the players names
3. Next the first players turn starts. They will be given the option to place as many pieces as they have been given using the place command as specified above.
4. Once they are done placing armies the player will then type proceed.
5. The Attack phase will now begin. The player can attack any unowned territory that is neighbouring one that the player does own.
The attacker may attack with up to 3 armies, and the defender can defend with up to 2 armies. If the defending territory runs out of armies
The attacker gains control of the territory and can then decide how many pieces to move from the attacking territory to the newly
conquered territory. The attacker may attack as many times as they like.
6. Next the player types proceed to move to the fortify phase of the turn. The player can move any number of armies from
any territory they control to any other territory that is both connected to the original territory and owned by the player.
This may only be done once.
7. Once any armies have been moved or proceed is typed it will become the next players turn, which look the same as the first players turn.
8. If a player no longer controls any territory they are eliminated from the game.
9. Once only one player is left, the game is over and the only remaining player is the winner. 

### Important design decisions:
- MVC -> Board becoming a model
- Comma over spaces,  Choose the easier method to implement on out behind as the console version of this class was going to be overshadows later on by the GUI implementation. This was easier as it allowed us to not have to change the xml file or any of the logic that was required to parse it.
- Reading a file vs Hardcoding in the map. We chose to make our first deliverable read a xml file as it avoided hardcoding and fulfilled a future. They currently are not enabled as it is currently always using the default map, however this allows us to easily update the project to what we need later on.
- Removing Game -> Splitting functions into board and parser -> Parser becomes our view / "Main" class.   There was limited use for game that did warrant the entire class for it. Once we switched to using MVC, It was clear that Parser was our view and that is where the user input was coming from, so it became our main.
- Removing Dice ^^, moved functions to board