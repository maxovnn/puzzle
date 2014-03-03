public class Board {
	private char[] blocks;
	private final char N;
	private final int internalN;
	private final boolean isGoal;
	private final char hamming;
	private final int manhattan;
	
    /**
     * construct a board from an N-by-N array of blocks
     * (where blocks[i][j] = block in row i, column j)
     * @param blocks
     */
	public Board(int[][] blocks) {
        N = (char)blocks.length;
        internalN = N*N;
        this.blocks = new char[N];

        boolean isgoal = true;
        int manhattanCounter = 0;
        char hammingCounter = 0;
        char block;
        char shift;
        for (char ii = 0; ii < N; ii++) {
            for (char jj = 0; jj < N; jj++) {
                block = (char)blocks[ii][jj];
                put(ii, jj, block);
                if (block != (ii*N+jj+1)) {
                    isgoal = false;
                    if (block != 0) {
                        hammingCounter++;
                        shift = (char) (block/N);
                        // column
                        manhattanCounter += Math.abs(ii-shift); 
                        // row
                        manhattanCounter += Math.abs(jj-(block - shift*N)); 
                    }
                }
            }
        }
        isGoal = isgoal;
        hamming = hammingCounter;
        manhattan = manhattanCounter;
    }

    private void put(char i, char j, char e) {
        blocks[(i) * N + j] = e;
    }
    
    private char get(char i, char j) {
        return blocks[(i) * N + j];
    }
    
	/**
	 * board dimension N
	 * @return
	 */
    public int dimension() {
    	return N;
    }
    
    /**
     * @return number of blocks out of place
     */
    public int hamming() {
    	return hamming;
    }
    
    /**
     * @return sum of Manhattan distances between blocks and goal
     */
    public int manhattan() {
    	return manhattan;
    }

    /**
     * @return is this board the goal board?
     */
    public boolean isGoal() {
    	return isGoal;
    }
    
    /**
     * @return a board obtained by exchanging two adjacent blocks in the same row
     */
    public Board twin() {
    	return null;
    }
    
    /**
     * @return does this board equal y?
     */
    public boolean equals(Object y) {
    	return false;
    }
    
    /**
     * @return all neighboring boards
     */
    public Iterable<Board> neighbors() {
    	return null;
    }
    
    /**
     * @return string representation of the board (in the output format specified below)
     */
    public String toString() {
    	return null;
    }
}