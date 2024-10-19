import { instructions } from "./constants.js";

export default class Assembler {
    constructor(text) {
        this.text = text.trim().split("\n").map(line => line.trim()).filter(line => line !== "");
        this.outputInstructions = [];
        this.labels = {};
    }

    assemble() {
        let instructionIndex = 0;
        this.text.forEach((line) => {
            if (line.endsWith(":")) {
                this.labels[line.slice(0, -1)] = instructionIndex;
            } else {
                const parts = line.split(" ");
                instructionIndex += (parts[0] === "ADD" || parts[0] === "SUB" || parts[0] === "MUL" || parts[0] === "DIV") ? 3 : 2;
            }
        });

        this.text.forEach(line => {
            if (line.endsWith(":")) {
                return;
            }

            const parts = line.split(" ");
            const instruction = parts[0];
            const opcode = instructions[instruction];
            
            if (opcode === undefined) {
                console.error(`Unknown instruction: ${instruction}`);
                return;
            }

            this.outputInstructions.push(opcode);

            if (instruction === "ADD" || instruction === "SUB" || instruction === "MUL" || instruction === "DIV") {
                const operandType = parseInt(parts[1]);
                let operandValue;
                if (operandType === 0) {
                    operandValue = parseInt(parts[2]);
                } else if (operandType === 1) {
                    operandValue = this.labels[parts[2]] !== undefined ? this.labels[parts[2]] : parseInt(parts[2]);
                } else {
                    throw new Error(`Invalid operand type: ${operandType}`);
                }
                this.outputInstructions.push(operandType);
                this.outputInstructions.push(operandValue);
            } else if (parts.length > 1) {
                let value;
                if (this.labels[parts[1]] !== undefined) {
                    value = this.labels[parts[1]];
                } else {
                    value = parseInt(parts[1]);
                }
                this.outputInstructions.push(value);
            }
        });

        return new Uint16Array(this.outputInstructions);
    }
}