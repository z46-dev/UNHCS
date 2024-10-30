package mapreduce

import (
	"math"
	"sync"
)

func MapReduce[T, M, R any](
	mapFunc func(T) M,
	shuffleFunc func(M) []M,
	reduceFunc func([]M) R,
	finalReduceFunc func([]R) R,
	inputBuckets []T,
) R {
	if len(inputBuckets) == 0 {
		return reduceFunc([]M{})
	}

	mappedChan := make(chan M, len(inputBuckets))
	shuffledChan := make(chan M, len(inputBuckets))
	reducedChan := make(chan R, 16)

	var mapWg, shuffleWg, reduceWg sync.WaitGroup
	const chunkSize = 1000

	numMapWorkers := int(math.Min(8, float64(len(inputBuckets))))
	for i := 0; i < numMapWorkers; i++ {
		mapWg.Add(1)
		go func(workerId int) {
			defer mapWg.Done()
			for j := workerId; j < len(inputBuckets); j += numMapWorkers {
				mappedChan <- mapFunc(inputBuckets[j])
			}
		}(i)
	}

	numShuffleWorkers := 4
	for i := 0; i < numShuffleWorkers; i++ {
		shuffleWg.Add(1)
		go func() {
			defer shuffleWg.Done()
			for mapped := range mappedChan {
				shuffled := shuffleFunc(mapped)
				for _, result := range shuffled {
					shuffledChan <- result
				}
			}
		}()
	}

	numReduceWorkers := 4
	for i := 0; i < numReduceWorkers; i++ {
		reduceWg.Add(1)
		go func() {
			defer reduceWg.Done()
			var chunk []M
			for shuffled := range shuffledChan {
				chunk = append(chunk, shuffled)
				if len(chunk) >= chunkSize {
					reducedChan <- reduceFunc(chunk)
					chunk = nil
				}
			}
			if len(chunk) > 0 {
				reducedChan <- reduceFunc(chunk)
			}
		}()
	}

	go func() {
		mapWg.Wait()
		close(mappedChan)
	}()

	go func() {
		shuffleWg.Wait()
		close(shuffledChan)
	}()

	go func() {
		reduceWg.Wait()
		close(reducedChan)
	}()

	var reduced []R
	for result := range reducedChan {
		reduced = append(reduced, result)
	}

	return finalReduceFunc(reduced)
}
