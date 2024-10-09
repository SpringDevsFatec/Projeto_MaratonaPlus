package com.example.maratona.model;

import java.sql.Date;

public class Inscricao {

    private int idInscricao;
    private int idCorredor;
    private int idMaratona;
    private Date dataHora;
    private String formaPagamento;

    public Inscricao(int idCorredor, int idInscricao, int idMaratona, Date dataHora, String formaPagamento) {
        this.idCorredor = idCorredor;
        this.idInscricao = idInscricao;
        this.idMaratona = idMaratona;
        this.dataHora = dataHora;
        this.formaPagamento = formaPagamento;
    }

    public Inscricao(int idInscricao) {
        this.idInscricao = idInscricao;
    }

    public Inscricao() {
    }

    // Getters e Setters


    public int getIdInscricao() {
        return idInscricao;
    }

    public void setIdInscricao(int idInscricao) {
        this.idInscricao = idInscricao;
    }

    public int getIdCorredor() {
        return idCorredor;
    }

    public void setIdCorredor(int idCorredor) {
        this.idCorredor = idCorredor;
    }

    public int getIdMaratona() {
        return idMaratona;
    }

    public void setIdMaratona(int idMaratona) {
        this.idMaratona = idMaratona;
    }

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }
}
