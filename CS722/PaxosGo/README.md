# About Project
- Simple basic implementation of the paxos consensus algorithm

## Usage
`go run main.go [mode]`

### Modes
1. `server`
    - Test the fake server to drop packets and add latency
2. `protcol`
    - Run a stressful extensive test on the protocol to make sure that it encodes and decodes correctly
3. `paxos`, `paxos-all`
    - Run the paxos test
4. `paxos-ellection`
    - Run the paxos election test