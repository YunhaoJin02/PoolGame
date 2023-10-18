# Pool Game Builder

To run the application, please use:

gradle run
When running, a difficulty menu will show up for user to choose relative difficulty level they want to play

To generate a javadoc, please use:

gradle javadoc


#Implemented feature
- Pockets and more colored balls are implemented
- Difficulty level implemented, this can be chosen after gradle run
- Time and score implemented, will show at the top buffer area
- Undo and cheat implemented, in button way.


# Game Notes
- In order to hit the ball, click and hold onto the edge of the cue ball where you'd like to hit. 
- Then, drag your cursor away (in the angle you'd like to hit), and then release.
- The power of your hit will be based on the length of your drag (although ball velocity is capped).
- Firstly will open a menu to select corresponding difficulty level. If you want to change, close the game window and can
rechoose the game difficulty.
- Click cheat button first and a dropdown box will show up, then select a colour type of ball for cheating
- Click save button to save the current state
- Click Undo button to undo the operation. It will go back to the previous saving state (the state when save button is clicked)
- Time will automatically stop when game wins.


# Config Notes
When entering config details, please note the following restrictions:
- Friction must be value between 0 - 1 (not inclusive). [Would reccomend switching between 0.95, 0.9, 0.85 to see changes].
- Ball X and Y positions must be within the size of the table width and length, including the ball radius (10).
- Ball colours must be Paint string values as expected.


# Design pattern
- Observer pattern is used, mostly implemented in the observer package
- Memento pattern is used, mostly implemented in the memento package
- Builder pattern is used, extended in the objects package. This is used to created pockets.
- Strategy pattern is extended, different balls strategy are now created with one of the prominent ball colour as class name.