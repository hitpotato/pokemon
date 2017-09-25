How Project flow: 

1. run GUI: I have change Yang view to Thai view which is in Graphic View
2. new game(Stansection type)  is created in the Gui, then passed to Graphic view
3. what in "game": 
	- game contains "map class and trainer class"
	- game initialize the trainer started poison.
4. what in "map":
	- basic is 2D array  which contain chars. Each char represent a character 
	- the whole map will set up to start the game. However, just display a part of the map which is build by 
	the function "public char[][] visiableMapGUI()"