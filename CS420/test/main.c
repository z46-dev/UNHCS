#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#define BYTES_PER_SECTION 2
#define SECTIONS 4

int encode(int values[SECTIONS]) {
    int output = 0;

    for (int i = 0; i < SECTIONS; i++) {
        if (values[i] >= pow(2, 8 * BYTES_PER_SECTION)) {
            printf("Value %d is too large to be encoded in %d bytes\n", values[i], BYTES_PER_SECTION);
            return -1;
        }

        output |= (values[i] & ((1 << (8 * BYTES_PER_SECTION)) - 1)) << (8 * BYTES_PER_SECTION * i);
    }

    return output;
}

int* decode(int value) {
    int* output = (int*)malloc(SECTIONS * sizeof(int));

    for (int i = 0; i < SECTIONS; i++) {
        output[i] = (value >> (8 * BYTES_PER_SECTION * i)) & ((1 << (8 * BYTES_PER_SECTION)) - 1);
    }

    return output;
}

int main() {
    int values[SECTIONS] = { 33223, 133, 88, 0 };
    int encoded = encode(values);

    if (encoded == -1) {
        return 1;
    }

    printf("Encoded value: %d\n", encoded);

    int* decoded = decode(encoded);

    for (int i = 0; i < SECTIONS; i++) {
        printf("Decoded value %d: %d\n", i, decoded[i]);
    }

    free(decoded);

    return 0;
}