/**
 * Sample class.
 * @author cs416
 * @version 1
 */
public class Sample {
    public int id;
    public double measurement;

    /**
    * Constructor.
    * @param id The identifier for the Sample
    * @param measurement The value of the measurement for this id
    */
    public Sample(int id, double measurement) {
        this.id = id;
        this.measurement = measurement;
    }

    /**
    * Returns the String reprensentation of this Sample.
    * @return String
    */
    public String toString() {
        return String.format("<%d,%.2f>", id, measurement);
    }
}