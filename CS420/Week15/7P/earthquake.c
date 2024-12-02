/*------------------------------------------*---------------------------------
   CS420 Earthquake analysis
-----------------------------------------------------------------------------*/
/*=======================================================*/
#include <stdio.h>
#include <stdlib.h>
#include "equake.h"
/*=======================================================*/

int main(int argc, char *argv[])
{
	void *ctrl;
	int status = 0;
	if (argc == 3) {
		ctrl = readData(argv[1]); //first argument is input file name
		
		if(ctrl == NULL) {
			fprintf(stderr,"Error creating data structures\n");
			return(-1);
		}
		status = printSummary(ctrl, argv[2]); //second argument is output/summary file name

		printDailyTotalsArray(ctrl); 
		
		printMagnitudeTotalsArray(ctrl);
		
		printDepthTotalsArray(ctrl);

		cleanUp(ctrl);
	}
	else {
		fprintf(stderr, "Incorrect number of arguments\n");
		return(-1);
	}
	return(0);
}