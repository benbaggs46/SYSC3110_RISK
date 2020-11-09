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
- [Resolved Issues](#resolved-issues)
- [User Manual](#complete-user-manual)
    * [Buttons](#buttons)
    * [Selecting Territories](#selecting-territories)
    * [The Map](#the-map)
    * [Current Player Information](#current-player-information)
    * [Playing the Game](#playing-the-game)
    * [Example Turn](#example-turn)
- [Important Design Descisions](#important-design-decisions)
- [Roadmap](#roadmap)

### Quick Start:
To play RISK, run the main function in Parser.java, or if running from the jar file type:
```
java -jar risk.jar
```

The game will then start! A window will open with a black square at the top and a row of buttons at the bottom. To start playing, press the button labelled:
```
New Game
```
The game will then prompt you for the number of players you want, and then the names of those players. After that, the game will begin at the start of the first player's turn.

### Deliverables:
SYSC3110_RISK_UML - Milestone 2.png gives a detailed uml diagram
The src directory contains all the source files for the project.
The test directory contains all the JUnit test files for the project.
The diagram directory contains the class and sequence diagrams.

### Issues:
- Some of the squares showing the owner and number of armies for a territory are not contained within the borders of their corresponding territories. This is because each square is simply centered on the midpoint of the rectangular bounding box of its territory. Because of this, the information can be partially obscured by the background color of an adjacent territory when it is updated.
- Borders are drawn over the territory information squares.

### Resolved Issues:
- Added sequence diagrams that model the events that occur over a turn for both the console and GUI based verions. 
- Removed the ability to place or retract 0 armies from a territory during the PLACEMENT phase.

### Complete User Manual:
To play RISK run the main function in BoardView.java, or if running from the jar file type:
```
java -jar risk.jar
```

The game will then start! 

#### Buttons

#### Selecting Territories

#### The Map

#### Current Player Information

#### Playing the Game

#### Example Turn:
At the beginning of this turn, PLAYER_A has 3 armies in India and 2 in China. PLAYER_B has 2 armies in Siam. All three of these territories neighbour each other. PLAYER_A has no territories selected on the map.

PLAYER_A begins their turn with 3 new armies to place, which they initially place in India. To do this, they click on India on the map and then press PLACE/RETRACT. When prompted for the number of armies they wish to place, they enter 3.
    
They then change their mind, retract 2 of those armies and place them in China.

To retract from India, they press PLACE/RETRACT. When prompted for the number of armies they wish to place, they enter 2.
To place in China, they click on India to deselect it, and on China to select it, before pressing PLACE/RETRACT. When prompted for the number of armies they wish to place, they enter 2.
    
Next, they move to the ATTACK phase of their turn. To do this, they press PROCEED.
   
Now, PLAYER_A decides to attack Siam from China. To do this, they select China and Siam before pressing ATTACK. When prompted to enter the number of armies they wish to attack with, they enter 3.

PLAYER_B is then asked how many armies they wish to defend with. In response, they enter 2.

An attack is then simulated, causing PLAYER_B to lose two armies. Siam now has no armies left, and has been conquered. This information will be shown in dialog boxes. PLAYER_A will not be asked how many armies they wish to move into Siam, as any player must move at least as many armies as they attacked with when conquering a territory. In this case, that amount is 3. Since PLAYER_A has only 4 armies in China, they cannot move any more than 3 armies either. Therefore, PLAYER_A automatically moves 3 armies into Siam, and will be given this information in another dialog box.

Now PLAYER_A has 2 armies in India, 1 in China, and 3 in Siam. They then choose to move to the FORTIFY phase of their turn by pressing PROCEED.

PLAYER_A moves 1 army from Siam into India by selecting Siam and India before pressing FORTIFY. When asked how many armies they would like to fortify, they enter 1.

Once the fortification is complete, PLAYER_B will then begin their turn in the PLACEMENT phase.

### Important Design Decisions:
- Choosing between having one mouseButtonListener or having one mouseListener and one buttonListner.  ... 
- Choosing what classes not to create JUnit tests for. This is because some classes were to be scrapped as they only served the version of the game where the console was required. 
- Adding lines between territories that were connected over oceans. ... ease of use ... 
- Adding onto the XML file to crete the GUI as well as board. This was done by having our XML file hold the points to create a polygon. This allows for custom map designers to have full freedom and not be held typical country shapes. 

### Roadmap:
- Add the option to have AI players.
- Enable loading and saving the game.
- Implement cards (Optional).
