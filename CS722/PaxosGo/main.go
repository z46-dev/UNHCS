package main

import (
	"fmt"
	"math/rand"
	"os"
	"sync"
	"time"

	"paxos.eparker.dev/fakeserver"
	"paxos.eparker.dev/paxos"
)

func TestProtocol() {
	fakeserver.Log.Basic("Testing protocol encoding and decoding...")
	var startTime time.Time = time.Now()
	var iterations, threads int = 1024 * 1024, 8

	var waitGroup sync.WaitGroup

	var randomString func() string = func() string {
		var length int = rand.Intn(32) + 1
		var bytes []byte = make([]byte, length)

		for i := 0; i < length; i++ {
			bytes[i] = byte(rand.Intn(255) + 1)
		}

		return string(bytes)
	}

	for i := 0; i < threads; i++ {
		waitGroup.Add(1)
		go func() {
			defer waitGroup.Done()
			for i := 0; i < iterations; i++ {
				var int8Val int8 = int8(rand.Intn(256))
				var int16Val int16 = int16(rand.Intn(65536))
				var int32Val int32 = int32(rand.Intn(4294967296))
				var int64Val int64 = int64(rand.Intn(9223372036854775807))
				var uint8Val uint8 = uint8(rand.Intn(256))
				var uint16Val uint16 = uint16(rand.Intn(65536))
				var uint32Val uint32 = uint32(rand.Intn(4294967296))
				var uint64Val uint64 = uint64(rand.Intn(9223372036854775807))
				var stringVal string = randomString()

				var writer *fakeserver.Writer = fakeserver.NewWriter()
				writer.I8(int8Val).I16(int16Val).I32(int32Val).I64(int64Val).U8(uint8Val).U16(uint16Val).U32(uint32Val).U64(uint64Val).String(stringVal)
				var reader *fakeserver.Reader = fakeserver.NewReader(writer.Bytes())

				if reader.I8() != int8Val {
					panic("I8 failed: " + fmt.Sprint(int8Val))
				}

				if reader.I16() != int16Val {
					panic("I16 failed: " + fmt.Sprint(int16Val))
				}

				if reader.I32() != int32Val {
					panic("I32 failed: " + fmt.Sprint(int32Val))
				}

				if reader.I64() != int64Val {
					panic("I64 failed: " + fmt.Sprint(int64Val))
				}

				if reader.U8() != uint8Val {
					panic("U8 failed: " + fmt.Sprint(uint8Val))
				}

				if reader.U16() != uint16Val {
					panic("U16 failed: " + fmt.Sprint(uint16Val))
				}

				if reader.U32() != uint32Val {
					panic("U32 failed: " + fmt.Sprint(uint32Val))
				}

				if reader.U64() != uint64Val {
					panic("U64 failed: " + fmt.Sprint(uint64Val))
				}

				if reader.String() != stringVal {
					panic("String failed: " + stringVal)
				}
			}
		}()
	}

	waitGroup.Wait()
	fakeserver.Log.Basic(fmt.Sprintf("All tests passed in %.2fs for %d iterations, split over %d threads", float64(time.Since(startTime).Milliseconds())/1000, iterations*threads, threads))
}

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

func TestPaxos() {
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
	if proposer.Campaign().Success {
		fmt.Println("Campaign successful")
	} else {
		fmt.Println("Campaign failed")
	}

	fmt.Printf("Campaign took %.2fs\n", float64(time.Since(startTime).Milliseconds())/1000)
}

func TestPaxosElection() {
	var server *fakeserver.FakeServer = fakeserver.NewFakeServer(fakeserver.ReliancyConfig{
		IsUnreliable:   true,
		MaximumLatency: 500,
		DropChance:     .15,
	})

	var acceptors []*paxos.PaxosAcceptor = make([]*paxos.PaxosAcceptor, 7)
	var proposers []*paxos.PaxosProposer = make([]*paxos.PaxosProposer, 2)

	for i := 0; i < 2; i++ {
		proposers[i] = paxos.NewProposer(fakeserver.NewFakeClient(server))
	}

	for i := 0; i < 7; i++ {
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

		for _, proposer := range proposers {
			proposer.LearnAcceptor(acceptors[i].GetID())
		}
	}

	for _, proposer := range proposers {
		fakeserver.Log.Important(fmt.Sprintf("Starting campaign for proposer %d", proposer.GetID()))

		var startTime = time.Now()
		var response = proposer.Campaign()
		fakeserver.Log.Important(fmt.Sprintf("Campaign for proposer %d %s, %d responses, %d asked, %d said yes. Campaign took %.2fs\n", proposer.GetID(), func() string {
			if response.Success {
				return "successful"
			} else {
				return "failed"
			}
		}(), response.Responses, response.Asked, response.Yes, float64(time.Since(startTime).Milliseconds())/1000))
	}
}

func main() {
	var opts map[string]func() = map[string]func(){
		"server":         TestServer,
		"protocol":       TestProtocol,
		"paxos":          TestPaxos,
		"paxos-all":      TestPaxos,
		"paxos-election": TestPaxosElection,
	}

	for _, arg := range os.Args[1:] {
		if fn, ok := opts[arg]; ok {
			fn()
			return
		}
	}

	var options string = ""

	for key := range opts {
		options += key + "|"
	}

	fmt.Printf("Usage main [%s]\n", options[:len(options)-1])
}
