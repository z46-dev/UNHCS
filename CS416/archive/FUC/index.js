import Tokenizer from "./Tokenizer.js";
import ASTBuilder from "./ASTBuilder.js";
import fs from "fs";

async function loadFile(path) {
    if (typeof require !== "undefined") {
        const fs = require("fs");
        return fs.readFileSync(path, "utf-8");
    }

    const response = await fetch(path);
    return response.text();
}

const fileContent = await loadFile("./FUC/main.fuc.c");

const tokenizer = new Tokenizer(fileContent);
const astBuilder = new ASTBuilder(tokenizer.toJSON());
fs.writeFileSync("./FUC/output.txt", astBuilder.toString(), "utf-8");