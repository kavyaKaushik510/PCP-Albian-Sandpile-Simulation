//Citation: M.M.Kuttel 2024 CSC2002S, UCT: Automaton Simulation
//AUtomaton Simulation file MODIFIED BY: Kavya Kaushik, KSHKAV001
//PARALLELAUTOMATONSIMULATION

package serialAbelianSandpile;

import java.io.IOException;
import java.util.concurrent.ForkJoinPool;

/**
 * Parallel Automaton class simulates an automaton using parallel processing. It extends the Automaton Simulation of the serial program
 */

public class ParallelAutomatonSimulation extends AutomatonSimulation {

    /**Start Time of simulation */
    static long startTime = 0;

    /**End Time of simulation */
    static long endTime = 0;
    
    /**
     * Records the system start time
     */
    private static void tick(){ //start timing
        startTime = System.currentTimeMillis();
    }

    /**
     * Records the system end time
     */
    private static void tock(){ //end timing
        endTime = System.currentTimeMillis();
    }

    /**
     *The method to obtain the Grid and call the parallel algorithm to process it
     *@param args - input csv file; output png file
     */

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Incorrect number of command line arguments provided.");
            System.exit(0);
        }

        String inputFileName = args[0];
        String outputFileName = args[1];

        // Initialize the grid
        Grid simulationGrid = new ParallelGrid(readArrayFromCSV(inputFileName));
        

        int counter = 0;
        tick();


        // Use the same pool for all updates
        while (simulationGrid.update()) {
            counter++;
        }
        tock();

        System.out.println("Simulation complete, writing image...");
        simulationGrid.gridToImage(outputFileName);

        //Output the simulation results
        System.out.printf("Number of steps to stable state: %d \n", counter);
        System.out.printf("Time: %d ms\n", endTime - startTime);
    }
}
