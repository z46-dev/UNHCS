package paxos

import (
	"time"

	"paxos.eparker.dev/fakeserver"
)

type PaxosMember struct {
	Client     *fakeserver.FakeClient
	Key, Value string
	ballotID   uint32
}

func (m *PaxosMember) GetID() int {
	return m.Client.ID
}

type PaxosProposer struct {
	*PaxosMember

	knownAcceptors []int
}

func NewProposer(client *fakeserver.FakeClient) *PaxosProposer {
	return &PaxosProposer{PaxosMember: &PaxosMember{Client: client}}
}

func (p *PaxosProposer) LearnAcceptor(acceptorID int) {
	p.knownAcceptors = append(p.knownAcceptors, acceptorID)
}

/**
 * Conditions for a successful campaign:
 * 1. A majority of acceptors have responded
 * 2. A majority of responding acceptors have accepted the proposal
 */
func (p *PaxosProposer) Campaign() struct {
	Success               bool
	Responses, Yes, Asked int
} {
	p.ballotID++

	// Send a ballot to all acceptors
	for _, acceptorID := range p.knownAcceptors {
		p.Client.SendTo(acceptorID, []byte{byte(p.GetID()), byte(p.ballotID)})
	}

	// Wait for responses
	var (
		//responses   map[int]bool
		remaining, responses, yes int = len(p.knownAcceptors), 0, 0
		waitChannel                   = make(chan bool)
	)

	p.Client.OnMessage = func(b []byte) {
		remaining--
		responses++
		yes += int(b[0])

		// When we're all done, send a signal
		if remaining == 0 {
			waitChannel <- true
		}
	}

	// Timeout of 5s
	go func() {
		<-time.After(5 * time.Second)
		waitChannel <- false
	}()

	<-waitChannel

	return struct {
		Success               bool
		Responses, Yes, Asked int
	}{
		Success:   yes > responses/2,
		Responses: responses,
		Yes:       yes,
		Asked:     responses + remaining,
	}
}

type PaxosAcceptor struct {
	*PaxosMember

	AcceptedBallotID uint32
}

func NewAcceptor(client *fakeserver.FakeClient) *PaxosAcceptor {
	return &PaxosAcceptor{PaxosMember: &PaxosMember{Client: client}}
}

/**
 * Paxos Algorithm Explanation
 *
 * 1. Proposer sends a ballot to all acceptors it can
 * 2. Acceptors respond with the highest active ballot they have seen
 * 3. Ballots expire after 10s (configurable)
 * 4. If a proposer receives a majority of responses, it sends a proposal to all acceptors
 * 5. Acceptors respond with an accept message if the proposal is valid
 * 6. If a proposer receives a majority of accept messages from ALL acceptors, not just the ones it won a ballot from, the proposal is accepted
 * 7. If a proposer receives a majority of reject messages, it can retry if desired
 */
