/************-----------------------------------------------************************
 * From CS61B-sp24-hw02, Percolation class for Monte Carlo simulation.
 * @author Guinsoo
 *************-------------------------------------------------********************/

import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    private int gridSize;
    private int virtualTop;
    private int virtualBottom;
    private int openSites;

    /**
     * 2D array to track the state of each site.
     * sites[row][col] = true if site is open, false if blocked.
     */
    private boolean[][] sites;

    /**
     * Primary union-find data structure.
     * Used to determine if system percolates by checking if virtualTop and virtualBottom are connected.
     */
    private WeightedQuickUnionUF uf;

    /**
     * Secondary union-find data structure without virtual bottom connection.
     * Used to prevent backwash when checking if a site is full.
     * Only connects sites upward to virtualTop, not downward to virtualBottom.
     */
    private WeightedQuickUnionUF antiBackwash;


    /**
     * Creates square grid, with all sites blocked.
     * The row and column indices are integers between 0 and n − 1,
     * where (0, 0) is the upper-left site
     * @param N siz of the gird side
     */
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N must be a positive integer");
        }
        // Initialize instance variables
        gridSize = N;
        virtualTop = N * N;
        virtualBottom = N * N + 1;
        openSites = 0;

        sites = new boolean[gridSize][gridSize];
        uf = new WeightedQuickUnionUF(gridSize * gridSize + 2);
        antiBackwash = new WeightedQuickUnionUF(gridSize * gridSize + 1);
    }


    /**
     * Opens a site at the specified row and column if it is not already open.
     * When a site is opened, it may connect to its adjacent open sites (up, down, left, right).
     * The row and column indices are integers between 0 and N-1.
     * @param row the row index of the site to open
     * @param col the column index of the site to open
     */
    public void open(int row, int col) {
        if (row < 0 || row >= gridSize || col < 0 || col >= gridSize) {
            throw new IndexOutOfBoundsException("row and col must be in range [0, " + (gridSize - 1) + "]");
        }

        // Basic open
        if (sites[row][col]) {
            return;
        }
        sites[row][col] = true;
        openSites++;

        // If site is in top row, connect to virtual top site
        if (row == 0) {
            uf.union(xyTo1D(row, col), virtualTop);
            antiBackwash.union(xyTo1D(row, col), virtualTop);
        }

        // If site is in bottom row, connect to virtual bottom site (only in main UF)
        if (row == gridSize - 1) {
            uf.union(xyTo1D(row, col), virtualBottom);
        }

        // Connect to adjacent open sites
        // Upper
        if (row > 0 && sites[row-1][col]) {
            uf.union(xyTo1D(row, col), xyTo1D(row-1, col));
            antiBackwash.union(xyTo1D(row, col), xyTo1D(row-1, col));
        }
        // Lower
        if (row < gridSize-1 && sites[row+1][col]) {
            uf.union(xyTo1D(row, col), xyTo1D(row+1, col));
            antiBackwash.union(xyTo1D(row, col), xyTo1D(row+1, col));
        }
        // Left
        if (col > 0 && sites[row][col-1]) {
            uf.union(xyTo1D(row, col), xyTo1D(row, col-1));
            antiBackwash.union(xyTo1D(row, col), xyTo1D(row, col-1));
        }
        // Right
        if (col < gridSize-1 && sites[row][col+1]) {
            uf.union(xyTo1D(row, col), xyTo1D(row, col+1));
            antiBackwash.union(xyTo1D(row, col), xyTo1D(row, col+1));
        }

    }
    /**
     * Helper method that converts 2D coordinates to 1D index for use with Union-Find data structure.
     */
    private int xyTo1D(int row, int col) {
        return row * gridSize + col;
    }

    /**
     * Checks if a site at specified row and column is open.
     * @param row
     * @param col
     * @return true if the site is open, false otherwise
     */
    public boolean isOpen(int row, int col) {
        if (row < 0 || row >= gridSize || col < 0 || col >= gridSize) {
            throw new IndexOutOfBoundsException("row and col must be in range [0, " + (gridSize - 1) + "]");
        }

        // Return the status of the site
        return sites[row][col];
    }

    /**
     * Checks if a site at specified row and column is full.
     * A full site is an open site that can be connected to an open site
     * in the top row via a chain of neighboring (left, right, up, down) open sites.
     * @param row
     * @param col
     * @return true if the site is full, false otherwise
     */
    public boolean isFull(int row, int col) {
        if (row < 0 || row >= gridSize || col < 0 || col >= gridSize) {
            throw new IndexOutOfBoundsException("row and col must be in range [0, " + (gridSize - 1) + "]");
        }
        if (!isOpen(row, col)) {
            return false;
        }
        // Use antiBackwash to check if the site is connected to the virtual top
        return antiBackwash.connected(xyTo1D(row, col), virtualTop);
    }

    /**
     * Returns the number of open sites in the grid.
     * @return the number of sites that are currently open
     */
    public int numberOfOpenSites() {
        return openSites;
    }


    /**
     * Checks if the system percolates.
     * The system percolates if there is a path of open sites from top to bottom,
     * @return true if the system percolates, false otherwise
     */
    public boolean percolates() {
        // Special case for 1x1 grid
        if (gridSize == 1) {
            return isOpen(0, 0);
        }

        // Check if virtual top is connected to virtual bottom
        return uf.connected(virtualTop, virtualBottom);
    }


}
