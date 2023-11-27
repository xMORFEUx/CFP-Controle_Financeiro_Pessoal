package com.example.ss.model;

public class Produto {

    private int foto;
    private String nome, descricao, preco;

    public Produto(int foto, String nome, String descricao, String preco) {

        this.foto = foto;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
    }

    public int getFoto(){
        return foto;
    }

    public void setFoto(int foto){
        this.foto = foto;
    }

    public String getNome(){
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getDescricao(){
        return descricao;
    }

    public void setDescricao(String desricao){
        this.descricao = desricao;
    }

    public String getPreco(){
        return preco;
    }

    public void setPreco(String preco){
        this.preco = preco;
    }
}
