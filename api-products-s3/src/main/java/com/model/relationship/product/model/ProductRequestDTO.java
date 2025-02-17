package com.model.relationship.product.model;

import org.springframework.web.multipart.MultipartFile;

public record ProductRequestDTO(String name, String description, String productType, int quantity, double price, MultipartFile imgFile) {
}
