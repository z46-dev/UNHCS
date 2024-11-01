#define MAX_ROWS 12
#define MAX_COLS 12

void read_array(char filename[30], int arr[][MAX_COLS], int *rows,
int *cols);

void print_array(int arr[][MAX_COLS], int rows, int cols);

float array_average(int arr[][MAX_COLS], int rows, int cols);

void replace_diagonal(int arr[][MAX_COLS], int rows, int
cols);