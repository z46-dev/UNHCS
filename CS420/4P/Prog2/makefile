dataDump.o: dataDump.c
	gcc -c dataDump.c

dataMain: dataMain.c dataDump.o
	gcc -o dataMain dataMain.c dataDump.o

clean:
	rm -f *.o dataMain

rebuild: clean dataMain