package com.model.relationship.buy.controller;

import com.model.relationship.buy.model.BuyProduct;
import com.model.relationship.buy.repository.BuyRepository;
import com.model.relationship.client.repository.ClientRepository;
import com.model.relationship.product.Repository.ProductRepository;
import com.model.relationship.product.model.Product;
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

    @PostMapping
    public BuyProduct BuyProducts(@RequestBody @Valid BuyProduct buy) {
        Optional<Product> productOpt = productRepository.findById(buy.getProduct().getId());

        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            int newQuantity = product.getProductQuantity() - buy.getQuantityPurchased();

            if (newQuantity >= 0) {
                product.setProductQuantity(Math.abs(newQuantity));
                productRepository.save(product);
                return buyRepository.save(buy);
            } else {
                throw new IllegalArgumentException("Quantidade insuficiente no estoque");
            }
        } else {
            throw new IllegalArgumentException("Produto não encontrado");
        }
    }

    @GetMapping
    public List<BuyProduct> getAllCompras() {
        return buyRepository.findAll();
    }

}
