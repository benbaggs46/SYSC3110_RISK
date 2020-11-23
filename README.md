# SYSC3110 RISK
### Version: 3.0.0
### Authors:
- Ben Baggs: @benbaggs46
- Liam Ballantyne: @ljjb97
- Imran Latif: @imran1503
- Vijay Ramalingom: @vijayramalingom12
- Razem Shahin: @razem999

### Changelog:
- 2.0.0 - Removed text-based interface and replaced it with new GUI.
- 3.0.0 - Added AI Players, reworked MVC structure, UI and gameplay changes.

### Table of Contents:
- [Quick Start](#quick-start)
- [Deliverables](#deliverables)
- [Issues](#issues)
- [Resolved Issues](#resolved-issues)
- [User Manual](#complete-user-manual)
    * [Playing the Game](#playing-the-game)
    * [The Map](#the-map)
    * [Current Player Information](#current-player-information)
    * [Selecting Territories](#selecting-territories)
    * [Buttons](#buttons)
    * [Example Turn](#example-turn)
    * [AI Players](#ai-players)
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
The game will then prompt you for the number of players you want, the names of those players, and whether they are human or AI players. After that, the game will begin at the start of the first player's turn.

### Deliverables:
SYSC3110_RISK_UML - Milestone 3.png gives a detailed uml diagram
The src directory contains all the source files for the project.
The test directory contains all the JUnit test files for the project.
The diagram directory contains the class and sequence diagrams.

### Issues:
- Some of the squares showing the owner and number of armies for a territory are not contained within the borders of their corresponding territories. This is because each square is simply centered on the midpoint of the rectangular bounding box of its territory. Because of this, the information can be partially obscured by the background color of an adjacent territory when it is updated.
- Borders are drawn over the territory information squares.

### Resolved Issues:
- Added sequence diagrams that model the events that occur over a turn for both the console and GUI based verions. 
- Removed the ability to place or retract 0 armies from a territory during the PLACEMENT phase.
- The RISK model was too tightly coupled to the game view.

### Complete User Manual:
To play RISK run the main function in BoardView.java, or if running from the jar file type:
```
java -jar risk.jar
```

The game will then start! 

#### Playing the Game
When the application is opened initially, the user must press the NEW GAME button to start a game of RISK.

Every player will begin their turn in the PLACEMENT phase. They will have been given a certain amount of armies, indicated at the top of the screen. They must place all of these armies on territories that they control by selecting the territory they wish to place armies in and using the ACTION BUTTON. Once armies are placed, they are not there permanently, and can be retracted in a similar manner. Once they player has placed all of their new armies to their satisfaction, they may move to the next phase of their turn using the PROCEED button.

Once in the ATTACK phase, they may select the two territories they wish to attack and attack from, respectively, and use the ACTION BUTTON to initiate an attack on another player. If, during an attack, the defending territory loses all armies, the territory is conquered and the attacker gains control of it. They may continue to attack until they decide to stop or do not have enough armies. They may then move to the final turn phase using the PROCEED button.

The FORTIFY phase allows the player to move a certain number of armies between two territories that they control. They may select these two territories and use the ACTION BUTTON to fortify. Once a successful fortification is completed, this turn phase automatically ends. If the player does not wish to fortify, they may simply press the PROCEED button.

After this, the next player's turn will begin. If they are also a human player, their turn will be similar. If they are AI controlled, they will play automatically, and the user will only be notified of game events when it directly concerns them. The game will cycle between each players' turn in a fixed, random order until a single player controls all territories on the board. This player is declared the winner and the game ends. The window will remain open and a new game may be started using the NEW GAME button.

At any time, a help menu may be opened using the HELP button.

The player may start a new game or close the application at any time using the NEW GAME and QUIT buttons respectively.

During any placement, retraction, attack, or fortification, the player will be propmted to enter any additional directions via dialog boxes when applicable.

#### The Map
The RISK game board will be drawn to the screen once the user starts a new game. Each polygon represents a territory, and the lines between them represent borders. Territories are adjacent if they share a border or there is an extra line connecting their borders. Alaska and Kamchatka are adjacent, and the line connecting them extends off the left and right sides of the map.

The color of a territory when it is not selected indicates which continent it is a part of. The player recieves extra armies at the beginning of their turn if they control entire continents. When selected, the territory will always appear red.

Every territory has a square drawn above it. The color of the square represents the player that currently controls it, and the number indicates the amount of armies on the territory.

During the PLACEMENT turn phase, the square may contain two numbers seperated by an addition sign. When this occurs, the number on the right represents armies that the player has placed in the territory, but have not been confirmed. The player may still retract these armies before moving to the ATTACK phase. The number on the left indicates the amount of armies on the territory at the beginning of the turn, before the player started placing armies.

#### Current Player Information
Throughout the game, information relevant to the current player will be displayed at the top of the screen. 

- The player's name and associated color will be displayed at the top center of the screen as text with a colored square beside it.
- The line labelled "Turn Phase" indicates the turn phase that the player is currently in. This can be either PLACEMENT, ATTACK, or FORTIFY.
- The line labelled "Total Armies" indicates the total number of combined armies the player has in all of their controlled territories.
- The line labelled "Total Territories" indicates the number of territories the player currently controls.
- The line labelled "Current Army Bonus" indicates how many armies the player will recieve at the start of their turn if they do not gain or lose any. This number will have a minimum value of 3 / turn.
- The line labelled "Armies to Place" indicates the amount of armies that the player has been given during the PLACEMENT turn phase that have not yet been placed on the board. The player must place all of these armies before proceeding to the ATTACK phase. This number will be zero at all other times.

#### Selecting Territories
Territories on the game board can be selected or deselected by clicking on them. The specific territories selected indicate the players intentions when they press the ACTION BUTTON. When selected, the background color of a territory will be red instead of the usual background color. Territories will stay in their current selection state until the player clicks on them or one of the following occurrs:

- When a player moves to the next phase of their turn, all territories will be deselected.
- After a player successfully attacks and conquers a territory, the territory that they attacked from will be deselected and the territory that they conquered will remain selected.
- When a player ends their turn, the new player's turn will begin with no territories selected.

#### Buttons
There are 5 buttons at the bottom of the window for the player to use. From left to right they are as follows:

```
New Game
```
This button starts a new game of RISK. After being pressed, the user will be prompted to enter the desired number of players and then names for those players. After that, the new game will begin on the first player's turn. If this button is pressed while a game is in progress, a new game will begin and the current one will be lost.

```
Help
```
This button opens a dialog box containing helpful information about the game.

```
Quit
```
This button terminates the application. When pressed, the game window will close and the current game will be lost.

```
Proceed
```
This button is used to move to the next phase of a player's turn, or when in the FORTIFY phase, move to the next player's turn. If the player tries to press this button during the PLACEMENT phase when they still have armies left to place on the board, a dialog box will inform them of that and nothing will happen.

```
Action Button (PLACE/RETRACT, ATTACK, FORTIFY)
```
This button is used by the player to perform actions during their turn, such as placing armies, attacking, and fortifying. The text of the button will change to reflect the turn phase that the player is currently in. 

In the PLACEMENT phase, the button label will say "Place / Retract", and the player must press the button after selecting a single territory to place or retract armies. The player will then be prompted for any additional details through dialog boxes.

In the ATTACK phase, the button label will say "Attack", and the player must press the button after selecting only the territory they wish to attack plus the one they wish to attack from. Then, the attacker and defender will both be prompted for additional details through dialog boxes.

In the FORTIFY phase, the button label will say "Fortify", and the player must press the button after selecting only the two territories between which they wish to move armies. Using dialog boxes, the player will then indicate the direction they wish to move armies, and how many, if applicable.

During any of the 3 turn phases, if the player presses this button when they have an invalid number of territories selected, they will be informed of their error with a dialog box and nothing will happen. If they have the correct number of territories selected, but their selection is invalid for another reason, a dialog box will provide more information and nothing will happen.

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

### AI Players:
In the PLACEMENT phase, AI players will place all of their new armies in the territory they intend to attack. If they do not intend to attack, they will instead place their armies in the border territory that they deem the most vulnerable to enemy attack.

In the ATTACK phase, the AI player will continously re-evaluate the state of the board, and attack until there are no possible attacks that the AI player deems good enough to perform. AI players will always attack or defend a territory with the maximum possible amount of armies, and will always move the maximum amount after conquering a territory.

In the FORTIFY phase, AI players will move armies from the non-border territory with the most extra armies (at least 2). If no such territory exists, they will move armies from the territory that they deem the least vulnerable to enemy attack. They will move armies to the border territory that they deem the most vulnerable to attack. The number of armies moved will always be half of the maximum possible amount.

When deciding the territory they intend to attack, AI players value five things, which they calculate for every possible territory:
- Having fewer border territories after taking the territory in question (Being able to concentrate armies).
- Having more than twice as many armies attacking as defending. Conversely, having less than twice the amount is a negative incentive to attack (Picking easy battles).
- Taking a large percentage of an opponent's territories (Eliminating opponents).
- Taking a large percentage of the unconquered portion of a continent (Conquering continents).
- Preventing an opponent from recieving a continent bonus (Preventing opponents from conquering continents).

### Important Design Decisions:
- Adding event classes and decoupling the model from views to more closely follow the MVC pattern.
- Choosing to have a RiskInput interface separate from the RiskView interface. When the model needs to recieve a user input during an action that affects how the model changes (asking number of armies to attack/defend with, direction to fortify, etc.) it does not make sense to poll multiple listeners for potentially different responses. Therefore, the Board model holds a reference to exactly one user input source.
- Choosing to implement the AI players as a static method in a class outside the model. Every AI player would follow the same procedure for every turn, and would completely re-evaluate the state of the board each turn regardless of how they were implemented. Therefore, the AI players were implemented as a static method called by the board model and given the current board state whenever required.
- Disabling some pop-up messages while AI players are taking their turns. This was done to make the gameplay faster, especially in a gme with only AI players.
- AI player turn structure. Since the game has to wait for input when a human player is taking their turn, but continously progress through potentially multiple AI turns without stopping, the following was implmented. After any human player ends their turn, or it is the start of the game, the game will calculate how many consecutive AI players are set to take their turns next. A for loop will then be used to call the takeTurn() method in AIPlayer (performing an AI turn) exactly that many times. The game will then pause before the next human turn. If the game has only AI players, there is a special case where the game will loop through turns until the game is won.

### Roadmap:
- Enable loading and saving the game.
