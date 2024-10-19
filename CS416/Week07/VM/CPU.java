/**
 * A class to model a CPU in a simple Virtual Machine.
 * 
 * @author Evan Parker
 * @version v0.0.1
 */
public class CPU {
    static final int IP = 0;
    static final int IS = 1;
    static final int R0 = 2;
    static final int R1 = 3;

    /**
     * Values of the registers.
     * Each register is a 4-bit value.
     * 
     * What consitutes a valid register value?
     * 0 <= register <= 15
     * 
     * What is IP?
     * The instruction pointer, which points to the
     * current instruction in memory.
     * 
     * What is IS?
     * The instruction set, which contains the current
     * instruction being executed.
     * 
     * What are R0 and R1?
     * General purpose registers.
     */
    int[] registers;

    /**
     * Creates an instance of the CPU class and initializes the registers to 0.
     */
    public CPU() {
        this.registers = new int[4];

        for (int i = 0; i < 4; i++) {
            this.registers[i] = 0;
        }
    }

    /**
     * Runs the CPU with the given Memory object and debug mode.
     * 
     * @param mem       the Memory object to run the CPU with
     * @param debugMode whether or not to run the CPU in debug mode
     * @throws IllegalStateException if the CPU enters an illegal state
     * @see Memory
     */
    public void run(Memory mem, boolean debugMode) throws IllegalStateException {
        int step = 0;
        int next = 0;
        
        execution: while (true) {
            this.registers[IS] = mem.read(this.registers[IP]);

            if (debugMode) {
                System.out.println("STEP " + step++ + ":");
                System.out.println(this);
                System.out.println(mem);
                System.out.println("----------");
            }

            switch (this.registers[IS]) {
                case 0x00: // HLT
                    break execution;
                case 0x01: // R0 = R0 + R1
                    this.registers[R0] = (this.registers[R0] + this.registers[R1]) & 0xF;
                    break;
                case 0x02: // R0 = R0 - R1
                    this.registers[R0] = (this.registers[R0] - this.registers[R1]) & 0xF;
                    break;
                case 0x03: // R0++
                    this.registers[R0] = (this.registers[R0] + 1) & 0xF;
                    break;
                case 0x04: // R1++
                    this.registers[R1] = (this.registers[R1] + 1) & 0xF;
                    break;
                case 0x05: // R0--
                    this.registers[R0] = (this.registers[R0] - 1) & 0xF;
                    break;
                case 0x06: // R1--
                    this.registers[R1] = (this.registers[R1] - 1) & 0xF;
                    break;
                case 0x07: // Beep
                    Beep.beep();
                    break;
                case 0x08: // Print X
                    System.out.println(mem.read(this.advance()));
                    break;
                case 0x09: // R0 = mem[X]
                    this.registers[R0] = mem.read(mem.read(this.advance()));
                    break;
                case 0x0A: // R1 = mem[X]
                    this.registers[R1] = mem.read(mem.read(this.advance()));
                    break;
                case 0x0B: // mem[X] = R0
                    next = this.advance();
                    mem.write(mem.read(next), this.registers[R0]);
                    break;
                case 0x0C: // mem[X] = R1
                    mem.write(mem.read(this.advance()), this.registers[R1]);
                    break;
                case 0x0D: // Jump X
                    this.registers[IP] = mem.read(this.advance());
                    continue execution;
                case 0x0E: // Jump X IF R0 == 0
                    next = this.advance();
                    if (this.registers[R0] == 0) {
                        this.registers[IP] = mem.read(next);
                        continue execution;
                    }
                    break;
                case 0x0F: // Jump X if R0 != 0
                    next = this.advance();
                    
                    if (this.registers[R0] != 0) {
                        this.registers[IP] = mem.read(next);
                        continue execution;
                    }
                    break;
            }

            this.advance();
        }
    }

    /**
     * Advances the instruction pointer safely.
     * @return the new value of the instruction pointer
     */
    private int advance() {
        this.registers[IP] = (this.registers[IP] + 1) & 0xF;

        return this.registers[IP];
    }

    /**
     * Returns a string representation of the CPU.
     * 
     * @return a string representation of the CPU
     */
    public String toString() {
        return String.format(
                "IP=%2d, IS=%2d, R0=%2d, R1=%2d",
                this.registers[IP], this.registers[IS],
                this.registers[R0], this.registers[R1]);
    }
}