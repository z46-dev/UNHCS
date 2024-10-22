import java.util.Scanner;
import java.util.Stack;

/**
 * @author Evan Parker
 * @version v0.0.1
 */
public class Postfix {
    /**
     * Main method.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String expr = null;

        System.out.println("Enter a postfix expression");
        if (in.hasNextLine()) {
            expr = in.nextLine();
            Integer val = Postfix.evaluateExpression(expr);

            if (val != null) {
                System.out.printf("Expression: %s --> %d\n", expr, val);
            }
        }

        in.close();
    }

    /**
     * Check if the input is a valid operator.
     * @param input The input to check
     * @return The operator if valid, '?' otherwise
     */
    public static char checkValidOperator(String input) {
        switch (input) {
            case "+":
            case "-":
            case "*":
            case "/":
                return input.charAt(0);
            default:
                return '?';
        }
    }

    /**
     * Execute the operation on the stack.
     * @param stack The stack to operate on
     * @param operator The operator to use
     * @return The result of the operation
     */
    public static int execute(Stack<Integer> stack, char operator) {
        int operand2 = stack.pop();
        int operand1 = stack.pop();
        int result = 0;

        switch (operator) {
            case '+':
                result = operand1 + operand2;
                break;
            case '-':
                result = operand1 - operand2;
                break;
            case '*':
                result = operand1 * operand2;
                break;
            case '/':
                result = operand1 / operand2;
                break;
        }

        stack.push(result);
        return result;
    }

    /**
     * Evaluate the expression.
     * @param expr The expression to evaluate
     * @return The result of the expression
     */
    public static Integer evaluateExpression(String expr) {
        Scanner reader = new Scanner(expr);
        Stack<Integer> stack = new Stack<Integer>();

        String tooFewOperands = "Too few operands";
        String unknownOperator = "Unknown operator: ";
        String tooManyOperands = "Too many operands.";
        String errMsg = null;

        while (reader.hasNext() && errMsg == null) {
            if (reader.hasNextInt()) {
                // Stack.push returns what was added, so we can skip the variable assignment
                System.out.printf("Operand read: %d\n", stack.push(reader.nextInt()));
                System.out.println("------ Stack state -----");
                System.out.println(stack);
                continue;
            }

            String token = reader.next();
            char operator = Postfix.checkValidOperator(token);

            if (operator == '?') {
                errMsg = unknownOperator + token;
                System.out.println("------ Stack state -----");
                System.out.println(stack);
                break;
            }

            System.out.printf("Operator read: %c\n", operator);

            if (stack.size() < 2) {
                errMsg = tooFewOperands;
                System.out.println("------ Stack state -----");
                System.out.println(stack);
                break;
            }

            Postfix.execute(stack, operator);
            System.out.println("------ Stack state -----");
            System.out.println(stack);
        }

        reader.close();

        if (errMsg != null) {
            System.out.printf("Failed evaluation of |%s|\n%s\n", expr, errMsg);
            return null;
        }

        if (stack.size() != 1) {
            System.out.printf("Failed evaluation of |%s|\n%s%s\n", expr, tooManyOperands, stack);
            return null;
        }

        return stack.peek();
    }
}