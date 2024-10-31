package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"
	"time"

	"mapreduceproject.eparker.dev/dataload"
	"mapreduceproject.eparker.dev/mapreduce"
)

// func mapFunc(doc dataload.Bucket) map[string]int {
// 	wordCounts := make(map[string]int)

// 	for _, word := range doc.Words {
// 		wordCounts[word]++
// 	}

// 	return wordCounts
// }

// func shuffleFunc(wordCounts map[string]int) []map[string]int {
// 	return []map[string]int{wordCounts}
// }

// func reduceFunc(wordCounts []map[string]int) map[string]int {
// 	totalWordCounts := make(map[string]int)

// 	for _, wordCount := range wordCounts {
// 		for word, count := range wordCount {
// 			totalWordCounts[word] += count
// 		}
// 	}

// 	return totalWordCounts
// }

type WordOccurrence struct {
	Word  string
	Count int
	URLs  map[string]int
}

func mapFunc(bucket dataload.Bucket) []WordOccurrence {
	wordMap := make(map[string]*WordOccurrence)

	for _, word := range bucket.Words {
		if occurrence, exists := wordMap[word]; exists {
			occurrence.Count++
			occurrence.URLs[bucket.URL]++
		} else {
			wordMap[word] = &WordOccurrence{
				Word:  word,
				Count: 1,
				URLs:  map[string]int{bucket.URL: 1},
			}
		}
	}

	result := make([]WordOccurrence, 0, len(wordMap))
	for _, occurrence := range wordMap {
		result = append(result, *occurrence)
	}
	return result
}

func shuffleFunc(occurrences []WordOccurrence) [][]WordOccurrence {
	return [][]WordOccurrence{occurrences}
}

func reduceFunc(occurrenceGroups [][]WordOccurrence) map[string]WordOccurrence {
	result := make(map[string]WordOccurrence)

	for _, group := range occurrenceGroups {
		for _, occurrence := range group {
			if existing, exists := result[occurrence.Word]; exists {
				existing.Count += occurrence.Count
				for url, count := range occurrence.URLs {
					existing.URLs[url] += count
				}
				result[occurrence.Word] = existing
			} else {
				result[occurrence.Word] = occurrence
			}
		}
	}

	return result
}

func finalReducer(occurrenceGroups []map[string]WordOccurrence) map[string]WordOccurrence {
	result := make(map[string]WordOccurrence)

	for _, group := range occurrenceGroups {
		for word, occurrence := range group {
			if existing, exists := result[word]; exists {
				existing.Count += occurrence.Count
				for url, count := range occurrence.URLs {
					existing.URLs[url] += count
				}
				result[word] = existing
			} else {
				result[word] = occurrence
			}
		}
	}

	return result
}

func formatBytes(bytes int) string {
	const unit = 1024
	if bytes < unit {
		return fmt.Sprintf("%dB", bytes)
	}

	div, exp := int64(unit), 0
	for n := bytes / unit; n >= unit; n /= unit {
		div *= unit
		exp++
	}

	return fmt.Sprintf("%.1f%cB", float64(bytes)/float64(div), "KMGTPE"[exp])
}

func MainDocuments() {
	start := time.Now()
	var docs []dataload.Document = dataload.LoadAllDocuments(5000)
	fmt.Printf("Loaded %d document(s) in %s, %s\n", len(docs), time.Since(start), formatBytes(dataload.SizeOfDocuments(docs)))

	var buckets []dataload.Bucket

	for _, doc := range docs {
		buckets = append(buckets, doc.Buckets...)
	}

	start = time.Now()
	searchDB := mapreduce.MapReduce(mapFunc, shuffleFunc, reduceFunc, finalReducer, buckets)
	fmt.Printf("MapReduce completed in %s\n", time.Since(start))

	// var word string

	// for {
	// 	fmt.Print("Enter a word to find its count: ")
	// 	_, err := fmt.Scanln(&word)

	// 	if err != nil {
	// 		fmt.Println("Error reading from stdin:", err)
	// 		return
	// 	}

	// 	result, ok := searchDB[word]

	// 	if ok {
	// 		var urls []string

	// 		for url, count := range result.URLs {
	// 			urls = append(urls, fmt.Sprintf("- %s (%d)", url, count))
	// 		}

	// 		fmt.Printf("Word \"%s\" found %d times in %d document(s):\n%s\n", word, result.Count, len(result.URLs), strings.Join(urls, "\n"))

	// 	} else {
	// 		fmt.Printf("Word \"%s\" not found\n", word)
	// 	}
	// }

	for {
		fmt.Print("Enter a phrase to search for in the documents: ")

		reader := bufio.NewReader(os.Stdin)
		inputstr, err := reader.ReadString('\n')

		if err != nil {
			fmt.Println("Error reading from stdin:", err)
			return
		}

		words := strings.Fields(string(inputstr))

		if len(words) == 0 {
			fmt.Println("No words entered")
			continue
		}

		// Get score of each document
		type DocumentScore struct {
			Document dataload.Document
			Score    int
			WordsHas map[string]bool
		}

		var documentScores []DocumentScore

		for _, word := range words {
			word = strings.Trim(strings.ToLower(word), " .,;:!?\"'‘’“”()[]{}")

			result, ok := searchDB[word]

			if !ok {
				fmt.Printf("Word \"%s\" not found\n", word)
				continue
			}

			for url, count := range result.URLs {
				for _, doc := range docs {
					if doc.URL == url {
						var found bool
						for _, score := range documentScores {
							if score.Document.URL == doc.URL {
								found = true
								score.Score += count
								score.WordsHas[word] = true
								break
							}
						}

						if !found {
							documentScores = append(documentScores, DocumentScore{
								Document: doc,
								Score:    count,
								WordsHas: map[string]bool{word: true},
							})
						}
					}
				}
			}

			// Increase score if it has more words
			for i, score := range documentScores {
				documentScores[i].Score *= len(score.WordsHas)
			}

			// Sort by score
			for i := 0; i < len(documentScores); i++ {
				for j := i + 1; j < len(documentScores); j++ {
					if documentScores[i].Score < documentScores[j].Score {
						documentScores[i], documentScores[j] = documentScores[j], documentScores[i]
					}
				}
			}

			// Print results
			for i, score := range documentScores {
				fmt.Printf("%d. %s (%d)\n", i+1, score.Document.URL, score.Score)
			}
		}
	}
}

func MainNumberReducer() {
	var numbers []int = make([]int, 1024*1024)
	for i := 0; i < len(numbers); i++ {
		numbers[i] = i
	}

	start := time.Now()
	sum := mapreduce.MapReduce(
		func(n int) int { return n },
		func(n int) []int { return []int{n} },
		func(ns []int) int {
			sum := 0
			for _, n := range ns {
				sum += n
			}
			return sum
		},
		func(sums []int) int {
			sum := 0

			for _, s := range sums {
				sum += s
			}

			return sum
		},
		numbers,
	)

	fmt.Println("Time taken: ", time.Since(start))
	fmt.Println("Sum:", sum)
}

func main() {
	MainDocuments()
}
