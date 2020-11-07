# SYSC3110 RISK
### Version: 1.0.0
### Authors:
- Ben Baggs: @benbaggs46
- Liam Ballantyne: @ljjb97
- Imran Latif: @imran1503
- Vijay Ramalingom: @vijayramalingom12
- Razem Shahin: @razem999

### Changelog:
No changes as of now.

### Table of Contents:
- [Quick Start](#quick-start)
- [Deliverables](#deliverables)
- [Issues](#issues)
- [User Manual](#complete-user-manual)
    * [Valid Commands and Syntax](#valid-commands-and-syntax)
    * [Playing the Game](#playing-the-game)
    * [Example Turn](#example-turn)
- [Important Design Descisions](#important-design-decisions)
- [Roadmap](#roadmap)

### Quick Start:
To play RISK, run the main function in Parser.java, or if running from the jar file type:
```
java -jar risk.jar
```

The game will then start! To start playing type: 
```
play,<number of players you want>
```
The game will then prompt you for the names of the players and then the game begins!

### Deliverables:
SYSC3110_RISK_UML.png gives a detailed uml diagram
The src directory contains all the source files for the project.

### Issues:
There are currently no known issues.

### Complete User Manual:
To play RISK run the main function in Parser.java, or if running from the jar file type:
```
java -jar risk.jar
```

The game will then start! 

#### Valid Commands and Syntax:

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

#### Playing the Game
1. To start playing type:
    ```
    play,<number of players>
    ```
    Where number of players is an integer from 2-6.
2. Next, type in the players names.
3. Then, the first player's turn starts. They will be given a number of armies based on the number of territories they control and any entire continents they control. They must place these armies using the PLACE command as specified above.
4. These placements are not immediately permanent, and can be undone using the RETRACT command. Placements will be confirmed once the player begins the ATTACK phase of their turn using the PROCEED command. The player must place all armies they have been given before starting the ATTACK phase.
5. The ATTACK phase will now begin. The player can use the ATTACK command to attack any unowned territory that is neighbouring one that the player does own. Then, the defender will be asked how many armies they wish to use for defense.
The attacker may attack with up to 3 armies, and the defender can defend with up to 2 armies. If the defending territory runs out of armies the attacker gains control of the territory and can then decide how many pieces to move from the attacking territory to the newly conquered territory. The attacker must move at least as many armies as they used for the conquering attack, and must leave at least one army in the territory they attacked from. The attacker may attack as many times as they like.
6. Next the player types PROCEED to move to the FORTIFY phase of the turn. The player can move any number of armies from
any territory they control to any other territory that is connected to the original territory through only territories owned by the player.
This may only be done once. Once a successful fortification is completed, the next player's turn will begin. If the player does not wish to fortify any armies, they can end their turn using the PROCEED command.
7. Then, it will immediately become the next player's turn, which will play the same as the first player's turn. The game will cycle turns through players until a winner is decided.
8. If a player no longer controls any territories, they are eliminated from the game. They will not have any more turns.
9. Once only one player is left, the game is over and they are declared the winner. 
10. After a game is finished, the game will return to its starting state, where the PLAY command may be used to start a new game. If the PLAY command is used during a game still in progress, the board will be reset and a new game will begin.
11. At any time, the user may request more information about any player, continent, or territory using the INFO command as specified above. The PRINT command will display all board information at once.
12. At any time, the user may enter the QUIT command to terminate the program.

#### Example Turn:
At the beginning of this turn, PLAYER_A has 3 armies in India and 2 in China. PLAYER_B has 2 armies in Siam. All three of these territories neighbour each other.

PLAYER_A begins their turn with 3 new armies to place, which they initially place in India:
    ```
    place,india,3
    ```
    
They then change their mind, retract 2 of those armies and place them in China:
    ```
    retract,india,2
    place,china,2
    ```
    
Next, they move to the ATTACK phase of their turn:
    ```
    proceed
    ```
    
To see which territories they may want to attack, PLAYER_A types:
    ```
    info,china
    ```
   
This displays the state of China, as well as all neighbouring territories. They then decide to attack Siam:
    ```
    attack,siam,china,3
    ```

PLAYER_B is then asked how many armies they wish to defend with. In response, they enter:
    ```
    2
    ```

An attack is then simulated, causing PLAYER_B to lose two armies. Siam now has no armies left, and has been conquered. PLAYER_A then attempts to move 4 armies into the newly acquired territory by entering:
    ```
    4
    ```

This cannot be done, as moving 4 armies from China to Siam would leave China with no armies. Realizing this mistake, PLAYER_A then enters an acceptable number of armies:
    ```
    3
    ```

Now PLAYER_A has 2 armies in India, 1 in China, and 3 in Siam. They then choose to move to the FORTIFY phase of their turn by entering:
    ```
    proceed
    ```

PLAYER_A moves 1 army from Siam into India:
    ```
    fortify,india,siam,1
    ```

PLAYER_B will then begin their turn.

### Important Design Decisions:
- Using Model View Controller Design Pattern.   We decided to build our console based object using this format in order to future proof it for when we would be adding GUI on top of the console version. 
- Comma over spaces.   We choose the easier method to implement on out behind as the console version of this class was going to be overshadows later on by the GUI implementation. This was easier as it allowed us to not have to change the xml file or any of the logic that was required to parse it.
- Reading a file vs Hardcoding in the map.   We chose to make our first deliverable read a xml file as it avoided hardcoding and fulfilled a future. They currently are not enabled as it is currently always using the default map, however this allows us to easily update the project to what we need later on.
- Removing Game -> Splitting functions into board and parser -> Parser becomes our view / "Main" class.   There was limited use for game that did warrant the entire class for it. Once we switched to using MVC, It was clear that Parser was our view and that is where the user input was coming from, so it became our main.
- Removing Dice.   We moved the functions it had to board in order to remove a class that would be responsible for doing next to nothing in the project. This was also inspired by using the MVC design pattern as we were giving the model more control over itself. 

### Roadmap:
- Implement a gui to allow the game to played with only a mouse (keyboard needed to input names)
- Add testing capabilities to enable us to find more issues we might be missing
- Add the option to have AI players
- Enable loading and saving the game
- Implement cards (Optional)
