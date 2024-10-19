/**
 * @author author
 * @version version
 */
public class BillPay {
    private Account account;
    private int payAmount;
    private int paymentsRemaining;

    /**
     *
     * @param account account to withdraw from
     * @param payAmount monthly payment amount
     * @param payments total number of payments
     */
    public BillPay(Account account, int payAmount, int payments) {
        this.account = account;
        this.payAmount = payAmount;
        this.paymentsRemaining = payments;
    }

    /**
     *
     */
    public void run() {

        System.out.println(String.format("Withdrawing $%d per month for %d payments...", this.payAmount,
                this.paymentsRemaining));
        System.out.println();
        for (int i = 0; i < this.paymentsRemaining; i++) {
            System.out.print(String.format("Attempting to withdraw payment %d: $%d", i + 1,  this.payAmount));
            if (this.account.withdraw(this.payAmount)) {
                System.out.println("...SUCCESS");
                System.out.println("New balance is: $" + this.account.getBalance());
            } else {
                System.out.println("...FAILED");
                System.out.println("Insufficient funds");
                break;
            }
        }
    }

    /**
     *
     * @param args args
     */
    public static void main(String[] args) {
        CheckingAccount c = new CheckingAccount("1003899", "Ayesha Reynolds", 1528);
        BillPay bp = new BillPay(c, 1024, 4);

        System.out.println("Account Starting");
        System.out.println("================");
        System.out.println(c);
        System.out.println();
        bp.run();
        System.out.println();
        System.out.println("Account Ending");
        System.out.println("================");
        System.out.println(c);
    }

}