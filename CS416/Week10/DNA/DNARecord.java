/**
 * @author Evan Parker
 * @version v0.0.1
 */
public class DNARecord {
    private String name;
    private Profile profile;

    /**
     * Constructor for DNARecord.
     * @param name The name of the Persion
     * @param profile The profile of the Person
     */
    public DNARecord(String name, Profile profile) {
        this.name = name;
        this.profile = profile;
    }

    /**
     * Get the name of the Person.
     * @return The name of the Person
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the profile of the Person.
     * @return The profile of the Person
     */
    public Profile getProfile() {
        return this.profile;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", this.name, this.profile);
    }
}
