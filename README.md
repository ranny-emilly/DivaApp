# NOME

Sistema de gerenciamento de uma loja de roupas femininas desenvolvido em **Java** para a disciplina de **Programação Orientada a Objetos** da Universidade Federal de Goiás (UFG).

## Sobre o Projeto

O projeto tem como objetivo simular o gerenciamento interno de uma loja de roupas femininas, permitindo o cadastro e gerenciamento de clientes, produtos e pedidos de venda.

A aplicação foi desenvolvida aplicando os principais conceitos de Programação Orientada a Objetos, além de persistência de dados, tratamento de exceções, interface gráfica e armazenamento de informações em arquivos.

---

## Objetivo

Desenvolver uma aplicação desktop utilizando Java que permita o gerenciamento completo de uma loja de roupas femininas, aplicando conceitos como:

* Encapsulamento
* Herança
* Polimorfismo
* Abstração
* Interfaces
* Classes Abstratas
* Coleções (ArrayList)
* Persistência em Arquivos
* Tratamento de Exceções
* Interface gráfica com JOptionPane

---

## Funcionalidades

### Clientes

* Cadastro de clientes
* Consulta de clientes
* Alteração de dados
* Exclusão de clientes
* Listagem completa

### Produtos

* Cadastro de roupas
* Controle de estoque
* Alteração de preços
* Consulta de produtos
* Exclusão de produtos
* Listagem completa

### Pedidos

* Registro de pedidos de venda
* Associação entre cliente e produtos
* Cálculo automático do valor total
* Atualização do estoque

### Persistência

* Leitura automática dos dados ao iniciar o sistema
* Gravação automática dos dados em arquivos locais

---

## Requisitos Funcionais

* CRUD completo de Clientes
* CRUD completo de Produtos
* Registro de Pedidos
* Associação entre Clientes e Produtos
* Geração automática de identificadores sequenciais
* Persistência dos dados
* Interface gráfica utilizando JOptionPane

---

## Tecnologias

* Java
* Programação Orientada a Objetos
* JOptionPane
* Java Collections (ArrayList)
* Serializable
* Javadoc

---

## Estrutura do Projeto

```text
src/
├── model/
├── service/
├── repository/
├── exception/
├── util/
└── view/
```

---

## Modelo do Sistema

O sistema foi modelado utilizando UML e possui as seguintes entidades principais:

* Cliente
* Produto (Classe Abstrata)
* Roupa
* Pedido

### Relacionamentos

* Roupa herda de Produto
* Pedido possui um Cliente
* Pedido contém diversos Produtos

---

## Conceitos Aplicados

* Encapsulamento
* Herança
* Polimorfismo
* Abstração
* Interfaces
* Classes Abstratas
* Coleções (ArrayList)
* Persistência em Arquivos
* Tratamento de Exceções
* Construtores
* Métodos de acesso (Getters e Setters)
* Sobrescrita de métodos
* Identificadores automáticos utilizando atributos estáticos

---

## Requisitos Implementados

* ✔ Implementação das classes do diagrama UML
* ✔ Identificadores automáticos utilizando atributos estáticos
* ✔ Persistência de dados em arquivos
* ✔ Classe abstrata
* ✔ Operações de Inclusão, Exclusão, Alteração, Consulta e Listagem (CRUD)
* ✔ Interface gráfica utilizando JOptionPane
* ✔ Tratamento de exceções personalizadas
* ✔ Utilização de ArrayList
* ✔ Documentação com Javadoc
* ✔ Geração do arquivo JAR

---

## Equipe

* Elisa Correia
* Emilly Ranny
* Jolie Pavan
* Thyago Siriano

---

## Disciplina

Programação Orientada a Objetos

Universidade Federal de Goiás (UFG)

---

Projeto desenvolvido como trabalho da disciplina de Programação Orientada a Objetos.
