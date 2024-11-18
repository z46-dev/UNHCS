#include "./heatFlow.h"
#include <stdio.h>
#include <math.h>

int read_data(char *fileName, int *rows, int *cols, double *tempA, double *tempB, double *T1, double *T2, double *stabilityFactor) {
    FILE *file = fopen(fileName, "r");
    if (file == NULL) {
        return -1;
    }

    fscanf(file, "%d %d %lf %lf %lf %lf %lf", rows, cols, tempA, tempB, T1, T2, stabilityFactor);
    fclose(file);
    return 0;
}

void initialize_plate(GridCell_t array[][COL], int rows, int cols, double T1, double T2) {
    // Quadrants
    /*
     * 1 2
     * 2 1
     */

    for (int i = 0; i < rows; i ++) {
        for (int j = 0; j < cols; j ++) {
            array[i][j].temperature = (i < rows / 2) ? (j < cols / 2) ? T1 : T2 : (j < cols / 2) ? T2 : T1;
            array[i][j].stability = 0;
            array[i][j].flip = 0;
        }
    }
}

/*
                    77.00
          -------------------------------------
          |  60.50 |  60.50 |  35.00 |  35.00 |
          -------------------------------------
          |  60.50 |  60.50 |  35.00 |  35.00 |
          -------------------------------------
          |  60.50 |  60.50 |  35.00 |  35.00 |
  77.00  -------------------------------------  23.10  
          |  35.00 |  35.00 |  60.50 |  60.50 |
          -------------------------------------
          |  35.00 |  35.00 |  60.50 |  60.50 |
          -------------------------------------
          |  35.00 |  35.00 |  60.50 |  60.50 |
          -------------------------------------
                    23.10
*/
void print_plate(GridCell_t array[][COL], int rows, int cols, double tempA, double tempB, int attribute) {
    printf("                    %.2f\n", tempA);

    printf("          ");
    for (int i = 0; i < cols; i++) {
        printf("---------");
    }

    printf("-\n");
    
    for (int i = 0; i < rows; i++) {
        printf("          ");
        for (int j = 0; j < cols; j++) {
            switch (attribute) {
                case TEMPERATURE:
                    printf("| %6.2f ", array[i][j].temperature);
                    break;
                case STABILITY:
                    printf("| %6d ", array[i][j].stability);
                    break;
                case FLIP:
                    printf("| %6d ", array[i][j].flip);
                    break;
            }
        }

        printf("|\n");
        
        if (i == rows / 2 - 1) {
            printf(" %6.2f  ", tempA);
        } else {
            printf("          ");
        }

        for (int j = 0; j < cols; j++) {
                printf("---------");
            }
        
        if (i == rows / 2 - 1) {
            printf("- %6.2f\n", tempB);
        } else {
            printf("-\n");
        }
    }
    
    printf("                    %.2f\n", tempB);
}



int compute_plate(GridCell_t newArray[][COL], GridCell_t oldArray[][COL], int rows, int cols, double tempA, double tempB, double stabilityFactor) {
    int stableCells = 0;

    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            newArray[i][j].temperature = (
                ((i > 0) ? oldArray[i - 1][j].temperature : tempA) +
                ((i < rows - 1) ? oldArray[i + 1][j].temperature : tempB) +
                ((j > 0) ? oldArray[i][j - 1].temperature : tempA) +
                ((j < cols - 1) ? oldArray[i][j + 1].temperature : tempB)
            ) / 4;

            if (fabs(oldArray[i][j].temperature - newArray[i][j].temperature) / oldArray[i][j].temperature <= stabilityFactor) {
                newArray[i][j].stability = 1;
                stableCells++;
            } else {
                newArray[i][j].stability = 0;
            }

            newArray[i][j].flip += oldArray[i][j].stability != newArray[i][j].stability;
        }
    }

    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            oldArray[i][j] = newArray[i][j];
        }
    }

    return stableCells;
}