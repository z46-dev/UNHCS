#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define INPUT_SIZE 16
#define FLAG_VALIDITY 1
#define FLAG_EXIT 2

// Check if the number is valid or not
int isValidNumber(const char *str) {
    char *end;
    strtod(str, &end);
    return *end == '\0' && *str != '\0';
}

// Read input and determine if it's valid or not and if we should exit or not
int userInput(char input[], int *flags) {
    if (scanf("%s", input) == EOF) {
        *flags = FLAG_EXIT;
        return 0;
    }

    *flags = FLAG_VALIDITY;

    if (strcasecmp(input, "exit") == 0) {
        *flags = (char) (FLAG_EXIT | FLAG_VALIDITY);
        return 0;
    }

    if (!isValidNumber(input)) {
        *flags = 0;
    }

    if (*flags == 0) {
        printf("Wrong query input\n");
        return 0;
    }

    return atoi(input);
}

// Main function to read, parse, and interpret data
int calSpan(int maxSize, char *filename, double data[]) {
    FILE *file = fopen(filename, "r");

    if (file == NULL) {
        printf("File not found\n");
        return -1;
    }

    char input[INPUT_SIZE];
    int i = 0;

    while (fscanf(file, "%s", input) != EOF && i < maxSize) {
        double x = isValidNumber(input) ? atof(input) : 0;
        data[i++] = (x >= -100 && x <= 100) ? x : 0;
    }

    fclose(file);


    // Accept inputs from user for minute number until exit is entered
    int flags, value;
    while (printf("Which minute to query? ") && (value = userInput(input, &flags), !(flags & FLAG_EXIT))) {
        if (flags & FLAG_VALIDITY) {
            if (value < 0 || value >= i) {
                printf("Query minute out of range\n");
            } else if (data[value] == 0) {
                printf("Data at minute %d is corrupted\n", value);
            } else {
                int span = 1;
                for (int j = value - 1; j >= 0 && data[j] <= data[value]; j--) {
                    span++;
                }

                printf("Data at minute %d is a %d-minute high\n", value, span);
            }
        }

        // The above could have been nicer using break/continues in my opinion
    }

    return i;
}