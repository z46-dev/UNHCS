#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "./ipAddress.h"

int readData(char filename[], ipInfo_t ipInfo[]) {
    FILE *file = fopen(filename, "r");

    if (file == NULL) {
        return -1;
    }

    int i = 0, scanfResult;

    do {
        ipInfo[i].ipAddressDot = (char *) malloc(16 * sizeof(char));
        ipInfo[i].subnetMaskDot = (char *) malloc(16 * sizeof(char));

        scanfResult = fscanf(file, "%s %s", ipInfo[i].ipAddressDot, ipInfo[i].subnetMaskDot);
    } while (scanfResult == 2 && ++i < MAX_RECORDS);

    fclose(file);

    return i;
}

void computeValues(ipInfo_t ipInfo[], int n) {
    for (int i = 0; i < n; i++) {
        unsigned int oa, ob, oc, od;

        sscanf(ipInfo[i].ipAddressDot, "%u.%u.%u.%u", &oa, &ob, &oc, &od);
        ipInfo[i].ipAddress = (oa << 24) | (ob << 16) | (oc << 8) | od;

        if (oa < 128) {
            ipInfo[i].networkClass = 'A';
        } else if (oa < 192) {
            ipInfo[i].networkClass = 'B';
        } else if (oa < 224) {
            ipInfo[i].networkClass = 'C';
        } else if (oa < 240) {
            ipInfo[i].networkClass = 'D';
        } else {
            ipInfo[i].networkClass = 'E';
        }

        sscanf(ipInfo[i].subnetMaskDot, "%u.%u.%u.%u", &oa, &ob, &oc, &od);
        ipInfo[i].subnetMask = (oa << 24) | (ob << 16) | (oc << 8) | od;

        int defaultNetworkBits = 0;
        switch (ipInfo[i].networkClass) {
            case 'A':
                defaultNetworkBits = 8;
                break;
            case 'B':
                defaultNetworkBits = 16;
                break;
            case 'C':
                defaultNetworkBits = 24;
                break;
        }

        // Count bits in host portion
        ipInfo[i].totalSubnets = 0;
        ipInfo[i].totalHosts = 0;

        for (int j = 0; j < 32 - defaultNetworkBits; j++) {
            if ((ipInfo[i].subnetMask & (1 << j)) != 0) {
                ipInfo[i].totalSubnets++;
            } else {
                ipInfo[i].totalHosts++;
            }
        }
        
        ipInfo[i].totalSubnets = 1 << ipInfo[i].totalSubnets;
        ipInfo[i].totalHosts = (1 << ipInfo[i].totalHosts) - 2;
    }
}

void printResults(char filename[], ipInfo_t ipInfo[], int n) {
    FILE *file = fopen(filename, "w");

    if (file == NULL) {
        return;
    }

    fprintf(file, "    Octet IP Address   IP Address   Octet Subnet Mask  Subnet Mask   Class Subnets   Hosts\n");

    for (int i = 0; i < n; i++) {
        fprintf(file, "     %15s     %08x     %15s     %x       %c % 7d% 8d\n", ipInfo[i].ipAddressDot, ipInfo[i].ipAddress, ipInfo[i].subnetMaskDot, ipInfo[i].subnetMask, ipInfo[i].networkClass, ipInfo[i].totalSubnets, ipInfo[i].totalHosts);
    }

    fclose(file);
}