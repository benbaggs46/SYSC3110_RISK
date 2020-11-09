# SYSC3110 RISK
### Version: 2.0.0
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
The diagram directory contains the class and sequence diagrams.

### Issues:
There are currently no known issues.

### Solved Issues:
- Added sequence diagrams that midel the events that occur over a turn for both the console and GUI based verions. 
- Removed the ability to place or retract 0 armies from a territory during the PLACEMENT phase.

…uring the PLACEMENT phase
### Complete User Manual:
To play RISK run the main function in Parser.java, or if running from the jar file type:
```
java -jar risk.jar
```

The game will then start! 


#### Playing the Game
1. To start, click the play buton and enter the number of players in the promt that will appear. The number of players is between 2-6.
2. Next, type in the players names.
3. Then, the first player's turn starts. They will be given a number of armies based on the number of territories they control and any entire continents they control. They must place these armies, and will be finalized using the PLACE button as specified above.
4. These placements are not immediately permanent, and can be undone using the RETRACT button. Placements will be confirmed once the player begins the ATTACK phase of their turn using the PROCEED button. The player must place all armies they have been given before starting the ATTACK phase.
5. The ATTACK phase will now begin. The player can use the ATTACK button to finalizr any attack on any unowned territory that is neighbouring one that the player does own. Then, the defender will be asked how many armies they wish to use for defense via a text prompt.
The attacker may attack with up to 3 armies, and the defender can defend with up to 2 armies. If the defending territory runs out of armies the attacker gains control of the territory and can then decide how many pieces to move from the attacking territory to the newly conquered territory. The attacker must move at least as many armies as they used for the conquering attack, and must leave at least one army in the territory they attacked from. The attacker may attack as many times as they like.
6. Next the player clicks the PROCEED button to move to the FORTIFY phase of the turn. The player can move any number of armies from
any territory they control to any other territory that is connected to the original territory through only territories owned by the player.
This may only be done once. Once a successful fortification is completed, the next player's turn will begin. If the player does not wish to fortify any armies, they can end their turn using the PROCEED button.
7. Then, it will immediately become the next player's turn, which will play the same as the first player's turn. The game will cycle turns through players until a winner is decided.
8. If a player no longer controls any territories, they are eliminated from the game. They will not have any more turns.
9. Once only one player is left, the game is over and they are declared the winner. 
10. After a game is finished, the game will return to its starting state, where the PLAY button may be used to start a new game. If the PLAY button is used during a game still in progress, the board will be reset and a new game will begin.
11. At any time, the user may request more information about any player, continent, or territory using the INFO button as specified above. The PRINT button will display all board information at once.
12. At any time, the user may enter the QUIT button to terminate the program.

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
- Choosing between having one mouseButtonListener or having one mouseListener and one buttonListner.  ... 
- Choosing what classes not to create JUnit tests for. This is because some classes were to be scrapped as they only served the version of the game where the console was required. 
- Adding lines between territories that were connected over oceans. ... ease of use ... 
- Adding onto the XML file to crete the GUI as well as board. This was done by having our XML file hold the points to create a polygon. This allows for custom map designers to have full freedom and not be held typical country shapes. 

### Roadmap:
- Add the option to have AI players.
- Enable loading and saving the game.
- Implement cards (Optional).
