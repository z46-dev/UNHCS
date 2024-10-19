import Assembler from "./Assembler.js";
import { opcodes, reverseOpcodes } from "./constants.js";

export default class CPU {
    constructor(memorySize) {
        this.registers = new Uint32Array(16);
        this.memory = new Uint32Array(memorySize);
        this.halted = false;
        this.debug = false;
        this.pc = 0;
        this.iterations = 0;
        this.instructionsPerSecond = 1;
        this.flags = {
            zero: false,
            carry: false,
            negative: false,
            overflow: false
        };
    }

    execute(instruction) {
        const opcode = instruction >>> 24;
        const regX = (instruction >>> 16) & 0xFF;
        const regY = (instruction >>> 8) & 0xFF;
        const regZ = instruction & 0xFF;
        const immediate = instruction & 0xFFFF;

        if (this.debug) {
            console.log(`pc: ${this.pc}, opcode: ${reverseOpcodes[opcode]}, regX: ${regX}, regY: ${regY}, regZ: ${regZ}, immediate: ${immediate}`);
        }

        switch (opcode) {
            case opcodes.LDI:
                this.registers[regX] = immediate;
                break;
            case opcodes.MOV:
                this.registers[regX] = this.registers[regY];
                break;
            case opcodes.ADD:
                this.registers[regX] = this.registers[regY] + this.registers[regZ];
                this.updateFlags(this.registers[regX]);
                break;
            case opcodes.SUB:
                this.registers[regX] = this.registers[regY] - this.registers[regZ];
                this.updateFlags(this.registers[regX]);
                break;
            case opcodes.MUL:
                this.registers[regX] = this.registers[regY] * this.registers[regZ];
                this.updateFlags(this.registers[regX]);
                break;
            case opcodes.DIV:
                if (this.registers[regZ] === 0) {
                    throw new Error("Division by zero");
                }
                this.registers[regX] = Math.floor(this.registers[regY] / this.registers[regZ]);
                this.updateFlags(this.registers[regX]);
                break;
            case opcodes.AND:
                this.registers[regX] = this.registers[regY] & this.registers[regZ];
                this.updateFlags(this.registers[regX]);
                break;
            case opcodes.OR:
                this.registers[regX] = this.registers[regY] | this.registers[regZ];
                this.updateFlags(this.registers[regX]);
                break;
            case opcodes.XOR:
                this.registers[regX] = this.registers[regY] ^ this.registers[regZ];
                this.updateFlags(this.registers[regX]);
                break;
            case opcodes.SHL:
                this.registers[regX] = this.registers[regY] << immediate;
                this.updateFlags(this.registers[regX]);
                break;
            case opcodes.SHR:
                this.registers[regX] = this.registers[regY] >>> immediate;
                this.updateFlags(this.registers[regX]);
                break;
            case opcodes.JMP:
                this.pc = immediate - 1;
                break;
            case opcodes.JZ:
                if (this.flags.zero) {
                    this.pc = immediate - 1;
                }
                break;
            case opcodes.JNZ:
                if (!this.flags.zero) {
                    this.pc = immediate - 1;
                }
                break;
            case opcodes.JG:
                if (this.registers[regX] > this.registers[regY]) {
                    this.pc = regZ - 1;
                }
                break;
            case opcodes.JL:
                if (this.flags.negative) {
                    this.pc = immediate - 1;
                }
                break;
            case opcodes.LOAD:
                this.registers[regX] = this.memory[this.registers[regY]];
                break;
            case opcodes.STORE:
                this.memory[this.registers[regY]] = this.registers[regX];
                break;
            case opcodes.PUSH:
                this.memory[this.registers[14]] = this.registers[regX];
                this.registers[14]--; // Assuming R14 is the stack pointer
                break;
            case opcodes.PUSH:
                this.registers[14]--;
                this.memory[this.registers[14]] = this.registers[regX];
                break;
            case opcodes.POP:
                this.registers[regX] = this.memory[this.registers[14]];
                this.registers[14]++;
                break;
            case opcodes.CALL:
                this.registers[14]--;
                this.memory[this.registers[14]] = this.pc + 1;
                this.pc = immediate - 1;
                break;
            case opcodes.RET:
                this.pc = this.memory[this.registers[14]] - 1;
                this.registers[14]++;
                break;
            case opcodes.HLT:
                this.halted = true;
                console.log("Program halted");
                break;
            case opcodes.OUT:
                console.log(this.registers[regX]);
                break;
            default:
                throw new Error(`Unknown opcode: ${opcode}`);
        }

        this.pc++;
    }

    updateFlags(result) {
        this.flags.zero = result === 0;
        this.flags.negative = (result & 0x80000000) !== 0;
        // Overflow and carry flags would require more complex logic
    }

    loadProgram(program) {
        if (program instanceof Assembler) {
            this.memory.set(program.program, 0);
            return;
        }
        this.memory.set(program, 0);
    }

    run(debug = false) {
        const startTime = performance.now();
        this.iterations = 0;
        this.debug = debug;

        while (!this.halted && this.pc < this.memory.length) {
            const instruction = this.memory[this.pc];
            this.execute(instruction);
            this.iterations++;
        }

        const endTime = performance.now();
        const duration = endTime - startTime;
        this.instructionsPerSecond = Math.floor(this.iterations / (duration / 1000));
    }

    dump(memorySize = this.pc) {
        console.log(`Registers: ${Array.from(this.registers).join(", ")}`);
        console.log(`Memory: ${Array.from(this.memory.slice(0, memorySize)).map(e => "0x" + e.toString(16).toUpperCase().padStart(8, "0")).join(", ")}`);
        console.log(`Iterations: ${this.iterations}`);
        console.log(`Instructions per second: ${this.instructionsPerSecond}`);
        console.log(`Flags: ${JSON.stringify(this.flags)}`);
    }
}