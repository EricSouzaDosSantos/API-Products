package com.model.relationship.client.service;

import com.model.relationship.buy.model.BuyProduct;
import com.model.relationship.buy.model.BuyProductDTO;
import com.model.relationship.buy.repository.BuyRepository;
import com.model.relationship.client.model.Client;
import com.model.relationship.client.model.ClientDTO;
import com.model.relationship.client.repository.ClientRepository;
import com.model.relationship.product.Repository.ProductRepository;
import com.model.relationship.product.model.Product;
import com.model.relationship.product.model.ProductRequestDTO;
import com.model.relationship.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private BuyRepository buyRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;

    public void createClient(ClientDTO clientDTO) {
        Client client = new Client();
        client.setName(clientDTO.name());
        client.setAddress(clientDTO.address());
        client.setEmail(clientDTO.email());


        Client savedClient = clientRepository.save(client);
        if (clientDTO.products() != null) {
            buyProduct(clientDTO, savedClient);
        }
    }

    public void updateClient(long id, ClientDTO clientDTO){
        Client client = clientRepository.findById(id).get();
        client.setName(clientDTO.name());
        client.setEmail(clientDTO.email());
        client.setAddress(clientDTO.address());
        if (clientDTO.products() != null) {
            buyProduct(clientDTO, client);
        }
    }

    public void deleteClient(long id){
        clientRepository.deleteById(id);
    }

    public ResponseEntity<List<Client>> getAllClients(){
        return ResponseEntity.ok(clientRepository.findAll());
    }

    public ResponseEntity<Client> getClientById(long id){
        return clientRepository.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    private void buyProduct(ClientDTO clientDTO, Client savedClient) {
        for (Product product : clientDTO.products()) {
            Optional<BuyProduct> buyProduct = buyRepository.findById(product.getId());

//            if (productOpt.isPresent()) {
//                Product productFound = productOpt.get();
//
//                if (product.getProductQuantity() >= productFound.getProductQuantity()) {
//                    BuyProduct buyProduct = new BuyProduct();
//                    buyProduct.setClient(savedClient);
//                    buyProduct.setProduct(product);
//                    buyProduct.setQuantityPurchased(product1.quantity());
//
//                    product.setProductQuantity(product.getProductQuantity() - product1.quantity());
//                    productRepository.save(product);
//
//                    buyRepository.save(buyProduct);
//                } else {
//                    throw new IllegalArgumentException("Quantidade insuficiente para o produto: " + product.getId());
//                }
//            } else {
//                throw new IllegalArgumentException("Produto não encontrado: " + product1.product().getId());
//            }
        }
    }
}
