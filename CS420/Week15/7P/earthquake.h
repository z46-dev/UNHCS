#define MAX_DAYS 31
#define MAX_COLS 10

//structure containing earthquake data for a single quake occurence
typedef struct equake_data {
	int year;
	int month;
	int day;
	char *timeOfQuake;
	float latitude;
	float longitude;
	float magnitude;
	float depth;
	char *location;
	struct equake_data *next;
} equake_data_t;

//structure containing region information
typedef struct region_header {
		char *region_name;
		struct equake_data *data;
} region_header_t;

//control structure 
typedef struct control {
	int numberOfRegions;		//Number of region files
	region_header_t **regions;	//Pointer to region header array
	int **dailyTotals;			//Pointer to daily totals array
	int **magnitudeTotals;		//Pointer to magnitude totals array
	int **depthTotals;			//Pointe to depth totals array
} control_t;

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
void *readData(char *);



/*Arguments: Pointer to Control structure, filename
	Function takes pointer to control structure, filename for creating output file (input argument)
	Start with the first region (via region header) and navigate through the region's (data) linked list 
		Keep track of all the statistics needed for the region
		Write the summary of statistics onto the output file
	Repeat for every region
	Close output file
  Function value return 0 if output file open/write fails or 1 if file open/write is successful
*/
int printSummary(void *, char *);


/*Arguments: Pointer to Control structure
	Create a dynamic 2-D array to store daily total information, size of 2-D array based on number of regions (rows), 
	max number of days in a month (columns). Pointer to 2-D array is stored in the control structure
	Start with the first region (via region header) and navigate through the region's (data) linked list 
		Pick up the day of quake occurence and bump up the count for that day in the 2-D array of daily records
		Continue until the end of the linked list of data nodes for that region
	Repeat for every region
*/
void printDailyTotalsArray(void *);


/*Arguments: Pointer to Control structure
	Create a dynamic 2-D array to store magnitude totals, size of 2-D array based on number of regions (rows), 
	MAX_COLS for ranges of totals (columns). Pointer to 2-D array is stored in the control structure
	Start with the first region (via region header) and navigate through the region's (data) linked list 
		Pick up the magnitude of quake, truncate to an integer value and bump up the count for that range
		Continue until the end of the linked list of data nodes for that region
	Repeat for every region
*/
void printMagnitudeTotalsArray(void *);


/*Arguments: Pointer to Control structure
	Create a dynamic 2-D array to store depth totals, size of 2-D array based on number of regions (rows), 
	MAX_COLS for ranges of totals (columns). Pointer to 2-D array is stored in the control structure
	Start with the first region (via region header) and navigate through the region's (data) linked list 
		Pick up the depth of quake, truncate to an integer value and bump up the count for that range
		Continue until the end of the linked list of data nodes for that region
	Repeat for every region
*/
void printDepthTotalsArray(void *);


/*Arguments: Pointer to Control structure
	All dynamic memory allocated must be freed. Care must be taken to free up any  member element in the linked list data structures
	before freeing up each node. All dynamic memory created for region headers and 2-D arrays must also be freed up. 
	Finally free up the control structure itself
*/
void cleanUp(void *);