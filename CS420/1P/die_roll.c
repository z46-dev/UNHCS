#include <stdio.h>
#include <stdlib.h>
#include <time.h>

int main() {
    // "Random number seed used"
    srand(time(NULL));

    int count;

    // "Input validation, discard and re-prompt after invalid inputs, continue until a valid input is obtained"
    do {
        printf("How many times do you want to roll the dice? Die roll: ");
        scanf("\n%d", &count);
    } while (count < 2);

    // "total and maximum consecutive values"
    int lastRoll = 0,
        consecutiveRolls = 0;

    int countsPerInt[6] = { 0, 0, 0, 0, 0, 0 };
    int consecutiveRollsPerInt[6] = { 0, 0, 0, 0, 0, 0 };

    printf("Numbers:\n");

    for (int i = 0; i < count; i ++) {
        int n = rand() % 6 + 1;

        if (n != lastRoll) {
            if (consecutiveRolls > consecutiveRollsPerInt[lastRoll - 1]) {
                consecutiveRollsPerInt[lastRoll - 1] = consecutiveRolls;
            }

            lastRoll = n;
            consecutiveRolls = 0;
        }

        countsPerInt[n - 1] ++;
        consecutiveRolls ++;

        printf("%2d", n);

        if (i % 30 == 0 && i > 1) {
            printf("\n");
        }
    }

    printf("\n\n");

    for (int i = 0; i < 6; i ++) {
        printf("%d came up %d times, %d consecutively\n", i + 1, countsPerInt[i], consecutiveRollsPerInt[i]);
    }
}