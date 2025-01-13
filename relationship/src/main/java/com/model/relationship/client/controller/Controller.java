package com.model.relationship.client.controller;


import com.model.relationship.client.model.Client;
import com.model.relationship.client.repository.ClientRepository;
import com.model.relationship.client.service.WhatsapService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class Controller {



    @Autowired
     private ClientRepository clientRepository;

    @GetMapping
    public ResponseEntity getAllClients(){
        var allClients = clientRepository.findAll();
        return ResponseEntity.ok(allClients);
    }

    @PostMapping
    public ResponseEntity registerClient(@RequestBody @Valid Client client){
        clientRepository.save(client);
        System.out.print("user inserted");
        return ResponseEntity.ok("registered users");
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity GetClientById(@PathVariable long id){
        return clientRepository.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());

    }

}
