#include <stdio.h>
#include <stdlib.h>

int main(int argc, char *argv[]) {
    printf("Filenames: %s, %s, %s\n", argv[1], argv[2], argv[3]);

    FILE *files[3] = {fopen(argv[1], "r"), fopen(argv[2], "r"), fopen(argv[3], "w")};

    for (int i = 0; i < 3; i++) {
        if (files[i] == NULL) {
            printf("File %d not found\n", i + 1);
            return 1;
        }
    }

    char line[100];
    int count = 0;

    while (fscanf(files[0], "%s", line) != EOF) {
        fprintf(files[2], "%d. %s\n", ++count, line);
    }

    while (fscanf(files[1], "%s", line) != EOF) {
        fprintf(files[2], "%d. %s\n", ++count, line);
    }

    for (int i = 0; i < 3; i++) {
        fclose(files[i]);
    }

    return 0;
}