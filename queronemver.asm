global main
extern printf
extern scanf

section .text
main: 	      ; Entrada do Programa
	push ebp
	mov ebp, esp
	sub esp, 8
	mov edx, ebp
	lea eax, [edx - 0]
	push eax
	push @Integer
	call scanf
	add esp, 8
	push dword[ebp - 0]
	push @Integer
	call printf
	add esp, 8
	push rotuloString1
	call printf
	add esp, 4
	push 10
	push @Integer
	call printf
	add esp, 8
	leave
	ret

section .data

@Integer: db '%d',0
rotuloString1: db 'texto',0