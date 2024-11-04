bitOperations.o: bitOperations.c
	gcc -c bitOperations.c

ipAddress.o: ipAddress.c
	gcc -c ipAddress.c -lm

# My local drivers
dev-date: ./Prog1/driver.c ./Prog1/bitOperations.c
	gcc -o dev-date ./Prog1/driver.c ./Prog1/bitOperations.c
	./dev-date