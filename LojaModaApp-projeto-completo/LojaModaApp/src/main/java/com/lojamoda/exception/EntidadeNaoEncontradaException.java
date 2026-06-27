package com.lojamoda.exception;

public class EntidadeNaoEncontradaException extends Exception {

    public EntidadeNaoEncontradaException(String tipoEntidade, int id) {
        super(String.format("%s com ID %d não foi encontrado(a).", tipoEntidade, id));
    }
}
