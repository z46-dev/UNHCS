; Calculate factorial of 5

LDI R0, 5             ; Input number
LDI R1, 1             ; Result
LDI R2, 0             ; Counter
LDI R3, 1             ; Increment

LOOP:
    ADD R2, R2, R3    ; Counter++
    MUL R1, R1, R2    ; Result = Result * Counter
    SUB R2, R2, R3    ; Counter--
    ADD R2, R2, R3    ; Counter++
    JG  R0, R2, LOOP  ; If Input > Counter, continue loop

OUT R1                ; Output the result
HLT                   ; Halt the CPUwind