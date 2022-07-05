==============
|First Screen|
==============

When the program first starts up, you will be greeted with a small window
presenting you with two options. If you want to start a new game, simply enterthe grid size that you desire and click the "generate grid" button. This will collect the grid size you entered, and generate a new Futoshiki instance with it. It will print the generated grid to output, call the gridToGUI() function that converts the back-end puzzle into GUI form and closes the current window.

The second button is the "load saved game" button. This loads a previously
saved game file by calling loadSave() and generates a Futoshiki instance using the data stored in, then calls gridToGUI() with the generated grid to show it on-screen (although constraints are not yet loaded in with this version). To help pick out the file you want, the program will ask you to enter the name of the save file you want to load, append ".txt" to the end, and load the file that matches that string. To decide what the grid size is, it will take the first number (representing the grid size) and use that to generate the Futoshiki instance with.

=====================
|Displaying the Data|
=====================

The new window to display the puzzle is set up, and a border pane is created, containing the grid pane for the actual numbers (buttons) and constraints (labels), with buttons for the "check puzzle" and "save game" buttons.

Check puzzle simply checks if the puzzle is legal. If the puzzle is legal (by calling grid.isLegal(), then the user is congratulated and given the option to restart the game, opening the initial menu again, or close the program (to easily test this, I have marked out the if statement that checks this with a comment, indicating that it can be changed to just a boolean true to test the options after this).

Save game gathers all the data within the puzzle back-end (just numbers for now in this version), and saves it to a text file with a number at the beginning that represents the grid size. This can be loaded later at the initial menu.

The grid is made up of buttons, representing numbers, and labels, representing constraints.

==================
|Editing the Data|
==================

To edit a number, you can click on any of the enabled nummber buttons. This will bring up a text input dialog box that prompts you to enter a new number between  1 and the grid size. There is also an "unmark" button to remove any button already entered into the button. If an invalid number is entered, an error dialog box will pop up to warn you.

If a number is disabled, it means it was generated already by the Futoshiki instance as part of the puzzle and thus is not editable.

=================
|Optional Extras|
=================

There is a saving and loading system that is mostly implemented, but does not support saving constraint yet. The numbers are accurately saved and loaded in a text file stored locally. The first number represents the size of the grid. This all means you can leave a game and return to it later to finish off if you want.