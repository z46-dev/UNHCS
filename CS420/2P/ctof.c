#include <stdio.h>
#include <math.h>
#include <stdarg.h>

// Constants for wind chill calculation
#define W_C1 35.74
#define W_C2 .6215
#define W_C3 35.75
#define W_C4 .4275

// Constants for heat index calculation
#define H_C1 -42.379
#define H_C2 2.04901523
#define H_C3 10.14333127
#define H_C4 -.22475541
#define H_C5 -6.83783E-3
#define H_C6 -5.481717E-2
#define H_C7 1.22874E-3
#define H_C8 8.5282E-4
#define H_C9 -1.99E-6

// Wind chill calculation
float windChill(float tempF, float windSpeedMPH) {
    return W_C1 + W_C2 * tempF - W_C3 * pow(windSpeedMPH, .16) + W_C4 * tempF * pow(windSpeedMPH, .16);
}

// Heat index calculation
float heatIndex(float tempF, float humPercent) {
    return H_C1 + H_C2 * tempF + H_C3 * humPercent + H_C4 * tempF * humPercent + H_C5 * tempF * tempF + H_C6 * humPercent * humPercent + H_C7 * tempF * tempF * humPercent + H_C8 * tempF * humPercent * humPercent + H_C9 * tempF * tempF * humPercent * humPercent;
}

/**
 * Quick explanation because this is a bit weird
 * I didn't want to to do the printf all the time
 * and I wanted there to be a uniform line after
 * each row, so I made a variadic function that
 * takes the same input args as printf, and safely
 * runs the printf function, then prints a line at
 * the end of the row of equal length to the printf's
 * format string.
 */
void tableLine(const char *const _Format, ...) {
    int size = 0;
    va_list args;
    va_start(args, _Format);
    size = vprintf(_Format, args);
    va_end(args);

    for (int i = 0; i < size; i++) {
        printf("-");
    }

    printf("\n");
}

int main() {
    // printf("%.2f\n", windChill(44.60, 10));
    // printf("%.2f\n", heatIndex(80.60, 40.0));

    float celciusMin, celciusMax;

    printf("This program converts temperature values from Celcius to Fahrenheit\n");
    printf("and also factors in Wind Chill and Heat Index at the appropriate temperatures\n\n");

    printf("Please enter the range of temperature values (in Celcius between -20 and 50) to be converted:\n");

    int isInvalid = 0;

    do {
        // printf("Input temperature range: ");
        // scanf("%f to %f", &celciusMin, &celciusMax);
        scanf("%f", &celciusMin);
        scanf("%f", &celciusMax);

        isInvalid = celciusMin >= celciusMax || celciusMin < -20 || celciusMax > 50;

        if (isInvalid) {
            printf("Invalid input %.0f to %.0f\n", celciusMin, celciusMax);
        }

    } while (isInvalid);

    printf("Input temperature range: %.0f to %.0f\n", celciusMin, celciusMax);
    printf("\n");

    // Table 1 (Celcius, Fahrenheit, 5mph, 10mph, 15mph, 20mph, 25mph, 30mph, 35mph, 40mph)
    printf("Celcius to Fahrenheit with Wind Chill factor\n----------------------------------------------------------------------------------------\n");
    tableLine("%10s%13s%8s%8s%8s%8s%8s%8s%8s%8s\n", "Celcius", "Fahrenheit", "5mph", "10mph", "15mph", "20mph", "25mph", "30mph", "35mph", "40mph");
    for (float i = celciusMin; i <= celciusMax; i++) {
        float fahrenheit = i * 9 / 5 + 32;
        
        if (fahrenheit < 50) {
            tableLine("%10.0f%13.2f%8.2f%8.2f%8.2f%8.2f%8.2f%8.2f%8.2f%8.2f\n", i, fahrenheit, windChill(fahrenheit, 5), windChill(fahrenheit, 10), windChill(fahrenheit, 15), windChill(fahrenheit, 20), windChill(fahrenheit, 25), windChill(fahrenheit, 30), windChill(fahrenheit, 35), windChill(fahrenheit, 40));
        } else {
            tableLine("%10.0f%13.2f%8s%8s%8s%8s%8s%8s%8s%8s\n", i, fahrenheit, "X", "X", "X", "X", "X", "X", "X", "X");
        }
    }

    printf("\n\n");

    // Table 2 (Celcius, Fahrenheit, 40%, 50%, 60%, 70%, 80%, 90%, 100%)
    printf("Celcius to Fahrenheit with Heat Index factor\n--------------------------------------------------------------------------------\n");
    tableLine("%10s%13s%8s%8s%8s%8s%8s%8s%8s\n", "Celcius", "Fahrenheit", "40%", "50%", "60%", "70%", "80%", "90%", "100%");
    for (float i = celciusMin; i <= celciusMax; i++) {
        float fahrenheit = i * 9 / 5 + 32;
        
        if (fahrenheit >= 80) {
            tableLine("%10.0f%13.2f%8.2f%8.2f%8.2f%8.2f%8.2f%8.2f%8.2f\n", i, fahrenheit, heatIndex(fahrenheit, 40), heatIndex(fahrenheit, 50), heatIndex(fahrenheit, 60), heatIndex(fahrenheit, 70), heatIndex(fahrenheit, 80), heatIndex(fahrenheit, 90), heatIndex(fahrenheit, 100));
        } else {
            tableLine("%10.0f%13.2f%8s%8s%8s%8s%8s%8s%8s\n", i, fahrenheit, "X", "X", "X", "X", "X", "X", "X");
        }
    }
}