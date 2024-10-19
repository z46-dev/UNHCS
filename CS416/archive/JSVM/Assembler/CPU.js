import { instructions, registers } from "./constants.js";

export default class CPU {
    constructor() {
        this.memory = new Uint16Array(65536);
        this.registers = new Uint16Array(5);
        this.instructionPointer = 0;
        this.running = false;
        this.flags = {
            zero: false,
            negative: false,
            carry: false
        };
        this.registers[registers["SP"]] = 0xFFFF;

        this.stack = new Uint16Array(1024);
        this.registers[registers["SP"]] = 1024;
    }

    fetch() {
        if (this.instructionPointer === undefined || this.instructionPointer < 0 || this.instructionPointer >= this.memory.length) {
            console.error(`Invalid instruction pointer: ${this.instructionPointer}`);
            this.running = false;
            return undefined;
        }
        return this.memory[this.instructionPointer];
    }

    decode(opcode) {
        switch (opcode) {
            case instructions["NOP"]:
                this.instructionPointer++;
                break;
            case instructions["LDA"]:
                this.registers[registers["A"]] = this.memory[this.memory[this.instructionPointer + 1]];
                this.instructionPointer += 2;
                break;
            case instructions["LDI"]:
                this.registers[registers["A"]] = this.memory[this.instructionPointer + 1];
                this.instructionPointer += 2;
                break;
            case instructions["STA"]:
                this.memory[this.memory[this.instructionPointer + 1]] = this.registers[registers["A"]];
                this.instructionPointer += 2;
                break;
            case instructions["ADD"]:
            case instructions["SUB"]:
            case instructions["MUL"]:
            case instructions["DIV"]:
                this.arithmetic(opcode);
                break;
            case instructions["JMP"]:
                this.instructionPointer = this.memory[this.instructionPointer + 1];
                break;
            case instructions["JZ"]:
                if (this.flags.zero) {
                    this.instructionPointer = this.memory[this.instructionPointer + 1];
                } else {
                    this.instructionPointer += 2;
                }
                break;
            case instructions["JNZ"]:
                if (!this.flags.zero) {
                    this.instructionPointer = this.memory[this.instructionPointer + 1];
                } else {
                    this.instructionPointer += 2;
                }
                break;
            case instructions["JN"]:
                if (this.flags.negative) {
                    this.instructionPointer = this.memory[this.instructionPointer + 1];
                } else {
                    this.instructionPointer += 2;
                }
                break;
            case instructions["JP"]:
                if (!this.flags.negative && !this.flags.zero) {
                    this.instructionPointer = this.memory[this.instructionPointer + 1];
                } else {
                    this.instructionPointer += 2;
                }
                break;
            case instructions["CALL"]:
                this.memory[--this.registers[registers["SP"]]] = this.instructionPointer + 2;
                this.instructionPointer = this.memory[this.instructionPointer + 1];
                break;
            case instructions["RET"]:
                if (this.registers[registers["SP"]] >= 0xFFFF) {
                    this.running = false;
                } else {
                    this.instructionPointer = this.memory[this.registers[registers["SP"]]];
                    this.registers[registers["SP"]]++;
                }
                break;
            case instructions["OUT"]:
                console.log(`Output: ${this.registers[registers["A"]]}`);
                this.instructionPointer++;
                break;
            case instructions["HLT"]:
                this.running = false;
                break;
            case instructions["PUSH"]:
                this.stack[--this.registers[registers["SP"]]] = this.registers[registers["A"]];
                this.instructionPointer++;
                break;
            case instructions["POP"]:
                this.registers[registers["A"]] = this.stack[this.registers[registers["SP"]]++];
                this.instructionPointer++;
                break;
            case instructions["CALL"]:
                this.stack[--this.registers[registers["SP"]]] = this.instructionPointer + 2;
                this.instructionPointer = this.memory[this.instructionPointer + 1];
                break;
            case instructions["RET"]:
                this.instructionPointer = this.stack[this.registers[registers["SP"]]++];
                break;
            default:
                console.error(`Unknown opcode: ${opcode}`);
                this.running = false;
                break;
        }
    }

    arithmetic(opcode) {
        const operandType = this.memory[this.instructionPointer + 1];
        const operandValue = this.memory[this.instructionPointer + 2];
        let operand;

        if (operandType === 0) {  // Immediate value
            operand = operandValue;
        } else if (operandType === 1) {  // Memory address
            operand = this.memory[operandValue];
        } else {
            console.error(`Unknown operand type: ${operandType}`);
            this.running = false;
            return;
        }

        let result;
        switch (opcode) {
            case instructions["ADD"]:
                result = this.registers[registers["A"]] + operand;
                break;
            case instructions["SUB"]:
                result = this.registers[registers["A"]] - operand;
                break;
            case instructions["MUL"]:
                result = this.registers[registers["A"]] * operand;
                break;
            case instructions["DIV"]:
                if (operand === 0) {
                    console.error("Division by zero error");
                    this.running = false;
                    return;
                }
                result = Math.floor(this.registers[registers["A"]] / operand);
                break;
        }
        this.registers[registers["A"]] = result & 0xFFFF;
        this.updateFlags(result);
        this.instructionPointer += 3;
    }

    updateFlags(result) {
        this.flags.zero = (result & 0xFFFF) === 0;
        this.flags.negative = (result & 0x8000) !== 0;
        this.flags.carry = result > 0xFFFF;
    }

    run() {
        this.running = true;
        while (this.running) {
            const opcode = this.fetch();
            if (opcode === undefined) {
                console.error("Fetched undefined opcode. Halting execution.");
                break;
            }

            this.decode(opcode);
        }
    }

    getInstructionName(opcode) {
        for (const [name, code] of Object.entries(instructions)) {
            if (code === opcode) return name;
        }
        return 'UNKNOWN';
    }

    dumpMemory(start = 0, end = 16) {
        console.log("Memory dump:");
        for (let i = start; i < end; i++) {
            console.log(`[${i}]: ${this.memory[i]} (${this.getInstructionName(this.memory[i])})`);
        }
    }

    dumpRegisters() {
        console.log("Register dump:");
        for (const [name, index] of Object.entries(registers)) {
            console.log(`${name}: ${this.registers[index]}`);
        }
    }
}