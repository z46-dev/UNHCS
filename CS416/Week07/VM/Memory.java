import java.util.Scanner;

/**
 * A class to model a Simple Virtual machine Memory.
 * @author Evan Parker
 * @version v0.0.1
 * 
 * I could use a byte array because it's the smallest data type that can
 * hold a 4-bit value. However I've had a long week and just want to get
 * this done so I'm going to use bit maniupation to store 16 4-bit values
 * in a 64-bit long. This normally wouldn't be a good idea because it's
 * not very readable, despite being more memory efficient than what I would
 * imagine the standard approach to this solution would be (int array).
 * 
 * I did it anyways. This won't work on 32-bit machines.
 */
public class Memory {
    /**
     * The raw memory values, as bytes.
     * 
     * What consitutes a valid memory address?
     * 0 <= address <= 15
     * 
     * What consitutes a valid memory value?
     * 0 <= value <= 15
     */
    byte[] rawMemoryValues;

    /**
     * Creates an instance of the Memory class and initializes the rawMemory to 0.
     */
    public Memory() {
        this.rawMemoryValues = new byte[16];
    }

    /**
     * Constructor for the Memory class that takes a Scanner,
     * reads from a file or input for an initial memory state.
     * @param s scanner object to read memory from
     */
    public Memory(Scanner s) {
        this.rawMemoryValues = new byte[16];

        int i = 0;

        while (s.hasNextInt()) {
            if (i > 15) {
                throw new IllegalStateException("Too many values in the input file");
            }
            
            this.write(i++, s.nextInt());
        }

        s.close();
    }

    /**
     * Serialize the raw value into an 8 bit value only using the lower 4 bits.
     * @param rawValue the raw value to serialize
     * @return the serialized value
     */
    public static byte serialize(int rawValue) {
        return (byte) (rawValue & 0xF);
    }

    /**
     * Read a value from memory at the given address.
     * @param address the address to read from
     * @return the value at the given address in memory
     * @throws IllegalStateException if the address does not map to a valid memory address
     * @see Memory#rawMemoryValue
     */
    public int read(int address) throws IllegalStateException {
        if (address < 0 || address > 15) {
            throw new IllegalStateException("Invalid memory address");
        }

        return this.rawMemoryValues[address];
    }

    /**
     * Write a value to memory at the given address.
     * @param address the address to write to
     * @param value the value to write
     * @throws IllegalStateException if the address does not map to a valid memory address
     * @see Memory#rawMemoryValue
     */
    public void write(int address, int value) throws IllegalStateException {
        if (address < 0 || address > 15) {
            throw new IllegalStateException("Invalid memory address");
        }

        this.rawMemoryValues[address] = Memory.serialize(value);
    }

    /**
     * Stringify the memory object.
     * @return the memory object as a string
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("address: |");

        for (int i = 0; i < 16; i++) {
            sb.append(String.format("%3d |", i));
        }

        sb.append("\n  value: |");

        for (int i = 0; i < 16; i++) {
            sb.append(String.format("%3d |", this.read(i)));
        }

        return sb.toString();
    }

    /**
     * Get the raw memory array.
     * @return the raw memory array
     */
    public int[] rawMemory() {
        int[] raw = new int[16];

        for (int i = 0; i < 16; i++) {
            raw[i] = this.read(i);
        }

        return raw;
    }

    /**
     * Main method to test stuff in this class.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Memory mem = new Memory();

        // Serialized:      1, 2, 3, 4, 15, 0,  1,  2,  0, 1, 2, 3, 15, 14, 13, 12
        int[] testValues = {1, 2, 3, 4, 15, 16, 17, 18, 0, 1, 2, 3, -1, -2, -3, -4};

        for (int i = 0; i < 16; i++) {
            mem.write(i, testValues[i]);
        }

        System.out.println(mem.toString());

        for (int i = 0; i < 16; i++) {
            assert mem.read(i) == (testValues[i] & 0xF);
        }

        System.out.println("All tests passed!");
    }
}