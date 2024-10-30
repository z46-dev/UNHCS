import java.io.File;
import java.util.Scanner;

/**
 * @author Evan Parker
 * @version v0.0.1
 */
public class DNA {
    /**
     * Reads data from file and returns DNA records.
     * 
     * @param fileName File to read from
     * @return DNARecord[] Array of DNA records
     */
    public static DNARecord[] readData(String fileName) {
        try {
            File file = new File(fileName);

            if (!file.exists()) {
                return null;
            }

            Scanner scanner = new Scanner(file);

            // Header line: name AGAT AATG TACT
            scanner.nextLine();

            DNARecord[] records = new DNARecord[20];
            int index = 0;

            while (scanner.hasNextLine() && index < 20) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");

                String name = parts[0];
                Profile profile = new Profile();
                profile.setAgatCount(Integer.parseInt(parts[1]));
                profile.setAatgCount(Integer.parseInt(parts[2]));
                profile.setTatcCount(Integer.parseInt(parts[3]));

                records[index++] = new DNARecord(name, profile);
            }

            scanner.close();

            return records;
        } catch (java.io.FileNotFoundException e) {
            return null;
        }
    }

    /**
     * Counts the maximum number of consecutive matches in a sequence.
     * 
     * @param sequence DNA sequence
     * @param match    DNA match
     * @return int Maximum number of consecutive matches
     */
    public static int countMaximumConsecutiveMatches(String sequence, String match) {
        int ct = 0;
        int maxCt = 0;
        int index = 0;
        boolean lastHadMatch = false;

        while (index < sequence.length()) {
            if (sequence.substring(index).startsWith(match)) {
                index += match.length();

                if (lastHadMatch) {
                    ct++;
                } else {
                    ct = 1;
                    lastHadMatch = true;
                }
            } else {
                if (ct > maxCt) {
                    maxCt = ct;
                }

                index++;
                lastHadMatch = false;
            }
        }

        return Math.max(ct, maxCt);
    }

    /**
     * Reads a sequence from a file and returns a profile.
     * @param fileName File to read from
     * @return Profile Profile of the sequence
     */
    public static Profile readSequence(String fileName) {
        try {
            File file = new File(fileName);

            if (!file.exists()) {
                return null;
            }

            Scanner scanner = new Scanner(file);
            Profile profile = new Profile();

            String sequence = scanner.nextLine();

            profile.setAgatCount(countMaximumConsecutiveMatches(sequence, "AGAT"));
            profile.setAatgCount(countMaximumConsecutiveMatches(sequence, "AATG"));
            profile.setTatcCount(countMaximumConsecutiveMatches(sequence, "TATC"));

            scanner.close();

            return profile;
        } catch (java.io.FileNotFoundException e) {
            return null;
        }
    }

    /**
     * Main method.
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        String dataFile = args[0];
        String sequenceFile = args[1];

        DNARecord[] records = readData(dataFile);
        Profile profile = readSequence(sequenceFile);

        if (records == null || profile == null) {
            System.out.println("Error reading files.");
            return;
        }

        for (DNARecord record : records) {
            if (record.getProfile().equals(profile)) {
                System.out.printf("Matched %s\n", record);
                return;
            }
        }

        System.out.printf("No match for %s\n", profile);
    }
}
