#include <stdlib.h>

int main(){
    int *p = malloc(2 * sizeof(int));
    p[3]=100;
    free(p);
}