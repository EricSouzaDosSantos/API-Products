package com.model.relationship.client.controller;


import com.model.relationship.client.model.Client;
import com.model.relationship.client.model.ClientDTO;
import com.model.relationship.client.repository.ClientRepository;
import com.model.relationship.client.service.ClientService;
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

    @Autowired
    private ClientService clientService;

    @GetMapping
    public ResponseEntity getAllClients(){
        var allClients = clientRepository.findAll();
        return ResponseEntity.ok(allClients);
    }

    @Operation(summary = "Register a new client", description = "Register a new client in data base.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Client registered", content = @Content(mediaType = "application/json"))})

    @PostMapping
    public ResponseEntity registerClient(@RequestBody @Valid ClientDTO client){
        try {
            clientService.createClient(client);
            return ResponseEntity.status(200).body("registered users");
        }catch (Exception e){
            throw new IllegalArgumentException("error registering user");
        }
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<Client> GetClientById(@PathVariable long id){
        return clientRepository.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());

    }

}
