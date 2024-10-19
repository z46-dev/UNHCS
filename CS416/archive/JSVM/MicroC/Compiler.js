export default class MicroCToAssembly {
    constructor(tokenizer) {
        this.tokenizer = tokenizer;
        this.tokens = tokenizer.tokens;
        this.currentToken = 0;
        this.output = "";
        this.variables = {};
        this.functions = {};
        this.currentFunction = null;
        this.labelCount = 0;
        this.nextMemoryAddress = 0;
    }

    compile() {
        while (this.currentToken < this.tokens.length) {
            this.parseTopLevelDeclaration();
        }
        this.output += "HLT\n";
        return this.output;
    }

    parseTopLevelDeclaration() {
        if (this.match("keyword", "int")) {
            const identifier = this.consume("identifier").value;
            if (this.match("punctuation", "(")) {
                this.parseFunctionDefinition(identifier);
            } else {
                this.parseGlobalVariableDeclaration(identifier);
            }
        } else {
            throw new Error(`Unexpected token: ${this.peek().value}`);
        }
    }

    parseGlobalVariableDeclaration(identifier) {
        this.variables[identifier] = this.getNextMemoryAddress();
        if (this.match("operator", "=")) {
            const value = this.consume("number").value;
            this.output += `LDI ${value}\n`;
            this.output += `STA ${this.variables[identifier]}\n`;
        }
        this.consume("punctuation", ";");
    }

    parseFunctionDefinition(name) {
        this.currentFunction = name;
        this.functions[name] = { params: [], localVars: {} };
        this.output += `${name}:\n`;

        this.consume("punctuation", "(");
        this.parseFunctionParameters();
        this.consume("punctuation", ")");
        this.parseCompoundStatement();

        if (name === "main") {
            this.output += "HLT\n";
        } else {
            this.output += "RET\n";
        }

        this.currentFunction = null;
    }

    parseFunctionParameters() {
        if (!this.check("punctuation", ")")) {
            do {
                if (this.match("keyword", "int")) {
                    const param = this.consume("identifier").value;
                    this.functions[this.currentFunction].params.push(param);
                    this.variables[param] = this.getNextMemoryAddress();
                }
            } while (this.match("punctuation", ","));
        }
    }

    parseCompoundStatement() {
        this.consume("punctuation", "{");
        while (!this.check("punctuation", "}")) {
            this.parseStatement();
        }
        this.consume("punctuation", "}");
    }

    parseStatement() {
        if (this.match("keyword", "int")) {
            this.parseVariableDeclaration();
        } else if (this.match("keyword", "return")) {
            this.parseReturnStatement();
        } else if (this.match("keyword", "printf")) {
            this.parsePrintfStatement();
        } else if (this.check("identifier")) {
            this.parseAssignmentOrFunctionCall();
        } else {
            throw new Error(`Unexpected token: ${this.peek().value}`);
        }
    }

    parseVariableDeclaration() {
        const identifier = this.consume("identifier").value;
        this.variables[identifier] = this.getNextMemoryAddress();
        if (this.match("operator", "=")) {
            this.parseExpression();
            this.output += `STA ${this.variables[identifier]}\n`;
        }
        this.consume("punctuation", ";");
    }

    parseReturnStatement() {
        this.parseExpression();
        this.output += `RET\n`;
        this.consume("punctuation", ";");
    }

    parsePrintfStatement() {
        this.consume("punctuation", "(");
        this.parseExpression();
        this.output += `OUT\n`;
        this.consume("punctuation", ")");
        this.consume("punctuation", ";");
    }

    parseAssignmentOrFunctionCall() {
        const identifier = this.consume("identifier").value;
        if (this.match("punctuation", "(")) {
            this.parseFunctionCall(identifier);
            this.consume("punctuation", ";");
        } else {
            this.consume("operator", "=");
            this.parseExpression();
            this.output += `STA ${this.variables[identifier]}\n`;
            this.consume("punctuation", ";");
        }
    }

    parseFunctionCall(name) {
        const args = [];
        if (!this.check("punctuation", ")")) {
            do {
                this.parseExpression();
                args.push("arg");
            } while (this.match("punctuation", ","));
        }
        this.consume("punctuation", ")");

        for (let i = args.length - 1; i >= 0; i--) {
            this.output += `PUSH\n`;
        }
        this.output += `CALL ${name}\n`;
        if (args.length > 0) {
            this.output += `ADD 0 ${args.length}\n`;
            this.output += `STA SP\n`;
        }
    }

    parseExpression() {
        this.parseTerm();
        while (this.match("operator", "+") || this.match("operator", "-")) {
            const operator = this.previous().value;
            this.output += `PUSH\n`;
            this.parseTerm();
            this.output += `POP\n`;
            this.output += `STA B\n`;
            this.output += operator === "+" ? `ADD 1 B\n` : `SUB 1 B\n`;
        }
    }

    parseTerm() {
        this.parseFactor();
        while (this.match("operator", "*") || this.match("operator", "/")) {
            const operator = this.previous().value;
            this.output += `PUSH\n`;
            this.parseFactor();
            this.output += `POP\n`;
            this.output += `STA B\n`;
            this.output += operator === "*" ? `MUL 1 B\n` : `DIV 1 B\n`;
        }
    }

    parseFactor() {
        if (this.match("number")) {
            this.output += `LDI ${this.previous().value}\n`;
        } else if (this.match("identifier")) {
            const identifier = this.previous().value;
            if (this.match("punctuation", "(")) {
                this.parseFunctionCall(identifier);
            } else {
                this.output += `LDA ${this.variables[identifier]}\n`;
            }
        } else if (this.match("punctuation", "(")) {
            this.parseExpression();
            this.consume("punctuation", ")");
        } else {
            throw new Error(`Unexpected token: ${this.peek().value}`);
        }
    }

    match(type, value) {
        if (this.check(type, value)) {
            this.currentToken++;
            return true;
        }
        return false;
    }

    check(type, value) {
        if (this.isAtEnd()) return false;
        if (this.peek().type !== type) return false;
        if (value !== undefined && this.peek().value !== value) return false;
        return true;
    }

    consume(type, value) {
        if (this.check(type, value)) {
            return this.advance();
        }
        throw new Error(`Expected ${type} ${value}, found ${this.peek().type} ${this.peek().value}`);
    }

    advance() {
        if (!this.isAtEnd()) this.currentToken++;
        return this.previous();
    }

    peek() {
        return this.tokens[this.currentToken];
    }

    previous() {
        return this.tokens[this.currentToken - 1];
    }

    isAtEnd() {
        return this.currentToken >= this.tokens.length;
    }

    getNextMemoryAddress() {
        return this.nextMemoryAddress++;
    }
}