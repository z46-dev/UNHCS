import CPU from "./lib/CPU.js";
import Assembler from "./lib/Assembler.js";
import Tokenizer from "./MicroC/Tokenizer.js";
import { Parser } from "./microc/parser.js";
import { CodeGenerator } from "./microc/compiler.js";

async function loadFile(path) {
    if (typeof require !== "undefined") {
        const fs = require("fs");
        return fs.readFileSync(path, "utf-8");
    }

    const response = await fetch(path);
    return response.text();
}

// const file = await loadFile("./JSVM2/main.micro.c");
// const tokenizer = new Tokenizer(file);
// const parser = new Parser(tokenizer.tokens);
// const program = parser.parse();

// const compiler = new CodeGenerator();
// const asmCode = compiler.generate(program);

// console.log(asmCode);

// const assembler = new Assembler(asmCode);

// const cpu = new CPU(2048);
// cpu.loadProgram(assembler);
// cpu.run(true);
// cpu.dump(16);

const cpu = new CPU(16);
cpu.loadProgram(new Assembler(await loadFile("./JSVM2/main.asm")));
cpu.run();
cpu.dump();

/**
 * Issue as of rn:
 * The ret statement makes pc go to 0 and
 * does everything all over again and again
 */