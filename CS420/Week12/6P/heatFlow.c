#include "./heatFlow.h"
#include <stdio.h>

int main(int argc, char *argv[]) {
    int rows, cols;
    double tempA, tempB, T1, T2, stabilityFactor;
    
    if (read_data(argv[1], &rows, &cols, &tempA, &tempB, &T1, &T2, &stabilityFactor) == -1) {
        printf("Error reading file\n");
        return -1;
    }

    printf("   Grid Size: %dx%d\n", rows, cols);
    printf("   HEATER/COOLER  A TEMPERATURE: %10.2f\n", tempA);
    printf("   HEATER/COOLER  B TEMPERATURE: %10.2f\n", tempB);
    printf("   INITIAL PLATE TEMPERATURE #1: %10.2f\n", T1);
    printf("   INITIAL PLATE TEMPERATURE #2: %10.2f\n\n", T2);
    printf("   STABILIZE CRITERION: %18.2f\n\n\n\n\n", stabilityFactor);

    GridCell_t old[ROW][COL];
    GridCell_t new[ROW][COL];

    initialize_plate(new, rows, cols, T1, T2);
    initialize_plate(old, rows, cols, T1, T2);

    print_plate(new, rows, cols, tempA, tempB, TEMPERATURE);

    int stableCells, counts = 0;

    do {
        stableCells = compute_plate(new, old, rows, cols, tempA, tempB, stabilityFactor);
        printf("    #### PLATE %d #### (Stable cells:%d)\n", ++counts, stableCells);

        print_plate(new, rows, cols, tempA, tempB, TEMPERATURE);
    } while (stableCells < rows * cols);
    
    printf("    #### PLATE %d #### (Number of Flips in each cell)\n", counts);
    print_plate(new, rows, cols, tempA, tempB, FLIP);


    return 0;
}