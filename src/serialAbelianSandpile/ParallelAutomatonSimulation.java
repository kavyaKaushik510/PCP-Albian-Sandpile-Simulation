package serialAbelianSandpile;

import java.io.IOException;
import serialAbelianSandpile.AutomatonSimulation;
import serialAbelianSandpile.Grid;

public class ParallelAutomatonSimulation extends AutomatonSimulation {

	//time
	static long startTime = 0;
	static long endTime = 0;

	//timers - note milliseconds
	private static void tick(){ //start timing
		startTime = System.currentTimeMillis();
	}
	private static void tock(){ //end timing
		endTime=System.currentTimeMillis(); 
	}
	//time

    public static void main(String[] args) throws IOException {
        Grid simulationGrid;

        if (args.length != 2) {
            System.out.println("Incorrect number of command line arguments provided.");
            System.exit(0);
        }

        String inputFileName = args[0];
        String outputFileName = args[1];

        simulationGrid = new ParallelGrid(readArrayFromCSV(inputFileName));
        int counter = 0;
        tick();
        if (DEBUG) {
            System.out.printf("starting config: %d \n", counter);
            simulationGrid.printGrid();
        }
        while (simulationGrid.update()) {
            if (DEBUG) simulationGrid.printGrid();
            counter++;
        }
        tock();

        System.out.println("Simulation complete, writing image...");
        simulationGrid.gridToImage(outputFileName);

        System.out.printf("Number of steps to stable state: %d \n", counter);
        System.out.printf("Time: %d ms\n", endTime - startTime);
    }
}

