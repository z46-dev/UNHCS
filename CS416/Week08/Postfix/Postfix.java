import java.util.Stack;

public class Postfix {
    public static void main(String[] args) {
        
    }

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

    public static int evaluateExpression(String expr) {
        Stack<Integer> stack = new Stack<>();
        String[] tokens = expr.split(" ");

        for (String token : tokens) {
            char operator = checkValidOperator(token);
            if (operator != '?') {
                execute(stack, operator);
            } else {
                stack.push(Integer.parseInt(token));
            }
        }

        return stack.pop();
    }
}