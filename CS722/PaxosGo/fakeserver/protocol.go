package fakeserver

type Reader struct {
	bytes  []byte
	offset int
}

func NewReader(bytes []byte) *Reader {
	return &Reader{bytes: bytes}
}

func (r *Reader) take() byte {
	x := r.bytes[r.offset]
	r.offset++
	return x
}

func (r *Reader) I8() int8 {
	return int8(r.take())
}

func (r *Reader) I16() int16 {
	return int16(r.take())<<8 | int16(r.take())
}

func (r *Reader) I32() int32 {
	return int32(r.take())<<24 | int32(r.take())<<16 | int32(r.take())<<8 | int32(r.take())
}

func (r *Reader) I64() int64 {
	return int64(r.take())<<56 | int64(r.take())<<48 | int64(r.take())<<40 | int64(r.take())<<32 | int64(r.take())<<24 | int64(r.take())<<16 | int64(r.take())<<8 | int64(r.take())
}

func (r *Reader) U8() uint8 {
	return r.take()
}

func (r *Reader) U16() uint16 {
	return uint16(r.take())<<8 | uint16(r.take())
}

func (r *Reader) U32() uint32 {
	return uint32(r.take())<<24 | uint32(r.take())<<16 | uint32(r.take())<<8 | uint32(r.take())
}

func (r *Reader) U64() uint64 {
	return uint64(r.take())<<56 | uint64(r.take())<<48 | uint64(r.take())<<40 | uint64(r.take())<<32 | uint64(r.take())<<24 | uint64(r.take())<<16 | uint64(r.take())<<8 | uint64(r.take())
}

func (r *Reader) String() string {
	var s []byte

	for {
		x := r.take()

		if x == 0 {
			break
		}

		s = append(s, x)
	}

	return string(s)
}

type Writer struct {
	bytes []byte
}

func NewWriter() *Writer {
	return &Writer{}
}

func (w *Writer) Bytes() []byte {
	return w.bytes
}

func (w *Writer) I8(x int8) *Writer {
	w.bytes = append(w.bytes, byte(x))
	return w
}

func (w *Writer) I16(x int16) *Writer {
	w.bytes = append(w.bytes, byte(x>>8), byte(x))
	return w
}

func (w *Writer) I32(x int32) *Writer {
	w.bytes = append(w.bytes, byte(x>>24), byte(x>>16), byte(x>>8), byte(x))
	return w
}

func (w *Writer) I64(x int64) *Writer {
	w.bytes = append(w.bytes, byte(x>>56), byte(x>>48), byte(x>>40), byte(x>>32), byte(x>>24), byte(x>>16), byte(x>>8), byte(x))
	return w
}

func (w *Writer) U8(x uint8) *Writer {
	w.bytes = append(w.bytes, x)
	return w
}

func (w *Writer) U16(x uint16) *Writer {
	w.bytes = append(w.bytes, byte(x>>8), byte(x))
	return w
}

func (w *Writer) U32(x uint32) *Writer {
	w.bytes = append(w.bytes, byte(x>>24), byte(x>>16), byte(x>>8), byte(x))
	return w
}

func (w *Writer) U64(x uint64) *Writer {
	w.bytes = append(w.bytes, byte(x>>56), byte(x>>48), byte(x>>40), byte(x>>32), byte(x>>24), byte(x>>16), byte(x>>8), byte(x))
	return w
}

func (w *Writer) String(x string) *Writer {
	w.bytes = append(w.bytes, []byte(x)...)
	w.bytes = append(w.bytes, 0)
	return w
}
