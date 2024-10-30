calSpan.o: calSpan.c
	gcc -c calSpan.c

calMain: calMain.c calSpan.o
	gcc -o calMain calMain.c calSpan.o

clean:
	rm -f *.o calMain

rebuild: clean calMain