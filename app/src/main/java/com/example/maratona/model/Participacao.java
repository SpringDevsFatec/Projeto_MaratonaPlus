package com.example.maratona.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Time;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Participacao {

    private int idParticipacao;
    private int idInscricao;
    private String statusConclusao;
    private Time tempoRegistrado;
    private Time tempoInicio;
    private Time tempoFim;
    private int Passos;

    public Participacao() {
    }

    public Participacao(int idParticipacao) {
        this.idParticipacao = idParticipacao;
    }

    public Participacao(int idParticipacao, int idInscricao, String statusConclusao, Time tempoRegistrado, Time tempoFim, Time tempoInicio, int passos) {
        this.idParticipacao = idParticipacao;
        this.idInscricao = idInscricao;
        this.statusConclusao = statusConclusao;
        this.tempoRegistrado = tempoRegistrado;
        this.tempoFim = tempoFim;
        this.tempoInicio = tempoInicio;
        Passos = passos;
    }

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

    public Time getTempoRegistrado() {
        return tempoRegistrado;
    }

    public void setTempoRegistrado(Time tempoRegistrado) {
        this.tempoRegistrado = tempoRegistrado;
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

    public Time getTempoFim() {
        return tempoFim;
    }

    public void setTempoFim(Time tempoFim) {
        this.tempoFim = tempoFim;
    }

    public int getPassos() {
        return Passos;
    }

    public void setPassos(int passos) {
        Passos = passos;
    }
}