/**
 * ~.
 * @author Evan Parker
 * @version v0.0.1
 */

public class Rock {
    private String name;
    private double numPounds;
    private double volume;

    /**
     * ~.
     * @param name ~
     */
    public Rock(String name) {
        this.name = name;
    }

    /**
     * ~.
     * @return ~
     */
    public String getName() {
        return name;
    }

    /**
     * ~.
     * @param name ~
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * ~.
     * @return ~
     */
    public double getNumPounds() {
        return numPounds;
    }

    /**
     * ~.
     * @param numPounds ~
     */
    public void setNumPounds(double numPounds) {
        this.numPounds = numPounds;
    }

    /**
     * ~.
     * @return ~
     */
    public double getVolume() {
        return volume;
    }

    /**
     * ~.
     * @param volume ~
     */
    public void setVolume(double volume) {
        this.volume = volume;
    }

    /**
     * ~.
     * @return ~
     */
    public int calculateDensity() {
        return (int) (this.numPounds / this.volume);
    }

    /**
     * ~.
     * @param increase ~
     */
    public void increasePounds(double increase) {
        this.numPounds += increase;
    }

    /**
     * ~.
     * @param decrease ~
     * @return ~
     */
    public double decreasePounds(double decrease) {
        this.numPounds -= decrease;
        return this.numPounds;
    }

    /**
     * @return ~
     */
    public String toString() {
        return String.format("Rock %s weighs %.3f pounds with a density of %d",
        this.name, this.numPounds, this.calculateDensity());
    }
}
