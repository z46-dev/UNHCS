package main

import (
	"fmt"

	"mapreduceproject.eparker.dev/dataload"
)

func main() {
	var docs []dataload.Document = dataload.LoadAllDocuments(5000)

	for _, doc := range docs {
		fmt.Println(doc.String())
	}
}
