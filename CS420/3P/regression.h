/********************************************************************/
/*                                  							    */
/*                                                                  */
/*  Functions to support linear regression                          */
/********************************************************************/
#include <stdio.h>
#include <math.h>

#define MAX 1000

int read_data( char filename[], double x[], double y[] ); 
/* function return: size of array; input argument: input file name; output arguments: x and y arrays */

void echo_data( int size, double x[], double y[]); 
/* function return: none; input arguments: size of arrays, x and y arrays */

void compute_regression_coefficients(int size, double x[], double y[], double *slope, double *yint); 
/* function return: none; input arguments: size of arrays, x and y arrays; output arguments: slope and yintercept */

void compute_fx_residues(int size, double x[], double y[], double slope, double yint, double fx[], double r[]);
/* function return: none; input arguments: size of arrays, x and y arrays, slope, yintercept; output arguments: fx and residual arrays */

void compute_correlation(int size, double r[], double y[], double *sum_residuals, double *sum_squares, double *coeff_of_determination);
/* function return: none; input arguments: size of arrays, fx and residual arrays; 
output arguments: sum of residuals, sum of squares, coefficient of determination */

double compute_pearson_coefficient(int size, double x[], double y[]);
/* function return: pearson_coefficient; input arguments: size of arrays, x and y arrays */