import java.util.ArrayList;

/**
 * State representation interface.
 *
 * @author sna4
 * @version 04/12/2019
 */
public interface State {

    /**
     * Finds all legal moves from the current state.
     *
     * @return ArrayList of moves.
     */
    ArrayList<Move> findAllMoves();

    /**
     * Tests whether the game is over.
     *
     * @return true if game is over, false otherwise.
     */
    boolean gameOver();

    /**
     * Returns the value of an end-game state. Throws an exception if the
     * current state is not an end-game.
     *
     * @return 1 for a win, -1 for a loss.
     */
    int getValue();

    /**
     * Returns whether it is the human player's turn or not.
     *
     * @return true if it is the human player's turn.
     */
    boolean isPlayerTurn();

    /**
     * Tests whether a move is legal and performs it if so.
     *
     * @param m Move The move to be done.
     *
     * @return true if move was legal, false otherwise.
     */
    boolean doMove(Move m);

    /**
     * Undoes the effects of the given move.
     *
     * @param m Move The move to be undone.
     */
    void undoMove(Move m);


    /**
     * Inner class to represent a move.
     */
    interface Move {
    }
}