package com.example.maratona.model;

import java.sql.Date;

public class Maratonas {



    //atributos
    private int Id;
    private int criador;
    private String Nome;
    private String local;
    private Date data_inicio;
    private Date data_final;
    private String status;
    private String distancia;
    private String descricao;
    private int limite_participantes;
    private String regras;
    private float valor;
    private String tipo_terreno;
    private String clima_esperado;

    public Maratonas(int id, int criador, String nome, String local, Date data_final, Date data_inicio, String status, String distancia, String descricao, String regras, int limite_participantes, float valor, String tipo_terreno, String clima_esperado) {
        Id = id;
        this.criador = criador;
        Nome = nome;
        this.local = local;
        this.data_final = data_final;
        this.data_inicio = data_inicio;
        this.status = status;
        this.distancia = distancia;
        this.descricao = descricao;
        this.regras = regras;
        this.limite_participantes = limite_participantes;
        this.valor = valor;
        this.tipo_terreno = tipo_terreno;
        this.clima_esperado = clima_esperado;
    }

    public Maratonas(int id) {
        Id = id;
    }

    public Maratonas() {
    }

    //Getters e Setters

    public int getId() {
        return Id;
    }

    public int getCriador() {
        return criador;
    }

    public String getLocal() {
        return local;
    }

    public String getNome() {
        return Nome;
    }

    public Date getData_inicio() {
        return data_inicio;
    }

    public Date getData_final() {
        return data_final;
    }

    public String getStatus() {
        return status;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getDistancia() {
        return distancia;
    }

    public int getLimite_participantes() {
        return limite_participantes;
    }

    public String getRegras() {
        return regras;
    }

    public float getValor() {
        return valor;
    }

    public String getTipo_terreno() {
        return tipo_terreno;
    }

    public String getClima_esperado() {
        return clima_esperado;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setCriador(int criador) {
        this.criador = criador;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public void setData_inicio(Date data_inicio) {
        this.data_inicio = data_inicio;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setData_final(Date data_final) {
        this.data_final = data_final;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setLimite_participantes(int limite_participantes) {
        this.limite_participantes = limite_participantes;
    }

    public void setRegras(String regras) {
        this.regras = regras;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public void setTipo_terreno(String tipo_terreno) {
        this.tipo_terreno = tipo_terreno;
    }

    public void setClima_esperado(String clima_esperado) {
        this.clima_esperado = clima_esperado;
    }
}
