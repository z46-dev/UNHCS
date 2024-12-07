#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "./equake.h"

/*Arguments: filename
	Function takes filename (input argument)
	File supplied contains names of actual data files containing quake records one for each region
	Open input file specified (data.txt for example) 
		Each line in data.txt is a filename to be opened - create a linked list of region headers
			Each data file (like NorthEast.txt) contains quake records one per link
			Best to use fscanf to read items in each record and create a data node in that region's linked list 
			Repeat for every line in region input/data file until EOF
		Repeat for every data file 
	Keep track of how many regions exist and return it via output argument
  Function value return is the handle to the control structure, NULL is returned if any operation in function fails
*/

typedef struct filesList {
    char *filename;
    struct filesList *last;
} filesList_t;

void *readData(char *filename) {
    FILE *fp;

    if ((fp = fopen(filename, "r")) == NULL) {
        printf("Error opening file %s\n", filename);
        return NULL;
    }

    control_t *control = (control_t *)malloc(sizeof(control_t));
    control->numberOfRegions = 0;

    filesList_t *filesList = NULL;

    char regionFilename[32];
    
    while (fscanf(fp, "%s", regionFilename) != EOF) {
        filesList_t *newFile = (filesList_t *)malloc(sizeof(filesList_t));
        newFile->filename = (char *)malloc(sizeof(char) * 32);
        strcpy(newFile->filename, regionFilename);
        newFile->last = filesList;
        filesList = newFile;
        control->numberOfRegions++;
    }

    fclose(fp);

    control->regions = (region_header_t **)malloc(sizeof(region_header_t *) * control->numberOfRegions);
    int i = 0;

    while (filesList != NULL) {
        FILE *regionFile;
        if ((regionFile = fopen(filesList->filename, "r")) == NULL) {
            printf("Error opening file %s\n", filesList->filename);
            return NULL;
        }

        region_header_t *regionHeader = (region_header_t *)malloc(sizeof(region_header_t));
        regionHeader->region_name = (char *)malloc(sizeof(char) * strlen(filesList->filename) + 1);
        strcpy(regionHeader->region_name, filesList->filename);
        regionHeader->data = NULL;

        equake_data_t data;

        char timeOfQuake[32];
        char location[64];

        while (fscanf(regionFile, "%d %d %d %s %f %f %f %f %s", &data.year, &data.month, &data.day, timeOfQuake, &data.latitude, &data.longitude, &data.magnitude, &data.depth, location) != EOF) {
            equake_data_t *newData = (equake_data_t *)malloc(sizeof(equake_data_t));
            newData->year = data.year;
            newData->month = data.month;
            newData->day = data.day;
            newData->timeOfQuake = (char *)malloc(sizeof(char) * (strlen(timeOfQuake) + 1));
            strcpy(newData->timeOfQuake, timeOfQuake);
            newData->latitude = data.latitude;
            newData->longitude = data.longitude;
            newData->magnitude = data.magnitude;
            newData->depth = data.depth;
            newData->location = (char *)malloc(sizeof(char) * (strlen(location) + 1));
            strcpy(newData->location, location);

            newData->next = NULL;

            if (regionHeader->data == NULL) {
                regionHeader->data = newData;
            } else {
                equake_data_t *temp = regionHeader->data;
                while (temp->next != NULL) {
                    temp = temp->next;
                }

                temp->next = newData;
            }
        }

        control->regions[i++] = regionHeader;

        filesList_t *temp = filesList;
        filesList = filesList->last;
        free(temp);
    }

    return control;
}

/*Arguments: Pointer to Control structure, filename
	Function takes pointer to control structure, filename for creating output file (input argument)
	Start with the first region (via region header) and navigate through the region's (data) linked list 
		Keep track of all the statistics needed for the region
		Write the summary of statistics onto the output file
	Repeat for every region
	Close output file
  Function value return 0 if output file open/write fails or 1 if file open/write is successful
*/

/*
*************************************************************************************
EARTHQUAKE SUMMARY FROM DATA FILE: Alaska.txt
Geographical Range: Latitudes 60.002998 to 64.995598, Longitudes 145.063705 to 160.951004
Total Number of Earthquakes recorded: 762
Depth Range (Kilometers): 0.000000 to 162.300003
Magnitude Range: 1.2 to 4.2
Largest magnitude of 4.2 occured on 10/14/2014 09:58:27.1 at: SOUTH-AND-CENTRAL-ALASKA
*/
int printSummary(void *control, char *filename) {
    control_t *controlPtr = (control_t *)control;

    FILE *outputFile;
    if ((outputFile = fopen(filename, "w")) == NULL) {
        printf("Error opening file %s\n", filename);
        return 0;
    }

    for (int i = 0; i < controlPtr->numberOfRegions; i++) {
        region_header_t *regionHeader = controlPtr->regions[i];
        equake_data_t *data = regionHeader->data;

        fprintf(outputFile, "************************************************************************************\n");
        fprintf(outputFile, "EARTHQUAKE SUMMARY FROM DATA FILE: %s\n", regionHeader->region_name);

        float minLatitude = data->latitude;
        float maxLatitude = data->latitude;
        float minLongitude = data->longitude;
        float maxLongitude = data->longitude;
        int totalQuakes = 0;
        float minDepth = data->depth;
        float maxDepth = data->depth;
        float minMagnitude = data->magnitude;
        float maxMagnitude = data->magnitude;
        char *maxMagnitudeTime = data->timeOfQuake;
        char *maxMagnitudeLocation = data->location;

        while (data != NULL) {
            if (data->latitude < minLatitude) {
                minLatitude = data->latitude;
            }

            if (data->latitude > maxLatitude) {
                maxLatitude = data->latitude;
            }

            if (data->longitude < minLongitude) {
                minLongitude = data->longitude;
            }

            if (data->longitude > maxLongitude) {
                maxLongitude = data->longitude;
            }

            totalQuakes++;

            if (data->depth < minDepth) {
                minDepth = data->depth;
            }

            if (data->depth > maxDepth) {
                maxDepth = data->depth;
            }

            if (data->magnitude < minMagnitude) {
                minMagnitude = data->magnitude;
            }

            if (data->magnitude > maxMagnitude) {
                maxMagnitude = data->magnitude;
                maxMagnitudeTime = data->timeOfQuake;
                maxMagnitudeLocation = data->location;
            }

            data = data->next;
        }

        fprintf(outputFile, "Geographical Range: Latitudes %.6f to %.6f, Longitudes %.6f to %.6f\n", minLatitude, maxLatitude, minLongitude, maxLongitude);
        fprintf(outputFile, "Total Number of Earthquakes recorded: %d\n", totalQuakes);
        fprintf(outputFile, "Depth Range (Kilometers): %.6f to %.6f\n", minDepth, maxDepth);
        fprintf(outputFile, "Magnitude Range: %.1f to %.1f\n", minMagnitude, maxMagnitude);
        fprintf(outputFile, "Largest magnitude of %.1f occured on %s at: %s\n\n", maxMagnitude, maxMagnitudeTime, maxMagnitudeLocation);
        
    }

    fclose(outputFile);
    return 1;
}

/*Arguments: Pointer to Control structure
	Create a dynamic 2-D array to store daily total information, size of 2-D array based on number of regions (rows), 
	max number of days in a month (columns). Pointer to 2-D array is stored in the control structure
	Start with the first region (via region header) and navigate through the region's (data) linked list 
		Pick up the day of quake occurence and bump up the count for that day in the 2-D array of daily records
		Continue until the end of the linked list of data nodes for that region
	Repeat for every region
*/
void printDailyTotalsArray(void *control) {
    control_t *controlPtr = (control_t *)control;

    controlPtr->dailyTotals = (int **)malloc(sizeof(int *) * controlPtr->numberOfRegions);

    for (int i = 0; i < controlPtr->numberOfRegions; i++) {
        region_header_t *regionHeader = controlPtr->regions[i];
        equake_data_t *data = regionHeader->data;

        controlPtr->dailyTotals[i] = (int *)malloc(sizeof(int) * 31);
        for (int j = 0; j < 31; j++) {
            controlPtr->dailyTotals[i][j] = 0;
        }

        while (data != NULL) {
            controlPtr->dailyTotals[i][data->day - 1]++;
            data = data->next;
        }
    }

    printf("*************************************TOTALS OF THE MONTH BY REGION*************************************\n");
    printf("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
    printf("                   |");
    for (int i = 0; i < 31; i++) {
        printf("%4d ", i + 1);
    }

    for (int i = 0; i < controlPtr->numberOfRegions; i++) {
        region_header_t *regionHeader = controlPtr->regions[i];

        printf("\n------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        printf("%18s |", regionHeader->region_name);
        for (int j = 0; j < 31; j++) {
            printf("%4d ", controlPtr->dailyTotals[i][j]);
        }
    }

    printf("\n------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
}

/*Arguments: Pointer to Control structure
	Create a dynamic 2-D array to store magnitude totals, size of 2-D array based on number of regions (rows), 
	MAX_COLS for ranges of totals (columns). Pointer to 2-D array is stored in the control structure
	Start with the first region (via region header) and navigate through the region's (data) linked list 
		Pick up the magnitude of quake, truncate to an integer value and bump up the count for that range
		Continue until the end of the linked list of data nodes for that region
	Repeat for every region
*/
void printMagnitudeTotalsArray(void *control) {
    control_t *controlPtr = (control_t *)control;

    controlPtr->magnitudeTotals = (int **)malloc(sizeof(int *) * controlPtr->numberOfRegions);

    for (int i = 0; i < controlPtr->numberOfRegions; i++) {
        region_header_t *regionHeader = controlPtr->regions[i];
        equake_data_t *data = regionHeader->data;

        controlPtr->magnitudeTotals[i] = (int *)malloc(sizeof(int) * 10);
        for (int j = 0; j < 10; j++) {
            controlPtr->magnitudeTotals[i][j] = 0;
        }

        while (data != NULL) {
            int magnitude = (int)data->magnitude;
            controlPtr->magnitudeTotals[i][magnitude]++;
            data = data->next;
        }
    }

    printf("*********************MAGNITUDE TOTALS BY REGION**********************\n");
    printf("---------------------------------------------------------------------\n");
    printf("                   | Magnitude Range (0 - 10, Increment 1)          \n");
    printf("---------------------------------------------------------------------\n");
    printf("                   |");

    for (int i = 0; i < 10; i++) {
        printf("%4d ", i);
    }

    for (int i = 0; i < controlPtr->numberOfRegions; i++) {
        region_header_t *regionHeader = controlPtr->regions[i];
        
        printf("\n---------------------------------------------------------------------\n");
        printf("%18s |", regionHeader->region_name);
        for (int j = 0; j < 10; j++) {
            printf("%4d ", controlPtr->magnitudeTotals[i][j]);
        }
    }

    printf("\n---------------------------------------------------------------------\n");
}

/*Arguments: Pointer to Control structure
	Create a dynamic 2-D array to store depth totals, size of 2-D array based on number of regions (rows), 
	MAX_COLS for ranges of totals (columns). Pointer to 2-D array is stored in the control structure
	Start with the first region (via region header) and navigate through the region's (data) linked list 
		Pick up the depth of quake, truncate to an integer value and bump up the count for that range
		Continue until the end of the linked list of data nodes for that region
	Repeat for every region
*/
void printDepthTotalsArray(void *control) {
    control_t *controlPtr = (control_t *)control;

    controlPtr->depthTotals = (int **)malloc(sizeof(int *) * controlPtr->numberOfRegions);

    for (int i = 0; i < controlPtr->numberOfRegions; i++) {
        region_header_t *regionHeader = controlPtr->regions[i];
        equake_data_t *data = regionHeader->data;

        controlPtr->depthTotals[i] = (int *)malloc(sizeof(int) * 10);
        for (int j = 0; j < 10; j++) {
            controlPtr->depthTotals[i][j] = 0;
        }

        while (data != NULL) {
            int depth = (int)data->depth;
            controlPtr->depthTotals[i][depth / 10]++;
            data = data->next;
        }
    }

    printf("*********************DEPTH TOTALS BY REGION**********************\n");
    printf("---------------------------------------------------------------------\n");
    printf("                   | Depth Range (0 - 100, Increment 10)          \n");
    printf("---------------------------------------------------------------------\n");
    printf("                   |");

    for (int i = 0; i < 10; i++) {
        printf("%4d ", i * 10);
    }

    for (int i = 0; i < controlPtr->numberOfRegions; i++) {
        region_header_t *regionHeader = controlPtr->regions[i];
        
        printf("\n---------------------------------------------------------------------\n");
        printf("%18s |", regionHeader->region_name);
        for (int j = 0; j < 10; j++) {
            printf("%4d ", controlPtr->depthTotals[i][j]);
        }
    }

    printf("\n---------------------------------------------------------------------\n");
}

/*Arguments: Pointer to Control structure
	All dynamic memory allocated must be freed. Care must be taken to free up any  member element in the linked list data structures
	before freeing up each node. All dynamic memory created for region headers and 2-D arrays must also be freed up. 
	Finally free up the control structure itself
*/
void cleanUp(void *control) {
    control_t *controlPtr = (control_t *)control;

    for (int i = 0; i < controlPtr->numberOfRegions; i++) {
        region_header_t *regionHeader = controlPtr->regions[i];
        equake_data_t *data = regionHeader->data;

        while (data != NULL) {
            equake_data_t *temp = data;
            data = data->next;
            free(temp->timeOfQuake);
            free(temp->location);
            free(temp);
        }

        free(regionHeader->region_name);
        free(regionHeader);
    }

    for (int i = 0; i < controlPtr->numberOfRegions; i++) {
        free(controlPtr->dailyTotals[i]);
        free(controlPtr->magnitudeTotals[i]);
        free(controlPtr->depthTotals[i]);
    }

    free(controlPtr->dailyTotals);
    free(controlPtr->magnitudeTotals);
    free(controlPtr->depthTotals);
    free(controlPtr->regions);
    free(controlPtr);
}