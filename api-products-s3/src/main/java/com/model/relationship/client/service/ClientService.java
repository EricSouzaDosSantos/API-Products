package com.model.relationship.client.service;

import com.model.relationship.buy.model.BuyProduct;
import com.model.relationship.buy.model.BuyProductDTO;
import com.model.relationship.buy.repository.BuyRepository;
import com.model.relationship.client.model.Client;
import com.model.relationship.client.model.ClientDTO;
import com.model.relationship.client.repository.ClientRepository;
import com.model.relationship.product.Repository.ProductRepository;
import com.model.relationship.product.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private BuyRepository buyRepository;

    @Autowired
    private ProductRepository productRepository;

    public void createClient(ClientDTO clientDTO) {
        Client client = new Client();
        client.setName(clientDTO.name());
        client.setAddress(clientDTO.address());
        client.setEmail(clientDTO.email());

        Client savedClient = clientRepository.save(client);

        if (clientDTO.buyProducts() != null) {
            buyProduct(clientDTO, savedClient);
        }
    }

    private void buyProduct(ClientDTO clientDTO, Client savedClient) {
        for (BuyProductDTO buyProductDTO : clientDTO.buyProducts()) {
            Optional<Product> productOpt = productRepository.findById(buyProductDTO.product().getId());

            if (productOpt.isPresent()) {
                Product product = productOpt.get();

                if (product.getProductQuantity() >= buyProductDTO.quantity()) {
                    BuyProduct buyProduct = new BuyProduct();
                    buyProduct.setClient(savedClient);
                    buyProduct.setProduct(product);
                    buyProduct.setQuantityPurchased(buyProductDTO.quantity());

                    product.setProductQuantity(product.getProductQuantity() - buyProductDTO.quantity());
                    productRepository.save(product);

                    buyRepository.save(buyProduct);
                } else {
                    throw new IllegalArgumentException("Quantidade insuficiente para o produto: " + product.getId());
                }
            } else {
                throw new IllegalArgumentException("Produto não encontrado: " + buyProductDTO.product().getId());
            }
        }
    }
}
