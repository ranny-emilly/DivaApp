package com.lojamoda.persistence;

import java.io.*;
import java.util.ArrayList;

/**
 * Classe utilitária responsável pela persistência de listas de objetos
 * em arquivos binários locais, utilizando serialização Java
 * ({@link Serializable}).
 *
 * <p>Esta classe é genérica e pode ser reutilizada para qualquer tipo de
 * entidade do sistema (Cliente, Produto, Pedido), bastando que a classe
 * implemente {@link Serializable}.</p>
 *
 * @param <T> tipo da entidade armazenada na lista persistida
 * @author Elisa Correia, Emilly Ranny, Jolie Pavan
 */
public class ArquivoDAO<T extends Serializable> {

    private final String caminhoArquivo;

    /**
     * Cria um novo gerenciador de persistência associado a um arquivo específico.
     *
     * @param caminhoArquivo caminho (relativo ou absoluto) do arquivo binário de dados
     */
    public ArquivoDAO(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    /**
     * Grava a lista de objetos informada no arquivo binário configurado,
     * sobrescrevendo qualquer conteúdo anterior.
     *
     * @param lista lista de objetos a serem persistidos
     * @throws IOException se ocorrer um erro de escrita no arquivo
     */
    public void salvar(ArrayList<T> lista) throws IOException {
        File arquivo = new File(caminhoArquivo);
        File diretorioPai = arquivo.getParentFile();
        if (diretorioPai != null && !diretorioPai.exists()) {
            diretorioPai.mkdirs();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            oos.writeObject(lista);
        }
    }

    /**
     * Carrega a lista de objetos armazenada no arquivo binário configurado.
     *
     * <p>Caso o arquivo não exista (por exemplo, na primeira execução da
     * aplicação), uma lista vazia é retornada em vez de lançar exceção.</p>
     *
     * @return a lista de objetos lida do arquivo, ou uma lista vazia caso o arquivo não exista
     * @throws IOException            se ocorrer um erro de leitura do arquivo
     * @throws ClassNotFoundException se a classe serializada não puder ser localizada
     */
    @SuppressWarnings("unchecked")
    public ArrayList<T> carregar() throws IOException, ClassNotFoundException {
        File arquivo = new File(caminhoArquivo);
        if (!arquivo.exists() || arquivo.length() == 0) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (ArrayList<T>) ois.readObject();
        }
    }
}
