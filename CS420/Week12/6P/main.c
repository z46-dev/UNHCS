#include "./heatFlow.h"

int main() {
    int rows, cols;
    double tempA, tempB, T1, T2, stabilityFactor;
    read_data("in0", &rows, &cols, &tempA, &tempB, &T1, &T2, &stabilityFactor);

    GridCell_t old[ROW][COL];
    GridCell_t new[ROW][COL];

    initialize_plate(old, rows, cols, T1, T2);
    print_plate(old, rows, cols, tempA, tempB, TEMPERATURE);

    return 0;
}