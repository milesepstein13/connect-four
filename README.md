# Connect Four Project

## CPSC 210 2020 WT1 

From-scratch project that runs connect four in console and in a pop-up window. Options for two-player (two users) and one-player (user against AI, which plays at a beginner-to-intermediate level--knows basic strategy but can't think far ahead)

## Miles Epstein

I plan to create an application that will allow the user
to play connect four, a common strategy game. While easy
to play and learn basic strategy, it doesn't have an obvious way to win
(like tic-tac-toe). It will have:
- single-player mode (playing 
against the computer) 
- two-player mode (two users playing
against each other) 

Anyone can use this application. It isn't something that
will have much impact of the user's life but rather would be used
 entertainment, friendly competition between
friends, or as a way to sharpen one's brain. 

As a kid, I had a handheld connect four game that I
spent a lot of time playing and that taught me the 
strategy of the game. I am interested in this
project both because it is something I personally enjoy and
because I already have some idea of the complex logic
of being good at the game but I still have more to learn. 
In particular, I would explore this logic by creating
the best possible automated oppenent for single player mode.
In addition, although this application doesn't have a direct
application to my field of study, the simple artificial
intelligence of the automated oppenent is certainly a step
toward work that I hope to do in the future.

# User Stories
- As a user, I want to be able to select a one or two player game before each game
- As a user, I want to be able to add a game piece of my own color to any of the allowed spots on the board
- As a user, I want to be able to see when the game is over and be told who won
- As a user, I want to be able to end the program or restart with the same options when a game is over
- As a user, I want to be able to save a game board
- As a user, I want to be able to load a saved game board and continue playing

# User stories for GUI
- As a user, I want to be able to select a one or two player game at any time
- As a user, I want to be able to add a game piece of my own color to any of the allowed spots on the board
- As a user, I want to hear an auditory indication when a game is over and see a tally of total wins for each player
- As a user, I want to be able to load and save the state of the application

# Phase 4: Task 2
There is a bi-directional association between the GUI and Spot classes. The GUI has
a list of Spots as a field and the Spots are painted on the board. 
The GUI calls update on the spots, which changes their color to whatever is needed. 
Spot has GUI as a field and calls getWidth and getHeight on the GUI. It performs a simple
calculation with the returned values to determine its own size (this is how the spots change size
whenever you change the size of the window).

# Phase 4: Task 3
Generally, I am happy with my design as it is fairly straightforward and not 
overly coupled or confusing. One possible change would be to create a class
along the lines of "player" that is abstract and provides the shared 
behavior of GUI and ConnectFour (they both have a board and 2 pieces as
fields and allow you to play connect four). This class would make it much
easier to create new user interfaces but is not especially important since 
Connect Four is no longer used and the GUI is a good final form of the game.

The biggest refactoring I would do is to create an AI class. Right now, 
all AI behavior is in the GameBoard class (i.e. you call board.aiMove 
and then the board thinks and puts a piece in a spot). Since the GameBoard
class is responsible for both keeping track of all the pieces and 
for making intelligent moves in a game it 
is lacking in cohesion. This design choice was  perfectly fine
when the AI move was just to put a piece in a random spot but as I spent
more time with the project I made the AI behavior more and more sophisticated.
Now, the GameBoard class is nearly 400 lines long, almost half of which is
AI behavior. I could refactor the program so that GameBoard has a 
bi-directional association with AI. Then, when the GUI calls on the board
to make an AI move, the board calls on the associated AI and the AI adds the 
piece to the board. This would not affect the logic or behavior but the code
would be more cohesive. 
