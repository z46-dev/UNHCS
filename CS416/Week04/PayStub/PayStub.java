import java.util.Scanner;

/**
 * PayStub program.
 * @author Evan Parker
 * @version v0.0.2
 */
public class PayStub {
    private String name;
    private int monthHired;
    private int yearHired;
    private int hoursWorked;
    private String jobTitle;
    private double hourlyPayRate;

    /**
     * Creates a new PayStub with specified objects.
     * @param name name
     * @param monthHired month hired
     * @param yearHired year hired
     * @param hoursWorked hours worked
     * @param jobTitle job title
     * @param hourlyPayRate hourly pay rate
     */
    public PayStub(String name, int monthHired, int yearHired, int hoursWorked, String jobTitle, double hourlyPayRate) {
        this.name = name;
        this.monthHired = monthHired;
        this.yearHired = yearHired;
        this.hoursWorked = hoursWorked;
        this.jobTitle = jobTitle;
        this.hourlyPayRate = hourlyPayRate;
    }

    /**
     * Creates a new PayStub with prefilled objects.
     */
    public PayStub() {
        this.name = "Karin Nadya";
        this.monthHired = 4;
        this.yearHired = 2015;
        this.hoursWorked = 170;
        this.jobTitle = "Computer System Analyst";
        this.hourlyPayRate = 42.12;
    }
    
    /**
     * Gets name.
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets mont.
     * @return month
     */
    public int getMonth() {
        return monthHired;
    }

    /**
     * Sets month.
     * @param monthHired month
     */
    public void setMonthHired(int monthHired) {
        this.monthHired = monthHired;
    }

    /**
     * Gets year.
     * @return year
     */
    public int getYear() {
        return yearHired;
    }

    /**
     * Sets year.
     * @param yearHired year
     */
    public void setYearHired(int yearHired) {
        this.yearHired = yearHired;
    }

    /**
     * Gets hours worked.
     * @return hours
     */
    public int getHours() {
        return hoursWorked;
    }

    /**
     * Sets hours worked.
     * @param hoursWorked hours
     */
    public void setHoursWorked(int hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    /**
     * Gets title.
     * @return tilte
     */
    public String getTitle() {
        return jobTitle;
    }

    /**
     * Sets title.
     * @param jobTitle title
     */
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    /**
     * Gets pay rate.
     * @return pay
     */
    public double getPayRate() {
        return hourlyPayRate;
    }

    /**
     * Sets pay rate.
     * @param hourlyPayRate pay
     */
    public void setHourlyPayRate(double hourlyPayRate) {
        this.hourlyPayRate = hourlyPayRate;
    }

    /**
     * Gets number of months worked.
     * @return months worked
     */
    int numMonthsWorked() {
        final int CURR_MONTH = 1;
        final int CURR_YEAR = 2019;

        return (CURR_YEAR - this.yearHired) * 12 + CURR_MONTH - this.monthHired;
    }

    /**
     * Gets vacation hours.
     * @return hours
     */
    double vacationHours() {
        return ((double) this.numMonthsWorked()) * 8.25;
    }

    /**
     * Gets gross pay.
     * @return gross pay
     */
    double grossPay() {
        return ((double) this.hoursWorked) * this.hourlyPayRate;
    }

    /**
     * Gets retirement hold.
     * @return hold
     */
    double retHold() {
        return this.grossPay() * .052;
    }

    /**
     * Gets tax witholdment.
     * @return tax
     */
    double tax() {
        return (this.grossPay() - this.retHold()) * .28;
    }

    /**
     * Gets net pay.
     * @return pay
     */
    double netPay() {
        return this.grossPay() - this.retHold() - this.tax();
    }

    /**
     * Draws logo.
     */
    void drawLogo() {
        System.out.println("      Gekko & Co.");
        System.out.println("");
        System.out.println("          \"$\"");
        System.out.println("          ~~~");
        System.out.println("         /  \\ `.");
        System.out.println("        /    \\  /");
        System.out.println("       /_ _ _ \\/");
    }

    /**
     * Prints info.
     */
    void printInfo() {
        System.out.println("==========================================");
        this.drawLogo();
        System.out.println("------------------------------------------");
        System.out.println("Pay Period:     " + "1" + "/" + "2019");
        System.out.println("Name:           " + this.getName());
        System.out.println("Title:          " + this.getTitle());
        System.out.println("Anniversary:    " + this.getMonth() + "/" + this.getYear());
        System.out.println("Months Worked:  " + this.numMonthsWorked() + " months");
        System.out.printf("Vacation hours: %.2f\n", this.vacationHours());
        System.out.println("------------------------------------------");
        System.out.printf("Gross Pay:     $%7.2f\n", this.grossPay());
        System.out.printf("Retirement:    $%7.2f\n", this.retHold());
        System.out.printf("Tax:           $%7.2f\n", this.tax());
        System.out.println("------------------------");
        System.out.printf("Net Pay:       $%7.2f\n", this.netPay());
        System.out.println("==========================================");
    }

    /**
     * Main method.
     * @param args args
     */
    public static void main(String[] args) {
        PayStub employee = readEmployee();
        employee.printInfo();
    }

    /**
     * Gets employee from console.
     * @return paystub
     */
    public static PayStub readEmployee() {
        Scanner input = new Scanner(System.in);
        PayStub output = new PayStub();

        System.out.print("Enter your Fullname: ");
        output.setName(input.nextLine());

        System.out.print("Enter your Anniversary Month(1-12): ");
        output.setMonthHired(input.nextInt());

        System.out.print("Enter your Anniversary Year: ");
        output.setYearHired(input.nextInt());

        System.out.print("Enter your hours worked this pay period(0-350): ");
        output.setHoursWorked(input.nextInt() % 351);

        input.nextLine();
        System.out.print("Enter your Job Title: ");
        output.setJobTitle(input.nextLine());

        System.out.print("Enter your pay rate: ");
        output.setHourlyPayRate(input.nextDouble());

        input.close();

        return output;
    }
}
