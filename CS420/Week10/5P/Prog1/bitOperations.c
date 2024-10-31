#include <stdio.h>
#include <stdlib.h>
#include "./bitOperations.h"

typedef struct {
    unsigned int day;
    unsigned int month;
    unsigned int year;
} Date;

char* printBinary(unsigned short int num) {
    char* binary = (char*) malloc(17 * sizeof(char));

    for (int i = 0; i < 16; i++) {
        binary[15 - i] = (num & (1 << i)) ? '1' : '0';
    }

    binary[16] = '\0';
    return binary;
}

unsigned short int reverseBits(unsigned short int num) {
    unsigned short int reversed = 0;

    for (int i = 0; i < 16; i ++) {
        reversed <<= 1;
        reversed |= (num & 1);
        num >>= 1;
    }

    return reversed;
}

unsigned short int circularShiftRight(unsigned short int num, unsigned short int n) {
    return (num >> n) | (num << (16 - n));
}

int convertDate(char inputFile[], char outputFile[]) {
    FILE *in = fopen(inputFile, "r");

    if (in == NULL) {
        return INVALID_INPUT_FILE;
    }

    FILE *out = fopen(outputFile, "w");

    if (out == NULL) {
        return INVALID_OUTPUT_FILE;
    }

    // Parse mm/dd/yy on each line
    Date date;

    while (fscanf(in, "%d/%d/%d", &date.month, &date.day, &date.year) != EOF) {
        unsigned short int binDate = date.year | (date.month << 7) | (date.day << 11);
        unsigned short int reversedBinDate = reverseBits(binDate);
        unsigned short int n = reversedBinDate & 0xF;
        unsigned short int shifted = circularShiftRight(reversedBinDate, n);

        int year = shifted & 0x7F;
        int month = (shifted >> 11) & 0xF;
        int day = (shifted >> 7) & 0xF;

        int isValid = (month >= 1 && month <= 12) && (day >= 1 && day <= 31) && (year >= 0 && year <= 99);

        fprintf(out, "%02d/%02d/%02d %04X %04X %04X %02d/%02d/%02d %s\n", date.month, date.day, date.year, binDate, reversedBinDate, shifted, month, day, year, isValid ? "" : "INVALID");
    }

    fclose(in);
    fclose(out);

    return NORMAL_SUCCESSFUL_COMPLETION;
}

int main(int argc, char** argv) {
    if (argc != 3) {
        printf("Usage: %s <input file> <output file>\n", argv[0]);
        return 1;
    }

    convertDate(argv[1], argv[2]);

    return 0;
}