# Structure
- Multi-Proposer
- Multi-Acceptor

## Steps
1. Election cycle w/ rolling ballots
2. Post data
3. If majority rules, commit
4. Rinse and repeat, use majority to understand who is better

# Implementation Goals
1. Actual networking usage for real nodes, integrate w/ db using event driven funcs
2. For testing, an implementation using the same interfaces that networking would use
 - Simulated latency & downtime & drops
 - I'm going insane