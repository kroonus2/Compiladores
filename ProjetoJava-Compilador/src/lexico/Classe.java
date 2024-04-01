package lexico;

public enum Classe {
    identificador,
    palavraReservada,
    numeroInteiro,
    EOF,
    operadorSoma, // + f
    operadorSubtracao, // - f
    operadorMultiplicacao, // * f
    operadorDivisao, // / f
    operadorMaior, // > f
    operadorMenor, // < f
    operadorMenorIgual, // <= f
    operadorDiferente, // <> f
    operadorMaiorIgual, // >= f 
    operadorIgual,  // = f 
    //operadorE,  // and
    //operadorOu,  // or
    //operadorNegacao,  // not
    atribuicao,  // := f
    pontoEVirgula,  // ; f
    virgula, // , f
    ponto,  // . f
    doisPontos, // : f
    parentesesEsquerdo, // ( f
    parentesesDireito, // ) f
    string
}