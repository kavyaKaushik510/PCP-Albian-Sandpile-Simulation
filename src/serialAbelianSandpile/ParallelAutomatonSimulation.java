package serialAbelianSandpile;

import serialAbelianSandpile.Grid;
import serialAbelianSandpile.AutomatonSimulation;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;

public class ParallelAutomatonSimulation {
    //static final boolean FLAG = false;
	static final boolean DEBUG=false;//for debugging output, off

	static long startTime = 0;
	static long endTime = 0;

	//timers - note milliseconds
	private static void tick(){ //start timing
		startTime = System.currentTimeMillis();
	}
	private static void tock(){ //end timing
		endTime=System.currentTimeMillis(); 
	}
   
    public static void main(String[] args) throws IOException, InterruptedException{

    	if (args.length!=2) {   //input is the name of the input and output files
    		System.out.println("Incorrect number of command line arguments provided.");   	
    		System.exit(0);
    	}

		String inputFileName = args[0];
        String outputFileName = args[1];

		Grid simulationGrid = new Grid(AutomatonSimulation.readArrayFromCSV(inputFileName));

        final ForkJoinPool forkPool = new ForkJoinPool();
        int numThds = 0;
		int numRows = simulationGrid.getRows();


		tick();

		forkPool.invoke(new ParallelGrid(simulationGrid, 0, numRows));
		
		tock();; // End timer

        // Output the results
        System.out.println("Simulation complete, writing image...");
        simulationGrid.gridToImage(outputFileName);
        System.out.printf("\t Rows: %d, Columns: %d\n", simulationGrid.getRows(), simulationGrid.getColumns());
        System.out.printf("Number of steps to stable state: %d \n", numThds);
        System.out.printf("Time: %d ms\n", AutomatonSimulation.endTime - AutomatonSimulation.startTime);
    }

}
