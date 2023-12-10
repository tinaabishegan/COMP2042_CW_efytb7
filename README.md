# COMP2042_CW_efytb7
## Compilation Instructions

### Step 1: Download the Game
- Download the provided zip file [BaladewanTinaabishegan.zip].

### Step 2: Extract Zip Contents
- Extract the contents of the zip file to a desired location on your computer.

### Step 3: Download Java SE 19
- Download [Java SE 19](https://www.oracle.com/java/technologies/javase/jdk19-archive-downloads.html). Choose the 
version compatible with your system.

### Step 4: Download JavaFX SDK
- Download [JavaFX SDK](https://gluonhq.com/products/javafx/). Choose the version compatible with your system.
- Extract the JavaFX SDK to a known location on your computer.

### Step 5: Open IntelliJ IDEA
- Open IntelliJ IDEA. If not installed, download and install it from 
[IntelliJ IDEA website](https://www.jetbrains.com/idea/download/).

### Step 6: Open Project
- In IntelliJ, select `File -> Open`.
- Navigate to the project location and open 
`COMP2042_BaladewanTinaabishegan -> COMP2042_CW_efytb7 -> BaladewanTinaabishegan_IntelliJ_19 -> cw`.

### Step 7: Project Structure
- Go to `File -> Project Structure`.
- Under `Libraries`, add the `lib` folder from the extracted JavaFX SDK as a new library to the project.
- If there is an existing `lib` folder, remove it and add it again.

### Step 8: Build Project
- Allow some time for IntelliJ to index the project and download any necessary dependencies (this process is automatic).
- If the project fails to build, retry steps from `Step 7` to `Step 10`.

### Step 9: Run Application
- Once the setup is complete, compile and run the application from within IntelliJ.
- Navigate to the main class, right-click on it, and select `Run`.

### Step 10: Play the Game
- Play and strive up the leaderboard!


## Implemented and Working Properly
### Max Verstappen Block

- `BLOCK_VERSTAPPEN` is a new block implemented, it has a slim chance of spawning in a level but never more than once. 
Once the block has been hit by the ball, temporarily, Max Verstappen in an F1 car will fly across the screen destroying 
all the blocks in that row. During this period, the background music will be paused and Max Verstappen's theme song will 
play. This feature is implemented to enable players to level up faster as it gets quite repetitive to play once the 
players reach a higher level. Initially this feature had an issue where when entity max and the ball hit the block at 
the same time, it will cause a concurrent modification error, it was quickly fixed by modifying the logic that 
identifies the destruction of the block.

### Start Menu

- Initially, once the program is run, it will load the level first then show the menu options, it looked finicky and 
not very appealing, thus I implemented a start menu in which the player will be able to `New Game`, `Load Game`, `Set 
Difficulty`, Open `Help` Menu, and `Quit Game`. This new and improved start menu has its own custom background and look, 
while also not loading the game in the background to save resources. 

### Help Menu

- When introduced to a new game, a player who is not familiar with the game or the controls will tend to struggle to 
learn and enjoy the game. So I have added a Help button that pops up an image that shows all the components of the 
game and its functionalities. It also includes all the control key mapping information so the player can play the 
game without bothering to experiment with the game. This popup also includes a close button that allow players to 
close the popup help menu.

### Pause Game

- There will come a time when the player has done so much progress but has to stop playing to do something else, thus I 
added a `Pause Game` function. When players press `P`, the game will be paused and a pause menu will appear. In that 
pause menu, there will be options such as, `Resume Game`, `Go Back To Main Menu`, `Help Menu`, `Quit Game`, and a 
`Volume Slider`. 

### Sound 

- `Sound` is part of the 5 basic human senses that will definitely affect any activity. Thus, I have added background 
music, sound effects when blocks are broken, special block music, and game over music. This implementation also allows 
players to adjust the volume of the gameplay. All the sounds will be paused when the game is paused as well.

### Difficulty

- Some prefer fast, some prefer calm, some prefer the middle ground. Thus, I added 3 new difficulties(`Easy`,`Medium`,
`Hard`). When it is set to easy the game is in its normal condition where there are 4 columns of blocks to be destroyed 
and progress. But when the difficulty is increased, the width of the game increases as well giving players a larger 
playing field together with many more columns of blocks. To match this playing field, the breaker will also increase in 
speed to catch the ball that is falling on the other end of the field. Few things will change with difficulty, the 
`ballSpeed` will increase, there will be more levels to complete to finish the game, the frequency in which the number 
of rows filled with blocks increases will get smaller, thus making it an endurance test as the difficulty increases, 
and also the `background` will also change to match the difficulty.

### Leaderboard

- Competitiveness is part of human nature. A leaderboard will help players get chance to get a sense of accomplishment 
when they play the game. Once the game ends, doesn't matter if the player wins or loses, the player will be give the 
choice to submit their score alongside with their name to the leaderboard. All `score` and `name` will be stored in a 
text file. Only the top 10 players with the highest scores will be displayed in the leaderboard in the `Game End Menu`.

### Game End Menu

- To add to the experience, I have decided to add an entirely new section when the game ends, this section shows the 
leaderboard that contains the top 10 players with the highest scores, a text box and submit button to allow the player 
to submit their score to the leaderboard, `Restart` button and a `Quit` button.

### Load Game

- Allows players to load the saved game state to continue their journey to conquer the game. This option will be 
presented in `Start Menu`.

### Golden Ball

- When the ball hits `BLOCK_STAR`, golden ball will be activated for a short period of time. During this short period of 
time, the player is essentially invincible, when the ball hits the floor, `heart` will not be effected. Also, during 
this period of time, the ball will have an appearance of a poke-ball. 

### Bonus

- When the ball hits `BLOCK_BONUS`, a `bonus` will be dropped from that broken block. This bonus will start dropping 
towards the ground. When the breaker hits the falling bonus, a bonus point will be awarded.


## Features Not Implemented
### Multi-Hit Blocks

- When initially the idea was developed, the path to implementation seemed clear. When I attempted to actually 
implement this feature, I'd realized that this will require the way Block reacts to a hit with the ball to be changed. 
When I implemented it, I had issues with the ball sometimes phasing through the blocks. When the multi-hit was 
implemented, sometimes the ball will phase through block and bounce multiple times within the block, causing it to look 
unpolished. Upon closer inspection, I realized that it is due to how often the collision check is processed and how the 
collision is checked. If I had tighten the collision physics with the block, it would simply pass through the block as 
the movement of the block is too high. So if I wanted to have a few ball speeds, I will have to make the processing 
speed to change instead of the ball speed to change. This requires to increase the tick rate based on the ball speed 
I desire. I did try that method, but when it was implemented, I saw a significant drop in performance. Because of this, 
the game actually runs slower and slows down the ball speed anyway. Since I needed this program to run on multiple 
computers which might include computers from TCR1, I had decided to completely scrap off this idea.

### Multiple Balls

- Great idea, hard implementation. There was supposed to be a block in which is destroyed, will spawn another ball. The 
problem was the way the ball physics and data was stored/implemented in the original code provided by the module 
conveyor. The code made it so that the ball is hard coded only for 1 ball. If I wanted to turn that ball into a class 
and have multiple objects of it, I would have to refactor ball completely of the initial code. Also, then each ball 
would have their own physics engine running. This was definitely possible, but not viable with the time constraint I 
had for the time given to complete the coursework. Upon further thinking, I also realized that multiple balls would 
have definitely slowed down the game, similar to the problem I had with Multi-Hit Blocks.

### Solid Bars

- Solid Bars where meant to be part of the stage that acted like a solid wall. The problem I faced is similar to 
Multi-Hit Blocks where the ball either phase through it or will get stuck inside the bar repetitiously bouncing in the 
bar.


## New Java Classes

- `GameEndMenu` : Represents the end menu of the game, including displaying game over/win messages, saving `score`, and 
showing the leaderboard. Is located in `GameEndMenu.java`.
- `Help` : Provides a method to display a help popup with an image and a `close` button. Is located in `Help.java`.
- `PauseMenu` : Represents the pause menu in the game. It provides options for `Resume`, `Go Back to Main Menu`,`Help`, 
`Quit`, and `VolumeSlider`. Is located in `PauseMenu.java`.
- `Sound` : Manages the game's audio features, including `background music`, `sfx`, and `game over music`. It follows 
the Singleton pattern, ensuring a single instance of the class throughout the application. Is located in `Sound.java`.
- StartMenu : Represents the initial menu of the brick game. It allows the user to start a `New Game`, `Load Game`, 
`set game difficulty`, access the `help` menu, and `quit` the game. Is located in `StartMenu.java`.
- `savedPlayer` : A simple class to help manage leaderboard data by having two member values(`name`,`score`).  Is 
located in `GameEndMenu.java`.

## Modified Java Classes

### Block
- Added `BLOCK_VERSTAPPEN`
- Modified `checkHitToBlock()` collision physics: Fixed ball phasing through blocks, added corner collision detection to 
fix weird ball movement when ball hits the corner of the block.

### Bonus
- Changed bonus block rectangle name from `choco` to `bonus`: make it easier to understand what is being referred to.
- Removed random `bonus` drop background: Made it more consistent and easier for the players to react to a bonus drop as 
different visuals tend to make players confused.

### GameEngine
- Made `GameEngine` a singleton: When `engine` is called from different classes, the same engine will be referred to 
prevent multiple engines running simultaneously.
- Added the instance of `GameEngine`: To make sure this is the only instance of the `engine` accessed by other classes.
- Removed `onInit()`: Served no purpose. Also removed it from the interface.
- In `stop()` changed `updateThread.stop()`,`physicsThread.stop()`,`timeThread.stop()` to `updateThread.interrupt()`, 
`physicsThread.interrupt()`, `timeThread.interrupt()` respectively: `stop()` is deprecated and unsafe. interrupt() sets
a flag for a graceful thread termination, allowing the thread to handle the interruption safely.
- `synchronized` `getInstance()`, `Update()`, `PhysicsCalculation()`, `TimeStart()`: synchronize functions that operate 
on shared data or resources when multiple threads are involved to prevent data inconsistencies, race conditions, and 
ensure thread safety. Synchronization helps serialize access to critical sections, promoting a consistent and reliable 
execution in a multithreaded environment.

### LoadSave
- Add more game state variables: Added `vX` and `vY` so that the velocity of the ball is restored when the saved state 
is loaded.

### Score
- Included `final Main main` as one of `show()`'s parameter: Easier access to main `root`.
- Changed position of message in `showMessage()`: Made it to show the message in the middle of the screen instead based 
on `screenWidth` and `screenHeight`.
- Refactored `showGameOver()` and `showWin()` to a new class `GameEndMenu`: Merged both methods into one and made it a 
new modularised design.

### Main
- Changed color palette of block `Color[]`: Changed to more appealing set of colors.
- Added `xVerstappen` and `yVerstappen`: x y coordinates of `Max Verstappen`(flying dutchmen).
- Adjusted `yBreak`: Made it stick to the floor to avoid doing the code for bottom collision for break.
- Added `volume` value: Local volume variable to change the volume in `Sound`.
- Added `isMaxTime`: To keep track of the existence of `Max Verstappen` on screen.
- Added `isExistVerstappenBlock`: To make sure `BLOCK_VERSTAPPEN` can only spawn once in a level.
- Added rectangle `verstappen`: Make `Max Verstappen` a flying `object`.
- Added `getInstance()` of `GameEngine` as `engine`: Follow singleton for `GameEngine` to get the one and only instance 
of the `engine`.
- Added `defaultvX` and `defaultvY`: To reset `vX` and `vY` values according to the `difficulty` when stage is reset.
- Added `breakSpeed`: Made the movement of the `breaker` snappier in higher difficulties to make it feel more intense.
- Added `maxLevel`: Reuse value for the maximum number of levels since the number of levels is based on the `difficulty` 
selected.
- Added `getInstance()` for `Sound` as `sound`: Follow singleton for Sound to get the one and only instance of the sound 
players.
- Added a new Object `lock`: A `lock` to `synchronize` different `Threads` happening at the same time to prevent 
`concurrent modification` errors and game crashes.
- Added `difficulty`: 3 difficulties(`Easy`,`Medium`,`Hard`), each difficulty changes the `sceneWidth`, `ballSpeed`, 
number of columns of blocks in a level, `maxLevel`, default velocity of the ball and the `background` of the window.
- Added `setDifficulty()`: To modify all variables affected by `difficulty` at once.
- Modified `start()`: Made it to show the StartMenu first.
- Added `startLevel()`: Game progression cycle and management have been shifted from `start()` to here.
- Added `setStage()`: To modularise and add all `javaFX` elements to the pane when a level has started.
- Added `gameEnd()`: To handle all the sounds, scene settings, boolean values and GameEndMenu all at one place together 
once called.
- Added `setScene()`: To set all pane values at one place and prevent redundant lines of code.
- Modified `initBall()`: Modified the `yBall` value randomisation to make sure the ball doesn't spawn inside the blocks 
as the level increases and the number of rows of blocks spawned increases.
- Modified `initBoard()`: Made it so that the number of rows of blocks in a level is based on `difficulty` and increases 
gradually according to `difficulty`. Added a spawn chance for `BLOCK_VERSTAPPEN`. Made the number of columns of blocks 
spawned also according to the `difficulty` as there will be a bigger playing field in higher difficulties.
- Modified `handle()`: Added a case for `PauseMenu`.
- Modified `move()`: Made the speed of the keyframe animation based on the `difficulty` to make it snappier in higher 
difficulty to move faster to cover the larger playing field.
- Modified `setPhysicsToBall()`: Added `resetCollideFlags()` when ball exceeds `sceneHeight`. Modified game end sequence 
when heart hit 0. Modified the ball velocity according the `level` and `difficulty` to when it bounces off the breaker 
to make it more intense as the game progresses.
- Modified `saveGame()`: Added newly implemented variables to be saved in the saved game state.
- Modified `loadGame()`: Added newly implemented variables to be loaded from the saved game state.
- Added `setNewStage()`: Refactored actions taken after newGame and loadGame is called into one.
- Added `resetStage()`: Reset all values to default game values except for `difficulty`. To prepare to load in next 
level or to clean up after game has ended.
- Modified `restartGame()`: Used `resetStage()` instead of manually resetting variables. Also, reset some untouched 
variables that needs to be reset to restart game.
- Modified `onUpdate()`: Added a `lock` to prevent `concurrent modification` errors. Modified code to include flying
`Max Verstappen` into the equation when detecting collisions. Added block collision logic for `BLOCK_VERSTAPPEN`. Added 
logic for when corner collision occurs. Added `Platform.runLater()` for `javaFX` elements. Added sound effect when a 
block collision occurs.
- Added `setVolume()`: To update `volume` value.
- Modified `onPhysicsUpdate()`: Updates `volume` value in `Sound`. Added `Platform.runLater()` for `javaFX` elements. 
Added snippet to make `Max Verstappen` to fly across the screen. 
- Removed `onInit()`: Served no purpose.
- Added `spawnMax()`: Pauses background music and starts `Max Verstappen`'s theme song. Adds Max Verstappen `javaFX`
elements and set it in place to start flying soon.
- Added `removeMax()`: Stops `Max Verstappen`'s theme song and resumes background music. Reset x and y coordinates of the 
Max Verstappen rectangle object. Removes the object from the current scene.
- Added `quit()`: stops the `engine` and closes the `primary stage`. Essentially ending the program and game.

## Unexpected Problems
### Game Crashes

- For the longest of times, when it was crashing with an error `java.lang.Error: Deadlock detected` or
`java.util.ConcurrentModificationException`, I'd thought it was a syntax error or a game logic error. Then I researched
more about `Threads` and how they worked, in the process I also learned that when dealing multiple Threads going on 
simultaneously, you will need to use a `lock`. A lock, like `synchronized`, helps control access to shared resources 
among multiple threads, preventing data corruption by allowing only one thread at a time to modify shared data. Proper 
use of locks is crucial for thread safety and to avoid issues like `deadlocks`.

### Game Freezes

- The game randomly freezes only visually, but actually the game is running as you can see the score increasing. After
fiddling around on the Internet, I quickly learned that it is due `javaFX` elements being used outside of start 
function. In `JavaFX`, you can stop the game from freezing by using `Platform.runLater()`. This helps when you're doing 
long tasks on non-UI parts. By putting UI updates in `Platform.runLater()`, you make sure the game stays responsive and 
doesn't freeze. It's also handy in event handlers to keep things smooth and avoid slowdowns. Using `Platform.runLater()` 
the right way helps handle multiple things happening at once and keeps the game running well, making it more enjoyable 
for players.