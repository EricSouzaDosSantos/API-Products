package com.model.relationship.client.model;

import com.model.relationship.buy.model.BuyProduct;
import com.model.relationship.buy.model.BuyProductDTO;

import java.util.List;

public record ClientDTO(String name, String email, String address, List<BuyProductDTO> buyProducts) {
}
