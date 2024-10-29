package MapReduce

type Document struct {
	ID       int
	Contents string
}

func mapPhase(documents []Document) {
	for i, doc := range documents {
		var length, index int = len(doc.Contents), 0
		var words []string = make([]string, 0)
		var word string = ""

		for index < length {

		}
	}
}
