package com.controleEstoque.model;

public class Produto {
    private int Id;
    private String Nome;
    private double Preco;
    private int Quantidade;
    private String Data_criacao;

    public Produto(int id, String nome, double preco, int quantidade) {
        this.Id = id;
        this.Nome = nome;
        this.Preco = preco;
        this.Quantidade = quantidade;
    }

    public Produto(int id, String nome, double preco, int quantidade, String data_criacao) {
        this.Id = id;
        this.Nome = nome;
        this.Preco = preco;
        this.Quantidade = quantidade;
        this.Data_criacao = data_criacao;
    }

    public int getId() {
        return Id;
    }
    public String getNome() {
        return Nome;
    }
    public double getPreco() {
        return Preco;
    }
    public int getQuantidade() {
        return Quantidade;
    }
    public String getData_criacao() { return Data_criacao; }
}
