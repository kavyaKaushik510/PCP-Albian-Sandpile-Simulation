package serialAbelianSandpile;

import serialAbelianSandpile.Grid;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

class ParallelGrid extends Grid{

    private static final int THRESHOLD = 100;

    //private int firstRow, rowCount, columnCount;
    //private Grid grid;
    //private boolean change;

    public ParallelGrid(int w, int h) {
        super(w, h);
    }

    public ParallelGrid(int[][] newGrid) {
        super(newGrid);
    }

    public ParallelGrid(Grid copyGrid) {
        super(copyGrid);
    }

    @Override
    public boolean update() {
        ForkJoinPool pool = new ForkJoinPool();
        boolean result = pool.invoke(new UpdateTask(1, getRows()));
        pool.shutdown();
        if (result) {
            nextTimeStep();
        }
        return result;
    }

    private class UpdateTask extends RecursiveTask<Boolean> {
    int startRow, endRow;

    UpdateTask(int startRow, int endRow) {
        this.startRow = startRow;
        this.endRow = endRow;
    }

    @Override
    protected Boolean compute() {
        if ((endRow - startRow) <= THRESHOLD) {
            return updateRows();
        } else {
            int midRow = (startRow + endRow) / 2;
            UpdateTask leftTask = new UpdateTask(startRow, midRow);
            UpdateTask rightTask = new UpdateTask(midRow + 1, endRow);
            
            // Fork the left task and compute the right task in the current thread
            leftTask.fork();
            boolean rightResult = rightTask.compute();

            // Join the left task result
            boolean leftResult = leftTask.join();

            // Return true if any of the sub-tasks resulted in a change
            return leftResult || rightResult;
        }
    }


    private boolean updateRows() {
        boolean change = false;
        for (int i = startRow; i <= endRow; i++) {
            for (int j = 1; j < getColumns() + 1; j++) {
                updateGrid[i][j] = (grid[i][j] % 4) +
                        (grid[i - 1][j] / 4) +
                        (grid[i + 1][j] / 4) +
                        (grid[i][j - 1] / 4) +
                        (grid[i][j + 1] / 4);
                if (grid[i][j] != updateGrid[i][j]) {
                    change = true;
                }
            }
        }
        return change;
    }
}
}
