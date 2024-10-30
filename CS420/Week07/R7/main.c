#include <stdio.h>

unsigned int AND(unsigned int x, unsigned int y) {
    return x & y;
}

unsigned int OR(unsigned int x, unsigned int y) {
    return x | y;
}

unsigned int XOR(unsigned int x, unsigned int y) {
    return x ^ y;
}

unsigned int NOT(unsigned int x) {
    return ~x;
}

unsigned int SHL(unsigned int x, unsigned int y) {
    return x << y;
}

unsigned int SHR(unsigned int x, unsigned int y) {
    return x >> y;
}

int leastSigBitSet(unsigned int x) {
    return x & 1;
}

int mostSigBitSet(unsigned int x) {
    return x & 0x80000000;
}

int main() {
    // Accept and stop when 0 is entered
    unsigned int x, y;

    while (1) {
        printf("Enter two numbers: ");
        scanf("%u %u", &x, &y);

        if (x == 0 && y == 0) {
            break;
        }

        printf("AND: %u\n", AND(x, y));
        printf("OR: %u\n", OR(x, y));
        printf("XOR: %u\n", XOR(x, y));
        printf("NOT: %u\n", NOT(x));
        printf("SHL: %u\n", SHL(x, y));
        printf("SHR: %u\n", SHR(x, y));

        if (leastSigBitSet(x)) {
            printf("Least significant bit is set\n");
        } else {
            printf("Least significant bit is not set\n");
        }

        if (mostSigBitSet(x)) {
            printf("Most significant bit is set\n");
        } else {
            printf("Most significant bit is not set\n");
        }
    }

    return 0;
}