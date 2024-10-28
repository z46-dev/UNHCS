package fakeserver

import "sync"

var clientID int = 0

type FakeServer struct {
	Clients     map[int]*FakeClient
	ClientsLock *sync.RWMutex
}

func (s *FakeServer) GetClient(id int) *FakeClient {
	s.ClientsLock.RLock()
	defer s.ClientsLock.RUnlock()

	return s.Clients[id]
}

func (s *FakeServer) Send(id int, content []byte) {
	s.ClientsLock.RLock()
	defer s.ClientsLock.RUnlock()

	if id < 1 {
		for _, client := range s.Clients {
			if id < 0 && client.ID == -id {
				continue
			}

			client.OnMessage(content)
		}
	} else {
		s.GetClient(id).OnMessage(content)
	}
}

func (s *FakeServer) Handle(content []byte, from *FakeClient) {
	s.Send(-from.ID, content)
}

func NewFakeServer() *FakeServer {
	return &FakeServer{
		Clients:     make(map[int]*FakeClient),
		ClientsLock: &sync.RWMutex{},
	}
}

type FakeClient struct {
	ID        int
	server    *FakeServer
	OnMessage func([]byte)
}

func NewFakeClient(server *FakeServer) *FakeClient {
	server.ClientsLock.Lock()
	defer server.ClientsLock.Unlock()

	clientID++
	var client = &FakeClient{}
	client.ID = clientID
	client.server = server
	server.Clients[clientID] = client

	return client
}

func (c *FakeClient) Send(content []byte) {
	c.server.Handle(content, c)
}
