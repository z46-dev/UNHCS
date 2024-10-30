#include <stdio.h>
#include <stdlib.h>

typedef struct {
    unsigned int day;
    unsigned int month;
    unsigned int year;
} Date;

int convertDate(char* inputFile, char* outputFile) {
    FILE *in = fopen(inputFile, "r");

    if (in == NULL) {
        printf("Error: Could not open file %s\n", inputFile);
        return 1;
    }

    FILE *out = fopen(outputFile, "w");

    // Parse mm/dd/yy on each line
    Date date;

    while (fscanf(in, "%d/%d/%d", &date.month, &date.day, &date.year) != EOF) {
        short int binDate = date.year | (date.month << 7) | (date.day << 11);

        // Write binary date to output file
        fprintf(out, "0x%x\n", binDate);
        fprintf(out, "%d/%d/%d\n", date.month, date.day, date.year);
    }

    fclose(in);
    fclose(out);
}

int main(int argc, char** argv) {
    if (argc != 3) {
        printf("Usage: %s <input file> <output file>\n", argv[0]);
        return 1;
    }

    convertDate(argv[1], argv[2]);

    return 0;
}