import java.util.ArrayList;
import java.util.Scanner;

/**
* TicTacToe main program.
* @author cs416
* @version 1
*/
public class TicTacToe {

    TicTacToeState curr;

    /**
    * Constructor.
    */
    public TicTacToe() {
        curr = new TicTacToeState(false);
        Scanner sc = new Scanner(System.in);
        while (!curr.gameOver()) {
            System.out.println(curr);
            String mark;
            if (curr.isPlayerTurn()) {
                mark = "O";
            } else {
                mark = "X";
            }
            System.out.println("Choose the next move for " + mark);
            ArrayList<State.Move> moves = curr.findAllMoves();
            for (int i = 0; moves != null && i < moves.size(); i++) {
                System.out.println(i + ": " + moves.get(i).toString());
            }
            int choice = sc.nextInt();
            curr.doMove(moves.get(choice));
        }

        System.out.println(curr);

        if (curr.getValue() == 1) {
            System.out.println("Win for X!");
        } else if (curr.getValue() == -1) {
            System.out.println("Win for O!");
        } else {
            System.out.println("Tie!");
        }

        sc.close();
    }

    /**
    * main functions.
    * @param args Command line arguments
    */
    public static void main(String[] args) {
        new TicTacToe();
    }
}