package main

import (
	"fmt"

	"paxos.eparker.dev/fakeserver"
)

func main() {
	var server *fakeserver.FakeServer = fakeserver.NewFakeServer()

	var client1 *fakeserver.FakeClient = fakeserver.NewFakeClient(server)
	client1.OnMessage = func(content []byte) {
		server.Send(client1.ID+1, content)
	}

	var client2 *fakeserver.FakeClient = fakeserver.NewFakeClient(server)
	client2.OnMessage = func(content []byte) {
		fmt.Println(string(content))
	}

	client2.Send([]byte("Hello, World!"))
}
