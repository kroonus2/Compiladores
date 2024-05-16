global main
extern printf
extern scanf

section .text
main: 	      ; Entrada do Programa
	push ebp
	mov ebp, esp
	sub esp, 8
	push rotuloString1
	call printf
	add esp, 4
	push rotuloStringLn
	call printf
	add esp, 4
	mov edx, ebp
	lea eax, [edx - 0]
	push eax
	push @Integer
	call scanf
	add esp, 8
	mov edx, ebp
	lea eax, [edx - 4]
	push eax
	push @Integer
	call scanf
	add esp, 8
	push rotuloString2
	call printf
	add esp, 4
	push rotuloStringLn
	call printf
	add esp, 4
	push dword[ebp - 0]
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLn
	call printf
	add esp, 4
	push rotuloString3
	call printf
	add esp, 4
	push rotuloStringLn
	call printf
	add esp, 4
	push 10
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLn
	call printf
	add esp, 4
	push dword[ebp - 0]
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLn
	call printf
	add esp, 4
	push rotuloString4
	call printf
	add esp, 4
	push rotuloStringLn
	call printf
	add esp, 4
	push 10
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLn
	call printf
	add esp, 4
	push rotuloString5
	call printf
	add esp, 4
	push rotuloStringLn
	call printf
	add esp, 4
	push 1
	pop dword[ebp - 0]
rotuloFOR6: 	push 10
	push ecx
	mov ecx, dword[ebp - 0]
	cmp ecx, dword[esp+4]
	jg rotuloFIMFOR7
	pop ecx
	push dword[ebp - 0]
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLn
	call printf
	add esp, 4
	push dword[ebp - 4]
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLn
	call printf
	add esp, 4
	add dword[ebp - 0], 1
	jmp rotuloFOR6
rotuloFIMFOR7: 	push rotuloString8
	call printf
	add esp, 4
	push rotuloStringLn
	call printf
	add esp, 4
	push 1
	pop eax
	mov dword[ebp - 0], eax
rotulorepeat9: 	push dword[ebp - 0]
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLn
	call printf
	add esp, 4
	push dword[ebp - 0]
	push 1
	pop eax
	add dword[ESP], eax
	pop eax
	mov dword[ebp - 0], eax
	push dword[ebp - 0]
	push 10
	pop eax
	cmp dword [ESP], eax
	jle rotuloFalsoREL10
	mov dword [ESP], 1
	jmp rotuloSaidaREL11
rotuloFalsoREL10: 	mov dword [ESP], 0
rotuloSaidaREL11: 	cmp dword[esp], 0

	je rotulorepeat9
	push rotuloString12
	call printf
	add esp, 4
	push rotuloStringLn
	call printf
	add esp, 4
rotuloWhile13: 	push dword[ebp - 0]
	push 1
	pop eax
	cmp dword [ESP], eax
	jl rotuloFalsoREL15
	mov dword [ESP], 1
	jmp rotuloSaidaREL16
rotuloFalsoREL15: 	mov dword [ESP], 0
rotuloSaidaREL16: 	cmp dword[esp], 0

	je rotuloFimWhile14
	push dword[ebp - 0]
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLn
	call printf
	add esp, 4
	push dword[ebp - 0]
	push 1
	pop eax
	sub dword[ESP], eax
	pop eax
	mov dword[ebp - 0], eax
	jmp rotuloWhile13
rotuloFimWhile14: 	push rotuloString17
	call printf
	add esp, 4
	push rotuloStringLn
	call printf
	add esp, 4
	push dword[ebp - 0]
	push 10
	pop eax
	cmp dword [ESP], eax
	jl rotuloFalsoREL18
	mov dword [ESP], 1
	jmp rotuloSaidaREL19
rotuloFalsoREL18: 	mov dword [ESP], 0
rotuloSaidaREL19: 	cmp dword[esp], 0

	je rotuloElse20
	push rotuloString22
	call printf
	add esp, 4
	push rotuloStringLn
	call printf
	add esp, 4
	push dword[ebp - 0]
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLn
	call printf
	add esp, 4
	push dword[ebp - 4]
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLn
	call printf
	add esp, 4
	jmp rotuloFimIf21
rotuloElse20:
rotuloFimIf21: 	push rotuloString23
	call printf
	add esp, 4
	push rotuloStringLn
	call printf
	add esp, 4
	push dword[ebp - 0]
	push 10
	pop eax
	cmp dword [ESP], eax
	jl rotuloFalsoREL24
	mov dword [ESP], 1
	jmp rotuloSaidaREL25
rotuloFalsoREL24: 	mov dword [ESP], 0
rotuloSaidaREL25: 	cmp dword[esp], 0

	je rotuloElse26
	push rotuloString28
	call printf
	add esp, 4
	push rotuloStringLn
	call printf
	add esp, 4
	push dword[ebp - 0]
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLn
	call printf
	add esp, 4
	push dword[ebp - 4]
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLn
	call printf
	add esp, 4
	jmp rotuloFimIf27
rotuloElse26:
	push rotuloString29
	call printf
	add esp, 4
	push rotuloStringLn
	call printf
	add esp, 4
	push dword[ebp - 4]
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLn
	call printf
	add esp, 4
	push dword[ebp - 0]
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLn
	call printf
	add esp, 4
rotuloFimIf27: 	push rotuloString30
	call printf
	add esp, 4
	push rotuloStringLn
	call printf
	add esp, 4
	push dword[ebp - 0]
	push 10
	pop eax
	cmp dword [ESP], eax
	jl rotuloFalsoREL31
	mov dword [ESP], 1
	jmp rotuloSaidaREL32
rotuloFalsoREL31: 	mov dword [ESP], 0
rotuloSaidaREL32: 	push dword[ebp - 4]
	push 10
	pop eax
	cmp dword [ESP], eax
	jg rotuloFalsoREL33
	mov dword [ESP], 1
	jmp rotuloSaidaREL34
rotuloFalsoREL33: 	mov dword [ESP], 0
rotuloSaidaREL34: 	cmp dword [ESP + 4], 1
	jne rotuloFalsoMTL36
	pop eax
	cmp dword [ESP], eax
	jne rotuloFalsoMTL36
	mov dword [ESP], 1
	jmp rotuloSaidaMTL35
rotuloFalsoMTL36: 	mov dword [ESP], 0
rotuloSaidaMTL35: 	cmp dword[esp], 0

	je rotuloElse37
	push rotuloString39
	call printf
	add esp, 4
	push rotuloStringLn
	call printf
	add esp, 4
	push dword[ebp - 0]
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLn
	call printf
	add esp, 4
	push dword[ebp - 4]
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLn
	call printf
	add esp, 4
	jmp rotuloFimIf38
rotuloElse37:
rotuloFimIf38: 	push rotuloString40
	call printf
	add esp, 4
	push rotuloStringLn
	call printf
	add esp, 4
	push dword[ebp - 0]
	push 10
	pop eax
	cmp dword [ESP], eax
	jl rotuloFalsoREL41
	mov dword [ESP], 1
	jmp rotuloSaidaREL42
rotuloFalsoREL41: 	mov dword [ESP], 0
rotuloSaidaREL42: 	push dword[ebp - 4]
	push 10
	pop eax
	cmp dword [ESP], eax
	jg rotuloFalsoREL43
	mov dword [ESP], 1
	jmp rotuloSaidaREL44
rotuloFalsoREL43: 	mov dword [ESP], 0
rotuloSaidaREL44: 	cmp dword [ESP + 4], 1
	je rotuloVerdadeMEL46
	cmp dword [ESP], 1
	je rotuloVerdadeMEL46
	mov dword [ESP + 4], 0
	jmp rotuloSaidaMEL45
rotuloVerdadeMEL46: 	mov dword [ESP + 4], 1
rotuloSaidaMEL45: 	add esp, 4
	cmp dword[esp], 0

	je rotuloElse47
	push rotuloString49
	call printf
	add esp, 4
	push rotuloStringLn
	call printf
	add esp, 4
	push dword[ebp - 0]
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLn
	call printf
	add esp, 4
	push dword[ebp - 4]
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLn
	call printf
	add esp, 4
	jmp rotuloFimIf48
rotuloElse47:
rotuloFimIf48: 	push rotuloString50
	call printf
	add esp, 4
	push rotuloStringLn
	call printf
	add esp, 4
	push dword[ebp - 0]
	push 10
	pop eax
	cmp dword [ESP], eax
	jl rotuloFalsoREL51
	mov dword [ESP], 1
	jmp rotuloSaidaREL52
rotuloFalsoREL51: 	mov dword [ESP], 0
rotuloSaidaREL52: 	cmp dword [ESP], 1
	jne rotuloFalsoFL53
	mov dword [ESP], 0
	jmp rotuloSaidaFL54
rotuloFalsoFL53: 	mov dword [ESP], 1
rotuloSaidaFL54: 	cmp dword[esp], 0

	je rotuloElse55
	push rotuloString57
	call printf
	add esp, 4
	push rotuloStringLn
	call printf
	add esp, 4
	push dword[ebp - 0]
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLn
	call printf
	add esp, 4
	push dword[ebp - 4]
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLn
	call printf
	add esp, 4
	jmp rotuloFimIf56
rotuloElse55:
rotuloFimIf56: 	push rotuloString58
	call printf
	add esp, 4
	push rotuloStringLn
	call printf
	add esp, 4
	push 10
	pop eax
	mov dword[ebp - 0], eax
	push rotuloString59
	call printf
	add esp, 4
	push rotuloStringLn
	call printf
	add esp, 4
	push dword[ebp - 0]
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLn
	call printf
	add esp, 4
	leave
	ret

section .data

rotuloString1: db '------------- READ --------------',0
rotuloStringLn: db '',10,0
@Integer: db '%d',0
rotuloString2: db '------------- WRITE --------------',0
rotuloString3: db 'texto',0
rotuloString4: db 'texto',0
rotuloString5: db '------------- FOR 1 --------------',0
rotuloString8: db '------------- REPEAT --------------',0
rotuloString12: db '------------- WHILE --------------',0
rotuloString17: db '------------- IF 1 --------------',0
rotuloString22: db 'ENTROU NO IF 1',0
rotuloString23: db '------------- IF ELSE 2 --------------',0
rotuloString28: db 'ENTROU NO IF 2',0
rotuloString29: db 'ENTROU NO ELSE 2',0
rotuloString30: db '------------- IF 3 --------------',0
rotuloString39: db 'ENTROU NO IF 3',0
rotuloString40: db '------------- IF 4 --------------',0
rotuloString49: db 'ENTROU NO IF 4',0
rotuloString50: db '------------- IF 5 --------------',0
rotuloString57: db 'ENTROU NO IF 5',0
rotuloString58: db '------------- ATRIBUICAO --------------',0
rotuloString59: db 'x:=10',0
