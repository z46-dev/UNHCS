package main

import (
	"fmt"

	"paxos.eparker.dev/paxos"
)

func main() {
	proposer := paxos.NewProposer()
	fmt.Println(proposer.GetID())

	acceptor := paxos.NewAcceptor()
	fmt.Println(acceptor.GetID())
}
