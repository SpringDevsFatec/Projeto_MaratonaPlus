package com.example.maratona.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Date;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Inscricao {

    private int idInscricao;
    private int idCorredor;
    private int idMaratona;
    private Date dataHora;
    private String formaPagamento;
    private String status;

    public Inscricao(int idInscricao, int idCorredor, int idMaratona, String formaPagamento, Date dataHora, String status) {
        this.idInscricao = idInscricao;
        this.idCorredor = idCorredor;
        this.idMaratona = idMaratona;
        this.formaPagamento = formaPagamento;
        this.dataHora = dataHora;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
