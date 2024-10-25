#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Data row
// 4-2-0 22 9 56
// Combo of positives, mpn index / 100ml, lower, upper

// MPN data structure
typedef struct {
    char triple[16];
    int mpn_index;
    int lower;
    int upper;
} mpn_t;

int mpn_read_data(char *filename, mpn_t *data[], int MAX_SIZE);

void print_mpn_table(char *filename, mpn_t *data[], int size);

int search_mpn_table(mpn_t *data[], int size, char *triple);