#include <stdio.h>
#include <stdlib.h>
#include "./bitOperations.h"

int main() {
    char *fileName = "./input.txt", *outputFile = "./output.txt";

    int result = convertDate(fileName, outputFile);

    if (result == INVALID_INPUT_FILE) {
        printf("Invalid input file\n");
    } else if (result == INVALID_OUTPUT_FILE) {
        printf("Invalid output file\n");
    } else {
        printf("Conversion successful\n");
    }
    
    return 0;
}