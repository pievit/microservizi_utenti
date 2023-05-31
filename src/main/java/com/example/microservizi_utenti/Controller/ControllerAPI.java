package com.example.microservizi_utenti.Controller;

import com.example.microservizi_utenti.Entities.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@CrossOrigin
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ControllerAPI {

    private final RabbitSender rabbitSender;
    @Autowired
    private ClienteRepository Clirepository;
    @Autowired
    private TassistaRepository Tasrepository;

    @Autowired
    private AutomobileRepository Autorepository;

    @Autowired
    private DisponibilitaRepository Disprepository;


    @GetMapping("/clienti")
    public List<Cliente> getAllClients(){
        return Clirepository.findAll();
    }

    @DeleteMapping("/deleteCliente/{email}")
    public String deleteCliente(@PathVariable("email") String email){
        try{
            Boolean flag = false;
            Iterator<Cliente> it =  Clirepository.findAll().iterator();
            while(it.hasNext()){
                Cliente cli = it.next();
                if(cli.getEmail().equals(email)){
                    Clirepository.delete(cli);
                    rabbitSender.sendMessage("delete: "+email);
                    flag = true;
                    break;
                }
            }
            if(flag){
                return "Cliente cancellato correttamente.";
            }else{
                return "Errore: Cliente non esiste.";
            }

        }catch(Exception e){
            return e.getMessage();
        }

    }


    @DeleteMapping("/deleteTassista/{email}")
    public String deleteTassista(@PathVariable("email") String email){
        try{
            Boolean flag = false;
            Iterator<Tassista> it =  Tasrepository.findAll().iterator();
            while(it.hasNext()){
                Tassista tas = it.next();
                if(tas.getEmail().equals(email)){
                    Tasrepository.delete(tas);
                    rabbitSender.sendMessage("delete: "+email);
                    flag = true;
                    break;
                }
            }
            if(flag){
                return "Tassista cancellato correttamente.";
            }else{
                return "Errore: Tassista non esiste.";
            }

        }catch(Exception e){
            return e.getMessage();
        }
    }

    @GetMapping("/tassisti")
    public List<Tassista> tassisti(){
        return Tasrepository.findAll();
    }

    @GetMapping("/tassista/{email}")
    public ResponseEntity<Tassista> tassista(@PathVariable("email") String email){
        try{

            Iterator<Tassista> it =  Tasrepository.findAll().iterator();
            while(it.hasNext()){
                Tassista tas = it.next();
                if(tas.getEmail().equals(email)){
                    return new ResponseEntity<>(tas, HttpStatus.OK);
                }
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/salvaDatiPatente")
    public String salvaDatiPatente(@RequestBody Tassista datitas){
        try{

            Tassista t = new Tassista();
            t.setEmail(datitas.getEmail());
            t.setNome(datitas.getNome());
            t.setCognome(datitas.getCognome());
            t.setnPatente(datitas.getnPatente());
            t.setScadenza(datitas.getScadenza());
            Tasrepository.save(t);
            return "OK, Dati aggiornati correttamente";
        }catch(Exception e){
            return "Qualcosa è andato storto :(" + e.getMessage()+"--->"+datitas.toString();
        }
    }

    @PutMapping("/automobile")
    public String automobile(@RequestBody Automobile auto){
       try{
           deleteAutomobileByEmail(auto.getTassista().getEmail());
           Autorepository.save(auto);
           return "Automobile inserita.";
       }catch(Exception e){
           return e.getMessage();
       }
    }

    @PostMapping("/salvaDatiTas")
    public String salvaDatiTas(@RequestBody ObjectNode map){
        try{
            ObjectMapper mapper = new ObjectMapper();
            Tassista tassista = mapper.readValue(map.get("tassista").toString(), Tassista.class);
            Automobile auto = mapper.readValue(map.get("automobile").toString(), Automobile.class);
            salvaDatiPatente(tassista);
            automobile(auto);
            return "Dati salvati correttamente.";
        }catch(Exception e){
            return e.getMessage();
        }

    }

    @GetMapping("/getAutomobile/{email}")
    public ResponseEntity<Automobile> getAutomobile(@PathVariable("email") String email){
        try{
            for (Automobile auto : Autorepository.findAll()) {
                if (auto.getTassista().getEmail().equals(email)) {
                    return new ResponseEntity<>(auto,HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.valueOf(e.getMessage()));
        }
    }

    @DeleteMapping("/deleteAutomobile/{targa}")
    public String deleteAutomobile(@PathVariable("targa") String targa) {
        try {
//            Autorepository.deleteById(targa);
//            return "Cancellazione automobile con targa:" + targa+" effettuata.";

            Boolean flag = false;
            Iterator<Automobile> it =  Autorepository.findAll().iterator();
            while(it.hasNext()){
                Automobile auto = it.next();
                if(auto.getTarga().equals(targa)){
                    Autorepository.deleteById(auto.getTarga());
                    flag = true;
                    break;
                }
            }
            if(flag){
                return "Atomobile con targa :"+targa+" cancellata.";
            }else{
                return "Errore: Automobile non esistente.";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }


    @DeleteMapping("/deleteAutomobileByEmail/{email}")
    public String deleteAutomobileByEmail(@PathVariable("email") String email) {
        try {

            Boolean flag = false;
            Iterator<Automobile> it =  Autorepository.findAll().iterator();
            while(it.hasNext()){
                Automobile auto = it.next();
                if(auto.getTassista().getEmail().equals(email)){
                    Autorepository.deleteById(auto.getTarga());
                    flag = true;
                    break;
                }
            }
            if(flag){
                return "Atomobile di "+ email +" cancellata.";
            }else{
                return "Errore: Automobile non esistente.";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PutMapping("/disponibilita")
    public String disponibilita(@RequestBody ObjectNode map){
        try{
            ObjectMapper mapper = new ObjectMapper();
            Disponibilita disp = new Disponibilita();
            disp.setEmail(mapper.readValue(map.get("email").toString(), String.class));
            disp.setDisponibilita(mapper.readValue(map.get("disp").toString(), String.class));
            Disprepository.save(disp);
            return "Disponibilità inserita.";
        }catch(Exception e){
            return e.getMessage();
        }
    }

    @DeleteMapping("/deleteDisponibilita/{email}")
    public String deleteDisponibilita(@PathVariable("email") String email){
       try{
//           Disprepository.deleteById(email);
//           return "Cancellazione disponibilità per "+email+" effettuata.";

           Boolean flag = false;
           Iterator<Disponibilita> it =  Disprepository.findAll().iterator();
           while(it.hasNext()){
               Disponibilita disp = it.next();
               if(disp.getEmail().equals(email)){
                   Disprepository.delete(disp);
                   flag = true;
                   break;
               }
           }
           if(flag){
               return "Disponibilità di "+email+" cancellata correttamente.";
           }else{
               return "Nessuna Disponibilità.";
           }

       }catch(Exception e){
           return e.getMessage();
       }

    }


    @GetMapping("/getDispTassista/{email}")
    public String getDispTassista(@PathVariable("email") String email){
        Iterator<Disponibilita> it = Disprepository.findAll().iterator();
        while(it.hasNext()){
            Disponibilita disp = it.next();
            if(disp.getEmail().equals(email)){
                return disp.getDisponibilita();
            }
        }
        return "Nessuna disponibilità per: "+email;
    }

    @GetMapping("/getDisponibilita/{giorno}/{orario}")
    public List<Object> getDisponibilita(@PathVariable("giorno") String giorno,@PathVariable("orario") String orario){
        Iterator<Disponibilita> it =  Disprepository.findAll().iterator();
        List<String> tassisti = new ArrayList<String>();
        while(it.hasNext()){
            Disponibilita disptas = it.next();
            String disp = disptas.getDisponibilita();
            JSONObject json = new JSONObject(disp);
            try{
                String[] ora = json.getJSONArray(giorno).toString().replace("[","").replace("]","").split(",");


                for(int i=0;i< ora.length;i++){
                    if(orario.equals(ora[i])){
                        tassisti.add(disptas.getEmail());
                        break;
                    }
                }
            }catch(Exception e){

            }

        }
        return Tasrepository.findAllInfo(tassisti);

    }

    @GetMapping("/getAllInfoTassista/{email}")
    public List<Object> getAllInfoTassista(@PathVariable("email") String email){
        return Tasrepository.findByEmail(email);
    }


}
