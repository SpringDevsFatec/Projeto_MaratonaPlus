package com.example.maratona.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Time;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Participacao {

    private int idParticipacao;
    private int idInscricao;
    private String statusConclusao;
    private String tempoRegistrado;
    private String tempoIngresso;
    private String tempoInicio;
    private String tempoFim;
    private int Passos;
    private float velocidadeKm;
    private float velocidadeMs;

    public Participacao() {
    }

    public Participacao(int idParticipacao) {
        this.idParticipacao = idParticipacao;
    }

    public Participacao(int idParticipacao, int idInscricao, String statusConclusao, String tempoRegistrado, String tempoIngresso, String tempoInicio, String tempoFim, int passos, float velocidadeKm, float velocidadeMs) {
        this.idParticipacao = idParticipacao;
        this.idInscricao = idInscricao;
        this.statusConclusao = statusConclusao;
        this.tempoRegistrado = tempoRegistrado;
        this.tempoIngresso = tempoIngresso;
        this.tempoInicio = tempoInicio;
        this.tempoFim = tempoFim;
        Passos = passos;
        this.velocidadeKm = velocidadeKm;
        this.velocidadeMs = velocidadeMs;
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

    public String getStatusConclusao() {
        return statusConclusao;
    }

    public void setStatusConclusao(String statusConclusao) {
        this.statusConclusao = statusConclusao;
    }

    public String getTempoRegistrado() {
        return tempoRegistrado;
    }

    public void setTempoRegistrado(String tempoRegistrado) {
        this.tempoRegistrado = tempoRegistrado;
    }

    public String getTempoInicio() {
        return tempoInicio;
    }

    public void setTempoInicio(String tempoInicio) {
        this.tempoInicio = tempoInicio;
    }

    public String getTempoFim() {
        return tempoFim;
    }

    public void setTempoFim(String tempoFim) {
        this.tempoFim = tempoFim;
    }

    public int getPassos() {
        return Passos;
    }

    public void setPassos(int passos) {
        Passos = passos;
    }

    public float getVelocidadeKm() {
        return velocidadeKm;
    }

    public void setVelocidadeKm(float velocidadeKm) {
        this.velocidadeKm = velocidadeKm;
    }

    public float getVelocidadeMs() {
        return velocidadeMs;
    }

    public void setVelocidadeMs(float velocidadeMs) {
        this.velocidadeMs = velocidadeMs;
    }

    public String getTempoIngresso() {
        return tempoIngresso;
    }

    public void setTempoIngresso(String tempoIngresso) {
        this.tempoIngresso = tempoIngresso;
    }
}