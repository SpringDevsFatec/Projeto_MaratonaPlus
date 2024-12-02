package com.example.maratona.model;

public class Ranking {

    private int id;
    private int idMaratona;
    private int idCorredor;
    private double tempoTotal;
    private int posicao;

    public Ranking(int id, int idCorredor, int idMaratona, double tempoTotal, int posicao) {
        this.id = id;
        this.idCorredor = idCorredor;
        this.idMaratona = idMaratona;
        this.tempoTotal = tempoTotal;
        this.posicao = posicao;
    }

    @Override
    public String toString() {
        return "Ranking{" +
                "idCorredor=" + idCorredor +
                '}';
    }

    public Ranking(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public double getTempoTotal() {
        return tempoTotal;
    }

    public void setTempoTotal(double tempoTotal) {
        this.tempoTotal = tempoTotal;
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
}
