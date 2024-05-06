package sintatico;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import lexico.Classe;
import lexico.Lexico;
import lexico.Token;

public class Sintatico {
    private String nomeArquivo;
    private Lexico lexico;
    private Token token;

    private TabelaSimbolos tabela = new TabelaSimbolos();
    private String rotulo = "";
    private int contRotulo = 1;
    private int offSetVariavel = 0;
    private static final int TAMANHO_INTEIRO = 4;

    private String nomeArquivoSaida;
    private String caminhoArquivoSaida;
    private BufferedWriter bw;
    private FileWriter fw;

    private List<String> variaveis = new ArrayList<String>();
    private List<String> sectionData = new ArrayList<String>();

    public Sintatico(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
        lexico = new Lexico(nomeArquivo);

        nomeArquivoSaida = "queronemver.asm";
        caminhoArquivoSaida = Paths.get(nomeArquivoSaida).toAbsolutePath().toString();
        bw = null;
        fw = null;
        try {
            fw = new FileWriter(caminhoArquivoSaida, Charset.forName("UTF-8"));
            bw = new BufferedWriter(fw);
        } catch (Exception e) {
            System.err.println("Erro ao criar arquivo de saída");
        }
    }

    public void analisar() {
        System.out.println("Analisando " + nomeArquivo);
        token = lexico.nextToken();
        programa();

    }

    private void escreverCodigo(String instrucoes) {
        try {
            if (rotulo.isEmpty()) {
                bw.write(instrucoes + "\n");
            } else {
                bw.write(rotulo + ": " + instrucoes + "\n");
                rotulo = "";
            }
        } catch (IOException e) {
            System.err.println("Erro escrevendo no arquivo de saída");
        }
    }

    private String criarRotulo(String texto) {
        String retorno = "rotulo" + texto + contRotulo;
        contRotulo++;
        return retorno;
    }

    // <programa> ::= program <id> {A01} ; <corpo> • {A45}
    public void programa() {
        if (token.getClasse() == Classe.palavraReservada
                && token.getValor().getValorTexto().equals("program")) {
            token = lexico.nextToken();
            // Sempre que reconhecr um novo terminar receba o proximo token
            if (token.getClasse() == Classe.identificador) {
                // {A01}
                Registro registro = tabela.add(token.getValor().getValorTexto());
                // rotulo = criarRotulo("main");
                offSetVariavel = 0;
                registro.setCategoria(Categoria.PROGRAMAPRINCIPAL);

                escreverCodigo("global main");
                escreverCodigo("extern printf");
                escreverCodigo("extern scanf\n");
                escreverCodigo("section .text");

                rotulo = "main";

                escreverCodigo("\t      ; Entrada do Programa");
                escreverCodigo("\tpush ebp");
                escreverCodigo("\tmov ebp, esp");
                System.out.println(tabela);

                token = lexico.nextToken();

                if (token.getClasse() == Classe.pontoEVirgula) {
                    token = lexico.nextToken();
                    corpo();
                    if (token.getClasse() == Classe.ponto) {
                        token = lexico.nextToken();
                        // {A45}
                        escreverCodigo("\tleave");
                        escreverCodigo("\tret");
                        if (!sectionData.isEmpty()) {
                            escreverCodigo("\nsection .data\n");
                            for (String mensagem : sectionData) {
                                escreverCodigo(mensagem);
                            }
                        }
                        try {
                            bw.close();
                            fw.close();
                        } catch (IOException e) {
                            System.err.println("Erro ao fechar arquivo de saída");
                        }
                    } else {
                        System.err.println(token.getLinha() + ", " + token.getColuna() +
                                " - (.) Ponto final esperado no final do programa.");
                    }
                } else {
                    System.err.println(token.getLinha() + ", " + token.getColuna() +
                            " - (;) Ponto e vírgula esperado depois do nome do programa.");
                }
            } else {
                System.err.println(token.getLinha() + ", " + token.getColuna() +
                        " - Nome do programa principal esperado.");
            }

        } else {
            System.err.println(token.getLinha() + ", " + token.getColuna() +
                    " - Palavra reservada 'program' esperada no início do programa principal.");
        }
    }

    // <corpo> ::= <declara> <rotina> {A44} begin <sentencas> end {A46}
    public void corpo() {
        declara();
        // rotina();
        // {A44}
        if (token.getClasse() == Classe.palavraReservada &&
                token.getValor().getValorTexto().equals("begin")) {
            token = lexico.nextToken();
            sentencas();
            if (token.getClasse() == Classe.palavraReservada &&
                    token.getValor().getValorTexto().equals("end")) {
                token = lexico.nextToken();
                // {A46}
            } else {
                System.err.println(token.getLinha() + ", " + token.getColuna() +
                        " - Palavra reservada 'end' esperada no final do programa principal.");
            }
        } else {
            System.err.println(token.getLinha() + ", " + token.getColuna() +
                    " - Palavra reservada 'begin' esperada no início do programa principal.");
        }
    }

    // <declara> ::= var <dvar> <mais_dc> | ε
    public void declara() {
        if (token.getClasse() == Classe.palavraReservada &&
                token.getValor().getValorTexto().equals("var")) {
            token = lexico.nextToken();
            dvar();
            mais_dc();
        }
    }

    // <dvar> ::= <variaveis> : <tipo_var> {A02}
    public void dvar() {
        variaveis();
        if (token.getClasse() == Classe.doisPontos) {
            token = lexico.nextToken();
            tipo_var();
            // {A02}

            int tamanho = 0;
            for (String var : variaveis) {
                tabela.get(var).setTipo(Tipo.INTEGER);
                tamanho += TAMANHO_INTEIRO;
            }
            escreverCodigo("\tsub esp, " + tamanho);
            variaveis.clear();
            System.out.println(tabela);

        } else {
            System.err.println(token.getLinha() + ", " + token.getColuna() +
                    " - (:) Dois pontos esperado depois das variaveis.");
        }
    }

    // <mais_dc> ::= ; <cont_dc>
    public void mais_dc() {
        if (token.getClasse() == Classe.pontoEVirgula) {
            token = lexico.nextToken();
            cont_dc();
        } else {
            System.err.println(token.getLinha() + ", " + token.getColuna() +
                    " - (;) Ponto e vírgula esperado depois do mais_dc.");
        }
    }

    // <cont_dc> ::= <dvar> <mais_dc> | ε
    public void cont_dc() {
        if (token.getClasse() == Classe.identificador) {
            dvar();
            mais_dc();
        }
    }

    // <variaveis> ::= <id> {A03} <mais_var>
    public void variaveis() {
        if (token.getClasse() == Classe.identificador) {
            // {A03}

            String variavel = token.getValor().getValorTexto();
            if (tabela.isPresent(variavel)) {
                System.err.println("Variável " + variavel + " já foi declarada anteriormente.");
            } else {
                tabela.add(variavel);
                tabela.get(variavel).setCategoria(Categoria.VARIAVEL);
                tabela.get(variavel).setOffSet(offSetVariavel);
                offSetVariavel += TAMANHO_INTEIRO;
                variaveis.add(variavel);
            }
            System.out.println(tabela);

            token = lexico.nextToken();
            mais_var();
        } else {
            System.err.println(token.getLinha() + ", " + token.getColuna() +
                    " - (identificador) Identificador esperado depois de variaveis.");
        }
    }

    // <tipo_var> ::= integer
    public void tipo_var() {
        if (token.getClasse() == Classe.palavraReservada &&
                token.getValor().getValorTexto().equals("integer")) {
            token = lexico.nextToken();
        } else {
            System.err.println(token.getLinha() + ", " + token.getColuna()
                    + " - (tipo variavel) Esperado o tipo de variável Integer depois dos dois pontos.");
        }
    }

    // <mais_var> ::= , <variaveis> | ε
    public void mais_var() {
        if (token.getClasse() == Classe.virgula) {
            token = lexico.nextToken();
            variaveis();
        }
    }

    // <sentencas> ::= <comando> <mais_sentencas>
    public void sentencas() {
        comando();
        mais_sentencas();
    }

    /*
     * if( (token.getClasse() == Classe.palavraReservada && () )
     * || (token.getClasse()) ){
     */
    public void mais_sentencas() {
        if (token.getClasse() == Classe.pontoEVirgula) {
            token = lexico.nextToken();
            cont_sentencas();
        } else {
            System.err.println(token.getLinha() + ", " + token.getColuna() +
                    " - (;) Ponto e vírgula esperado depois de mais_sentencas.");
        }
    }

    // <cont_sentencas> ::= <sentencas> | ε
    public void cont_sentencas() {
        if ((token.getClasse() == Classe.palavraReservada
                && ((token.getValor().getValorTexto().equals("read"))
                        || (token.getValor().getValorTexto().equals("write"))
                        || (token.getValor().getValorTexto().equals("writeln"))
                        || (token.getValor().getValorTexto().equals("for"))
                        || (token.getValor().getValorTexto().equals("repeat"))
                        || (token.getValor().getValorTexto().equals("while"))
                        || (token.getValor().getValorTexto().equals("if"))))
                || (token.getClasse() == Classe.identificador)) {
            sentencas();
        }
    }

    // <var_read> ::= <id> {A08} <mais_var_read>
    public void var_read() {
        if (token.getClasse() == Classe.identificador) {
            // {A08}

            String variavel = token.getValor().getValorTexto();
            if (!tabela.isPresent(variavel)) {
                System.err.println("Variável " + variavel + " não foi declarada");
                System.exit(-1);
            } else {
                Registro registro = tabela.get(variavel);
                if (registro.getCategoria() != Categoria.VARIAVEL) {
                    System.err.println("Identificador " + variavel + " não é uma variável");
                    System.exit(-1);
                } else {
                    escreverCodigo("\tmov edx, ebp");
                    escreverCodigo("\tlea eax, [edx - " + registro.getOffSet() + "]");
                    escreverCodigo("\tpush eax");
                    escreverCodigo("\tpush @Integer");
                    escreverCodigo("\tcall scanf");
                    escreverCodigo("\tadd esp, 8");
                    if (!sectionData.contains("@Integer: db '%d',0")) {
                        sectionData.add("@Integer: db '%d',0");
                    }
                }
            }
            token = lexico.nextToken();
            mais_var_read();
        } else {
            System.err.println(token.getLinha() + ", " + token.getColuna() +
                    " - (identificador) Identificador esperado depois da função var_read().");
        }
    }

    // <mais_var_read> ::= , <var_read> | ε
    public void mais_var_read() {
        if (token.getClasse() == Classe.virgula) {
            token = lexico.nextToken();
            var_read();
        }
    }

    /*
     * <exp_write> ::= <id> {A09} <mais_exp_write> |
     * <string> {A59} <mais_exp_write> |
     * <intnum> {A43} <mais_exp_write>
     */
    public void exp_write() {
        if ((token.getClasse() == Classe.identificador)
                || (token.getClasse() == Classe.string)
                || (token.getClasse() == Classe.numeroInteiro)) {

            // <id> {A09} <mais_exp_write>
            if (token.getClasse() == Classe.identificador) {
                // {A09}

                String variavel = token.getValor().getValorTexto();
                if (!tabela.isPresent(variavel)) {
                    System.err.println("Variável " + variavel + " não foi declarada");
                    System.exit(-1);
                } else {
                    Registro registro = tabela.get(variavel);
                    if (registro.getCategoria() != Categoria.VARIAVEL) {
                        System.err.println("Identificador " + variavel + " não é uma variável");
                        System.exit(-1);
                    } else {
                        escreverCodigo("\tpush dword[ebp - " + registro.getOffSet() + "]");
                        escreverCodigo("\tpush @Integer");
                        escreverCodigo("\tcall printf");
                        escreverCodigo("\tadd esp, 8");
                        if (!sectionData.contains("@Integer: db '%d',0")) {
                            sectionData.add("@Integer: db '%d',0");
                        }
                    }
                }
                token = lexico.nextToken();
                mais_exp_write();
            } else if (token.getClasse() == Classe.string) {
                // {A59}
                String string = token.getValor().getValorTexto();
                String rotulo = criarRotulo("String");
                sectionData.add(rotulo + ": db '" + string + "',0");
                escreverCodigo("\tpush " + rotulo);
                escreverCodigo("\tcall printf");
                escreverCodigo("\tadd esp, 4");

                token = lexico.nextToken();
                mais_exp_write();
            } else if (token.getClasse() == Classe.numeroInteiro) {
                // {A43}
                int numero = token.getValor().getValorInteiro();
                escreverCodigo("\tpush " + numero);
                escreverCodigo("\tpush @Integer");
                escreverCodigo("\tcall printf");
                escreverCodigo("\tadd esp, 8");
                if (!sectionData.contains("@Integer: db '%d',0")) {
                    sectionData.add("@Integer: db '%d',0");
                }
                token = lexico.nextToken();
                mais_exp_write();
            }
        } else {
            System.err.println(token.getLinha() + ", " + token.getColuna() +
                    " - Identificador/string,/numeroInteiro esperado ao ler a função exp_write()");
        }
    }

    // <mais_exp_write> ::= , <exp_write> | ε
    public void mais_exp_write() {
        if (token.getClasse() == Classe.virgula) {
            token = lexico.nextToken();
            exp_write();
        }
    }

    private void comando() {
        if (token.getClasse() == Classe.palavraReservada &&
                token.getValor().getValorTexto().equals("read")) {
            token = lexico.nextToken();
            // {A04}
            if (token.getClasse() == Classe.parentesesEsquerdo) {
                token = lexico.nextToken();
                var_read();
                if (token.getClasse() == Classe.parentesesDireito) {
                    token = lexico.nextToken();
                    // {A05}
                } else {
                    System.err.println(token.getLinha() + "," + token.getColuna()
                            + " Erro: era esperado um parênteses direito após a lista de variáveis");
                }
            } else {
                System.err.println(token.getLinha() + "," + token.getColuna()
                        + " Erro: era esperado um sinal de atribuição após o identificador");
            }
        } else {
            if (token.getClasse() == Classe.palavraReservada &&
                    token.getValor().getValorTexto().equals("write")) {
                token = lexico.nextToken();
                // {A06}
                if (token.getClasse() == Classe.parentesesEsquerdo) {
                    token = lexico.nextToken();
                    exp_write();
                    if (token.getClasse() == Classe.parentesesDireito) {
                        token = lexico.nextToken();
                        // {A07}
                    } else {
                        System.err.println(token.getLinha() + "," + token.getColuna()
                                + " Erro: era esperado um parênteses direito após a lista de expressões");
                    }
                } else {
                    System.err.println(token.getLinha() + "," + token.getColuna()
                            + " Erro: era esperado um sinal de atribuição após o identificador");
                }
            } else {
                if (token.getClasse() == Classe.palavraReservada &&
                        token.getValor().getValorTexto().equals("writeln")) {
                    token = lexico.nextToken();
                    // {A06}
                    if (token.getClasse() == Classe.parentesesEsquerdo) {
                        token = lexico.nextToken();
                        exp_write();
                        if (token.getClasse() == Classe.parentesesDireito) {
                            token = lexico.nextToken();
                            // {A07}
                        } else {
                            System.err.println(token.getLinha() + "," + token.getColuna()
                                    + " Erro: era esperado um parênteses direito após a lista de expressões");
                        }
                    } else {
                        System.err.println(token.getLinha() + "," + token.getColuna()
                                + " Erro: era esperado um sinal de atribuição após o identificador");
                    }
                } else {
                    if (token.getClasse() == Classe.palavraReservada
                            && token.getValor().getValorTexto().equals("for")) {
                        token = lexico.nextToken();
                        if (token.getClasse() == Classe.identificador) {
                            token = lexico.nextToken();
                            if (token.getClasse() == Classe.atribuicao) {
                                token = lexico.nextToken();
                                if (token.getClasse() == Classe.numeroInteiro) {
                                    token = lexico.nextToken();
                                    if (token.getClasse() == Classe.palavraReservada
                                            && token.getValor().getValorTexto().equals("to")) {
                                        token = lexico.nextToken();
                                        if (token.getClasse() == Classe.numeroInteiro) {
                                            token = lexico.nextToken();
                                            if (token.getClasse() == Classe.palavraReservada
                                                    && token.getValor().getValorTexto().equals("do")) {
                                                token = lexico.nextToken();
                                                if (token.getClasse() == Classe.palavraReservada
                                                        && token.getValor().getValorTexto().equals("begin")) {
                                                    token = lexico.nextToken();
                                                    sentencas();
                                                    if (token.getClasse() == Classe.palavraReservada
                                                            && token.getValor().getValorTexto().equals("end")) {
                                                        token = lexico.nextToken();
                                                    } else {
                                                        System.err.println(token.getLinha() + "," + token.getColuna()
                                                                + " Erro: era esperada a palavra reservada end");
                                                    }
                                                } else {
                                                    System.err.println(token.getLinha() + "," + token.getColuna()
                                                            + " Erro: era esperada a palavra reservada begin");
                                                }
                                            } else {
                                                System.err.println(token.getLinha() + "," + token.getColuna()
                                                        + " Erro: era esperada a palavra reservada do");
                                            }
                                        } else {
                                            System.err.println(token.getLinha() + "," + token.getColuna()
                                                    + " Erro: era esperado um número inteiro");
                                        }
                                    } else {
                                        System.err.println(token.getLinha() + "," + token.getColuna()
                                                + " Erro: era esperada a palavra reservada to");
                                    }
                                } else {
                                    System.err.println(token.getLinha() + "," + token.getColuna()
                                            + " Erro: era esperado um número inteiro");
                                }
                            } else {
                                System.err.println(token.getLinha() + "," + token.getColuna()
                                        + " Erro: era esperado um sinal de atribuição");
                            }
                        } else {
                            System.err.println(token.getLinha() + "," + token.getColuna()
                                    + " Erro: era esperado um identificador");
                        }
                    } else {
                        if (token.getClasse() == Classe.palavraReservada
                                && token.getValor().getValorTexto().equals("repeat")) {
                            token = lexico.nextToken();
                            sentencas();
                            if (token.getClasse() == Classe.palavraReservada
                                    && token.getValor().getValorTexto().equals("until")) {
                                token = lexico.nextToken();
                                // {A09}
                                if (token.getClasse() == Classe.parentesesEsquerdo) {
                                    token = lexico.nextToken();
                                    expressao_logica();
                                    if (token.getClasse() == Classe.parentesesDireito) {
                                        token = lexico.nextToken();
                                    } else {
                                        System.err.println(token.getLinha() + "," + token.getColuna()
                                                + " Erro: era esperado um parênteses esquerdo");
                                    }
                                } else {
                                    System.err.println(token.getLinha() + "," + token.getColuna()
                                            + " Erro: era esperado um parênteses direito");
                                }
                            } else {
                                System.err.println(token.getLinha() + "," + token.getColuna()
                                        + " Erro: era esperada a palavra reservada until");
                            }
                        } else {
                            if (token.getClasse() == Classe.palavraReservada
                                    && token.getValor().getValorTexto().equals("while")) {
                                token = lexico.nextToken();
                                // {A10}
                                if (token.getClasse() == Classe.parentesesEsquerdo) {
                                    token = lexico.nextToken();
                                    expressao_logica();
                                    if (token.getClasse() == Classe.parentesesDireito) {
                                        token = lexico.nextToken();
                                        if (token.getClasse() == Classe.palavraReservada
                                                && token.getValor().getValorTexto().equals("do")) {
                                            token = lexico.nextToken();
                                            if (token.getClasse() == Classe.palavraReservada
                                                    && token.getValor().getValorTexto().equals("begin")) {
                                                token = lexico.nextToken();
                                                sentencas();
                                                if (token.getClasse() == Classe.palavraReservada
                                                        && token.getValor().getValorTexto().equals("end")) {
                                                    token = lexico.nextToken();
                                                } else {
                                                    System.err.println(token.getLinha() + "," + token.getColuna()
                                                            + " Erro: era esperada a palavra reservada end");
                                                }
                                            } else {
                                                System.err.println(token.getLinha() + "," + token.getColuna()
                                                        + " Erro: era esperada a palavra reservada begin");
                                            }
                                        } else {
                                            System.err.println(token.getLinha() + "," + token.getColuna()
                                                    + " Erro: era esperada a palavra reservada do");
                                        }
                                    } else {
                                        System.err.println(token.getLinha() + "," + token.getColuna()
                                                + " Erro: era esperado um parênteses direito");
                                    }
                                } else {
                                    System.err.println(token.getLinha() + "," + token.getColuna()
                                            + " Erro: era esperado um parênteses esquerdo");
                                }
                            } else {
                                if (token.getClasse() == Classe.palavraReservada
                                        && token.getValor().getValorTexto().equals("if")) {
                                    token = lexico.nextToken();
                                    // {A11}
                                    if (token.getClasse() == Classe.parentesesEsquerdo) {
                                        token = lexico.nextToken();
                                        expressao_logica();
                                        if (token.getClasse() == Classe.parentesesDireito) {
                                            token = lexico.nextToken();
                                            if (token.getClasse() == Classe.palavraReservada
                                                    && token.getValor().getValorTexto().equals("then")) {
                                                token = lexico.nextToken();
                                                if (token.getClasse() == Classe.palavraReservada
                                                        && token.getValor().getValorTexto().equals("begin")) {
                                                    token = lexico.nextToken();
                                                    sentencas();
                                                    if (token.getClasse() == Classe.palavraReservada
                                                            && token.getValor().getValorTexto().equals("end")) {
                                                        token = lexico.nextToken();
                                                        pfalsa();
                                                        // {A12}
                                                        if (token.getClasse() == Classe.palavraReservada
                                                                && token.getValor().getValorTexto().equals("else")) {
                                                            token = lexico.nextToken();
                                                            if (token.getClasse() == Classe.palavraReservada
                                                                    && token.getValor().getValorTexto()
                                                                            .equals("begin")) {
                                                                token = lexico.nextToken();
                                                                sentencas();
                                                                if (token.getClasse() == Classe.palavraReservada
                                                                        && token.getValor().getValorTexto()
                                                                                .equals("end")) {
                                                                    token = lexico.nextToken();
                                                                } else {
                                                                    System.err.println(token.getLinha() + ","
                                                                            + token.getColuna()
                                                                            + " Erro: era esperada a palavra reservada end");
                                                                }
                                                            } else {
                                                                System.err.println(token.getLinha() + ","
                                                                        + token.getColuna()
                                                                        + " Erro: era esperada a palavra reservada begin ");
                                                            }
                                                        }
                                                        // else {
                                                        // System.err.println(token.getLinha() + ","
                                                        // + token.getColuna()
                                                        // + " Erro: era a Palavra Reserva Else após um If");
                                                        // }
                                                    } else {
                                                        System.err.println(token.getLinha() + "," + token.getColuna()
                                                                + " Erro: era a Palavra Reserva end");
                                                    }
                                                } else {
                                                    System.err.println(token.getLinha() + "," + token.getColuna()
                                                            + " Erro: era esperada a palavra reservada begin");
                                                }
                                            } else {
                                                System.err.println(token.getLinha() + "," + token.getColuna()
                                                        + " Erro: era esperada a palavra reservada then");
                                            }
                                        } else {
                                            System.err.println(token.getLinha() + "," + token.getColuna());
                                        }
                                    }
                                } else {
                                    if (token.getClasse() == Classe.identificador) {
                                        token = lexico.nextToken();
                                        // {A13}
                                        if (token.getClasse() == Classe.atribuicao) {
                                            token = lexico.nextToken();
                                            expressao();
                                        } else {
                                            System.err.println(token.getLinha() + "," + token.getColuna()
                                                    + " Erro: era esperado um sinal de atribuição");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // <pfalsa> ::= else {A25} begin <sentencas> end | ε
    private void pfalsa() {
        if (token.getClasse() == Classe.palavraReservada
                && token.getValor().getValorTexto().equals("else")) {
            token = lexico.nextToken();
            if (token.getClasse() == Classe.palavraReservada
                    && token.getValor().getValorTexto().equals("begin")) {
                token = lexico.nextToken();
                sentencas();
                if (token.getClasse() == Classe.palavraReservada
                        && token.getValor().getValorTexto().equals("end")) {
                    token = lexico.nextToken();
                }
            }
        }
    }

    // <expressao_logica> ::= <termo_logico> <mais_expr_logica>
    private void expressao_logica() {
        termo_logico();
        mais_expr_logica();
    }

    // <mais_expr_logica> ::= or <termo_logico> <mais_expr_logica> {A26} | ε
    private void mais_expr_logica() {
        if (token.getClasse() == Classe.palavraReservada
                && token.getValor().getValorTexto().equals("or")) {
            token = lexico.nextToken();
            termo_logico();
            mais_expr_logica();
        }
    }

    // <termo_logico> ::= <fator_logico> <mais_termo_logico>
    private void termo_logico() {
        fator_logico();
        mais_termo_logico();
    }

    // <mais_termo_logico> ::= and <fator_logico> <mais_termo_logico> {A27} | ε
    private void mais_termo_logico() {
        if (token.getClasse() == Classe.palavraReservada
                && token.getValor().getValorTexto().equals("and")) {
            token = lexico.nextToken();
            fator_logico();
            mais_termo_logico();
        }
    }

    private void fator_logico() {
        if (token.getClasse() == Classe.palavraReservada
                && token.getValor().getValorTexto().equals("not")) {
            token = lexico.nextToken();
            fator_logico();
        } else {
            if (token.getClasse() == Classe.parentesesEsquerdo) {
                token = lexico.nextToken();
                expressao_logica();
                if (token.getClasse() == Classe.parentesesDireito) {
                    token = lexico.nextToken();
                } else {
                    System.err.println(token.getLinha() + "," + token.getColuna()
                            + " Erro: era esperado um parênteses direito");
                }
            } else {
                if (token.getClasse() == Classe.palavraReservada &&
                        (token.getValor().getValorTexto().equals("true") ||
                                token.getValor().getValorTexto().equals("false") ||
                                token.getValor().getValorTexto().equals("not"))) {
                    token = lexico.nextToken();
                } else {
                    relacional();
                }
            }
        }
    }

    private void relacional() {
        expressao();
        if (token.getClasse() == Classe.operadorIgual
                || token.getClasse() == Classe.operadorDiferente
                || token.getClasse() == Classe.operadorMaior
                || token.getClasse() == Classe.operadorMaiorIgual
                || token.getClasse() == Classe.operadorMenor
                || token.getClasse() == Classe.operadorMenorIgual) {
            token = lexico.nextToken();
            expressao();
        } else {
            System.err.println(token.getLinha() + "," + token.getColuna()
                    + " Erro: era esperado um operador relacional");
        }
    }

    // <expressao> ::= <termo> <mais_expressao>
    private void expressao() {
        termo();
        mais_expressao();
    }

    // <mais_expressao> ::= + <termo> <mais_expressao> {A37} |
    // - <termo> <mais_expressao> {A38} | ε
    private void mais_expressao() {
        if (token.getClasse() == Classe.operadorSoma
                || token.getClasse() == Classe.operadorSubtracao) {
            token = lexico.nextToken();
            termo();
            mais_expressao();
        }
    }

    // <termo> ::= <fator> <mais_termo>
    private void termo() {
        fator();
        mais_termo();
    }

    // <mais_termo> ::= * <fator> <mais_termo> {A39} | / <fator> <mais_termo> {A40}
    // | ε
    private void mais_termo() {
        if (token.getClasse() == Classe.operadorMultiplicacao
                || token.getClasse() == Classe.operadorDivisao) {
            token = lexico.nextToken();
            fator();
            mais_termo();
        }
    }

    // <fator> ::= <id> {A55} | <intnum> {A41} | ( <expressao> )
    private void fator() {
        if (token.getClasse() == Classe.identificador) {
            token = lexico.nextToken();
        } else {
            if (token.getClasse() == Classe.numeroInteiro) {
                token = lexico.nextToken();
            } else {
                if (token.getClasse() == Classe.parentesesEsquerdo) {
                    token = lexico.nextToken();
                    expressao();
                    if (token.getClasse() == Classe.parentesesDireito) {
                        token = lexico.nextToken();
                    } else {
                        System.err.println(token.getLinha() + "," + token.getColuna()
                                + " Erro: era esperado um parênteses direito");
                    }
                } else {
                    System.err.println(token.getLinha() + "," + token.getColuna()
                            + " Erro: era esperado um identificador, número inteiro ou parênteses esquerdo");
                }
            }
        }
    }
}
