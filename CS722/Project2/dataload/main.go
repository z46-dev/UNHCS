package dataload

import (
	"fmt"
	"io"
	"net/http"
	"strings"
	"sync"
)

var URLS = []string{
	"https://www.gutenberg.org/cache/epub/3160/pg3160.txt",
	"https://www.gutenberg.org/cache/epub/1522/pg1522.txt",
	"https://www.gutenberg.org/cache/epub/10897/pg10897.txt",
	"https://www.gutenberg.org/cache/epub/84/pg84.txt",
	"https://www.gutenberg.org/cache/epub/1342/pg1342.txt",
	"https://www.gutenberg.org/cache/epub/25344/pg25344.txt",
	"https://www.gutenberg.org/cache/epub/43/pg43.txt",
	"https://www.gutenberg.org/cache/epub/6130/pg6130.txt",
	"https://www.gutenberg.org/cache/epub/22381/pg22381.txt",
	"https://www.gutenberg.org/cache/epub/14977/pg14977.txt",
	"https://www.gutenberg.org/cache/epub/22062/pg22062.txt",
	"https://www.gutenberg.org/cache/epub/66286/pg66286.txt",
	"https://www.gutenberg.org/cache/epub/52356/pg52356.txt",
	"https://www.gutenberg.org/cache/epub/2600/pg2600.txt",
	"https://www.gutenberg.org/cache/epub/1257/pg1257.txt",
	"https://www.gutenberg.org/cache/epub/345/pg345.txt",
	"https://www.gutenberg.org/cache/epub/74167/pg74167.txt",
}

var filterOutWords = []string{
	"a", "an", "the", "and", "or", "but", "if", "then", "else",
	"with", "of", "at", "by", "from", "to", "in", "on", "for",
	"is", "it", "that", "this", "as", "are", "was", "were", "be",
	"has", "have", "had", "not", "no", "yes", "you", "we", "they",
	"he", "she", "I", "do", "does", "did", "can", "could", "would",
}

func loadDocument(url string) string {
	response, err := http.Get(url)

	if err != nil {
		return ""
	}

	defer response.Body.Close()

	if response.StatusCode != http.StatusOK {
		return ""
	}

	buff, err := io.ReadAll(response.Body)

	if err != nil {
		return ""
	}

	return string(buff)
}

func parseDocument(document string) []string {
	doc := strings.Split(strings.Split(document, "*** START OF THE PROJECT GUTENBERG")[1], "*** END OF THE PROJECT GUTENBERG")[0]

	// Split into words
	words := strings.Fields(doc)

	return words
}

type Bucket struct {
	Words []string
	URL   string
}

type Document struct {
	URL     string
	Buckets []Bucket
}

func (d *Document) String() string {
	wordCount := 0

	for _, bucket := range d.Buckets {
		wordCount += len(bucket.Words)
	}

	return fmt.Sprintf("[%s]: %d buckets, %d words", d.URL, len(d.Buckets), wordCount)
}

func LoadDocument(url string, bucketSize int) Document {
	words := parseDocument(loadDocument(url))

	var doc Document = Document{URL: url}

	var bucket Bucket = Bucket{URL: url}

outer:
	for _, word := range words {
		cleanWord := strings.ToLower(strings.Trim(word, ".,;:!?\"'‘’“”()[]{}"))

		if len(cleanWord) == 0 {
			continue
		}

		for _, filterWord := range filterOutWords {
			if cleanWord == filterWord {
				continue outer
			}
		}

		bucket.Words = append(bucket.Words, cleanWord)

		if len(bucket.Words) == bucketSize {
			doc.Buckets = append(doc.Buckets, bucket)
			bucket = Bucket{URL: url}
		}
	}

	if len(bucket.Words) > 0 {
		doc.Buckets = append(doc.Buckets, bucket)
	}

	return doc
}

func LoadAllDocuments(bucketSize int) []Document {
	var docs []Document
	var waitGroup sync.WaitGroup

	for _, url := range URLS {
		waitGroup.Add(1)
		go func(url string) {
			defer waitGroup.Done()
			doc := LoadDocument(url, bucketSize)
			docs = append(docs, doc)
		}(url)
	}

	waitGroup.Wait()

	return docs
}

func SizeOfDocuments(docs []Document) int {
	size := 0

	for _, doc := range docs {
		for _, bucket := range doc.Buckets {
			for _, word := range bucket.Words {
				size += len(word)
			}
		}
	}

	return size
}
