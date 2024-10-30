#include <stdio.h>

int dataDump(char *filename) {
    FILE *file = fopen(filename, "r");

    if (file == NULL) {
        return 1;
    }

    int currentLine[16];
    int currPtr = 0, curr = 0, lineCounter = 0, total = 0;

    while ((curr = fgetc(file)) != EOF) {
        if (currPtr == 0) {
            printf("%08d ", (lineCounter++ * 16));
        }

        currentLine[currPtr++] = curr;
        total ++;
        
        printf("%02x ", curr);

        if (currPtr == 16) {
            for (int j = 0; j < 16; j++) {
                if (currentLine[j] >= 30 && currentLine[j] <= 126) {
                    printf("%c", currentLine[j]);
                } else {
                    printf(".");
                }
            }

            printf("\n");
            currPtr = 0;
        }
    }

    if (currPtr != 0) {
        for (int j = currPtr; j < 16; j++) {
            printf("   ");
        }

        for (int j = 0; j < currPtr; j++) {
            if (currentLine[j] >= 30 && currentLine[j] <= 126) {
                printf("%c", currentLine[j]);
            } else {
                printf(".");
            }
        }

        printf("\n");
    }

    printf("%08d\n", total);
    fclose(file);

    return total;
}