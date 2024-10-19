/**
 * @author Evan
 * @version v0.0.1
 */
public class CheckingAccount implements Account {
    private int balance;
    private String id;
    private String name;

    /**
     * Constructs a new CheckingAccount.
     * @param id String
     * @param name String
     * @param startingBalance int
     */
    public CheckingAccount(String id, String name, int startingBalance) {
        this.id = id;
        this.name = name;
        this.balance = startingBalance;
    }

    /**
     * Adds currency to the balance.
     * @param amount int
     */
    public void deposit(int amount) {
        this.balance += amount;
    }

    /**
     * Attempts to remove currency from the balance.
     * @param amount int
     * @return boolean
     */
    public boolean withdraw(int amount) {
        if (amount > this.balance) {
            return false;
        }

        this.balance -= amount;
        return true;
    }

    /**
     * Getter for id param.
     * @return string
     */
    public String getId() {
        return this.id;
    }

    /**
     * Getter for name param.
     * @return string
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for balance param.
     * @return int
     */
    public int getBalance() {
        return this.balance;
    }

    /**
     * Overwritten toString method.
     * @return string
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.id);
        builder.append(" ");
        builder.append(this.name);
        builder.append(" $");
        builder.append(this.balance);

        return builder.toString();
    }
}
