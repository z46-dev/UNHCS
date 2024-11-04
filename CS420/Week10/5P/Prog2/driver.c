#include <stdio.h>
#include "./ipAddress.h"

int main() {
    ipInfo_t ipInfo[MAX_RECORDS];
    int n = readData("./input.txt", ipInfo);

    if (n == -1) {
        printf("Invalid input file\n");
        return 1;
    }

    computeValues(ipInfo, n);
    printResults("./output.txt", ipInfo, n);

    return 0;
}