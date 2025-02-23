package com.model.relationship.buy.model;

import com.model.relationship.client.model.Client;
import com.model.relationship.product.model.Product;

public record BuyProductDTO(Client client, Product product, int quantity) {
}
