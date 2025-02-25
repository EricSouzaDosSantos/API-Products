package com.model.relationship.product.controller;

import com.model.relationship.product.Repository.ProductRepository;
import com.model.relationship.product.model.Product;
import com.model.relationship.product.model.ProductRequestDTO;
import com.model.relationship.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Operation(summary = "Get all products", description = "Retrieve a list of all registered products.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of products retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class)))
    })
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> allProducts = productRepository.findAll();
        return ResponseEntity.ok(allProducts);
    }

    @Operation(summary = "Register a new product", description = "Registers a new product in the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product successfully registered",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Product> registerProduct(@RequestParam("name") String name,
                                                   @RequestParam("description") String description,
                                                   @RequestParam("productType") String productType,
                                                   @RequestParam("quantity") int quantity,
                                                   @RequestParam("price") double price,
                                                   @RequestParam(value = "image", required = true) MultipartFile image) {
        try {
            ProductRequestDTO product = new ProductRequestDTO(name, description, productType, quantity, price, image);
            Product newProduct = productService.createProduct(product);
            return ResponseEntity.status(201).body(newProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Operation(summary = "Get product by ID", description = "Retrieve details of a specific product using its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping(path = "/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable long id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update product", description = "Updates the details of an existing product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping(path = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Product> updateProduct(@PathVariable long id,
                                                 @RequestParam("name") String name,
                                                 @RequestParam("description") String description,
                                                 @RequestParam("productType") String productType,
                                                 @RequestParam("quantity") int quantity,
                                                 @RequestParam("price") double price,
                                                 @RequestParam(value = "image", required = true) MultipartFile image) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    ProductRequestDTO product = new ProductRequestDTO(name, description, productType, quantity, price, image);
                    Product updatedProduct = productService.updateProduct(product, existingProduct.getImageUrl(), id);
                    return ResponseEntity.ok(updatedProduct);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete product by ID", description = "Deletes a product by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteProductById(@PathVariable long id) {
        return productRepository.findById(id)
                .map(record -> {
                    productRepository.deleteById(id);
                    productService.deleteFile(record.getImageUrl());
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
