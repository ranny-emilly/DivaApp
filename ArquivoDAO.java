package com.lojamoda.persistence;

import java.io.*;
import java.util.ArrayList;

public class ArquivoDAO<T extends Serializable> {

    private final String caminhoArquivo;
    public ArquivoDAO(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

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
