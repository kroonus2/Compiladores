package sintatico;

import lexico.Classe;
import lexico.Lexico;
import lexico.Token;

public class Sintatico {
    private String nomeArquivo;
    private Lexico lexico;
    private Token token;

    public Sintatico(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
        lexico = new Lexico(nomeArquivo);
    }

    public void analisar() {
        System.out.println("Analisando " + nomeArquivo);
        token = lexico.nextToken();
        programa();

    }

    // <programa> ::= program <id> {A01} ; <corpo> • {A45}
    public void programa() {
        if (token.getClasse() == Classe.palavraReservada
                && token.getValor().getValorTexto().equals("program")) {
            token = lexico.nextToken();
            // Sempre que reconhecr um novo terminar receba o proximo token
            if (token.getClasse() == Classe.identificador) {
                token = lexico.nextToken();
                // {A01}
                if (token.getClasse() == Classe.pontoEVirgula) {
                    token = lexico.nextToken();
                    corpo();
                    if (token.getClasse() == Classe.ponto) {
                        token = lexico.nextToken();
                        // {A45}
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
            token = lexico.nextToken();
            // {A03}
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
            token = lexico.nextToken();
            // {A08}
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
                token = lexico.nextToken();
                // {A09}
                mais_exp_write();
            } else if (token.getClasse() == Classe.string) {
                token = lexico.nextToken();
                // {A59}
                mais_exp_write();
            } else if (token.getClasse() == Classe.numeroInteiro) {
                token = lexico.nextToken();
                // {A43}
                mais_exp_write();
            }
        } else {
            System.err.println(token.getLinha() + ", " + token.getColuna() +
                    " - Identificador, ou string, ou numeroInteiro esperado ao ler a função exp_write()");
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
                                                                        + " Erro: era esperada a palavra reservada begin");
                                                            }
                                                        }
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

    private void expressao_logica() {
        termo_logico();
        mais_expr_logica();
    }

    private void mais_expr_logica() {
        if (token.getClasse() == Classe.operadorOu) {
            token = lexico.nextToken();
            termo_logico();
            mais_expr_logica();
        }
    }

    private void termo_logico() {
        fator_logico();
        mais_termo_logico();
    }

    private void mais_termo_logico() {
        if (token.getClasse() == Classe.operadorE) {
            token = lexico.nextToken();
            fator_logico();
            mais_termo_logico();
        }
    }

    private void fator_logico() {
        if (token.getClasse() == Classe.operadorNegacao) {
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

    private void expressao() {
        termo();
        mais_expressao();
    }

    private void mais_expressao() {
        if (token.getClasse() == Classe.operadorSoma
                || token.getClasse() == Classe.operadorSubtracao) {
            token = lexico.nextToken();
            termo();
            mais_expressao();
        }
    }

    private void termo() {
        fator();
        mais_termo();
    }

    private void mais_termo() {
        if (token.getClasse() == Classe.operadorMultiplicacao
                || token.getClasse() == Classe.operadorDivisao) {
            token = lexico.nextToken();
            fator();
            mais_termo();
        }
    }

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
