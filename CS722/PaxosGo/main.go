package main

import (
	"paxos.eparker.dev/fakeserver"
)

func main() {
	// Initialize
	var server *fakeserver.FakeServer = fakeserver.NewFakeServer(&fakeserver.ReliancyConfig{
		IsUnreliable:   true,
		LogMode:        true,
		MaximumLatency: 1500,
		DropChance:     .1,
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
	client1.SendTo(client2.ID, []byte("Hello, World!"))

	// Close after timeout
	client1.SetClosure(3000)
	client2.SetClosure(3000)

	// Keep alive
	select {}
}
