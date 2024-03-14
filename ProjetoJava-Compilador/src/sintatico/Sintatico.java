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
        System.out.println("\nAnalisando: " + nomeArquivo);
        token = lexico.nextToken();
        programa();
    }

    // <programa> ::= program <id> {A01} ; <corpo> • {A45}
    private void programa() {
        if (ehPalavraReservada(token, "program")) {
            token = lexico.nextToken();
            if (token.getClasse() == Classe.identificador) {
                token = lexico.nextToken();
                // {AO1}
                if (token.getClasse() == Classe.pontoEVirgula) {
                    token = lexico.nextToken();
                    corpo();
                    if (token.getClasse() == Classe.ponto) {
                        token = lexico.nextToken();
                        // {A45}
                    } else {
                        System.err.println(token.getLinha() + ", " + token.getColuna() + " |"
                                + "(.) Ponto final esperado ao final do programa!");
                    }
                } else {
                    System.err.println(token.getLinha() + ", " + token.getColuna() + " |"
                            + "(;) Ponto e virgula esperado após ao nome do programa!");
                }
            } else {
                System.err.println(token.getLinha() + ", " + token.getColuna() + " |"
                        + "Nome do programa principal esperado!");
            }
        } else {
            System.err.println(token.getLinha() + ", " + token.getColuna() + " |"
                    + "Palavra reservada 'program' esperada no inicio do programa principal!");
        }
    }

    // <corpo> ::= <declara> <rotina> {A44} begin <sentencas> end {A46}
    private void corpo() {
        declara();
        // rotina();
        // {A44}
        if (ehPalavraReservada(token, "begin")) {
            token = lexico.nextToken();
            // sentencas();
            if (ehPalavraReservada(token, "end")) {
                token = lexico.nextToken();
                // {A46}
            } else {
                System.err.println(token.getLinha() + ", " + token.getColuna() + " |"
                        + "Palavra reservada 'end' esperada no final do programa principal!");
            }
        } else {
            System.err.println(token.getLinha() + ", " + token.getColuna() + " |"
                    + "Palavra reservada 'begin' esperada no inicio do programa principal!");
        }
    }

    // <declara> ::= var <dvar> <mais_dc> | ε
    private void declara() {
        if (ehPalavraReservada(token, "var")) {
            token = lexico.nextToken();
            dvar();
            mais_dc();
        }
    }

    // <mais_dc> ::= ; <cont_dc>
    private void mais_dc() {
        if (token.getClasse() == Classe.pontoEVirgula) {
            token = lexico.nextToken();
            cont_dc();
        } else {
            System.err.println(token.getLinha() + "," + token.getColuna()
                    + " Erro: era esperado um ponto e vírgula após a declaração - s mais_dc()");
        }
    }

    // <cont_dc> ::= <dvar> <mais_dc> | ε
    private void cont_dc() {
        if (token.getClasse() == Classe.identificador) {
            dvar();
            mais_dc();
        }
    }

    // <dvar> ::= <variaveis> : <tipo_var> {A02}
    private void dvar() {
        variaveis();
        if (token.getClasse() == Classe.doisPontos) {
            token = lexico.nextToken();
            tipo_var();
        } else {
            System.err.println(token.getLinha() + "," + token.getColuna()
                    + " Erro: era esperado dois pontos após a declaração de variáveis - Regra dvar");
        }
    }

    // <tipo_var> ::= integer
    private void tipo_var() {
        if (ehPalavraReservada(token, "integer")) {
            token = lexico.nextToken();
        } else {
            System.err.println(token.getLinha() + "," + token.getColuna()
                    + " Erro: era esperada a palavra reservada integer ou real - Regra tipo_var");
        }
    }

    // <variaveis> ::= <id> {A03} <mais_var>
    private void variaveis() {
        if (token.getClasse() == Classe.identificador) {
            token = lexico.nextToken();
            // {A03}
            mais_var();
        } else {
            System.err.println(token.getLinha() + "," + token.getColuna()
                    + " Erro: era esperado um identificador - Regra  variaveis");
        }
    }

    // <mais_var> ::= , <variaveis> | ε
    private void mais_var() {
        if (token.getClasse() == Classe.virgula) {
            token = lexico.nextToken();
            variaveis();
        }
    }

    // <sentencas> ::= <comando> <mais_sentencas>
    private void sentencas() {
        comando();
        mais_sentencas();
    }

    // <mais_sentencas> ::= ; <cont_sentencas>
    private void mais_sentencas() {
        if (token.getClasse() == Classe.pontoEVirgula) {
            token = lexico.nextToken();
            cont_sentencas();
        }
    }

    // <cont_sentencas> ::= <sentencas> | ε
    private void cont_sentencas() {
        if (token.getClasse() == Classe.identificador
                || token.getClasse() == Classe.palavraReservada && (token.getValor().getValorTexto().equals("if") ||
                        token.getValor().getValorTexto().equals("for") ||
                        token.getValor().getValorTexto().equals("while") ||
                        token.getValor().getValorTexto().equals("read") ||
                        token.getValor().getValorTexto().equals("repeat") ||
                        token.getValor().getValorTexto().equals("writeln") ||
                        token.getValor().getValorTexto().equals("write"))) {
            sentencas();
        }
    }
    //<var_read> ::= <id> {A08} <mais_var_read>
    private void var_read(){
        if(token.getClasse() == Classe.identificador){
            token = lexico.nextToken();
            //{A08}
            mais_var_read();
        }else{
            System.err.println(token.getLinha() + "," + token.getColuna()
            + " Erro: era esperado um identificador - Regra var_read");
        }
    }
    //<mais_var_read> ::= , <var_read> | ε
    private void mais_var_read(){
        if (token.getClasse() == Classe.virgula) {
            token = lexico.nextToken();
            var_read();
        }
    }
    //<exp_write> ::= <id> {A09} <mais_exp_write> |
    //<string> {A59} <mais_exp_write> |
    //<intnum> {A43} <mais_exp_write>
    private void exp_write(){
        if (token.getClasse() == Classe.identificador ||
            token.getClasse() == Classe.string || 
            token.getClasse() == Classe.numeroInteiro) {
            token = lexico.nextToken();
            //{A09}
            mais_exp_write();
        }else{
            System.err.println(token.getLinha() + "," + token.getColuna()
                    + " Erro: era esperado uma expressão após a palavra reservada write - Regra exp_write");
        }
    }
    // <mais_exp_write> ::=  ,  <exp_write> | ε
    private void mais_exp_write(){
        if (token.getClasse() == Classe.virgula) {
            token = lexico.nextToken();
        }
    }   
    //     <comando> ::=
    //    read ( <var_read> ) |
    //    write ( <exp_write> ) |
    //    writeln ( <exp_write> ) {A61} |
    //   for <id> {A57} := <expressao> {A11} to <expressao> {A12} do begin <sentencas> end {A13} |
    //    repeat {A14} <sentencas> until ( <expressao_logica> ) {A15} |
    //    while {A16} ( <expressao_logica> ) {A17} do begin <sentencas> end {A18} |
    //    if ( <expressao_logica> ) {A19} then begin <sentencas> end {A20} <pfalsa> {A21} |
    //    <id> {A49} := <expressao> {A22} | <chamada_procedimento>
    // <pfalsa> ::= else {A25} begin <sentencas> end | ε
    private void comando() {
        if (token.getClasse() == Classe.identificador) {
            token = lexico.nextToken();
            // {A04}
            if (token.getClasse() == Classe.atribuicao) {
                token = lexico.nextToken();
                // expressao();
            } else {
                System.err.println(token.getLinha() + "," + token.getColuna()
                        + " Erro: era esperado um operador de atribuição após o identificador");
            }
        } else if (token.getClasse() == Classe.palavraReservada &&
                token.getValor().getValorTexto().equals("if")) {
            token = lexico.nextToken();
            // {A05}
            if (token.getClasse() == Classe.parentesesEsquerdo) {
                token = lexico.nextToken();
                // expressao();
                // {A06}
                if (token.getClasse() == Classe.parentesesDireito) {
                    token = lexico.nextToken();
                    // {A07}
                    if (token.getClasse() == Classe.palavraReservada &&
                            token.getValor().getValorTexto().equals("then")) {
                        token = lexico.nextToken();
                        sentencas();
                        // {A09}
                        if (token.getClasse() == Classe.palavraReservada &&
                                token.getValor().getValorTexto().equals("else")) {
                            token = lexico.nextToken();
                            sentencas();
                        }
                    } else {
                        System.err.println(token.getLinha() + "," + token.getColuna()
                                + " Erro: era esperada a palavra reservada then");
                    }
                } else {
                    System.err.println(token.getLinha() + "," + token.getColuna()
                            + " Erro: era esperado um parêntese direito");
                }
            } else {
                System.err.println(token.getLinha() + "," + token.getColuna()
                        + " Erro: era esperado um parêntese esquerdo");
            }
        } else if (token.getClasse() == Classe.palavraReservada &&
                token.getValor().getValorTexto().equals("while")) {
            token = lexico.nextToken();
            // {A10}
            if (token.getClasse() == Classe.parentesesEsquerdo) {
                token = lexico.nextToken();
                // expressao();
            } else {
                System.err.println(token.getLinha() + "," + token.getColuna()
                        + " Erro: era esperado um parêntese esquerdo");
            }
        }
    }

    private boolean ehPalavraReservada(Token token, String palavra) {
        return (token.getClasse() == Classe.palavraReservada &&
                token.getValor().getValorTexto().equals(palavra));
    }
}
