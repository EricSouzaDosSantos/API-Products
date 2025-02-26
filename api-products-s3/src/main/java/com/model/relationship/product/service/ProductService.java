package com.model.relationship.product.service;

import com.amazonaws.services.s3.*;
import com.amazonaws.services.s3.model.*;
import com.model.relationship.product.Repository.ProductRepository;
import com.model.relationship.product.model.Product;
import com.model.relationship.product.model.ProductRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private AmazonS3 s3;

    @Autowired
    private ProductRepository productRepository;

    @Value("${aws.bucket}")
    private String bucketName;


    public Product createProduct(ProductRequestDTO data){
        String imgUrl = "";

        if(data.imgFile() != null){
            imgUrl = this.uploadImage(data.imgFile());
        }

            Product product = new Product();
            product.setProductName(data.name());
            product.setProductType(data.productType());
            product.setProductPrice(data.price());
            product.setProductQuantity(data.quantity());
            product.setProductDescription(data.description());
            product.setImageUrl(imgUrl);

            productRepository.save(product);

            return product;
    }

    public Optional<Product> getProductById(long id){
        return productRepository.findById(id);
    }

    public Product updateProduct(ProductRequestDTO data, String imgName, long id){
        String imgUrl = "";

        if(data.imgFile() != null){
            imgUrl = this.changeImage(imgName, data.imgFile());
        }

        Product product = productRepository.findById(id).get();

        product.setProductName(data.name());
        product.setProductType(data.productType());
        product.setProductPrice(data.price());
        product.setProductQuantity(data.quantity());
        product.setProductDescription(data.description());
        product.setImageUrl(imgUrl);

        productRepository.save(product);

        return product;
    }

    private String changeImage(String imageURL, MultipartFile multipartFile) {
        try {
            // Remove a parte da URL e obtém a chave do objeto
            String objectKey = imageURL.replace("https://relacoes-imagens.s3.us-east-2.amazonaws.com/", "");

            // Converte o MultipartFile para um File
            File file = this.convertMultiPartFile(multipartFile);

            s3.putObject(new PutObjectRequest(bucketName, objectKey, file));

            file.delete();

            return s3.getUrl(bucketName, objectKey).toString();
        } catch (Exception e) {
            System.out.println("Erro ao atualizar objeto: " + e.getMessage());
            return "";
        }
    }


//    private String changeImage(String imageURL, MultipartFile multipartFile){
//        try {
//            String objectKey = imageURL.replace("https://relacoes-imagens.s3.us-east-2.amazonaws.com/", "");
//            System.out.println(objectKey);
//            File file = this.convertMultiPartFile(multipartFile);
//            s3.putObject(bucketName, objectKey, file);
//            file.delete();
//            return s3.getUrl(bucketName, imageURL).toString();
//        }catch (Exception e){
//            System.out.println("Erro ao atualizar objeto");
//            return  "";
//        }
//    }


    private String uploadImage(MultipartFile multipartFile){
        String fileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
        try {
            File file = this.convertMultiPartFile(multipartFile);
            s3.putObject(bucketName, fileName, file);
            file.delete();
            return s3.getUrl(bucketName, fileName).toString();
        }catch (Exception e){
            System.out.println("error uploading image to s3");
            return "";
        }
    }

    private File convertMultiPartFile(MultipartFile multipartFile) throws IOException {
         File convertFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream photo = new FileOutputStream(convertFile);
        photo.write(multipartFile.getBytes());
        photo.close();
        return convertFile;
    }

    public void deleteFile(String fileUrl) {
        String fileKey = fileUrl.replace("https://relacoes-imagens.s3.us-east-2.amazonaws.com/", "");
        System.out.println(fileUrl);
        System.out.println(fileKey);
        s3.deleteObject(bucketName, fileKey);
    }

}
