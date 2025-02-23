package com.model.relationship.client.controller;


import com.model.relationship.client.model.Client;
import com.model.relationship.client.model.ClientDTO;
import com.model.relationship.client.repository.ClientRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
     private ClientRepository clientRepository;

    @GetMapping
    public ResponseEntity getAllClients(){
        var allClients = clientRepository.findAll();
        return ResponseEntity.ok(allClients);
    }

    @Operation(summary = "Register a new client", description = "Register a new client in data base.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Client registered", content = @Content(mediaType = "application/json"))})

    @PostMapping
    public ResponseEntity registerClient(@RequestBody @Valid ClientDTO client){
        Client newClient = new Client();
        newClient.setName(client.name());
        newClient.setEmail(client.email());
        newClient.setAddress(client.address());

        clientRepository.save(newClient);
        System.out.print("user inserted");
        return ResponseEntity.status(200).body("registered users");
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity GetClientById(@PathVariable long id){
        return clientRepository.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());

    }

}
