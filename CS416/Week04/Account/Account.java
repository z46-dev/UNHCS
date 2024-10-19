/**
 * @author author
 * @version version
 */
public interface Account {

    /** deposit adds money to the account.
     * @param amount - the amount of the deposit, a nonnegative integer  */
    void deposit(int amount);

    /** withdraw deducts money from the account, if possible.
     * @param amount - the amount of the withdrawal, a nonnegative integer
     * @return true, if the the withdrawal was successful;
     *  return false, otherwise.  */
    boolean withdraw(int amount);

    /**
     *
     * @return balance
     */
    int getBalance();

}