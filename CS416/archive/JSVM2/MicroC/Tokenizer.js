export default class MicroCTokenizer {
    static Token = class {
        constructor(type, value) {
            this.type = type;
            this.value = value;
        }
    }

    static keywords = ["int", "float", "return", "if", "else", "while", "printf", "void"];

    constructor(text) {
        this.text = text;
        this.tokens = [];
        this.tokenize();
    }

    tokenize() {
        const whitespaceRegex = /^\s+/;
        const singleLineCommentRegex = /^\/\/.*/;
        const multiLineCommentRegex = /^\/\*.*?\*\//s;
        const identifierRegex = /^[a-zA-Z_]\w*/;
        const numberRegex = /^(\d+(\.\d*)?|\.\d+)([eE][+-]?\d+)?/;
        const stringRegex = /^".*?"/;
        const operatorRegex = /^[+\-*/=<>!&|^%]=?|^[[\]]/;
        const punctuationRegex = /^[,{};()]/;

        while (this.text.length > 0) {
            if (whitespaceRegex.test(this.text)) {
                this.text = this.text.replace(whitespaceRegex, "");
                continue;
            }

            if (singleLineCommentRegex.test(this.text)) {
                this.text = this.text.replace(singleLineCommentRegex, "");
                continue;
            }

            if (multiLineCommentRegex.test(this.text)) {
                this.text = this.text.replace(multiLineCommentRegex, "");
                continue;
            }

            if (identifierRegex.test(this.text)) {
                const [match] = this.text.match(identifierRegex);

                if (MicroCTokenizer.keywords.includes(match)) {
                    this.tokens.push(new MicroCTokenizer.Token("keyword", match));
                } else {
                    this.tokens.push(new MicroCTokenizer.Token("identifier", match));
                }

                this.text = this.text.slice(match.length);
                continue;
            }

            if (numberRegex.test(this.text)) {
                const [match] = this.text.match(numberRegex);
                this.tokens.push(new MicroCTokenizer.Token("number", match));
                this.text = this.text.slice(match.length);
                continue;
            }

            if (stringRegex.test(this.text)) {
                const [match] = this.text.match(stringRegex);
                this.tokens.push(new MicroCTokenizer.Token("string", match));
                this.text = this.text.slice(match.length);
                continue;
            }

            if (operatorRegex.test(this.text)) {
                const [match] = this.text.match(operatorRegex);
                this.tokens.push(new MicroCTokenizer.Token("operator", match));
                this.text = this.text.slice(match.length);
                continue;
            }

            if (punctuationRegex.test(this.text)) {
                const [match] = this.text.match(punctuationRegex);
                this.tokens.push(new MicroCTokenizer.Token("punctuation", match));
                this.text = this.text.slice(match.length);
                continue;
            }

            throw new Error(`Unexpected character: '${this.text[0]}' at position ${this.text.length}`);
        }

        return this.tokens;
    }
}