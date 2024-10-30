#include <stdio.h>
#include <math.h>

#define PI 3.14159
#define TAU 2 * PI
#define MUIR_S 1.5
#define HOLDER_S (log(2) / log(PI / 2))
#define DAVID_S .825056

void main() {
    float a, b, h;

    scanf("%f", &a);
    scanf("%f", &b);

    printf("Ellipse Circumference for Major Axis: %6.2f and Minor Axis: %6.2f\n", a, b);
    printf("+-----------------------------------------------------+\n");
    printf("| %37s |  %11f|\n", "Ramanujan's First Approximation", PI * (3 * (a + b) - sqrt((3 * a + b) * (a + 3 * b))));
    printf("+-----------------------------------------------------+\n");
    h = pow(a - b, 2) / pow(a + b, 2);
    printf("| %37s |  %11f|\n", "Ramanujan's Second Approximation", PI * (a + b) * (1 + 3 * h / (10 + pow(4 - 3 * h, .5))));
    printf("+-----------------------------------------------------+\n");
    printf("| %37s |  %11f|\n", "Muir's Formula", TAU * pow((pow(a, MUIR_S) / 2 + pow(b, MUIR_S) / 2), 1 / MUIR_S));
    printf("+-----------------------------------------------------+\n");
    h = pow(a - b, 2) / pow(a + b, 2);
    printf("| %37s |  %11f|\n", "Hudson Formula", .25 * PI * (a + b) * (3 * (1 + h / 4) + 1 / (1 - h / 4)));
    printf("+-----------------------------------------------------+\n");
    printf("| %37s |  %11f|\n", "Holder mean", 4 * pow(pow(a, HOLDER_S) + pow(b, HOLDER_S), 1 / HOLDER_S));
    printf("+-----------------------------------------------------+\n");
    printf("| %37s |  %11f|\n", "David Cantrell's formula", 4 * (a + b) - 2 * (4 - PI) * a * b / pow(pow(a, DAVID_S) / 2 + pow(b, DAVID_S) / 2, 1 / DAVID_S));
    printf("+-----------------------------------------------------+\n");
}