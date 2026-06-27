package com.lojamoda.exception;

/**
 * Exceção lançada quando um CPF informado para um Cliente é inválido
 * (não respeita o formato esperado) ou já está cadastrado para outro cliente.
 *
 * @author Elisa Correia, Emilly Ranny, Jolie Pavan
 */
public class CpfInvalidoException extends Exception {

    /**
     * Cria uma nova exceção de CPF inválido com a mensagem informada.
     *
     * @param mensagem descrição do motivo da invalidade do CPF
     */
    public CpfInvalidoException(String mensagem) {
        super(mensagem);
    }
}
