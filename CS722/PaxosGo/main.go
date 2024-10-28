package main

import (
	"fmt"
	"math/rand"
	"time"

	"paxos.eparker.dev/fakeserver"
)

func testServer() {
	// Initialize server
	fakeserver.Log.Status("Starting server...")
	var server *fakeserver.FakeServer = fakeserver.NewFakeServer(fakeserver.ReliancyConfig{
		IsUnreliable:   true,
		LogMode:        true,
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
	testServer()
}
