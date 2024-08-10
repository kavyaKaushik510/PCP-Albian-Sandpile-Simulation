package serialAbelianSandpile;

import java.io.IOException;
import java.util.concurrent.ForkJoinPool;

public class ParallelAutomatonSimulation extends AutomatonSimulation {

    static long startTime = 0;
    static long endTime = 0;

    private static void tick(){ //start timing
        startTime = System.currentTimeMillis();
    }

    private static void tock(){ //end timing
        endTime = System.currentTimeMillis();
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Incorrect number of command line arguments provided.");
            System.exit(0);
        }

        String inputFileName = args[0];
        String outputFileName = args[1];

        // Initialize the grid
        Grid simulationGrid = new ParallelGrid(readArrayFromCSV(inputFileName));

        // Create the ForkJoinPool once in the simulation class
        ForkJoinPool pool = new ForkJoinPool();

        int counter = 0;
        tick();


        // Use the same pool for all updates
        while (((ParallelGrid) simulationGrid).update(pool)) {
            counter++;
        }
        tock();

        pool.shutdown(); // Shutdown the pool when done

        System.out.println("Simulation complete, writing image...");
        simulationGrid.gridToImage(outputFileName);

        System.out.printf("Number of steps to stable state: %d \n", counter);
        System.out.printf("Time: %d ms\n", endTime - startTime);
    }
}
