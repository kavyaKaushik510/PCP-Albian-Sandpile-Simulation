package serialAbelianSandpile;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class ParallelGrid extends Grid {

    private static final int THRESHOLD = 3000;
    //extra
    ForkJoinPool pool;

    public ParallelGrid(int w, int h, ForkJoinPool pool) {
        super(w, h);
        this.pool = pool;
    }

    public ParallelGrid(int[][] newGrid, ForkJoinPool pool) {
        super(newGrid);
        this.pool = pool;
    }

    public ParallelGrid(Grid copyGrid, ForkJoinPool pool) {
        super(copyGrid);
        this.pool = pool;
    }

  //  @Override
    public boolean update() {
        // Use the common ForkJoinPool instead of creating a new one
    boolean result = pool.invoke(new UpdateTask(1, 1, getRows(), getColumns()));
    //ForkJoinPool customPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
       
        if (result) {
            swapGrids();
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

            if (rows * cols <= THRESHOLD) {
                return updateSegment();
            } else {
                int midRow = (startRow + endRow) / 2;
                int midCol = (startCol + endCol) / 2;

                UpdateTask task1 = new UpdateTask(startRow, startCol, midRow, midCol);
                UpdateTask task2 = new UpdateTask(startRow, midCol + 1, midRow, endCol);
                UpdateTask task3 = new UpdateTask(midRow + 1, startCol, endRow, midCol);
                UpdateTask task4 = new UpdateTask(midRow + 1, midCol + 1, endRow, endCol);

                task1.fork();
                task2.fork();
                task3.fork();
                boolean result4 = task4.compute();

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
