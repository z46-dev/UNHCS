import java.util.Scanner;
import java.util.Stack;

public class Reader {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a string: ");
        String input = scanner.nextLine();

        char[] opens = {'(', '[', '{', '<'};
        char[] closes = {')', ']', '}', '>'};

        Stack<Character> stack = new Stack<Character>();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == ' ' || c == '\t') {
                continue;
            }

            for (int j = 0; j < opens.length; j++) {
                if (c == opens[j]) {
                    stack.push(c);
                    break;
                }
            }

            for (int j = 0; j < closes.length; j++) {
                if (c == closes[j]) {
                    if (stack.isEmpty()) {
                        System.out.println("Unbalanced: " + c);
                        return;
                    }

                    char top = stack.pop();
                    if (top != opens[j]) {
                        System.out.println("Unbalanced: " + c);
                        return;
                    }
                }
            }
        }
    }
}