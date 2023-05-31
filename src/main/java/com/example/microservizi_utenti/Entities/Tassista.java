package com.example.microservizi_utenti.Entities;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;


@Entity
    @Table(name = "tassista")
public class Tassista {
    @Id
    private String email;

    private String nome;

    private String cognome;

    private String nPatente;

    private String scadenza;


    @OneToOne(mappedBy = "tassista", fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private Disponibilita disponibilita;

    @OneToOne(mappedBy = "tassista", fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private Automobile auto;



    public String getnPatente() {
        return nPatente;
    }

    public void setnPatente(String nPatente) {
        this.nPatente = nPatente;
    }

    public String getScadenza() {
        return scadenza;
    }

    public void setScadenza(String scadenza) {
        this.scadenza = scadenza;
    }


    public Tassista(String email, String nome, String cognome) {
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
    }

    public Tassista() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public Tassista(String email, String nome, String cognome, String nPatente, String scadenza) {
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
        this.nPatente = nPatente;
        this.scadenza = scadenza;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    @Override
    public String toString() {
        return "Tassista{" +
                "email='" + email + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", nPatente='" + nPatente + '\'' +
                ", scadenza=" + scadenza +
                '}';
    }


}
