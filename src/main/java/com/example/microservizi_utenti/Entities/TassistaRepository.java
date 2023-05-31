package com.example.microservizi_utenti.Entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TassistaRepository extends JpaRepository<Tassista, String> {

    @Query("SELECT tas.email,tas.nome,tas.cognome,tas.nPatente,tas.scadenza,d.disponibilita,a.targa,a.descrizione,a.nPosti,a.bagagliaio,a.seggiolino FROM Tassista AS tas, Disponibilita AS d, Automobile AS a  WHERE  tas.email=d.email AND d.email=a.tassista.email AND tas.email = ?1")
    public List<Object> findByEmail(String email);

    @Query("SELECT tas.email,tas.nome,tas.cognome,tas.nPatente,tas.scadenza,d.disponibilita,a.targa,a.descrizione,a.nPosti,a.bagagliaio,a.seggiolino FROM Tassista AS tas, Disponibilita AS d, Automobile AS a  WHERE  tas.email=d.email AND d.email=a.tassista.email AND tas.email IN ?1")
    public List<Object> findAllInfo(List<String> tassisti);

}
