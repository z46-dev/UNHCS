import { TOKEN_TYPES } from "./Tokenizer.js";

class ASTNode {
    constructor(type, value = null, children = []) {
        this.type = type;
        this.value = value;
        this.children = children;
    }

    addChild(child) {
        this.children.push(child);
    }
}

export default class ASTBuilder {
    constructor(tokens, debug = true) {
        this.tokens = tokens;
        this.current = 0;
        this.debug = debug;

        this.program = this.build();
    }

    toString() {
        return this.nodeToString(this.program, 0);
    }

    nodeToString(node, indent = 0) {
        const indentation = "    ".repeat(indent);
        let result = `${indentation}${node.type}:\n`;

        if (node.value) {
            if (typeof node.value === "object") {
                for (const [key, value] of Object.entries(node.value)) {
                    result += `${indentation}    ${key}: ${value}\n`;
                }
            } else {
                result += `${indentation}    value: ${node.value}\n`;
            }
        }

        if (node.children && node.children.length > 0) {
            result += `${indentation}    children:\n`;
            for (const child of node.children) {
                result += this.nodeToString(child, indent + 2);
            }
        }

        return result;
    }

    build() {
        const program = new ASTNode("Program");
        while (!this.isAtEnd()) {
            // try {
                const declaration = this.declaration();
                if (declaration) {
                    program.addChild(declaration);
                }
            // } catch (error) {
            //     console.error("Error in build:", error.message);

            //     if (this.debug) {
            //         console.log(this.peekAround(5));

            //         process.exit(1);
            //     }

            //     this.synchronize();
            // }
        }

        return program;
    }

    declaration() {
        if (this.match(TOKEN_TYPES.PREPROCESSOR)) {
            return this.preprocessorDirective();
        } else if (this.match(TOKEN_TYPES.KEYWORD, "class")) {
            return this.classDeclaration();
        } else if (this.check(TOKEN_TYPES.KEYWORD)) {
            return this.functionOrVariableDeclaration();
        }

        return this.statement();
    }

    preprocessorDirective() {
        const directive = this.previous().value;
        const name = this.consume(TOKEN_TYPES.IDENTIFIER, "Expect identifier after preprocessor directive").value;
        let value = null;
        if (this.match(TOKEN_TYPES.NUMBER)) {
            value = this.previous().value;
        }
        return new ASTNode("PreprocessorDirective", { directive, name, value });
    }

    classDeclaration() {
        const name = this.consume(TOKEN_TYPES.IDENTIFIER, "Expect class name").value;
        this.consume(TOKEN_TYPES.PUNCTUATION, "{", "Expect \"{\" before class body");
        const members = [];
        while (!this.check(TOKEN_TYPES.PUNCTUATION, "}") && !this.isAtEnd()) {
            members.push(this.classMember());
        }
        this.consume(TOKEN_TYPES.PUNCTUATION, "}", "Expect \"}\" after class body");
        this.consume(TOKEN_TYPES.PUNCTUATION, ";", "Expect \";\" after class declaration");
        return new ASTNode("ClassDeclaration", { name }, members);
    }

    classMember() {
        if (this.match(TOKEN_TYPES.KEYWORD, "New")) {
            return this.constructorDeclaration();
        } else if (this.check(TOKEN_TYPES.KEYWORD)) {
            return this.functionOrVariableDeclaration(true);
        } else {
            throw new Error("Unexpected class member");
        }
    }

    constructorDeclaration() {
        const params = this.parameters();
        const body = this.block();
        return new ASTNode("ConstructorDeclaration", null, [params, body]);
    }

    functionOrVariableDeclaration(isClassMember = false) {
        const type = this.advance().value;
        let isPrivate = false;
        if (this.match(TOKEN_TYPES.PREPROCESSOR)) {
            isPrivate = true;
        }
        const name = this.consume(TOKEN_TYPES.IDENTIFIER, "Expect identifier").value;

        if (this.check(TOKEN_TYPES.PUNCTUATION, "(")) {
            const params = this.parameters();
            const body = this.functionBody();
            return new ASTNode("FunctionDeclaration", { name, returnType: type, isPrivate }, [params, body]);
        } else {
            let initializer = null;
            if (this.match(TOKEN_TYPES.OPERATOR, "=")) {
                initializer = this.expression();
            } else if (this.match(TOKEN_TYPES.PUNCTUATION, "[")) {
                this.consume(TOKEN_TYPES.PUNCTUATION, "]", "Expect \"]\" after array declaration");
                if (this.match(TOKEN_TYPES.OPERATOR, "=")) {
                    initializer = this.arrayInitializer();
                }
            }
            this.consume(TOKEN_TYPES.PUNCTUATION, ";", "Expect \";\" after variable declaration");
            return new ASTNode("VariableDeclaration", { name, type, isPrivate }, initializer ? [initializer] : []);
        }
    }

    arrayInitializer() {
        this.consume(TOKEN_TYPES.PUNCTUATION, "{", "Expect \"{\" before array initializer");
        const elements = [];
        if (!this.check(TOKEN_TYPES.PUNCTUATION, "}")) {
            do {
                elements.push(this.expression());
            } while (this.match(TOKEN_TYPES.PUNCTUATION, ","));
        }
        this.consume(TOKEN_TYPES.PUNCTUATION, "}", "Expect \"}\" after array initializer");
        return new ASTNode("ArrayInitializer", null, elements);
    }

    parameters() {
        this.consume(TOKEN_TYPES.PUNCTUATION, "(", "Expect \"(\" after function name");
        const params = [];
        if (!this.check(TOKEN_TYPES.PUNCTUATION, ")")) {
            do {
                const type = this.consume(TOKEN_TYPES.KEYWORD, "Expect parameter type").value;
                const name = this.consume(TOKEN_TYPES.IDENTIFIER, "Expect parameter name").value;
                params.push(new ASTNode("Parameter", { type, name }));
            } while (this.match(TOKEN_TYPES.PUNCTUATION, ","));
        }
        this.consume(TOKEN_TYPES.PUNCTUATION, ")", "Expect \")\" after parameters");
        return new ASTNode("Parameters", null, params);
    }

    block() {
        this.consume(TOKEN_TYPES.PUNCTUATION, "{", "Expect \"{\" before block");
        const statements = [];
        while (!this.check(TOKEN_TYPES.PUNCTUATION, "}") && !this.isAtEnd()) {
            statements.push(this.declaration());
        }
        this.consume(TOKEN_TYPES.PUNCTUATION, "}", "Expect \"}\" after block");
        return new ASTNode("Block", null, statements);
    }

    functionBody() {
        this.consume(TOKEN_TYPES.PUNCTUATION, "{", "Expect \"{\" before function body");
        const statements = [];
        while (!this.check(TOKEN_TYPES.PUNCTUATION, "}") && !this.isAtEnd()) {
            statements.push(this.statement());
        }
        this.consume(TOKEN_TYPES.PUNCTUATION, "}", "Expect \"}\" after function body");
        return new ASTNode("Block", null, statements);
    }

    statement() {
        if (this.match(TOKEN_TYPES.KEYWORD, "return")) {
            return this.returnStatement();
        } else if (this.match(TOKEN_TYPES.KEYWORD, "while")) {
            return this.whileStatement();
        } else if (this.check(TOKEN_TYPES.KEYWORD)) {
            return this.functionOrVariableDeclaration();
        }
        return this.expressionStatement();
    }

    returnStatement() {
        const value = this.expression();
        this.consume(TOKEN_TYPES.PUNCTUATION, ";", "Expect \";\" after return statement");
        return new ASTNode("ReturnStatement", null, [value]);
    }

    whileStatement() {
        this.consume(TOKEN_TYPES.PUNCTUATION, "(", "Expect \"(\" after while");
        const condition = this.expression();
        this.consume(TOKEN_TYPES.PUNCTUATION, ")", "Expect \")\" after while condition");
        const body = this.block();
        return new ASTNode("WhileStatement", null, [condition, body]);
    }

    expressionStatement() {
        const expr = this.expression();
        this.consume(TOKEN_TYPES.PUNCTUATION, ";", "Expect \";\" after expression");
        return new ASTNode("ExpressionStatement", null, [expr]);
    }

    expression() {
        return this.assignment();
    }

    assignment() {
        const expr = this.equality();
        if (this.match(TOKEN_TYPES.OPERATOR, "=") || this.match(TOKEN_TYPES.OPERATOR, "+=") || this.match(TOKEN_TYPES.OPERATOR, "*=")) {
            const operator = this.previous().value;
            const value = this.assignment();
            return new ASTNode("AssignmentExpression", { operator }, [expr, value]);
        }
        return expr;
    }

    equality() {
        let expr = this.comparison();
        while (this.match(TOKEN_TYPES.OPERATOR, "==") || this.match(TOKEN_TYPES.OPERATOR, "!=")) {
            const operator = this.previous().value;
            const right = this.comparison();
            expr = new ASTNode("BinaryExpression", { operator }, [expr, right]);
        }
        return expr;
    }

    comparison() {
        let expr = this.addition();
        while (this.match(TOKEN_TYPES.OPERATOR, ">") || this.match(TOKEN_TYPES.OPERATOR, ">=") ||
            this.match(TOKEN_TYPES.OPERATOR, "<") || this.match(TOKEN_TYPES.OPERATOR, "<=")) {
            const operator = this.previous().value;
            const right = this.addition();
            expr = new ASTNode("BinaryExpression", { operator }, [expr, right]);
        }
        return expr;
    }

    addition() {
        let expr = this.multiplication();
        while (this.match(TOKEN_TYPES.OPERATOR, "+") || this.match(TOKEN_TYPES.OPERATOR, "-")) {
            const operator = this.previous().value;
            const right = this.multiplication();
            expr = new ASTNode("BinaryExpression", { operator }, [expr, right]);
        }
        return expr;
    }

    multiplication() {
        let expr = this.unary();
        while (this.match(TOKEN_TYPES.OPERATOR, "*") || this.match(TOKEN_TYPES.OPERATOR, "/")) {
            const operator = this.previous().value;
            const right = this.unary();
            expr = new ASTNode("BinaryExpression", { operator }, [expr, right]);
        }
        return expr;
    }

    unary() {
        if (this.match(TOKEN_TYPES.OPERATOR, "!") || this.match(TOKEN_TYPES.OPERATOR, "-") || this.match(TOKEN_TYPES.OPERATOR, "&")) {
            const operator = this.previous().value;
            const right = this.unary();
            return new ASTNode("UnaryExpression", { operator }, [right]);
        }
        return this.postfix();
    }

    postfix() {
        let expr = this.primary();
        while (this.match(TOKEN_TYPES.OPERATOR, "++") || this.match(TOKEN_TYPES.OPERATOR, "--") ||
            this.match(TOKEN_TYPES.PUNCTUATION, "(") || this.match(TOKEN_TYPES.PUNCTUATION, "[") ||
            this.match(TOKEN_TYPES.PUNCTUATION, ".") || this.match(TOKEN_TYPES.MACRO)) {
            if (this.previous().type === TOKEN_TYPES.OPERATOR) {
                expr = new ASTNode("PostfixExpression", { operator: this.previous().value }, [expr]);
            } else if (this.previous().value === "(") {
                expr = this.finishCall(expr);
            } else if (this.previous().value === "[") {
                const index = this.expression();
                this.consume(TOKEN_TYPES.PUNCTUATION, "]", "Expect \"]\" after array index");
                expr = new ASTNode("ArrayAccess", null, [expr, index]);
            } else if (this.previous().value === ".") {
                const name = this.consume(TOKEN_TYPES.IDENTIFIER, "Expect property name after \".\"");
                expr = new ASTNode("MemberAccess", { name: name.value }, [expr]);
            } else if (this.previous().type === TOKEN_TYPES.MACRO) {
                const name = this.consume(TOKEN_TYPES.IDENTIFIER, "Expect identifier after macro");
                expr = new ASTNode("MacroExpression", { macro: this.previous().value, name: name.value }, [expr]);
            }
        }
        return expr;
    }

    finishCall(callee) {
        const args = [];
        if (!this.check(TOKEN_TYPES.PUNCTUATION, ")")) {
            do {
                args.push(this.expression());
            } while (this.match(TOKEN_TYPES.PUNCTUATION, ","));
        }
        this.consume(TOKEN_TYPES.PUNCTUATION, ")", "Expect \")\" after arguments");
        return new ASTNode("CallExpression", null, [callee, ...args]);
    }

    primary() {
        if (this.match(TOKEN_TYPES.NUMBER)) {
            return new ASTNode("Literal", { value: this.previous().value, type: "number" });
        }
        if (this.match(TOKEN_TYPES.STRING)) {
            return new ASTNode("Literal", { value: this.previous().value, type: "string" });
        }
        if (this.match(TOKEN_TYPES.IDENTIFIER)) {
            return new ASTNode("Identifier", { name: this.previous().value });
        }
        if (this.match(TOKEN_TYPES.PUNCTUATION, "(")) {
            const expr = this.expression();
            this.consume(TOKEN_TYPES.PUNCTUATION, ")", "Expect \")\" after expression");
            return new ASTNode("GroupingExpression", null, [expr]);
        }
        throw new Error(`Unexpected token: ${JSON.stringify(this.peek())}`);
    }

    match(type, value = null) {
        if (this.check(type, value)) {
            this.advance();
            return true;
        }
        return false;
    }

    check(type, value = null) {
        if (this.isAtEnd()) return false;
        if (value === null) {
            return this.peek().type === type;
        }
        return this.peek().type === type && this.peek().value === value;
    }

    advance() {
        if (!this.isAtEnd()) this.current++;
        return this.previous();
    }

    consume(type, valueOrMessage, messageOrNull = null) {
        // if (this.check(type, valueOrMessage)) {
        //     return this.advance();
        // }

        // let errorMessage;
        // if (messageOrNull === null) {
        //     errorMessage = valueOrMessage;
        // } else {
        //     errorMessage = messageOrNull;
        // }

        // throw new Error(`${errorMessage} at token: ${JSON.stringify(this.peekAround())}`);

        if (arguments.length === 2) {
            messageOrNull = valueOrMessage;
            valueOrMessage = null;
        }

        if (this.check(type, valueOrMessage)) {
            return this.advance();
        }

        throw new Error(`${messageOrNull} at token: ${JSON.stringify(this.peekAround())}`);
    }

    peek() {
        return this.tokens[this.current];
    }

    peekAround(distance = 2) {
        const tokens = [];
        for (let i = Math.max(0, this.current - distance); i < Math.min(this.tokens.length, this.current + distance + 1); i++) {
            tokens.push(this.tokens[i]);
        }
        return tokens;
    }

    previous() {
        return this.tokens[this.current - 1];
    }

    isAtEnd() {
        return this.current >= this.tokens.length;
    }

    synchronize() {
        this.advance();
        while (!this.isAtEnd()) {
            if (this.previous().type === TOKEN_TYPES.PUNCTUATION && this.previous().value === ";") return;
            switch (this.peek().type) {
                case TOKEN_TYPES.KEYWORD:
                    return;
            }
            this.advance();
        }
    }
}