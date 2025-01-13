package com.model.relationship.product.controller;

import com.model.relationship.product.Repository.ProductRepository;
import com.model.relationship.product.model.Product;
import com.model.relationship.product.model.ProductRequestDTO;
import com.model.relationship.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity getAllProducts() {
        var allProducts = productRepository.findAll();
        return ResponseEntity.ok(allProducts);
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Product> registerProduct(@RequestParam("name") String name,
                                                   @RequestParam("description") String description,
                                                   @RequestParam("productType") String productType,
                                                   @RequestParam("quantity") int quantity,
                                                   @RequestParam("price") double price,
                                                   @RequestParam(value = "image", required = true) MultipartFile image){
        try {
            ProductRequestDTO product = new ProductRequestDTO(name, description, productType, quantity, price, image);
            Product newProduct = this.productService.createProduct(product);
            return ResponseEntity.ok(newProduct);
        }catch (Exception e){
            throw new IllegalArgumentException("error inserting user");
        }
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity GetProductById(@PathVariable long id) {
        return productRepository.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(path = {"/{id}"}, consumes = "multipart/form-data")
    public ResponseEntity UpdateProduct(@PathVariable long id,
                                        @RequestParam("name") String name,
                                        @RequestParam("description") String description,
                                        @RequestParam("productType") String productType,
                                        @RequestParam("quantity") int quantity,
                                        @RequestParam("price") double price,
                                        @RequestParam(value = "image", required = true) MultipartFile image) {

        if(id >= 0) {
            Product productFound = productRepository.findById(id).get();

            // BeanUtils.copyProperties(product, productFound, "id");
        try {

            ProductRequestDTO product = new ProductRequestDTO(name, description, productType, quantity, price, image);
//            Product newProduct = this.productService.UpdateProduct(product);
            return ResponseEntity.ok(productService.updateProduct(product, productFound.getImageUrl(), id));
        }catch (Exception e){
            throw new IllegalArgumentException("error inserting user");
        }

        }else{
            throw new IllegalArgumentException("User not Exists");
        }
    }

    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity DeletProductById(@PathVariable long id) {
        return productRepository.findById(id)
                .map(record -> {
                    productRepository.deleteById(id);
                    productService.deleteFile(record.getImageUrl());
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());    }
}
