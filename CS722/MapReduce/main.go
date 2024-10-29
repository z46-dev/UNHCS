package main

import (
	"fmt"
	"regexp"
)

func main() {
	var message string = "HEllo, world! This is such a super duper cool thing 123"

	regex, err := regexp.Compile(`\w+`)

	if err != nil {
		panic(err)
	}

	fmt.Println(regex.Match([]byte(message)))
}
