package com.example.microservizi_utenti.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "disponibilita")
public class Disponibilita {

    @Id
    private String email;

    private String disponibilita;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "email", nullable = false)
    private Tassista tassista;

    public Disponibilita(String email, String disponibilita) {
        this.email = email;
        this.disponibilita = disponibilita;
    }

    public Disponibilita() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisponibilita() {
        return disponibilita;
    }

    public void setDisponibilita(String disponibilita) {
        this.disponibilita = disponibilita;
    }

}
