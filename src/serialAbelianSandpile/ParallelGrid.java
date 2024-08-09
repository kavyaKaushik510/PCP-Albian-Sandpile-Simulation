package serialAbelianSandpile;

import serialAbelianSandpile.Grid;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

/**
 * Parallel version of the Grid class using Fork/Join framework with quadrant splitting.
 * This approach recursively splits the grid into four quadrants.
 */
public class ParallelGrid extends Grid {

    private static final int THRESHOLD = 3000; // Threshold to decide when to stop splitting

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
        boolean result = pool.invoke(new UpdateTask(1, 1, getRows(), getColumns()));
        pool.shutdown();

        if (result) {
            nextTimeStep();
        }
        return result;
    }

    private class UpdateTask extends RecursiveTask<Boolean> {
        int startRow, startCol, endRow, endCol;

        UpdateTask(int startRow, int startCol, int endRow, int endCol) {
            this.startRow = startRow;
            this.startCol = startCol;
            this.endRow = endRow;
            this.endCol = endCol;
        }

        @Override
        protected Boolean compute() {
            int rows = endRow - startRow + 1;
            int cols = endCol - startCol + 1;

            // If the grid segment is small enough, process it sequentially
            if (rows * cols <= THRESHOLD) {
                return updateSegment();
            } else {
                // Split the grid into four quadrants
                int midRow = (startRow + endRow) / 2;
                int midCol = (startCol + endCol) / 2;

                UpdateTask task1 = new UpdateTask(startRow, startCol, midRow, midCol); // Top-left
                UpdateTask task2 = new UpdateTask(startRow, midCol + 1, midRow, endCol); // Top-right
                UpdateTask task3 = new UpdateTask(midRow + 1, startCol, endRow, midCol); // Bottom-left
                UpdateTask task4 = new UpdateTask(midRow + 1, midCol + 1, endRow, endCol); // Bottom-right

                // Fork three tasks and compute one directly
                task1.fork();
                task2.fork();
                task3.fork();
                boolean result4 = task4.compute();

                // Join the results of the other three tasks
                boolean result1 = task1.join();
                boolean result2 = task2.join();
                boolean result3 = task3.join();

                return result4 || result1 || result2 || result3;
            }
        }

        private boolean updateSegment() {
            boolean change = false;

            for (int i = startRow; i <= endRow; i++) {
                for (int j = startCol; j <= endCol; j++) {
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
