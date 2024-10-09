package com.example.maratona.model;

import java.util.Date;

public class Corredores {
    private int idCorredor;
    private String nome;
    private String telefone;
    private String email;
    private String senha;
    //private Date dataNascimento;
    private String cpf;
    private String endereco;
    //private String genero;
    //private String urlFoto;
    private String paisOrigem;

    public Corredores(int idCorredor, String nome, String telefone, String email, String senha, String cpf, String endereco, String paisOrigem) {
        this.idCorredor = idCorredor;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;
        this.endereco = endereco;
        this.paisOrigem = paisOrigem;
    }

    public Corredores(int idCorredor) {
        this.idCorredor = idCorredor;
    }

    public Corredores() {
    }

    //Getters e Setters


    public int getIdCorredor() {
        return idCorredor;
    }

    public void setIdCorredor(int idCorredor) {
        this.idCorredor = idCorredor;
    }

    public String getPaisOrigem() {
        return paisOrigem;
    }

    public void setPaisOrigem(String paisOrigem) {
        this.paisOrigem = paisOrigem;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
