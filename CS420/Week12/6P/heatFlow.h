
#define ROW 12 /* max rows */
#define COL 12 /* max columns */
#define TRUE  1 /* TRUE & FALSE flags */
#define FALSE 0

/*Flags for printing cell attribute */
#define TEMPERATURE 1
#define STABILITY 2
#define FLIP 3

#include <stdio.h>
#include <math.h>

/* Each cell has attributes for storing temperature, stability, count of flipping between stable/unstable states */
typedef struct {
	double temperature;	/*current temperature of cell */
	int stability; 		/* 0 for unstable, 1 for stable */
	int flip;			/* number of times a cell flips between stable and unstable states */
} GridCell_t;

/*  read_data reads information from the input data file and returns several values to be used for computation 
    Input argument: filename 
    Output arguments: grid rows, grid columns, temperature heater/cooler A, temperature heater/cooler B, 
	temperature T1, temperature T2, stability factor
    Return value: -1 if file cannot be opened, 0 otherwise
*/
int read_data(char [], int *, int *, double *, double *, double *, double *, double *);

/*  initialize_plate takes the input arguments with T1 and T2 temperature values and initializes the temperature
    attribute of each cell of the array supplied as an argument to either T1 or T2 based on the specifiation
    Input/Output argument: array
    Input arguments: row size of array, column size of array, temperature T1, temperature T2
    Return value: None
*/
void initialize_plate(GridCell_t [][COL], int, int, double , double );

/*  print_plate takes the array passed in and displays a neatly formatted grid of cells with the value
    of the attribute being passed in. The heater/cooler temperatures must also be printed appropriately outside 
    the grid
    Input arguments: array, row size of array, column size of array, 
	temperature heater/cooler A, temperature heater/cooler B, attribute to be printed
    Return value: None
*/
void print_plate(GridCell_t [][COL],int, int, double,double, int);

/*  compute_plate computes the temperature values of the next cycle of the (new) array based on the current (old) array
    It updates its attributes for each cell. Since we need to use the old array again, care must be taken to copy the
    contents of the new array to the old array (either at the beginning or end of the function) for reusing it for abort
    possible next cycle. Function returns the total number of cells in stable state for the new cycle computed.
    Input/Output arguments: new array, old array, row size of array, column size of array, 
    temperature heater/cooler A, temperature heater/cooler B, stability factor
    Return value: number of cells in stable state
*/
int compute_plate( GridCell_t [][COL] , GridCell_t [][COL],int, int, double, double,double);