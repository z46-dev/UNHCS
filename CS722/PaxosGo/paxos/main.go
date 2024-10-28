package paxos

type PaxosMember struct {
	memberID, ballotID uint32
	Key, Value         string
}

func (m *PaxosMember) GetID() uint32 {
	return m.memberID
}

type PaxosProposer struct {
	*PaxosMember
}

type PaxosAcceptor struct {
	*PaxosMember

	AcceptedBallotID uint32
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

var memberID uint32

func NewProposer() *PaxosProposer {
	memberID = memberID + 1
	return &PaxosProposer{
		PaxosMember: &PaxosMember{memberID: memberID - 1},
	}
}

func NewAcceptor() *PaxosAcceptor {
	memberID = memberID + 1
	return &PaxosAcceptor{
		PaxosMember: &PaxosMember{memberID: memberID - 1},
	}
}
