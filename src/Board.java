import java.util.Arrays;
import java.util.Iterator;

public class Board {
    private short[] blocks;
    private final short N;
    private final short internalN;
    private final int hamming;
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

        int manhattanCounter = 0;
        int hammingCounter = 0;
        short block;
        for (int ii = 0; ii < N; ii++) {
            for (int jj = 0; jj < N; jj++) {
                block = (short)blocks[ii][jj];
                put(ii, jj, block);
                if (block != (ii*N+jj+1)) {
                    if (block != 0) {
                        hammingCounter++;
                        manhattanCounter += calcManhattanDist(ii, jj, N, block); 
                    }
                }
            }
        }
        hamming = hammingCounter;
        manhattan = manhattanCounter;
    }

    private static void exch(short[] blocks, int i, int j) {
        short tmp = blocks[i];
        blocks[i] = blocks[j];
        blocks[j] = tmp;
    }

    private Board(Board board, int i, int j, int iN, int jN) {
        N = board.N;
        internalN = board.internalN;
        this.blocks = new short[internalN];

        int manhattanCounter = board.manhattan;
        int hammingCounter = board.hamming;
        this.blocks = Arrays.copyOf(board.blocks, internalN);
        if (N >1) {
            int[] result = move(this.blocks, N, i, j , iN, jN, board.hamming, board.manhattan);
            hammingCounter = result[0];
            manhattanCounter = result[1];
        }

        this.hamming = hammingCounter;
        this.manhattan = manhattanCounter;
    }

    private static int calcManhattanDist(int i, int j, int dimension, short target) {
        int shift = target/(dimension+1);
        int manhattenCounter = Math.abs(i-shift);
        manhattenCounter += Math.abs(j-(target - shift*(dimension) - 1));
        return manhattenCounter;
    }
    
    private static int[] move(short[] blocks, int dimension, int i, int j, int iN, int jN, int hamming, int manhattan) {
        int manhattanMod = manhattan;
        int hammingMod = hamming;
        int arrayIndOld = (i) * blocks.length + j; 
        int arrayIndNew = (iN) * blocks.length + jN;

        // simple exchange
        exch(blocks, arrayIndOld, arrayIndNew);

        if (hamming == 0) {
            hammingMod = 2;
            manhattanMod = 2;
        } else {
            if (blocks[arrayIndNew] == (arrayIndNew+1)) {
                hammingMod--;
                manhattanMod--;
            } else {
                if (blocks[arrayIndNew] == (arrayIndOld+1)) {
                    hammingMod++;
                }
                manhattanMod -= calcManhattanDist(i, j, dimension, blocks[arrayIndNew]);
                manhattanMod += calcManhattanDist(iN, jN, dimension, blocks[arrayIndNew]);
            }
            if (blocks[arrayIndOld] == (arrayIndOld+1)) {
                hammingMod--;
                manhattanMod--;
            } else {
                if (blocks[arrayIndOld] == (arrayIndNew+1)) {
                    hammingMod++;
                }
                hammingMod++;
                manhattanMod -= calcManhattanDist(iN, jN, blocks.length, blocks[arrayIndOld]);
                manhattanMod += calcManhattanDist(i, j, blocks.length, blocks[arrayIndOld]);
            }
        }
        return new int[]{hammingMod, manhattanMod};
    }
    
    private void put(int i, int j, short e) {
        blocks[(i) * N + j] = e;
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
    	return hamming == 0;
    }

    /**
     * @return a board obtained by exchanging two adjacent blocks in the same row
     */
    public Board twin() {
    	return new Board(this, 0, 0, 0, 1);
    }

    /**
     * @return does this board equal y?
     */
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }

        Board b = (Board) y;
        if (this.N != b.N) {
            return false;
        } else if (this.hamming != b.hamming) {
        	return false;
        } else if (this.manhattan != b.manhattan) {
        	return false;
        }

        return Arrays.equals(this.blocks, b.blocks);
    }

    /**
     * @return all neighboring boards
     */
    public Iterable<Board> neighbors() {
    	return new BoardItterable();
    }

    private class BoardItterable implements Iterable<Board> {

        @Override
        public Iterator<Board> iterator() {
            // TODO Auto-generated method stub
            return null;
        }

        private class BoardIterator implements Iterator<Board> {

            @Override
            public boolean hasNext() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public Board next() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void remove() {
                // TODO Auto-generated method stub
                
            }
            
        }

        
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
        In in = new In("8puzzle/puzzle05.txt");
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board board = new Board(blocks);
        StdOut.println(board);
        StdOut.println("manhattan - " + board.manhattan());
        StdOut.println("hamming   - " + board.hamming());
        StdOut.println("--------");
        Board board1 = board.twin();
        StdOut.println(board1);
        StdOut.println("manhattan - " + board1.manhattan());
        StdOut.println("hamming   - " + board1.hamming());
    }
}