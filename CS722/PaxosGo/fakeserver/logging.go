package fakeserver

import "fmt"

const (
	Reset      = "\033[0m"
	Bold       = "\033[1m"
	Red        = "\033[31m"
	Green      = "\033[32m"
	Yellow     = "\033[33m"
	Blue       = "\033[34m"
	Magenta    = "\033[35m"
	Cyan       = "\033[36m"
	White      = "\033[37m"
	BoldRed    = "\033[1;31m"
	BoldGreen  = "\033[1;32m"
	BoldYellow = "\033[1;33m"
	BoldBlue   = "\033[1;34m"
	BoldPurple = "\033[1;35m"
	BoldCyan   = "\033[1;36m"
	BoldWhite  = "\033[1;37m"
)

type Logger struct{}

func (l *Logger) Basic(message string) {
	fmt.Printf("%s[>]%s %s\n", Cyan, Reset, message)
}

func (l *Logger) Error(message string) {
	fmt.Printf("%s[!]%s %s\n", BoldRed, Reset, message)
}

func (l *Logger) ClientAdd(client *FakeClient) {
	fmt.Printf("%s[+]%s Client %d added\n", Green, Reset, client.ID)
}

func (l *Logger) ClientRemove(client *FakeClient) {
	fmt.Printf("%s[-]%s Client %d removed\n", Red, Reset, client.ID)
}

func (l *Logger) DropPacket(id int, content []byte) {
	fmt.Printf("%s[:]%s Dropped packet from %d\n", Yellow, Reset, id)
}

func (l *Logger) Send(from, to int, latency float64) {
	fmt.Printf("%s[~]%s Sent message from %d to %d, latency: %.2f\n", Blue, Reset, from, to, latency)
}

func (l *Logger) Broadcast(from int) {
	fmt.Printf("%s[=]%s Broadcast from %d\n", Magenta, Reset, from)
}
