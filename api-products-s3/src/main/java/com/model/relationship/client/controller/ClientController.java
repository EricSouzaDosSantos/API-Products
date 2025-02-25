package com.model.relationship.client.controller;

import com.model.relationship.client.model.Client;
import com.model.relationship.client.model.ClientDTO;
import com.model.relationship.client.repository.ClientRepository;
import com.model.relationship.client.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientService clientService;

    @Operation(summary = "Get all clients", description = "Retrieve a list of all registered clients.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of clients retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Client.class)))
    })
    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> allClients = clientRepository.findAll();
        return ResponseEntity.ok(allClients);
    }

    @Operation(summary = "Register a new client", description = "Registers a new client in the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client successfully registered",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<String> registerClient(@RequestBody @Valid ClientDTO client) {
        try {
            clientService.createClient(client);
            return ResponseEntity.status(201).body("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error registering user: " + e.getMessage());
        }
    }


    @Operation(summary = "Get client by ID", description = "Retrieve details of a specific client using their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Client.class))),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @GetMapping(path = "/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable long id) {
        return clientRepository.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }
}
