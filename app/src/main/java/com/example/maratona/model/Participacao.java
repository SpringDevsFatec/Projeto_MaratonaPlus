package com.example.maratona.model;

import java.sql.Time;

public class Participacao {

    private int idParticipacao;
    private int idInscricao;
    private String statusConclusao;
    private Time tempoRegistrado;
    private Time tempoInicio;
    private Time tempoFim;

    public Participacao(int idInscricao, String statusConclusao, int idParticipacao, Time tempoRegistrado, Time tempoInicio, Time tempoFim) {
        this.idInscricao = idInscricao;
        this.statusConclusao = statusConclusao;
        this.idParticipacao = idParticipacao;
        this.tempoRegistrado = tempoRegistrado;
        this.tempoInicio = tempoInicio;
        this.tempoFim = tempoFim;
    }

    public Participacao(int idParticipacao) {
        this.idParticipacao = idParticipacao;
    }

    public Participacao() {
    }

    //Getters e Setters

    public int getIdParticipacao() {
        return idParticipacao;
    }

    public void setIdParticipacao(int idParticipacao) {
        this.idParticipacao = idParticipacao;
    }

    public int getIdInscricao() {
        return idInscricao;
    }

    public void setIdInscricao(int idInscricao) {
        this.idInscricao = idInscricao;
    }

    public String getStatusConclusao() {
        return statusConclusao;
    }

    public void setStatusConclusao(String statusConclusao) {
        this.statusConclusao = statusConclusao;
    }

    public Time getTempoInicio() {
        return tempoInicio;
    }

    public void setTempoInicio(Time tempoInicio) {
        this.tempoInicio = tempoInicio;
    }

    public Time getTempoRegistrado() {
        return tempoRegistrado;
    }

    public void setTempoRegistrado(Time tempoRegistrado) {
        this.tempoRegistrado = tempoRegistrado;
    }

    public Time getTempoFim() {
        return tempoFim;
    }

    public void setTempoFim(Time tempoFim) {
        this.tempoFim = tempoFim;
    }
}
