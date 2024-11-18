# heatFlow – to build your driver heatFlow.c with the functions defined in
# heatflowFunctions.c
# heatFlow.o – to build and create just an object file (.o file) after compiling your
# heatFlow.c module
# heatFlowFunctions.o – to build and create just an object file (.o file) after compiling your
# heatFlowFunctions.c module
# clean – delete all object and executable files

heatFlow: heatFlow.o heatFlowFunctions.o
	gcc -o heatFlow heatFlow.o heatFlowFunctions.o -lm

heatFlow.o: heatFlow.c
	gcc -c heatFlow.c

heatFlowFunctions.o: heatFlowFunctions.c
	gcc -c heatFlowFunctions.c

clean:
	rm -f heatFlow heatFlow.o heatFlowFunctions.o