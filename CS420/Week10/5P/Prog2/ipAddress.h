#define MAX_RECORDS 100

typedef struct {
    char *ipAddressDot, *subnetMaskDot, networkClass;
    unsigned int ipAddress, subnetMask, totalSubnets, totalHosts;
} ipInfo_t;

int readData(char [], ipInfo_t []);

void computeValues(ipInfo_t [], int);

void printResults(char [], ipInfo_t [], int);