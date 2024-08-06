package serialAbelianSandpile;

import serialAbelianSandpile.Grid;
import java.util.concurrent.RecursiveAction;

class ParallelGrid extends RecursiveAction{

    private static final int THRESHOLD = 5;//number of cores
    private int start, end;
    private Grid grid;

    public ParallelGrid(Grid Grid, int start, int end){
        this.grid = grid;
        this.start = start;
        this.end = end;
    }

    protected void compute(){
        for (int i = start; i<end; i++){
            for(int j = 1; j< grid.getColumns()+i;j++){
                grid.updateGrid[i][j] = (grid.get(i, j) % 4) +
                                        (grid.get(i - 1, j) / 4) +
                                        (grid.get(i + 1, j) / 4) +
                                        (grid.get(i, j - 1) / 4) +
                                        (grid.get(i, j + 1) / 4);


            }


        }

    }

}