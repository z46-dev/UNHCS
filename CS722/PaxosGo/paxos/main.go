package paxos

import (
	"time"

	"paxos.eparker.dev/fakeserver"
)

type PaxosMember struct {
	Client     *fakeserver.FakeClient
	Key, Value string
	BallotID   uint64
}

func (m *PaxosMember) GetID() int {
	return m.Client.ID
}

type PaxosProposer struct {
	*PaxosMember

	knownAcceptors []int

	ballotHandler func(*fakeserver.Reader)
}

func NewProposer(client *fakeserver.FakeClient) *PaxosProposer {
	var proposer *PaxosProposer = &PaxosProposer{PaxosMember: &PaxosMember{Client: client}}
	proposer.Client.OnMessage = func(data []byte) {
		var reader *fakeserver.Reader = fakeserver.NewReader(data)

		switch reader.U8() {
		case PTYPE_CAMPAIGN:
			if proposer.ballotHandler != nil {
				proposer.ballotHandler(reader)
			}
		default:
			panic("Proposer doesn't know how to handle message!")
		}
	}

	return proposer
}

func (p *PaxosProposer) LearnAcceptor(acceptorID int) {
	p.knownAcceptors = append(p.knownAcceptors, acceptorID)
}

/**
 * Conditions for a successful campaign:
 * 1. A majority of acceptors have responded
 * 2. A majority of responding acceptors have accepted the proposal
 */
func (p *PaxosProposer) Campaign(timeout float64) struct {
	Success               bool
	Responses, Yes, Asked int
} {
	p.BallotID++

	// Send a ballot to all acceptors
	for _, acceptorID := range p.knownAcceptors {
		p.Client.SendTo(acceptorID, fakeserver.NewWriter().U8(PTYPE_CAMPAIGN).U64(p.BallotID).U16(uint16(p.GetID())).Bytes())
	}

	// Wait for responses
	var (
		//responses   map[int]bool
		remaining, responses, yes int = len(p.knownAcceptors), 0, 0
		waitChannel                   = make(chan bool)
	)

	p.ballotHandler = func(reader *fakeserver.Reader) {
		remaining--
		responses++

		if reader.U8() == SUCCESS {
			yes++
		}

		if remaining == 0 {
			waitChannel <- true
		}
	}

	// Timeout of 5s
	go func() {
		<-time.After(time.Duration(timeout) * time.Millisecond)
		waitChannel <- false
	}()

	<-waitChannel

	return struct {
		Success               bool
		Responses, Yes, Asked int
	}{
		Success:   yes > responses/2 && responses > (responses+remaining)/2,
		Responses: responses,
		Yes:       yes,
		Asked:     responses + remaining,
	}
}

type PaxosAcceptor struct {
	*PaxosMember
}

func NewAcceptor(client *fakeserver.FakeClient) *PaxosAcceptor {
	var acceptor *PaxosAcceptor = &PaxosAcceptor{PaxosMember: &PaxosMember{Client: client}}
	acceptor.Client.OnMessage = acceptor.handleMessage

	return acceptor
}

func (a *PaxosAcceptor) handleMessage(data []byte) {
	var reader *fakeserver.Reader = fakeserver.NewReader(data)

	switch reader.U8() {
	case PTYPE_CAMPAIGN:
		{
			var ballotID uint64 = reader.U64()
			var proposerID int = int(reader.U16())

			if ballotID <= a.BallotID {
				a.Client.SendTo(proposerID, fakeserver.NewWriter().U8(PTYPE_CAMPAIGN).U8(ERR_BALLOT_TOO_LOW).Bytes())
				return
			}

			a.Client.SendTo(proposerID, fakeserver.NewWriter().U8(PTYPE_CAMPAIGN).U8(SUCCESS).Bytes())
			a.BallotID++
		}
	}
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
