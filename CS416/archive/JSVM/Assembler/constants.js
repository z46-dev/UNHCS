export const instructions = {
    "NOP":  0x00,
    "LDA":  0x01,
    "ADD":  0x02,
    "SUB":  0x03,
    "STA":  0x04,
    "LDI":  0x05,
    "JMP":  0x06,
    "JZ":   0x07,
    "JNZ":  0x08,
    "JN":   0x09,
    "JP":   0x0A,
    "MUL":  0x0B,
    "DIV":  0x0C,
    "CALL": 0x0D,
    "RET":  0x0E,
    "OUT":  0x0F,
    "HLT":  0x10,
    "PUSH": 0x11,
    "POP":  0x12
};

export const registers = {
    "A":  0x00,
    "B":  0x01,
    "C":  0x02,
    "D":  0x03,
    "SP": 0x04 // Stack Pointer
};