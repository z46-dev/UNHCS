export class CodeGenerator {
    constructor() {
        this.assembly = [];
        this.variableMap = new Map();
        this.nextRegister = 1; // R0 is reserved for return values
        this.labelCounter = 0;
        this.isMainFunction = false;
    }

    generate(ast) {
        this.generateProgram(ast);
        // Add entry point
        this.assembly.unshift("_start:");
        this.assembly.unshift("; Program entry point");
        this.assembly.splice(2, 0, "    CALL main");
        this.assembly.splice(3, 0, "    HLT");
        return this.assembly.join("\n");
    }

    generateProgram(program) {
        for (const func of program.functions) {
            this.generateFunction(func);
        }
    }

    generateFunction(func) {
        this.variableMap.clear();
        this.nextRegister = 1;
        this.isMainFunction = func.name === "main";
        this.assembly.push(`${func.name}:`);
        if (this.isMainFunction) {
            this.assembly.push("    LDI R14, 1024    ; Initialize stack pointer");
        }
        
        // Handle parameters
        for (let i = 0; i < func.params.length; i++) {
            const reg = this.getNextRegister();
            this.variableMap.set(func.params[i].name, reg);
            this.assembly.push(`    POP R${reg}    ; Get parameter ${func.params[i].name}`);
        }

        for (const statement of func.body) {
            this.generateStatement(statement);
        }

        // Add a default return if not present
        if (func.body[func.body.length - 1].type !== "ReturnStatement") {
            this.assembly.push("    LDI R0, 0    ; Default return value");
            this.assembly.push("    RET");
        }
    }

    generateStatement(statement) {
        this.assembly.push(`    ; Statement: ${statement.type}`);
        switch (statement.type) {
            case "ReturnStatement":
                this.generateExpression(statement.argument);
                this.assembly.push("    MOV R0, R1    ; Move return value to R0");
                this.assembly.push("    RET");
                break;
            case "FunctionCall":
                this.generateFunctionCall(statement);
                break;
            case "VariableDeclaration":
                this.generateVariableDeclaration(statement);
                break;
            default:
                throw new Error(`Unsupported statement type: ${statement.type}`);
        }
    }

    generateVariableDeclaration(variable) {
        const reg = this.getNextRegister();
        this.variableMap.set(variable.name, reg);
        this.generateExpression(variable.initialValue);
        this.assembly.push(`    MOV R${reg}, R1    ; Initialize ${variable.name}`);
    }

    generateExpression(expr) {
        this.assembly.push(`    ; Expression: ${expr.type}`);
        switch (expr.type) {
            case "BinaryExpression":
                this.generateBinaryExpression(expr);
                break;
            case "Identifier":
                this.generateIdentifier(expr);
                break;
            case "NumberLiteral":
                this.generateNumberLiteral(expr);
                break;
            case "FunctionCall":
                this.generateFunctionCall(expr);
                break;
            default:
                throw new Error(`Unsupported expression type: ${expr.type}`);
        }
    }

    generateBinaryExpression(expr) {
        this.generateExpression(expr.right);
        this.assembly.push("    PUSH R1");
        this.generateExpression(expr.left);
        this.assembly.push("    POP R2");
        switch (expr.operator) {
            case "+":
                this.assembly.push("    ADD R1, R1, R2");
                break;
            case "-":
                this.assembly.push("    SUB R1, R1, R2");
                break;
            case "*":
                this.assembly.push("    MUL R1, R1, R2");
                break;
            case "/":
                this.assembly.push("    DIV R1, R1, R2");
                break;
            default:
                throw new Error(`Unsupported operator: ${expr.operator}`);
        }
    }

    generateIdentifier(identifier) {
        const reg = this.variableMap.get(identifier.name);
        if (reg === undefined) {
            throw new Error(`Undefined variable: ${identifier.name}`);
        }
        this.assembly.push(`    MOV R1, R${reg}    ; Load ${identifier.name}`);
    }

    generateNumberLiteral(literal) {
        this.assembly.push(`    LDI R1, ${literal.value}`);
    }

    generateFunctionCall(call) {
        if (call.name === "printf") {
            this.generateExpression(call.args[0]);
            this.assembly.push("    OUT R1");
        } else {
            // Push arguments onto the stack in reverse order
            for (let i = call.args.length - 1; i >= 0; i--) {
                this.generateExpression(call.args[i]);
                this.assembly.push("    PUSH R1");
            }
            this.assembly.push(`    CALL ${call.name}`);
            // Clean up the stack
            if (call.args.length > 0) {
                this.assembly.push(`    ADD R14, R14, ${call.args.length}    ; Adjust stack pointer`);
            }
            this.assembly.push("    MOV R1, R0    ; Move return value to R1");
        }
    }

    getNextRegister() {
        if (this.nextRegister >= 14) {
            throw new Error("Register overflow. Too many variables.");
        }
        return this.nextRegister++;
    }

    newLabel() {
        return `L${this.labelCounter++}`;
    }
}