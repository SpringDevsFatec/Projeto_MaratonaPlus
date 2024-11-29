package com.example.maratona.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Date;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Maratonas {


    //atributos
    private int idMaratona;
    private int criador;
    private String Nome;
    private String local;
    private String data_inicio;
    private String data_final;
    private String status;
    private String distancia;
    private String descricao;
    private int limite_participantes;
    private String regras;
    private float valor;
    private String tipo_terreno;
    private String clima_esperado;
    private String NomeCriador;

    @Override
    public String toString() {
        return "Maratona "+ Nome
                //+ " id " + idMaratona // ou qualquer outra informação que você queira exibir na ListView
    ;
    }

    public Maratonas() {
    }

    public Maratonas(int id) {
        idMaratona = id;
    }

    public Maratonas(int idMaratona, int criador, String nome, String local, String data_inicio, String data_final, String status, String distancia, String descricao, String regras, int limite_participantes, float valor, String clima_esperado, String tipo_terreno) {
        this.idMaratona = idMaratona;
        this.criador = criador;
        Nome = nome;
        this.local = local;
        this.data_inicio = data_inicio;
        this.data_final = data_final;
        this.status = status;
        this.distancia = distancia;
        this.descricao = descricao;
        this.regras = regras;
        this.limite_participantes = limite_participantes;
        this.valor = valor;
        this.clima_esperado = clima_esperado;
        this.tipo_terreno = tipo_terreno;
    }

    public String getNomeCriador() {
        return NomeCriador;
    }

    public void setNomeCriador(String nomeCriador) {
        NomeCriador = nomeCriador;
    }

    public int getIdMaratona() {
        return idMaratona;
    }

    public void setIdMaratona(int id) {
        idMaratona = id;
    }

    public int getCriador() {
        return criador;
    }

    public void setCriador(int criador) {
        this.criador = criador;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getData_final() {
        return data_final;
    }

    public void setData_final(String data_final) {
        this.data_final = data_final;
    }

    public String getData_inicio() {
        return data_inicio;
    }

    public void setData_inicio(String data_inicio) {
        this.data_inicio = data_inicio;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getLimite_participantes() {
        return limite_participantes;
    }

    public void setLimite_participantes(int limite_participantes) {
        this.limite_participantes = limite_participantes;
    }

    public String getRegras() {
        return regras;
    }

    public void setRegras(String regras) {
        this.regras = regras;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getTipo_terreno() {
        return tipo_terreno;
    }

    public void setTipo_terreno(String tipo_terreno) {
        this.tipo_terreno = tipo_terreno;
    }

    public String getClima_esperado() {
        return clima_esperado;
    }

    public void setClima_esperado(String clima_esperado) {
        this.clima_esperado = clima_esperado;
    }
}

