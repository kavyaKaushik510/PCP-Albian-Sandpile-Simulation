# Parallel and Concurrent Programming_Abelian-Sandpile
The problem outlined in this assignment is that of a two-dimensional cellular automaton called an Abelian Sandpile. This automation is a grid where each cell contains values representing “grains of sand”. When the number of grains in a cell exceeds 4, in the next time step the cell distributes the grains evenly to the neighbouring cells. The border around a grid is called a “sink”.


A starting configuration is created of the Abelian Sandpile where each cell is given a value and it is changed based on the logic described until it reaches a stable state where the grains in each cell are less than 4. The final state is represented by different colours for different values in the cell (black for 0, green for 1, blue for 2 and red for 3). The serial program to simulate this has been provided in the Grid and AutomatonSimulation classes.
This assignment aims to develop a parallel solution for the problem. The parallel solution is required to achieve the following:

*	Runs faster than the serial solution to solve the slow runtime especially when larger grids are processed.
*	Avoid all data races 
*	Only utilise the fork/join framework for synchronisation.
*	Provide accurate outputs from the specified input format (.csv files) and validate the output(.png images).
*	The solution must be run on at least two machines and the outputs must be thoroughly evaluated. 
