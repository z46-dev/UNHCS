#include <stdlib.h>
#include <stdio.h>
#include <time.h>

int* initArray(int);
int fillArray(int *, int);
int* doubleArray(int *, int);
void displayArray(int *, int);

/* 
 * The program will create an array of given size
 * populate the array with random number from (0-99)
 * and display the array. Next, the program will double
 * the size of the array, repopulate the array and 
 * display it again.
 */

int main(int argc, char ** argv){
    if (argc != 2){
        printf("wrong number of arguments\n");
        exit(1);
    }
    
    int n = atoi(argv[1]); // get size
    srand(time(0));
    
    // create initial array and display it
    int* ptr = initArray(n);
    fillArray(ptr, n);
    displayArray(ptr, n);
    
    // create the double sized array and display it
    ptr = doubleArray(ptr, n);
    fillArray(ptr, 2*n);
    displayArray(ptr, 2*n);

    free(ptr);
}

// Create an array of size n and return its address
int* initArray(int n){
    return malloc(n * sizeof(int));
}

// Fill array ptr with n random numbers
int fillArray(int *ptr, int n){
    for(int i=0; i<n; i++){
        ptr[i] = rand() % 100;
    }
}

// Double the size of the array, make sure no memory leak
int* doubleArray(int *ptr, int n){
    int *newPtr = malloc(2*n * sizeof(int));
    for(int i=0; i<n; i++){
        newPtr[i] = ptr[i];
    }
    free(ptr);
    return newPtr;
}

// Display n array elements
void displayArray(int *ptr, int n){
    for(int i=0; i<n; i++){
        printf("%d ", ptr[i]);
    }
    printf("\n");
}