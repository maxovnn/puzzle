public class Board {
	private short[] blocks;
	private final short N;
	private final short internalN;
	private final boolean isGoal;
	private final short hamming;
	private final int manhattan;
	
    /**
     * construct a board from an N-by-N array of blocks
     * (where blocks[i][j] = block in row i, column j)
     * @param blocks
     */
	public Board(int[][] blocks) {
        N = (short) blocks.length;
        internalN = (short) (N*N);
        this.blocks = new short[internalN];

        boolean isgoal = true;
        int manhattanCounter = 0;
        short hammingCounter = 0;
        short block;
        short shift;
        for (short ii = 0; ii < N; ii++) {
            for (short jj = 0; jj < N; jj++) {
                block = (short)blocks[ii][jj];
                put(ii, jj, block);
                if (block != (ii*N+jj+1)) {
                    isgoal = false;
                    if (block != 0) {
                        hammingCounter++;
                        shift = (short) ((block)/(N+1));
                        // column
                        manhattanCounter += Math.abs(ii-shift); 
                        // row
                        manhattanCounter += Math.abs(jj-(block - shift*(N) - 1)); 
                    }
                }
            }
        }
        isGoal = isgoal;
        hamming = hammingCounter;
        manhattan = manhattanCounter;
    }

    private void put(short i, short j, short e) {
        blocks[(i) * N + j] = e;
    }
    
    private short get(short i, short j) {
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
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (short ii = 0; ii < internalN; ii++) {
            s.append(String.format("%h ", blocks[ii]));
            if ((ii+1)%(N) == 0) {
                s.append("\n");
            }
        }
        return s.toString();   
     }
    public static void main(String[] args) {
        In in = new In("8puzzle/puzzle04.txt");
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board board = new Board(blocks);
        StdOut.println(board);
    }
}