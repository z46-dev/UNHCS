#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main(int argc, char *argv[]) {
    printf("Filename: %s\n", argv[1]);

    FILE *file = fopen(argv[1], "r");

    if (file == NULL) {
        printf("File not found\n");
        return 1;
    }

    char line[100];
    int count = 0;

    const char *QUERY_STRINGS[] = {"Johnson", "Roosevelt"};
    
    while (fscanf(file, "%s", line) != EOF) {
        for (int i = 0; i < 2; i++) {
            if (strstr(line, QUERY_STRINGS[i]) != NULL) {
                printf("%d. %s\n", ++count, line);
                break;
            }
        }
    }

    fclose(file);

    return 0;
}