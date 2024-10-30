/**
 * @author Evan Parker
 * @version v0.0.1
 */
public class Profile {
    private int agatCount;
    private int aatgCount;
    private int tatcCount;

    /**
     * Getter for agatCount.
     * @return int
     */
    public int getAgatCount() {
        return this.agatCount;
    }

    /**
     * Setter for agatCount.
     * @param agatCount int
     */
    public void setAgatCount(int agatCount) {
        this.agatCount = agatCount;
    }

    /**
     * Getter for aatgCount.
     * @return int
     */
    public int getAatgCount() {
        return this.aatgCount;
    }

    /**
     * Setter for aatgCount.
     * @param aatgCount int
     */
    public void setAatgCount(int aatgCount) {
        this.aatgCount = aatgCount;
    }

    /**
     * Getter for tatcCount.
     * @return int
     */
    public int getTatcCount() {
        return this.tatcCount;
    }

    /**
     * Setter for tatcCount.
     * @param tatcCount int
     */
    public void setTatcCount(int tatcCount) {
        this.tatcCount = tatcCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Profile)) {
            return false;
        }

        Profile profile = (Profile) o;

        return this.agatCount == profile.agatCount
            && this.aatgCount == profile.aatgCount
            && this.tatcCount == profile.tatcCount;
    }

    @Override
    public String toString() {
        return String.format("(AGAT = %d, AATG = %d, TATC = %d)", this.agatCount, this.aatgCount, this.tatcCount);
    }
}
