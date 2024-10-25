#include <stdio.h>

// a)
int is_even(unsigned int n) {
    return (n & 1) == 0;
}

// b)
int is_power_of_2(unsigned int n) {
    return n && !(n & (n - 1));
}

// c)
int num_bits_set(unsigned int n) {
    int count = 0;
    while (n) {
        count += n & 1;
        n >>= 1;
    }
 
    return count;
}

/*
 * I'm not gonna lie I have no idea if __builtin_popcount is C++ only
 * or if it's available in C as well. I'm assuming it's available in C
 * since it compiled with gcc on a .c file and no C++ version specified.
 * 
 * 
 * It worked on my machine.
 */
int main() {
    for (int i = 0; i <= 2048; i ++) {
        int evenPass = is_even(i) == (i % 2 == 0);
        int powerOf2Pass = is_power_of_2(i) == (i && !(i & (i - 1)));
        int numBitsSetPass = num_bits_set(i) == __builtin_popcount(i);

        if (!evenPass || !powerOf2Pass || !numBitsSetPass) {
            printf("Test failed for i = %d\n", i);
            return 1;
        }

        printf("Test passed for i = %d\n", i);
    }

    return 0;
}