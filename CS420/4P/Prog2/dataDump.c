#include <stdio.h>

int isPrintable(char c) {
    return c >= 30 && c <= 126;
}

int dataDump(char *filename) {
    FILE *file = fopen(filename, "r");

    if (file == NULL) {
        return 1;
    }

    int currentLine[16];
    int i = 0, c = 0, k = 0, t = 0;

    while ((c = fgetc(file)) != EOF) {
        if (i == 0) {
            printf("%08d ", (k++ * 16));
        }

        currentLine[i++] = c;
        t ++;
        
        printf("%02x ", c);

        if (i == 16) {
            for (int j = 0; j < 16; j++) {
                if (isPrintable(currentLine[j])) {
                    printf("%c", currentLine[j]);
                } else {
                    printf(".");
                }
            }

            printf("\n");
            i = 0;
        }
    }

    if (i != 0) {
        for (int j = i; j < 16; j++) {
            printf("   ");
        }

        for (int j = 0; j < i; j++) {
            if (isPrintable(currentLine[j])) {
                printf("%c", currentLine[j]);
            } else {
                printf(".");
            }
        }

        printf("\n");
    }

    printf("%08d\n", t);

    fclose(file);

    return t;
}