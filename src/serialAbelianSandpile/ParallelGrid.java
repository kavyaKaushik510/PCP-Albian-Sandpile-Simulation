package serialAbelianSandpile;

import serialAbelianSandpile.Grid;
import java.util.concurrent.RecursiveAction;

class ParallelGrid extends RecursiveAction{

    private static final int THRESHOLD = 5;//number of cores
    private int firstRow, rowCount, columnCount;
    private Grid grid;
    //private int cores;

    public ParallelGrid(Grid Grid, int firstRow, int rowCount){
        this.grid = grid;
        this.firstRow = firstRow;
        this.rowCount = rowCount;
    }

    protected void compute(){
        if ((rowCount - firstRow)<=grid.getColumns()){
            //process directly for a small task 
            continue;


                

        }else {
        //splitting the task
        int mid = (firstRow + rowCount) / 2;
        ParallelGrid grid1 = new ParallelGrid(grid, firstRow, mid);
        ParallelGrid grid2 = new ParallelGrid(grid,mid, rowCount);
        grid1.fork();
        grid2.compute();
        grid1.join();
        }
    }  
    
}
