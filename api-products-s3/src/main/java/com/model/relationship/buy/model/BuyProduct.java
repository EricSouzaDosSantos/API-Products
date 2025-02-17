package com.model.relationship.buy.model;

import com.model.relationship.client.model.Client;
import com.model.relationship.product.model.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class BuyProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Product product;

    private int quantityPurchased;

}
