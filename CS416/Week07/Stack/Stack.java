/**
 * The stack class.
 * @author Evan Parker
 * @version v0.0.1s
 */
public class Stack {
    int[] stack = new int[10];
    int index;

    /**
     * Creates an instance of the Stack class and initializes the stack to 0.
     */
    public Stack() {
        this.index = -1;
    }

    /**
     * Checks if the stack is empty.
     * @return whether or not the stack is empty
     */
    public boolean isEmpty() {
        return this.index == -1;
    }

    /**
     * Pops the top value off the stack.
     * @param value the value to push onto the stack
     * @return whether or not the value was successfully pushed onto the stack
     */
    public boolean push(int value) {
        if (this.index == this.stack.length - 1) {
            return false;
        }

        this.stack[++this.index] = value;
        return true;
    }

    /**
     * Pops the top value off the stack.
     * @return the value popped off the stack
     */
    public int pop() {
        if (this.isEmpty()) {
            return -1;
        }

        return this.stack[this.index--];
    }

    /**
     * Peeks at the top value of the stack.
     * @return the value at the top of the stack
     */
    public int peek() {
        if (this.isEmpty()) {
            return -1;
        }

        return this.stack[this.index];
    }

    /**
     * Returns the stack as a string.
     * @return the stack as a string
     */
    public String toString() {
        if (this.isEmpty()) {
            return "";
        }

        String str = "";

        for (int i = this.index; i >= 0; i--) {
            str += this.stack[i] + ", ";
        }

        str = str.substring(0, str.length() - 2);

        return str;
    }
}
