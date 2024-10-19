export const opcodes = {
    "LDI": 0x01,
    "MOV": 0x02,
    "ADD": 0x03,
    "SUB": 0x04,
    "MUL": 0x05,
    "DIV": 0x06,
    "AND": 0x07,
    "OR": 0x08,
    "XOR": 0x09,
    "SHL": 0x0A,
    "SHR": 0x0B,
    "JMP": 0x0C,
    "JZ": 0x0D,
    "JNZ": 0x0E,
    "JG": 0x0F,
    "JL": 0x10,
    "LOAD": 0x11,
    "STORE": 0x12,
    "PUSH": 0x13,
    "POP": 0x14,
    "CALL": 0x15,
    "RET": 0x16,
    "OUT": 0x17,
    "HLT": 0xFF
};

export const reverseOpcodes = Object.fromEntries(Object.entries(opcodes).map(([k, v]) => [v, k]));