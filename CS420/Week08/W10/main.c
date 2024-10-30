#include <stdio.h>

int getLeadingZeros(int x) {
    if (x == 0) {
        return 32;
    }

    int index = 0;

    while (x > 0) {
        x = x >> 1;
        index ++;
    }

    return 32 - index;
}

int main() {
    for (int i = 0; i < 31; i++) {
        int x = 1 << i;
        printf("Leading zeros in %d: %d\n", x, getLeadingZeros(x));
    }
}