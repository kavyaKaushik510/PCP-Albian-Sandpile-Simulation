//Citation: M.M.Kuttel 2024 CSC2002S, UCT: Grid
//Grid file MODIFIED BY: Kavya Kaushik, KSHKAV001
//ParallelGrid

package serialAbelianSandpile;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;


/**
 * Parallel Grid class extends the Grid class to provide a parllel implementation.
 * It utilises the fork/join framework
 * It calls super methods to the Grid class. The mthods are defined in the Grid class.
 */

public class ParallelGrid extends Grid {

   private static final int THRESHOLD = 2000;

    public ParallelGrid(int w, int h) {
        super(w, h);
    }

    public ParallelGrid(int[][] newGrid) {
        super(newGrid);
    }

    public ParallelGrid(Grid copyGrid) {
        super(copyGrid);
    }

    /**
     * Updates teh grid using parallel processing as required.
     * @return true if any changes made to the grid; false if unchanged.
     */
  //  @Override
    public boolean update() {
    boolean result = ForkJoinPool.commonPool().invoke(new UpdateTask(1, 1, getRows(), getColumns()));

        if (result) {
            swapGrids();
        }
        return result;
    }

    /**
     * A recurive task to update a segment of the grid
     */

    private class UpdateTask extends RecursiveTask<Boolean> {
        int startRow, startCol, endRow, endCol;

        /**
         * Constructs an UpdateTask with given grid boundaries.
         *
         * @param startRow The starting row of the segment.
         * @param startCol The starting column of the segment.
         * @param endRow   The ending row of the segment.
         * @param endCol   The ending column of the segment.
         */

        UpdateTask(int startRow, int startCol, int endRow, int endCol) {
            this.startRow = startRow;
            this.startCol = startCol;
            this.endRow = endRow;
            this.endCol = endCol;
        }

        /**
         * The main parallel algorithm
         */
        @Override
        protected Boolean compute() {
            int rows = endRow - startRow + 1;
            int cols = endCol - startCol + 1;

            //check grid size
            if (rows * cols <= THRESHOLD) {

                //process grid sequentially
                return updateSegment();
            } else {

                int midRow = (startRow + endRow) / 2;
                int midCol = (startCol + endCol) / 2;

                //Create recusive tasks for each segment of the grid

                UpdateTask task1 = new UpdateTask(startRow, startCol, midRow, midCol);
                UpdateTask task2 = new UpdateTask(startRow, midCol + 1, midRow, endCol);
                UpdateTask task3 = new UpdateTask(midRow + 1, startCol, endRow, midCol);
                UpdateTask task4 = new UpdateTask(midRow + 1, midCol + 1, endRow, endCol);

                // fork all the tasks and wait for them to join and complete.
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

        /**
         * Sequentially updates a portion of the grid and checks if there are any changes made.
         * @return true if grid changes, false otherwise.
         */

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
