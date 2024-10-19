# CPU Emulator Documentation

## Overview

This project implements a simple 32-bit CPU emulator with a custom assembly language. The emulator consists of three main components: the CPU, the Assembler, and a set of opcodes defined in constants.

## Components

### CPU (CPU.js)

The CPU class simulates the behavior of a 32-bit processor. It includes:

- 16 general-purpose registers (R0-R15)
- A program counter (PC)
- Flags (zero, carry, negative, overflow)
- Memory (configurable size)

Key methods:
- `execute(instruction)`: Executes a single instruction
- `run()`: Runs the loaded program until completion
- `dump()`: Displays the current state of the CPU

### Assembler (Assembler.js)

The Assembler class converts assembly code into machine code that can be executed by the CPU. It supports labels and resolves them to memory addresses.

### Opcodes (constants.js)

Defines the set of instructions supported by the CPU, including arithmetic operations, jumps, and memory operations.

## Instruction Set

- `LDI Rx, immediate`: Load immediate value into register
- `MOV Rx, Ry`: Move value from one register to another
- `ADD Rx, Ry, Rz`: Add values in Ry and Rz, store in Rx
- `SUB Rx, Ry, Rz`: Subtract Rz from Ry, store in Rx
- `MUL Rx, Ry, Rz`: Multiply Ry by Rz, store in Rx
- `DIV Rx, Ry, Rz`: Divide Ry by Rz, store in Rx
- `AND Rx, Ry, Rz`: Bitwise AND of Ry and Rz, store in Rx
- `OR Rx, Ry, Rz`: Bitwise OR of Ry and Rz, store in Rx
- `XOR Rx, Ry, Rz`: Bitwise XOR of Ry and Rz, store in Rx
- `SHL Rx, Ry, immediate`: Shift left Ry by immediate, store in Rx
- `SHR Rx, Ry, immediate`: Shift right Ry by immediate, store in Rx
- `JMP address`: Jump to address
- `JZ address`: Jump to address if zero flag is set
- `JNZ address`: Jump to address if zero flag is not set
- `JG Rx, Ry, address`: Jump to address if Rx > Ry
- `JL address`: Jump to address if negative flag is set
- `LOAD Rx, [Ry]`: Load value from memory address in Ry to Rx
- `STORE [Ry], Rx`: Store value from Rx to memory address in Ry
- `PUSH Rx`: Push value in Rx onto the stack
- `POP Rx`: Pop value from stack into Rx
- `CALL address`: Call subroutine at address
- `RET`: Return from subroutine
- `OUT Rx`: Output value in Rx
- `HLT`: Halt the CPU

## Usage Example

```javascript
import CPU from "./lib/CPU.js";
import Assembler from "./lib/Assembler.js";

const assemblyCode = `
    LDI R0, 5     ; Input number
    LDI R1, 1     ; Result
    LDI R2, 1     ; Counter
    LDI R3, 1     ; Increment

LOOP:
    MUL R1, R1, R2    ; Result = Result * Counter
    ADD R2, R2, R3    ; Counter++
    JG R0, R2, LOOP   ; If Input > Counter, continue loop
    
    OUT R1          ; Output the result
    HLT             ; Halt the CPU
`;

const cpu = new CPU(16);
cpu.loadProgram(new Assembler(assemblyCode));
cpu.run();
cpu.dump();
```

This example calculates the factorial of 5 and outputs the result.