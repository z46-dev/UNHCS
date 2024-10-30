#include <stdio.h>
#include <string.h>

int main(int argc, char *argv[]) {
    // Arg1: <> Length=<>
    // Arg2: <> Input=<>
    // Arg1 & Arg2 concatenated: <>

    printf("Arg1: %s Length=%d\n", argv[1], strlen(argv[1]));
    printf("Arg2: %s Length=%d\n", argv[2], strlen(argv[2]));
    printf("Arg1 & Arg2 concatenated: %s\n", strcat(argv[1], argv[2]));
    printf("Arg3 converted to uppercase: %s\n", strupr(argv[3]));

    // Compare arg 4 and arg5
    int result = strcmp(argv[4], argv[5]);
    if (result == 0) {
        printf("%s and %s are equal\n", argv[4], argv[5]);
    } else if (result < 0) {
        printf("%s is greater than %s\n", argv[5], argv[4]);
    } else {
        printf("%s is greater than %s\n", argv[4], argv[5]);
    }

    // convert arg6 to floating point and
    printf("Arg6 converted to floating point: %f\n", atof(argv[6]));
}