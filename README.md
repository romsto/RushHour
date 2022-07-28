# RushHour

#### Video Demo:  [Youtube Link](https://youtu.be/bLLiRoKcoZE)
#### Description:
Play the famous boardgame and Solve the puzzle automatically with graphs and recursion.
![image](https://user-images.githubusercontent.com/10656815/181606676-a3ca285b-0c99-403a-ab3a-f7283d4c43ac.png)

## What is Rush Hour? And what’s the project?

Rush Hour is a puzzle formed by a 6 by 6 grid filled with vehicles such as cars (filling 2 squares) or trucks (3 squares), of different colors and oriented in a certain way. The goal is to get the red car (positioned horizontally on the 3rd line) out of the grid by moving the other vehicles.

![image](https://user-images.githubusercontent.com/10656815/181606801-29572dcf-716b-46b4-9b6f-c79495759650.png)

My project is first to create an algorithm to find the shortest solution path from any grid, i.e., the solution with the fewest possible moves. I also decided to redo the graphical interface of the game, and to add the possibility of playing it yourself.

## How did I proceed?

Before starting the resolution of my graphical interface, I realized the system allowing to load pre-existing configurations. These are CSV files containing the positions, type, orientation, and color of each vehicle initially. Now this programmed loading, I was then able to think about the resolution algorithm of the game.

To carry out the algorithm, I first tried to calculate all the possible paths by listing sequences of movements, without conditions, which caused a stack overflow error. Indeed, some paths had no end (since they had loops or repetitions) and made the program run ad infinitum (thus causing the error).

To face this problem, I carried out a pruning which consists in eliminating the repetitions in the branches by imposing certain conditions (which we will see in my analysis part).

Thus, the final tree is only composed of unique grids, with certain stopped paths not leading to a solution and various possible solutions. (I then perform a naive search through the list of solutions to find the one that is fastest)

This solution no longer caused an error, but took a lot of time for some grids (like for the 14 with its 14647 possibilities).

I therefore sought to optimize our algorithm by using character strings to save vehicles and avoid rebuilding them with each new grid, thus saving memory and improving its efficiency. This improvement has made it possible to significantly optimize the creation of the tree and to calculate its entirety much more quickly.

I then sought to improve this construction further, but to achieve this maximum optimization, I unfortunately had to modify the entire structure (which I decided not to do). I therefore decided to save in files the calculated trees of each configuration to then allow an almost instantaneous loading of our graph!

Then I recreated the graphical interface in Java FX in order to display the shortest solution from the initial grid chosen, but also to offer the user to play the game himself, by moving the vehicles.

## Details

### Finding the shortest path
To start, you must create an initial Grid containing a list of Vehicles. For that I proposed two possibilities: either I "create by hand" this grid by placing the vehicles one by one, or I can extract already existing configurations from files of the config_xx.csv type. This last type of file contains in each line a vehicle with all its information – the position, the color, the orientation, and the type of vehicle. To retrieve this information, the CSVParser class reads the desired file, and converts each line of this file into a Vehicle. I thus obtain the initial grid with all the vehicles.

To find the shortest path to get the red car out of our grid, I created a Graph that allows us to observe all the solutions. I then look for the fastest solution among this set.

First, I needed to create the Grid class which takes each Vehicle from a list of vehicles, converting them into a Vehicle object and placing them in a grid like the following.
![image](https://user-images.githubusercontent.com/10656815/181607085-ab19cf7c-081b-4f3e-8787-325632c0e4d4.png)

To create these Vehicles, I formed the Vehicle class which has the following attributes:
- The type of vehicle (car or truck).
- Its “coordinates”: the line and the column of the rear of the vehicle.
- Its orientation (horizontal or vertical).
- Its color (coded in hexadecimal).
- And the grid to which it belongs.

From a Grid, I look for the movements that can be made (thanks to the Vehicle class). These movements make it possible to give new grids and so on until a finished Grid is reached, with the red car in the final position:

![image](https://user-images.githubusercontent.com/10656815/181607156-8e302f32-e67d-4653-b7e4-d17704aca42c.png)

As the grids are calculated, pruning is carried out:
- Grids that already exist are not created, thus preventing the creation of a new Changement;
- Carrying out the reverse movement is not possible.

From a given Grid, we can know the Move that led to it, as well as the following Changes that can be made thanks to the Node class that connects all this information.

To carry out a Movement, we take as argument the Vehicle in its position before it is moved and the movement which can be carried out (either it advances, or it retreats).
A Changement links the Move made to a new Grid.

Finally, the Graph will be built by forming successive Nodes from the initial Grid, until all possible paths have been calculated. The Graph is therefore the list of nodes forming a set of possible paths.

Thanks to the construction of the Graph, I obtain the list of all the possible paths leading to the solution. So, I extract the shortest list to display the best solution.

As I explained before, I added a graph saving system to allow solutions to load very quickly. For this we had to convert the graph into text, and vice versa to ensure the transaction between our Graph and the files. This is all done by the Save class. 

### Creation of the graphical interface (GUI)

The interface consists of two panels. The first is the game panel, which is of the Pane type, having the image of the board and the Shapes in the background. The second panel is a GridPane which allows the display of the different controls of the game.

I created the Form class to create the vehicles and allow their display. It is in fact a javafx Group comprising a colored rectangle, and an image (corresponding to the type of the vehicle).

So that the vehicles have the right color, we create a colored rectangle with the dimensions of the vehicle (which depends on its type) on which we superimpose the png image of the vehicle. If ever the vehicle is in a vertical position, it is rotated 90°, with a suitable rotation pivot.

For them to be displayed in the correct position, their coordinates are retrieved from the grid and transformed into pixel coordinates.

The movement of each vehicle will be done using a slide, which makes it possible to make an animation pleasant to the sight, as for the manual movement (via the user), it is calculated from the different possible positions that each vehicle can take. So, when the user keeps the left click pressed on a vehicle, he can move it freely, but the vehicle will only take the authorized positions.

## Algorithms explanation

### nextGrids(lastMove) method of the Grid class

In the Grid class, the method nextGrids(lastMovement) allows to obtain a list of possible Changes from the current Grid. For this, we carry out the following steps:

1. I search for the possible moves for each vehicle, removing the possibility of performing the move that allowed access to the current grid. For this, the vehicle that was moved previously cannot perform the reverse move.
2. When a move is possible, we create a new grid with the move made and add the change made to our list containing the next changes

### Graph class build() method

In the Graph class, the build() method allows us to calculate all the levels of our tree from an initial grid.

We create a queue in which we put all the grids that are not finished through the Node that represents them. If this queue is not empty, we check whether moves are possible from the nodes in the queue, if this is the case, we calculate the following nodes and add them to the queue.

If ever the move performed results in a grid that already exist then the Node is not created.

## Tests

Various tests were carried out throughout the development of my program, including for example the correct termination of programs.

The following test method makes it possible to solve all the configurations and to calculate the time required. This is solvesAllConfigurations(). It is simply made up of a loop incrementing an integer by 1 on each pass, to scan the loading and resolution of all configurations.

We note for example that configuration 14 took more than 30 minutes to resolve.

## Quick manual

### How to use the interface?

The interface is very simple. The left part contains the game grid where it is possible to move the vehicles by hand, and where the resolution steps will be displayed.

The right side has controls. The text zone is used to enter the configuration number (ranging from 1 to 40). It will be used by the two buttons below. The "Load" button is used to load the configuration entered, this allows for example to view the initial grid and play on it. The "Solve" button allows you to load the configuration, and display the steps for solving the grid.

### How to create your own grid?

1. Create a new grid instance.
Grid myGrid = new Grid();
2. Place the red car. Attention, it is necessary that this car is horizontal, red (0xFF0000) and on the third line (line of index 2). Vehicle carRed = new Vehicle(column, 2, 0xFF0000, VehicleType.CAR, Orientation.HORIZONTAL, myGrid);
myGrid.vehicles.add(redcar);
3. You can then add as many vehicles as you wish (in the same way as before). The vehicles must not overlap, and they must all have a different color.

### How to recover an existing grid?

There are 40 already existing configurations of Rush Hour game. This method consists in recovering the initial grid of one of these configurations.

1. Get the parser. (This method may possibly throw an exception: IOException if the configuration does not exist)
int config=2; // Number between 1 and 40
CSVParser parser = new CSVParser(configuration);
2. To convert the configuration file to Grid, simply call the method provided for this purpose.
Grid = grid.convert();

### How to solve a grid?

Solving a grid consists in finding the set of possible paths, to eventually find the shortest. For this we go through the Graph object.

1. Create the graph of a grid
Graph = new Graph(grid);
2. It is then necessary to construct the nodes and roots of this graph.
graph.build();
3. Once the construction is finished, all the paths can be retrieved in the form of a list of paths (a path is in fact a list of changes) by this method: graph.getInitial().listAllPaths();

### How to find the shortest path?

Once the graph has been constructed, the shortest path must be found in the list of all possible paths.

1. Retrieve the list of all possible paths.
List<List<Changement>> paths = graph.getInitial().listAllPaths();
2. Perform the desired search method.
