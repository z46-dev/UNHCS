#include <stdio.h>
#include <math.h>
#include "regression.h"

int main(char argc, char *argv[]) {
    double x[MAX], y[MAX], slope = 0, yint = 0, fx[MAX], r[MAX], sum_residuals = 0, sum_squares = 0, coeff_of_determination =0, pearson_coefficient = 0;
    int size;

    size = read_data(argv[1], x, y);
    if (size == -1) {
        printf("Error: Unable to open file\n");
        return -1;
    }

    // Call all computations
    echo_data(size, x, y);
    compute_regression_coefficients(size, x, y, &slope, &yint);
    compute_fx_residues(size, x, y, slope, yint, fx, r);
    compute_correlation(size, r, y, &sum_residuals, &sum_squares, &coeff_of_determination);

    printf("\nLinear Regression Function: f(x) = %.4f(x) + %.5f\n", slope, yint);


    // Formatting here really hurt my soul
    printf("\n          x\t          y\t       f(x)\t  Residuals\n");
    printf("-----------------------------------------------------------------\n");
    for (int i = 0; i < size; i++) {
        printf("   %8.6f\t   %8.6f\t   %8.6f\t  % 8.6f\n", x[i], y[i], fx[i], r[i]);
    }

    printf("\nLEAST SQUARES APPROXIMATION\n\n");
    printf("Sum of Squared Residuals = %e\n", sum_residuals);
    printf("Total Sum of Squareds = %e\n", sum_squares);
    printf("Coefficient of Determination = %f\n", coeff_of_determination);

    printf("\n\nPEARSON METHOD\n\n");
    printf("Pearson Coefficient = %.6f\n", compute_pearson_coefficient(size, x, y));

    return 0;
}