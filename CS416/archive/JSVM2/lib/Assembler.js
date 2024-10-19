import { opcodes } from "./constants.js";

export default class Assembler {
    constructor(code = null) {
        this.program = new Uint32Array(1);
        this.labels = {};

        if (code !== null) {
            this.assemble(code);
        }

        console.log(this.program);
    }

    assemble(assemblyCode) {
        const lines = assemblyCode.trim().split("\n").map(line => line.trim());
        const program = [];
        let address = 0;

        // First pass: collect labels
        for (const line of lines) {
            if (!line || line.startsWith(";")) continue;

            if (line.endsWith(":")) {
                const label = line.slice(0, -1);
                this.labels[label] = address;
            } else {
                address++;
            }
        }

        // Second pass: assemble instructions
        for (const line of lines) {
            if (!line || line.startsWith(";") || line.endsWith(":")) continue;

            const [instruction, ...args] = line.split(/\s+/);
            const opcode = opcodes[instruction];

            if (opcode === undefined) {
                throw new Error(`Unknown instruction: ${instruction}`);
            }

            let machineCode = opcode << 24;
            let regX, regY, regZ, immediate;

            switch (instruction) {
                case "ADD":
                case "SUB":
                case "MUL":
                case "DIV":
                case "AND":
                case "OR":
                case "XOR":
                    regX = parseInt(args[0].slice(1));
                    regY = parseInt(args[1].slice(1));
                    regZ = parseInt(args[2].slice(1));
                    machineCode |= (regX << 16) | (regY << 8) | regZ;
                    break;
                case "SHL":
                case "SHR":
                    regX = parseInt(args[0].slice(1));
                    regY = parseInt(args[1].slice(1));
                    immediate = parseInt(args[2]);
                    machineCode |= (regX << 16) | (regY << 8) | (immediate & 0xFF);
                    break;
                case "LDI":
                    regX = parseInt(args[0].slice(1));
                    immediate = parseInt(args[1]);
                    machineCode |= (regX << 16) | (immediate & 0xFFFF);
                    break;
                case "JMP":
                case "JZ":
                case "JNZ":
                case "JL":
                case "CALL":
                    immediate = this.resolveLabel(args[0]);
                    machineCode |= (immediate & 0xFFFF);
                    break;
                case "JG":
                    regX = parseInt(args[0].slice(1));
                    regY = parseInt(args[1].slice(1));
                    immediate = this.resolveLabel(args[2]);
                    machineCode |= (regX << 16) | (regY << 8) | (immediate & 0xFF);
                    break;
                case "MOV":
                case "LOAD":
                case "STORE":
                    regX = parseInt(args[0].slice(1));
                    regY = parseInt(args[1].slice(1));
                    machineCode |= (regX << 16) | (regY << 8);
                    break;
                case "PUSH":
                case "POP":
                    regX = parseInt(args[0].slice(1));
                    machineCode |= (regX << 16);
                    break;
                case "OUT":
                    regX = parseInt(args[0].slice(1));
                    machineCode |= (regX << 16);
                    break;
                case "HLT":
                case "RET":
                    // No additional arguments needed
                    break;
                default:
                    throw new Error(`Unsupported instruction: ${instruction}`);
            }

            program.push(machineCode);
        }

        this.program = new Uint32Array(program);
        return this.program;
    }

    resolveLabel(label) {
        if (label in this.labels) {
            return this.labels[label];
        } else {
            return parseInt(label);
        }
    }
}