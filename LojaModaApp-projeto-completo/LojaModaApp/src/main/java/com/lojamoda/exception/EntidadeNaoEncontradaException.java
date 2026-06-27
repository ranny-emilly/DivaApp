package com.lojamoda.exception;

/**
 * Exceção lançada quando uma operação de consulta, alteração ou exclusão
 * é solicitada para um identificador (ID) que não existe na base de dados
 * em memória (Cliente, Produto ou Pedido).
 *
 * @author Elisa Correia, Emilly Ranny, Jolie Pavan
 */
public class EntidadeNaoEncontradaException extends Exception {

    /**
     * Cria uma nova exceção informando que a entidade do tipo indicado
     * com o ID informado não foi localizada.
     *
     * @param tipoEntidade nome do tipo de entidade (ex: "Cliente", "Produto", "Pedido")
     * @param id           identificador que não foi encontrado
     */
    public EntidadeNaoEncontradaException(String tipoEntidade, int id) {
        super(String.format("%s com ID %d não foi encontrado(a).", tipoEntidade, id));
    }
}
