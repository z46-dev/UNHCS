#include <stdlib.h>

int main(){
    char *p = malloc(100);
    free(p);
    p[0] = 'a';
}