#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "./mpn.h"

int mpn_read_data(char *filename, mpn_t *data[], int MAX_SIZE) {
    FILE *fp = fopen(filename, "r");
    
    if (fp == NULL) {
        printf("File Not Found");
        return -1;
    }

    int i = 0;
    while (i < MAX_SIZE) {
        data[i] = (mpn_t *)malloc(sizeof(mpn_t));
        
        if (fscanf(fp, "%s %d %d %d", data[i]->triple, &data[i]->mpn_index, &data[i]->lower, &data[i]->upper) == EOF) {
            break;
        }

        i++;
    }

    fclose(fp);
    return i;
}

void print_mpn_table(char *filename, mpn_t *data[], int size) {
    FILE *fp = fopen(filename, "w");

    fprintf(fp, "Combo of positives, mpn index / 100ml, lower, upper\n");

    for (int i = 0; i < size; i++) {
        fprintf(fp, "%s                     %d          %d       %d\n", data[i]->triple, data[i]->mpn_index, data[i]->lower, data[i]->upper);
    }

    fclose(fp);
}

int search_mpn_table(mpn_t *data[], int size, char *triple) {
    for (int i = 0; i < size; i++) {
        if (strcmp(data[i]->triple, triple) == 0) {
            return i;
        }
    }

    return -1;
}