export class ASTNode {
    constructor(type) {
        this.type = type;
    }
}

export class Program extends ASTNode {
    constructor() {
        super("Program");
        this.functions = [];
    }
}

export class FunctionDeclaration extends ASTNode {
    constructor(returnType, name, params, body) {
        super("FunctionDeclaration");
        this.returnType = returnType;
        this.name = name;
        this.params = params;
        this.body = body;
    }
}

export class Parameter extends ASTNode {
    constructor(type, name) {
        super("Parameter");
        this.type = type;
        this.name = name;
    }
}

export class VariableDeclaration extends ASTNode {
    constructor(type, name, initialValue) {
        super("VariableDeclaration");
        this.vType = type;
        this.name = name;
        this.initialValue = initialValue;
    }
}

export class FunctionCall extends ASTNode {
    constructor(name, args) {
        super("FunctionCall");
        this.name = name;
        this.args = args;
    }
}

export class BinaryExpression extends ASTNode {
    constructor(operator, left, right) {
        super("BinaryExpression");
        this.operator = operator;
        this.left = left;
        this.right = right;
    }
}

export class Identifier extends ASTNode {
    constructor(name) {
        super("Identifier");
        this.name = name;
    }
}

export class NumberLiteral extends ASTNode {
    constructor(value) {
        super("NumberLiteral");
        this.value = value;
    }
}

export class ReturnStatement extends ASTNode {
    constructor(argument) {
        super("ReturnStatement");
        this.argument = argument;
    }
}

export class ArrayAccess extends ASTNode {
    constructor(array, index) {
        super("ArrayAccess");
        this.array = array;
        this.index = index;
    }
}

export class Parser {
    constructor(tokens) {
        this.tokens = tokens;
        this.current = 0;
    }

    parse() {
        const program = new Program();
        while (!this.isAtEnd()) {
            program.functions.push(this.parseFunctionDeclaration());
        }
        return program;
    }

    parseFunctionDeclaration() {
        const returnType = this.consume("keyword").value;
        const name = this.consume("identifier").value;
        this.consume("punctuation", "(");
        const params = [];
        if (!this.check("punctuation", ")")) {
            do {
                const paramType = this.consume("keyword").value;
                const paramName = this.consume("identifier").value;
                params.push(new Parameter(paramType, paramName));
            } while (this.match("punctuation", ","));
        }
        this.consume("punctuation", ")");
        const body = this.parseBlock();
        return new FunctionDeclaration(returnType, name, params, body);
    }

    parseBlock() {
        this.consume("punctuation", "{");
        const statements = [];
        while (!this.check("punctuation", "}")) {
            statements.push(this.parseStatement());
        }
        this.consume("punctuation", "}");
        return statements;
    }

    parseStatement() {
        if (this.match("keyword", "int") || this.match("keyword", "float")) {
            return this.parseVariableDeclaration();
        } else if (this.match("keyword", "return")) {
            return this.parseReturnStatement();
        } else if (this.check("keyword", "printf")) {
            return this.parsePrintfStatement();
        } else {
            return this.parseExpressionStatement();
        }
    }

    parseVariableDeclaration() {
        const type = this.previous().value;
        const name = this.consume("identifier").value;
        let initialValue = null;
        if (this.match("operator", "=")) {
            initialValue = this.parseExpression();
        }
        this.consume("punctuation", ";");
        return new VariableDeclaration(type, name, initialValue);
    }

    parseReturnStatement() {
        const argument = this.parseExpression();
        this.consume("punctuation", ";");
        return new ReturnStatement(argument);
    }

    parsePrintfStatement() {
        this.consume("keyword", "printf");
        this.consume("punctuation", "(");
        const args = [this.parseExpression()];
        this.consume("punctuation", ")");
        this.consume("punctuation", ";");
        return new FunctionCall("printf", args);
    }

    parseExpressionStatement() {
        const expr = this.parseExpression();
        this.consume("punctuation", ";");
        return expr;
    }

    parseExpression() {
        return this.parseAssignment();
    }

    parseAssignment() {
        let expr = this.parseAdditive();

        if (this.match("operator", "=")) {
            const value = this.parseAssignment();
            if (expr instanceof Identifier) {
                return new BinaryExpression("=", expr, value);
            } else {
                throw new Error("Invalid assignment target");
            }
        }

        return expr;
    }

    parseAdditive() {
        let expr = this.parseMultiplicative();

        while (this.match("operator", "+") || this.match("operator", "-")) {
            const operator = this.previous().value;
            const right = this.parseMultiplicative();
            expr = new BinaryExpression(operator, expr, right);
        }

        return expr;
    }

    parseMultiplicative() {
        let expr = this.parsePrimary();

        while (this.match("operator", "*") || this.match("operator", "/") || this.match("operator", "%")) {
            const operator = this.previous().value;
            const right = this.parsePrimary();
            expr = new BinaryExpression(operator, expr, right);
        }

        return expr;
    }

    parsePrimary() {
        if (this.match("number")) {
            return new NumberLiteral(this.previous().value);
        }

        if (this.match("identifier")) {
            const name = this.previous().value;
            if (this.match("punctuation", "(")) {
                return this.parseFinishFunctionCall(name);
            } else if (this.match("operator", "[")) {
                return this.parseFinishArrayAccess(name);
            }
            return new Identifier(name);
        }

        if (this.match("punctuation", "(")) {
            const expr = this.parseExpression();
            this.consume("punctuation", ")");
            return expr;
        }

        throw new Error("Unexpected token");
    }

    parseFinishFunctionCall(name) {
        const args = [];
        if (!this.check("punctuation", ")")) {
            do {
                args.push(this.parseExpression());
            } while (this.match("punctuation", ","));
        }
        this.consume("punctuation", ")");
        return new FunctionCall(name, args);
    }

    parseFinishArrayAccess(name) {
        const index = this.parseExpression();
        this.consume("operator", "]");
        return new ArrayAccess(new Identifier(name), index);
    }

    consume(type, value) {
        if (this.check(type, value)) {
            return this.advance();
        }
        throw new Error(`Expected ${value} but got ${this.peek().value}`);
    }

    match(type, value) {
        if (this.check(type, value)) {
            this.advance();
            return true;
        }
        return false;
    }

    check(type, value) {
        if (this.isAtEnd()) return false;
        return this.peek().type === type && (!value || this.peek().value === value);
    }

    advance() {
        if (!this.isAtEnd()) this.current++;
        return this.previous();
    }

    peek() {
        return this.tokens[this.current];
    }

    previous() {
        return this.tokens[this.current - 1];
    }

    isAtEnd() {
        return this.current >= this.tokens.length;
    }
}