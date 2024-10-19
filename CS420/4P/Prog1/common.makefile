# Create object file of calSpan.c
calSpan.o: calSpan.c
    gcc -c calSpan.c

# Create object file of calSpan.c
calMain: calMain.c calSpan.o
    gcc -o calMain calMain.c calSpan.o