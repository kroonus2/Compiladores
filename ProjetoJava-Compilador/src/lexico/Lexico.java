package lexico;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class Lexico {

    private String nomeArquivo;
    private Token token;
    private BufferedReader br;
    private char caractere;
    private StringBuilder lexema = new StringBuilder();
    private int linha;
    private int coluna;
    private ArrayList<String> palavrasReserdas = new ArrayList<String>(
            Arrays.asList("and", "array", "begin", "case", "const",
                    "div", "do", "downto", "else", "end",
                    "file", "for", "function", "goto", "if",
                    "in", "label", "mod", "nil", "not",
                    "of", "or", "packed", "procedure", "program",
                    "record", "repeat", "set", "then", "to",
                    "type", "until", "var", "while", "with",
                    "integer", "real", "boolean", "char", "string",
                    "write", "writeln", "read"));

    public Lexico(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
        String caminhoArquivo = Paths.get(nomeArquivo).toAbsolutePath().toString();
        try {
            BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo, StandardCharsets.UTF_8));
            this.br = br;
            linha = 1;
            coluna = 1;
            caractere = proximoChar();
        } catch (IOException e) {
            System.err.println("Não foi possível abrir o arquivo: " + nomeArquivo);
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private boolean isPalavraReservada(String palavra) {
        return palavrasReserdas.contains(palavra);
    }

    private char proximoChar() {
        try {
            return (char) br.read();
        } catch (IOException e) {
            System.err.println("Não foi possível ler do arquivo: " + nomeArquivo);
            e.printStackTrace();
            System.exit(-1);
        }
        return 0;
    }

    public Token nextToken() {
        lexema.setLength(0);
        do {
            if (Character.isLetter(caractere)) {
                token = new Token(linha, coluna);
                lexema.append(caractere);
                caractere = proximoChar();
                coluna++;
                while (Character.isLetterOrDigit(caractere)) {
                    lexema.append(caractere);
                    caractere = proximoChar();
                    coluna++;
                }
                if (isPalavraReservada(lexema.toString())) {
                    token.setClasse(Classe.palavraReservada);
                } else {
                    token.setClasse(Classe.identificador);
                }
                token.setValor(new Valor(lexema.toString()));
                return token;
            } else if (Character.isDigit(caractere)) {
                token = new Token(linha, coluna);
                lexema.append(caractere);
                caractere = proximoChar();
                coluna++;
                while (Character.isDigit(caractere)) {
                    lexema.append(caractere);
                    caractere = proximoChar();
                    coluna++;
                }
                token.setClasse(Classe.numeroInteiro);
                token.setValor(new Valor(Integer.parseInt(lexema.toString())));
                return token;
            } else if (caractere == ' ') {
                caractere = proximoChar();
                coluna++;
            } else if (caractere == '\n') {
                caractere = proximoChar();
                linha++;
                coluna = 1;
            } else if (caractere == '\t') {
                caractere = proximoChar();
                coluna += 4;
            } else if (caractere == '+') {
                token = new Token(linha, coluna, Classe.operadorSoma);
                caractere = proximoChar();
                coluna++;
                return token;
            } else if (caractere == '-') {
                token = new Token(linha, coluna, Classe.operadorSubtracao);
                caractere = proximoChar();
                coluna++;
                return token;
            } else if (caractere == '*') {
                token = new Token(linha, coluna, Classe.operadorMultiplicacao);
                caractere = proximoChar();
                coluna++;
                return token;
            } else if (caractere == '/') {
                token = new Token(linha, coluna, Classe.operadorDivisao);
                caractere = proximoChar();
                coluna++;
                return token;
            } else if (caractere == '>') {
                caractere = proximoChar();
                coluna++;
                if (caractere == '=') {
                    token = new Token(linha, coluna, Classe.operadorMaiorIgual);
                    caractere = proximoChar();
                    coluna++;
                    return token;
                } else {
                    token = new Token(linha, coluna, Classe.operadorMaior);
                    return token;
                }
            } else if (caractere == '<') {
                caractere = proximoChar();
                coluna++;
                if (caractere == '=') {
                    token = new Token(linha, coluna, Classe.operadorMenorIgual);
                    caractere = proximoChar();
                    coluna++;
                    return token;
                } else if (caractere == '>') {
                    token = new Token(linha, coluna, Classe.operadorDiferente);
                    caractere = proximoChar();
                    coluna++;
                    return token;
                } else {
                    token = new Token(linha, coluna, Classe.operadorMenor);
                    return token;
                }
            } else if (caractere == '=') {
                token = new Token(linha, coluna, Classe.operadorIgual);
                caractere = proximoChar();
                coluna++;
                return token;
            } else if (caractere == ':') {
                caractere = proximoChar();
                coluna++;
                if (caractere == '=') {
                    token = new Token(linha, coluna, Classe.atribuicao);
                    caractere = proximoChar();
                    coluna++;
                    return token;
                } else {
                    token = new Token(linha, coluna, Classe.doisPontos);
                    return token;
                }
            } else if (caractere == ',') {
                token = new Token(linha, coluna, Classe.virgula);
                caractere = proximoChar();
                coluna++;
                return token;
            } else if (caractere == ';') {
                token = new Token(linha, coluna, Classe.pontoEVirgula);
                caractere = proximoChar();
                coluna++;
                return token;
            } else if (caractere == '.') {
                token = new Token(linha, coluna, Classe.ponto);
                caractere = proximoChar();
                coluna++;
                return token;
            } else if (caractere == '(') {
                token = new Token(linha, coluna, Classe.parentesesEsquerdo);
                caractere = proximoChar();
                coluna++;
                return token;
            } else if (caractere == ')') {
                token = new Token(linha, coluna, Classe.parentesesDireito);
                caractere = proximoChar();
                coluna++;
                return token;
            } else if (caractere == '{') {
                caractere = proximoChar();
                coluna++;
                while (caractere != '}') {
                    caractere = proximoChar();
                    coluna++;
                    if (caractere == '\n') {
                        linha++;
                        coluna = 1;
                    } else if (caractere == 65535) {
                        System.err.println("Erro lexico...Comentário aberto, "+" linha: "+linha+" ,coluna: "+coluna);
                        System.exit(-1);
                    }
                }
                caractere = proximoChar();
            } else if (caractere == '\'') {
                token = new Token(linha, coluna);
                caractere = proximoChar();
                coluna++;
                while (caractere != '\'') {
                    lexema.append(caractere);
                    caractere = proximoChar();
                    coluna++;
                    if (caractere == '\n') {
                        System.err.println("Erro lexico...String aberta, "+" linha: "+linha+" ,coluna: "+coluna);
                        System.exit(-1);
                    } else if (caractere == 65535) {
                        System.err.println("Erro lexico...String aberta - EOF");
                        System.exit(-1);
                    }
                }
                caractere = proximoChar();
                coluna++;
                token.setClasse(Classe.string);
                token.setValor(new Valor(lexema.toString()));
                return token;
            }
        } while (caractere != 65535); // EOF
        return new Token(linha, coluna, Classe.EOF);
    }

}
