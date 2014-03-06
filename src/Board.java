import java.util.Arrays;
import java.util.Iterator;

public class Board {
    private short[] blocks;
    private final short N;
    private final short internalN;
    private final int hamming;
    private final int manhattan;
    private int blankIndexI;
    private int blankIndexJ;

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
                if (block == 0) {
                    blankIndexI = ii;
                    blankIndexJ = jj;
                    continue;
                }
                if (block != (ii*N+jj+1)) {
                    hammingCounter++;
                    manhattanCounter += calcManhattanDist(ii, jj, N, block); 
                }
            }
        }
        hamming = hammingCounter;
        manhattan = manhattanCounter;
    }

    private void exch(short[] blocks, int i, int j) {
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
            int[] result = move(this.blocks, N, i, j, iN, jN, board.hamming, board.manhattan);
            hammingCounter = result[0];
            manhattanCounter = result[1];
        }

        this.hamming = hammingCounter;
        this.manhattan = manhattanCounter;
    }

    private int calcManhattanDist(int i, int j, int dimension, short target) {
        int x = target%dimension;
        int y = target/dimension;
        int manhattenCounter = Math.abs(i-y);
        manhattenCounter += Math.abs(j-x);
        return manhattenCounter;
    }
    
    private int[] move(short[] blocks, int dimension, int i, int j, int iN, int jN, int hamming, int manhattan) {
        int manhattanMod = manhattan;
        int hammingMod = hamming;
        int arrayIndOld = (i) * dimension + j; 
        int arrayIndNew = (iN) * dimension + jN;

        // simple exchange
        exch(blocks, arrayIndOld, arrayIndNew);

        if (hamming == 0) {
            hammingMod = 2;
            manhattanMod = 2;
        } else {
            if (blocks[arrayIndNew] == 0) {
                blankIndexI = iN;
                blankIndexJ = jN;
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
            }
            if (blocks[arrayIndOld] == 0) {
                blankIndexI = i;
                blankIndexJ = j;
            } else {
                if (blocks[arrayIndOld] == (arrayIndOld+1)) {
                    hammingMod--;
                    manhattanMod--;
                } else {
                    if (blocks[arrayIndOld] == (arrayIndNew+1)) {
                        hammingMod++;
                    }
                    manhattanMod -= calcManhattanDist(iN, jN, dimension, blocks[arrayIndOld]);
                    manhattanMod += calcManhattanDist(i, j, dimension, blocks[arrayIndOld]);
                }
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
        if (this == y) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;

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
    	return new BoardItterable(this);
    }

    private class BoardItterable implements Iterable<Board> {
        private final Board board;
        BoardItterable(Board board) {
            this.board = board;
        }

        @Override
        public Iterator<Board> iterator() {
            // TODO Auto-generated method stub
            return new BoardIterator(board);
        }

        private class BoardIterator implements Iterator<Board> {
            private Board[] items;
            private int current;

            BoardIterator(Board board) {
                items = new Board[4];
                final int blankIndexI = board.blankIndexI;
                final int blankIndexJ = board.blankIndexJ;
                if ((board.blankIndexI + 1) != board.N) {
                    items[current++] = new Board(board, blankIndexI, blankIndexJ, blankIndexI+1, blankIndexJ);
                }
                if (blankIndexI != 0) {
                    items[current++] = new Board(board, blankIndexI, blankIndexJ, blankIndexI-1, blankIndexJ);
                }
                if ((blankIndexJ + 1) != board.N) {
                    items[current++] = new Board(board, blankIndexI, blankIndexJ, blankIndexI, blankIndexJ+1);
                }
                if (blankIndexJ != 0) {
                    items[current++] = new Board(board, blankIndexI, blankIndexJ, blankIndexI-1, blankIndexJ-1);
                }
            }

            @Override
            public boolean hasNext() {
                return current > 0;
            }

            @Override
            public Board next() {
                return items[--current];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
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
/*        In in = new In("8puzzle/puzzle04.txt");
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board board = new Board(blocks);
        StdOut.println(board);
        StdOut.println("manhattan - " + board.manhattan());
        StdOut.println("hamming   - " + board.hamming());
        StdOut.println("-------- Twin");
        Board board1 = board.twin();
        StdOut.println(board1);
        StdOut.println("manhattan - " + board1.manhattan());
        StdOut.println("hamming   - " + board1.hamming());
        StdOut.println("-------- Neighbors");
        for (Board neigh: board.neighbors()) {
            StdOut.println(neigh);
            StdOut.println("manhattan - " + neigh.manhattan());
            StdOut.println("hamming   - " + neigh.hamming());
            StdOut.println("--");
        }*/
    }
}