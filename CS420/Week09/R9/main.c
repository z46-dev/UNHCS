#include <stdio.h>
#include "./mpn.h"

#define MAX_SIZE 2048

void main(int argc, char *argv[]) {
    mpn_t *data[MAX_SIZE];

    int size = mpn_read_data(argv[1], data, MAX_SIZE);

    if (size == -1) {
        return;
    }

    printf("Data read: %d\n", size);

    print_mpn_table(argv[2], data, size);

    char triple[16];
    while (printf("Enter triple: ") && scanf("%s", triple) != EOF && strcmp(triple, "exit") != 0) {
        int index = search_mpn_table(data, size, triple);

        if (index == -1) {
            printf("Combination of Positives not found for %s\n", triple);
        } else {
            printf("%s, MPN = %d, 95%% of samples contain between %d and %d bacteria per ml\n", data[index]->triple, data[index]->mpn_index, data[index]->lower, data[index]->upper);
        }
    }
}