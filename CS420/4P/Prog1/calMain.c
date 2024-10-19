#include <stdio.h>
#include "calSpan.h"

#define MAX_SIZE 100

int main() {
    double data[MAX_SIZE];
    int result = calSpan(MAX_SIZE, "data1", data);

    if (result != -1) {
        printf("Number of valid data points: %d\n", result);
    }
}