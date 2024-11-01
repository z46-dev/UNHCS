/*=======================================================*/
/*=======================================================*/
#include <stdio.h>
#include "array2D.h"

/*=======================================================*/
int main(void)
{
        int array[MAX_ROWS][MAX_COLS];
        float average = 0;
        char filename[30];

        printf("Enter Data File Name:");
        scanf("\n%s", filename);

        int rows, cols;
        read_array(filename, array, &rows, &cols);
        print_array(array, rows, cols);
        average = array_average(array, rows, cols);
        printf("Average: %.2f\n", average);
        replace_diagonal(array, rows, cols);
        print_array(array, rows, cols);

        return 0;
}