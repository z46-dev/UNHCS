package main

import (
	"fmt"
	"math/rand"
	"os"
	"time"

	"paxos.eparker.dev/fakeserver"
	"paxos.eparker.dev/paxos"
)

func TestServer() {
	// Initialize server
	fakeserver.Log.Status("Starting server...")
	var server *fakeserver.FakeServer = fakeserver.NewFakeServer(fakeserver.ReliancyConfig{
		IsUnreliable:   true,
		MaximumLatency: 120,
		DropChance:     .25,
	})

	var client1 *fakeserver.FakeClient = fakeserver.NewFakeClient(server)
	var client2 *fakeserver.FakeClient = fakeserver.NewFakeClient(server)

	client1.OnMessage = func(content []byte) {
		client1.SendTo(client2.ID, content)
	}

	client2.OnMessage = func(content []byte) {
		fakeserver.Log.Basic(string(content))
		client2.SendTo(client1.ID, content)
	}

	// Do stuff
	for i := 0; i < 10; i++ {
		go func() {
			time.Sleep(time.Duration(rand.Intn(3000)) * time.Millisecond)
			client1.SendTo(client2.ID, []byte(fmt.Sprintf("Routine %d", i)))
		}()
	}

	// Close after timeout
	client1.SetClosure(3000)
	client2.SetClosure(3000)

	server.Wait()
	fakeserver.Log.Status("All clients closed, server destroyed")
}

func main() {
	for _, arg := range os.Args[1:] {
		if arg == "test" {
			TestServer()
			return
		}
	}

	var server *fakeserver.FakeServer = fakeserver.NewFakeServer(fakeserver.ReliancyConfig{
		IsUnreliable:   true,
		MaximumLatency: 120,
		DropChance:     .2,
	})

	var acceptors []*paxos.PaxosAcceptor = make([]*paxos.PaxosAcceptor, 3)
	var proposer = paxos.NewProposer(fakeserver.NewFakeClient(server))

	for i := 0; i < 3; i++ {
		acceptors[i] = paxos.NewAcceptor(fakeserver.NewFakeClient(server))
		acceptors[i].Client.OnMessage = func(content []byte) {
			var proposerID, ballotID = content[0], content[1]

			if ballotID <= byte(acceptors[i].AcceptedBallotID) {
				acceptors[i].Client.SendTo(int(proposerID), []byte{0})
			} else {
				acceptors[i].AcceptedBallotID = uint32(ballotID)
				acceptors[i].Client.SendTo(int(proposerID), []byte{1})
			}
		}

		proposer.LearnAcceptor(acceptors[i].GetID())
	}

	var startTime = time.Now()
	if proposer.Campaign() {
		fmt.Println("Campaign successful")
	} else {
		fmt.Println("Campaign failed")
	}

	fmt.Printf("Campaign took %.2fs\n", float64(time.Since(startTime).Milliseconds())/1000)
}
