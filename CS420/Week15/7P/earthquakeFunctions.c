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

        printf("Reading file %s\n", filesList->filename);

        region_header_t *regionHeader = (region_header_t *)malloc(sizeof(region_header_t));
        regionHeader->region_name = (char *)malloc(sizeof(char) * 32);
        strcpy(regionHeader->region_name, filesList->filename);

        equake_data_t data = {};

        while (fscanf(regionFile, "%d %d %d %s %f %f %f %f %s", &data.year, &data.month, &data.day, data.timeOfQuake, &data.latitude, &data.longitude, &data.magnitude, &data.depth, data.location) != EOF) {
            printf("Read data: %d %d %d %s %f %f %f %f %s\n", data.year, data.month, data.day, data.timeOfQuake, data.latitude, data.longitude, data.magnitude, data.depth, data.location);
            
            equake_data_t *newData = (equake_data_t *)malloc(sizeof(equake_data_t));
            newData->year = data.year;
            newData->month = data.month;
            newData->day = data.day;
            newData->timeOfQuake = (char *)malloc(sizeof(char) * strlen(data.timeOfQuake));
            strcpy(newData->timeOfQuake, data.timeOfQuake);
            newData->latitude = data.latitude;
            newData->longitude = data.longitude;
            newData->magnitude = data.magnitude;
            newData->depth = data.depth;
            newData->location = (char *)malloc(sizeof(char) * strlen(data.location));
            strcpy(newData->location, data.location);

            printf("Read data: %d %d %d %s %f %f %f %f %s\n", newData->year, newData->month, newData->day, newData->timeOfQuake, newData->latitude, newData->longitude, newData->magnitude, newData->depth, newData->location);

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

        printf("Finished reading file %s\n", filesList->filename);

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

        int dailyTotals[MAX_COLS] = {0};
        float magnitudeTotals[MAX_COLS] = {0};
        float depthTotals[MAX_COLS] = {0};

        while (data != NULL) {
            dailyTotals[data->day]++;
            magnitudeTotals[data->day] += data->magnitude;
            depthTotals[data->day] += data->depth;
            data = data->next;
        }

        fprintf(outputFile, "Region: %s\n", regionHeader->region_name);
        fprintf(outputFile, "Day\tDaily Totals\tMagnitude Totals\tDepth Totals\n");
        for (int j = 0; j < MAX_COLS; j++) {
            fprintf(outputFile, "%d\t%d\t%f\t%f\n", j, dailyTotals[j], magnitudeTotals[j], depthTotals[j]);
        }
    }
}