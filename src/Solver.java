public class Solver {
    private MinPQ<Element> queue;
    private MinPQ<Element> queueCheck;
    private Element gameList;
    private boolean isSolvable;

    private class Element implements Comparable<Element> {
        final Element prev;
        final Board board;
        final int move;
        final int priority;
        Element(Element prev, Board board, int move) {
            this.prev = prev;
            this.board = board;
            this.move = move;
            this.priority = move + board.manhattan();
        }
        @Override
        public int compareTo(Element that) {
            return this.priority - that.priority;
        }
    }
    
    private boolean isAlreadyInTheGameTree(Element gameTree, Board board) {
        Element elem = gameTree;
        do {
            if (board.equals(elem.board)) {
                return true;
            }
            elem = elem.prev;
        } while (elem != null);
        return false;
    }

    /**
     * find a solution to the initial board (using the A* algorithm)
     * @param initial
     */
    public Solver(Board initial) {
        if (initial.isGoal()) {
            gameList = new Element(null, initial, 0);
            isSolvable = true;
            return;
        }
        queue = new MinPQ<Element>();
        queueCheck = new MinPQ<Element>();

        queue.insert(new Element(null, initial, 0));
        queueCheck.insert(new Element(null, initial.twin(), 0));
        while (true) {
            Element element = queue.delMin();
            for (Board board: element.board.neighbors()) {
                if (board.isGoal()) {
                    gameList = new Element(element, board, element.move + 1);
                    queueCheck = null;
                    queue = null;
                    isSolvable = true;
                    return;
                }
                if (!isAlreadyInTheGameTree(element, board)) {
                    Element elem = new Element(element, board, element.move + 1);
                    queue.insert(elem);
//                    StdOut.println(" " + board);
//                    StdOut.println("priority : " + elem.priority);
//                    StdOut.println("moves    : " + elem.move);
//                    StdOut.println("manhattan: " + board.manhattan());
//                    StdOut.println("hamming  : " + board.hamming());
                }
            }
//            StdOut.println("--------");
            element = queueCheck.delMin();
            for (Board board: element.board.neighbors()) {
                if (board.isGoal()) {
                    isSolvable = false;
                    gameList = null;
                    queueCheck = null;
                    queue = null;
                    return;
                }
                if (!isAlreadyInTheGameTree(element, board)) {
                    Element elem = new Element(element, board, element.move + 1);
                    queueCheck.insert(elem);
                }
            }
        }
    }
    
    /**
     * @return is the initial board solvable?
     */
    public boolean isSolvable() {
        return isSolvable;
    }
    
    /**
     * @return min number of moves to solve initial board; -1 if no solution
     */
    public int moves() {
        return isSolvable? gameList.move: -1;
    }

    /**
     * @return sequence of boards in a shortest solution; null if no solution
     */
    public Iterable<Board> solution() {
        if (isSolvable) {
            Stack<Board> stack = new Stack<Board>();
            Element elem = gameList;
            do {
                stack.push(elem.board);
                elem = elem.prev;
            } while (elem != null);
            return stack;
        }
        return null;
    }

    /**
     * solve a slider puzzle (given below)
     * @param args
     */
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
//        In in = new In("8puzzle/puzzle4x4-78.txt");
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }    
    }
}