package com.model.relationship.buy.controller;

import com.model.relationship.buy.model.BuyProduct;
import com.model.relationship.buy.repository.BuyRepository;
import com.model.relationship.client.model.Client;
import com.model.relationship.client.repository.ClientRepository;
import com.model.relationship.product.Repository.ProductRepository;
import com.model.relationship.product.model.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/buy")
public class BuyController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BuyRepository buyRepository;


    @Operation(summary = "Buy products", description = "methos for buy products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "created Product"),
            @ApiResponse(responseCode = "404", description = "Error creating product")
    }
    )
    @PostMapping
    public BuyProduct buyProducts(@RequestBody @Valid BuyProduct buy) {
        Optional<Product> productOpt = productRepository.findById(buy.getProduct().getId());
        Optional<Client> clientOpt = clientRepository.findById(buy.getClient().getId());

        if (productOpt.isEmpty()) {
            throw new IllegalArgumentException("Produto não encontrado");
        }

        if (clientOpt.isEmpty()) {
            throw new IllegalArgumentException("Cliente não encontrado");
        }

        Product product = productOpt.get();
        int newQuantity = product.getProductQuantity() - buy.getQuantityPurchased();

        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantidade insuficiente no estoque");
        }

        product.setProductQuantity(newQuantity);
        productRepository.save(product);

        return buyRepository.save(buy);
    }

    @Operation(summary = "Get all buy's", description = "Get all buy's")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "get all buys with success"),
            @ApiResponse(responseCode = "404", description = "error when trying to pick up the product")
    })
    @GetMapping
    public List<BuyProduct> getAllBuys() {
        return buyRepository.findAll();
    }

}
