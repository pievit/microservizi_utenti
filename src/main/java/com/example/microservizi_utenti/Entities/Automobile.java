package com.example.microservizi_utenti.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "automobile")
public class Automobile {
    @Id
    private String targa;

    private String descrizione;

    private int nPosti;

    private Boolean bagagliaio;

    private Boolean seggiolino;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "email_tas", nullable = false)
    private Tassista tassista;

    public Automobile(String targa, String descrizione, int nPosti, Boolean bagagliaio, Boolean seggiolino, Tassista tassista) {
        this.targa = targa;
        this.descrizione = descrizione;
        this.nPosti = nPosti;
        this.bagagliaio = bagagliaio;
        this.seggiolino = seggiolino;
        this.tassista = tassista;
    }


    public Automobile() {

    }

    public Tassista getTassista() {
        return tassista;
    }

    public void setTassista(Tassista tassista) {
        this.tassista = tassista;
    }

    public String getTarga() {
        return targa;
    }

    public void setTarga(String targa) {
        this.targa = targa;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public int getnPosti() {
        return nPosti;
    }

    public void setnPosti(int nPosti) {
        this.nPosti = nPosti;
    }

    public Boolean getBagagliaio() {
        return bagagliaio;
    }

    public void setBagagliaio(Boolean bagagliaio) {
        this.bagagliaio = bagagliaio;
    }

    public Boolean getSeggiolino() {
        return seggiolino;
    }

    public void setSeggiolino(Boolean seggiolino) {
        this.seggiolino = seggiolino;
    }

    @Override
    public String toString() {
        return "Automobile{" +
                "targa='" + targa + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", nPosti=" + nPosti +
                ", bagagliaio=" + bagagliaio +
                ", seggiolino=" + seggiolino +
                ", tassista=" + tassista +
                '}';
    }
}
