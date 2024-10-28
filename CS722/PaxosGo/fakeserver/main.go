package fakeserver

import (
	"math/rand"
	"sync"
	"time"
)

/**
 * A simulated server which will mimic
 * network latency and packet loss.
 *
 * Supports direct messages and broadcasts.
 * Clients can be added and removed.
 * Clients can be set to timeout after a period of inactivity.
 */

var Log Logger
var clientID int = 0

type ReliancyConfig struct {
	IsUnreliable, LogMode      bool
	MaximumLatency, DropChance float64
}

type FakeServer struct {
	Clients     map[int]*FakeClient
	ClientsLock *sync.RWMutex
	Config      ReliancyConfig
}

func NewFakeServer(reliancy ReliancyConfig) *FakeServer {
	return &FakeServer{
		Clients:     make(map[int]*FakeClient),
		ClientsLock: &sync.RWMutex{},
		Config:      reliancy,
	}
}

func (s *FakeServer) Wait() {
	for _, client := range s.Clients {
		<-client.done
	}
}

func (s *FakeServer) GetClient(id int) *FakeClient {
	s.ClientsLock.RLock()
	defer s.ClientsLock.RUnlock()

	return s.Clients[id]
}

func (s *FakeServer) msg(fromID int, client *FakeClient, content []byte) {
	// Simulated packet loss
	if s.Config.IsUnreliable && rand.Float64() < s.Config.DropChance {
		Log.DropPacket(client.ID, content)
		return
	}

	// Only simulate latency if we're unreliable
	latency := rand.Float64() * (func() float64 {
		if s.Config.IsUnreliable {
			return s.Config.MaximumLatency
		}

		return 0
	})()

	Log.Send(fromID, client.ID, latency)

	// Wait and send message
	timer := time.NewTimer(time.Duration(latency) * time.Millisecond)
	select {
	case <-timer.C:
		if client.onMessage != nil {
			client.onMessage(content)
		}
	case <-client.done:
		timer.Stop()
		return
	}
}

func (s *FakeServer) Send(fromID, toID int, content []byte) {
	s.ClientsLock.RLock()
	defer s.ClientsLock.RUnlock()

	// Send to a specific client
	if toID >= 0 {
		if client := s.Clients[toID]; client != nil {
			go s.msg(fromID, client, content)
		}

		return
	}

	// Broadcast to all clients that aren't us (unless we ask to send to ourselves too)
	for _, client := range s.Clients {
		if toID < 0 && client.ID == -toID {
			continue
		}

		go s.msg(fromID, client, content)
	}
}

type FakeClient struct {
	ID                int
	server            *FakeServer
	OnMessage         func([]byte)
	onMessage         func([]byte)
	closureTimer      *time.Timer
	closesAfterMillis float64
	done              chan struct{}
}

func NewFakeClient(server *FakeServer) *FakeClient {
	server.ClientsLock.Lock()
	defer server.ClientsLock.Unlock()

	clientID++
	client := &FakeClient{
		ID:     clientID,
		server: server,
		done:   make(chan struct{}),
	}

	client.onMessage = func(content []byte) {
		if client.closureTimer != nil {
			client.closureTimer.Reset(time.Duration(client.closesAfterMillis) * time.Millisecond)
		}

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

	if _, exists := c.server.Clients[c.ID]; exists {
		Log.ClientRemove(c)
		delete(c.server.Clients, c.ID)
		close(c.done)

		if c.closureTimer != nil {
			c.closureTimer.Stop()
		}
	}
}

func (c *FakeClient) Broadcast(content []byte) {
	c.server.Send(c.ID, -c.ID, content)
}

func (c *FakeClient) SendTo(id int, content []byte) {
	c.server.Send(c.ID, id, content)
}

func (c *FakeClient) SetClosure(timeout float64) {
	if c.closureTimer != nil {
		c.closureTimer.Stop()
	}

	c.closesAfterMillis = timeout
	c.closureTimer = time.AfterFunc(time.Duration(timeout)*time.Millisecond, func() {
		c.Remove()
	})
}
