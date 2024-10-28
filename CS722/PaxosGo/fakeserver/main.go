package fakeserver

import (
	"math/rand"
	"sync"
	"time"
)

var Log Logger

var clientID int = 0

type ReliancyConfig struct {
	IsUnreliable, LogMode      bool
	MaximumLatency, DropChance float64
}

type FakeServer struct {
	Clients     map[int]*FakeClient
	ClientsLock *sync.RWMutex
	Config      *ReliancyConfig
}

func (s *FakeServer) GetClient(id int) *FakeClient {
	s.ClientsLock.RLock()
	defer s.ClientsLock.RUnlock()

	return s.Clients[id]
}

func (s *FakeServer) Send(fromID, toID int, content []byte) {
	s.ClientsLock.RLock()
	defer s.ClientsLock.RUnlock()

	if toID < 1 {
		for _, client := range s.Clients {
			if toID < 0 && client.ID == -toID {
				continue
			}

			if !s.Config.IsUnreliable || rand.Float64() > s.Config.DropChance {
				client.onMessage(content)
			} else {
				Log.DropPacket(client.ID, content)
			}
		}
	} else {
		if !s.Config.IsUnreliable || rand.Float64() > s.Config.DropChance {
			go func() {
				var latency float64 = rand.Float64() * s.Config.MaximumLatency
				Log.Send(fromID, toID, latency)
				<-time.After(time.Duration(latency) * time.Millisecond)
				s.GetClient(toID).onMessage(content)
			}()
		} else {
			Log.DropPacket(toID, content)
		}
	}
}

func NewFakeServer(reliancy *ReliancyConfig) *FakeServer {
	return &FakeServer{
		Clients:     make(map[int]*FakeClient),
		ClientsLock: &sync.RWMutex{},
		Config:      reliancy,
	}
}

type FakeClient struct {
	ID                int
	server            *FakeServer
	OnMessage         func([]byte)
	onMessage         func([]byte)
	closureTimer      *time.Timer
	closesAfterMillis float64
}

func NewFakeClient(server *FakeServer) *FakeClient {
	server.ClientsLock.Lock()
	defer server.ClientsLock.Unlock()

	clientID++
	var client = &FakeClient{}
	client.ID = clientID
	client.server = server
	client.onMessage = func(content []byte) {
		client.closureTimer.Reset(time.Duration(client.closesAfterMillis) * time.Millisecond)

		if client.OnMessage != nil {
			client.OnMessage(content)
		}
	}

	server.Clients[clientID] = client

	Log.ClientAdd(client)

	return client
}

func (c *FakeClient) Remove() {
	c.server.ClientsLock.Lock()
	defer c.server.ClientsLock.Unlock()

	delete(c.server.Clients, c.ID)

	Log.ClientRemove(c)
}

func (c *FakeClient) Broadcast(content []byte) {
	c.server.Send(c.ID, -c.ID, content)
}

func (c *FakeClient) SendTo(id int, content []byte) {
	c.server.Send(c.ID, id, content)
}

func (c *FakeClient) SetClosure(timeout float64) {
	c.closesAfterMillis = timeout
	c.closureTimer = time.AfterFunc(time.Duration(timeout)*time.Millisecond, func() {
		c.Remove()
	})
}
