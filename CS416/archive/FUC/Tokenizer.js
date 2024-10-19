export const TOKEN_TYPES = {
    COMMENT: 0,
    PREPROCESSOR: 1,
    NUMBER: 2,
    STRING: 3,
    KEYWORD: 4,
    MACRO: 5,
    OPERATOR: 6,
    PUNCTUATION: 7,
    IDENTIFIER: 8,
    WHITESPACE: 9
};

export const TOKEN_TYPE_NAMES = Object.fromEntries(Object.entries(TOKEN_TYPES).map(([key, value]) => [value, key]));

const tokenSpecification = [
    [TOKEN_TYPES.COMMENT, /^\/\/.*|^\/\*[\s\S]*?\*\//],
    [TOKEN_TYPES.PREPROCESSOR, /^#\w+/],
    [TOKEN_TYPES.NUMBER, /^\d+(\.\d*)?/],
    [TOKEN_TYPES.STRING, /^"[^"]*"/],
    [TOKEN_TYPES.KEYWORD, /^(int|void|class|return|while|if|else|New)\b/],
    [TOKEN_TYPES.MACRO, /^::/],
    [TOKEN_TYPES.OPERATOR, /^([+\-*/=<>!%&|^]=?|\+\+|--|&&|\|\||\*=|<<=?|>>=?|~|\?|:)/],
    [TOKEN_TYPES.PUNCTUATION, /^[{}()\[\];,.]/],
    [TOKEN_TYPES.IDENTIFIER, /^[a-zA-Z_]\w*/],
    [TOKEN_TYPES.WHITESPACE, /^\s+/]
];

class Token {
    constructor(type, value, start, end) {
        this.type = type;
        this.value = value;
        this.start = start;
        this.end = end;
    }

    get isImportant() {
        return this.type !== TOKEN_TYPES.WHITESPACE && this.type !== TOKEN_TYPES.COMMENT;
    }

    toString() {
        return `${this.type}: ${JSON.stringify(this.value)} (${this.start}-${this.end})`;
    }

    toJSON() {
        return {
            type: this.type,
            typeName: TOKEN_TYPE_NAMES[this.type],
            value: this.value
        };
    }
}

export default class Tokenizer {
    constructor(input) {
        this.tokens = [];
        this.input = input;
        this.position = 0;
        this.tokenize();
    }

    tokenize() {
        while (this.position < this.input.length) {
            let matched = false;
            for (const [type, regex] of tokenSpecification) {
                const match = regex.exec(this.input.slice(this.position));
                if (match) {
                    const value = match[0];
                    this.tokens.push(new Token(type, value, this.position, this.position + value.length));
                    this.position += value.length;
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                console.error(`Unexpected character at position ${this.position}: ${JSON.stringify(this.input[this.position])}`);
                this.position++;
            }
        }
    }

    getTokens() {
        return this.tokens;
    }

    toJSON() {
        return this.tokens.filter(token => token.isImportant).map(token => token.toJSON());
    }
}