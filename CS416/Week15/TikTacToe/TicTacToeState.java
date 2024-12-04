import java.util.ArrayList;

/**
 * Game State for TicTacToe.
 * 
 * @author cs416
 * @version 1
 */
public class TicTacToeState implements State {

    String[][] board;
    boolean playerTurn;

    /**
     * Default constructor. Creates a starting game state.
     * Computer will be X, and player will be O.
     *
     * @param turn Indicates whether it is the player's turn first.
     */
    public TicTacToeState(boolean turn) {
        board = new String[3][3];
        this.playerTurn = turn;
    }

    /**
     * Copy constructor. Creates a new state by
     * copying the values in the board parameter.
     * Computer will be X, and player will be O.
     *
     * @param board The current game board to be copied.
     * @param turn  Indicates whether it is the player's turn in this state.
     */
    public TicTacToeState(String[][] board, boolean turn) {
        this.board = new String[3][3];

        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                this.board[r][c] = board[r][c];
            }
        }

        this.playerTurn = turn;
    }

    /**
     * Returns the mark for the player whose turn it is in this state.
     *
     * @return "O" if playerTurn is true, "X" otherwise.
     */
    public String getMark() {
        return playerTurn ? "O" : "X";
    }

    /**
     * Returns the board for this state.
     *
     * @return The board.
     */
    public String[][] getBoard() {
        return board;
    }

    /**
     * Returns whether it is the human player's turn or not.
     *
     * @return true if it is the human player's turn. (The current turn is "O".)
     */
    public boolean isPlayerTurn() {
        return playerTurn;
    }

    /**
     * Returns a string representation of this state.
     *
     * @return The string representing this state.
     */
    public String toString() {
        String ret = "";
        String bar = " -------------\n";
        ret += bar;

        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (board[r][c] == null) {
                    ret += " |  ";
                } else {
                    ret += " | " + board[r][c];
                }
            }
            ret += " |\n";
            ret += bar;
        }

        return ret;
    }

    /**
     * Finds all legal moves from the current state.
     *
     * @return ArrayList of moves.
     */
    public ArrayList<State.Move> findAllMoves() {
        ArrayList<State.Move> moves = new ArrayList<State.Move>();

        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (board[r][c] == null) {
                    moves.add(new Move(r, c));
                }
            }
        }

        return moves;
    }

    /**
     * Tests whether the game is over.
     *
     * @return true if game is over, false otherwise.
     */
    public boolean gameOver() {
        for (int i = 0; i < board.length; i++) {
            if (board[i][0] != null && board[i][0].equals(board[i][1]) && board[i][0].equals(board[i][2])) {
                return true;
            }

            if (board[0][i] != null && board[0][i].equals(board[1][i]) && board[0][i].equals(board[2][i])) {
                return true;
            }
        }

        if (board[0][0] != null && board[0][0].equals(board[1][1]) && board[0][0].equals(board[2][2])) {
            return true;
        }

        if (board[0][2] != null && board[0][2].equals(board[1][1]) && board[0][2].equals(board[2][0])) {
            return true;
        }

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == null) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Returns the value of an end-game state. Throws a new IllegalStateException if
     * the
     * current state is not an end-game.
     *
     * @return 1 for a win for X, -1 for a loss.
     */
    public int getValue() {
        if (!gameOver()) {
            throw new IllegalStateException("Game is not over.");
        }

        for (int i = 0; i < board.length; i++) {
            if (board[i][0] != null && board[i][0].equals(board[i][1]) && board[i][0].equals(board[i][2])) {
                return board[i][0].equals("X") ? 1 : -1;
            }

            if (board[0][i] != null && board[0][i].equals(board[1][i]) && board[0][i].equals(board[2][i])) {
                return board[0][i].equals("X") ? 1 : -1;
            }
        }

        if (board[0][0] != null && board[0][0].equals(board[1][1]) && board[0][0].equals(board[2][2])) {
            return board[0][0].equals("X") ? 1 : -1;
        }

        if (board[0][2] != null && board[0][2].equals(board[1][1]) && board[0][2].equals(board[2][0])) {
            return board[0][2].equals("X") ? 1 : -1;
        }

        return 0;
    }

    /**
     * Tests whether a move is legal and performs it if so.
     *
     * @param m Move The move to be done.
     * @return true if move was legal, false otherwise.
     */
    public boolean doMove(State.Move m) {
        Move move = (Move) m;

        if (
                move.r < 0
                || 
                move.r >= board.length 
                ||
                move.c < 0 
                ||
                move.c >= board[0].length
                ||
                board[move.r][move.c] != null
        ) {
            return false;
        }

        board[move.r][move.c] = getMark();
        playerTurn = !playerTurn;

        return true;
    }

    /**
     * Undoes the effects of the given move.
     *
     * @param m Move The move to be undone.
     */
    public void undoMove(State.Move m) {
        Move move = (Move) m;

        board[move.r][move.c] = null;
        playerTurn = !playerTurn;
    }

    /**
     * Inner class to represent a move.
     */
    public class Move implements State.Move {

        int r;
        int c;

        /**
         * Default constructor.
         * 
         * @param r Radius
         * @param c Column
         */
        public Move(int r, int c) {
            this.r = r;
            this.c = c;
        }

        /**
         * Returns a string representation of this move.
         *
         * @return The string representing this move.
         */
        public String toString() {
            return "row " + r + " column " + c;
        }

        /**
         * Determine whether this move is equal to another object.
         *
         * @param o Object for comparing
         * @return true if all data from the move matches, false otherwise.
         */
        public boolean equals(Object o) {
            if (o instanceof Move) {
                Move m = (Move) o;

                return m.r == r && m.c == c;
            }

            return false;
        }
    }
}