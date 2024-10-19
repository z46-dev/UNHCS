import CPU from "./Assembler/CPU.js";
import Assembler from "./Assembler/Assembler.js";
import MicroCTokenizer from "./MicroC/Tokenizer.js";
import MicroCToAssembly from "./MicroC/Compiler.js";
import { instructions } from "./Assembler/constants.js";

// File loader based on either browser or Node.js
const loadFile = async (path) => {
    if (typeof window !== "undefined") {
        const response = await fetch(path);
    } else {
        const fs = require("fs");
        return fs.readFileSync(path, "utf-8");
    }
}

// MicroC
const microCProgram = await loadFile("./JSVM/main.micro.c");
console.log("MicroC Program:");
console.log(microCProgram, "\n");

// Compiler
const compiler = new MicroCToAssembly(new MicroCTokenizer(microCProgram));
const assemblyProgram = compiler.compile();
console.log("Assembly Program:");
console.log(assemblyProgram, "\n");

// Assembler
const assembler = new Assembler(assemblyProgram);
const machineCode = assembler.assemble();

console.log("Machine Code:", machineCode);

// CPU
const cpu = new CPU();
cpu.memory.set(machineCode);
cpu.run();