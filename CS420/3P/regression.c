#include <stdio.h>
#include <math.h>
#include "regression.h"

// Open the file and read data and then close the file
int read_data(char filename[], double x[], double y[]) {
    FILE *file = fopen(filename, "r");

    if (file == NULL) {
        return -1;
    }

    int i = 0;
    while (fscanf(file, "%f   %f", &x[i], &y[i]) != EOF && i < MAX) {
        i++;
    }

    fclose(file);
    return i;
}

// Print the data
void echo_data(int size, double x[], double y[]) {
    printf("      Input data: %d data points\n", size);
    printf("          Load             Compression\n");
    printf("      -----------------------------------\n");

    for (int i = 0; i < size; i++) {
        printf("         %f            %f\n", x[i], y[i]);
    }
}

// Compute the regression coefficients
void compute_regression_coefficients(int size, double x[], double y[], double *slope, double *yint) {
    double sumX = 0, sumY = 0, sumXY = 0, sumXX = 0;

    // Compute sums
    for (int i = 0; i < size; i++) {
        sumX += x[i];
        sumY += y[i];
        sumXY += x[i] * y[i];
        sumXX += x[i] * x[i];
    }

    // Compute slope and y-intercept
    *slope = (sumX * sumY - sumXY * size) / ((sumX * sumX) - (sumXX * size));
    *yint = (sumY - sumX * *slope) / size;
}

// Compute the f(x) vals and residues
void compute_fx_residues(int size, double x[], double y[], double slope, double yint, double fx[], double r[]) {
    for (int i = 0; i < size; i++) {
        fx[i] = slope * x[i] + yint;
        r[i] = y[i] - fx[i];
    }
}

// Compute the correlation
void compute_correlation(int size, double r[], double y[], double *sum_residuals, double *sum_squares, double *coeff_of_determination) {
    // Get the mean of y
    double meanY = 0.0;
    for (int i = 0; i < size; i++) {
        meanY += y[i];
    }

    meanY /= size;

    // Compute the sum of residuals and sum of squares
    for (int i = 0; i < size; i++) {
        *sum_residuals += r[i] * r[i];
        *sum_squares += (y[i] - meanY) * (y[i] - meanY);
    }

    // Compute the coefficient of determination
    *coeff_of_determination = 1 - (*sum_residuals / *sum_squares);
}

// Compute the pearson coefficient
double compute_pearson_coefficient(int size, double x[], double y[]) {
    double sumX = 0, sumY = 0, sumXY = 0, sumXX = 0, sumYY = 0;

    // Compute sums
    for (int i = 0; i < size; i++) {
        sumX += x[i];
        sumY += y[i];
        sumXY += x[i] * y[i];
        sumXX += x[i] * x[i];
        sumYY += y[i] * y[i];
    }

    // Compute the pearson coefficient
    return (size * sumXY - sumX * sumY) / sqrt((size * sumXX - sumX * sumX) * (size * sumYY - sumY * sumY));
}