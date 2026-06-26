package com.lojamoda.exception;

/**
 * Exceção customizada lançada quando uma operação de venda tenta
 * consumir uma quantidade de produto maior do que a disponível em estoque.
 *
 * <p>Esta exceção representa uma regra de negócio do domínio da loja de
 * moda: nenhuma venda pode ser concluída se o produto não possuir
 * quantidade suficiente em estoque no momento do registro do pedido.</p>
 *
 * @author Elisa Correia, Emilly Ranny, Jolie Pavan
 */
public class EstoqueInsuficienteException extends Exception {

    private final String nomeProduto;
    private final int quantidadeSolicitada;
    private final int quantidadeDisponivel;

    /**
     * Cria uma nova exceção de estoque insuficiente.
     *
     * @param nomeProduto          nome do produto que não possui estoque suficiente
     * @param quantidadeSolicitada quantidade que se tentou vender
     * @param quantidadeDisponivel quantidade atualmente disponível em estoque
     */
    public EstoqueInsuficienteException(String nomeProduto, int quantidadeSolicitada, int quantidadeDisponivel) {
        super(String.format(
                "Estoque insuficiente para o produto '%s'. Solicitado: %d, Disponível: %d",
                nomeProduto, quantidadeSolicitada, quantidadeDisponivel));
        this.nomeProduto = nomeProduto;
        this.quantidadeSolicitada = quantidadeSolicitada;
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    /**
     * @return o nome do produto cujo estoque é insuficiente
     */
    public String getNomeProduto() {
        return nomeProduto;
    }

    /**
     * @return a quantidade que foi solicitada na operação que falhou
     */
    public int getQuantidadeSolicitada() {
        return quantidadeSolicitada;
    }

    /**
     * @return a quantidade que efetivamente está disponível em estoque
     */
    public int getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }
}
