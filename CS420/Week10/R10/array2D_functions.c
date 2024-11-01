#include <stdio.h>
#include "array2D.h"

void read_array(char filename[30], int arr[][MAX_COLS], int *rows, int *cols) {
    FILE *file = fopen(filename, "r");
    if (file == NULL) {
        printf("Error: File not found\n");
        return;
    }

    fscanf(file, "%d %d", rows, cols);
    for (int i = 0; i < *rows; i++) {
        for (int j = 0; j < *cols; j++) {
            fscanf(file, "%d", &arr[i][j]);
        }
    }
    
    fclose(file);
}

void print_array(int arr[][MAX_COLS], int rows, int cols) {
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            printf("%3d ", arr[i][j]);
        }
        printf("\n");
    }
}

float array_average(int arr[][MAX_COLS], int rows, int cols) {
    float sum = 0;
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            sum += arr[i][j];
        }
    }
    return sum / (rows * cols);
}

void replace_diagonal(int arr[][MAX_COLS], int rows, int cols) {
    for (int i = 0; i < rows; i++) {
        arr[i][i] *= 2;

        if (arr[i][i] > 100) {
            arr[i][i] = 100;
        }
    }
}