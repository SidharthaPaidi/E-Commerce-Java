package com.sidhu.ecomApp.service;

import com.sidhu.ecomApp.model.Product;
import com.sidhu.ecomApp.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepo productRepo;

    public List<Product> getProducts() {
        return productRepo.findAll();
    }

    public Product getProductById(int productId) {
        return productRepo.findById(productId).get();
    }

    public Product addOrUpdateProduct(Product product, MultipartFile image) throws IOException {
        product.setImageName(image.getOriginalFilename());
        product.setImageType(image.getContentType());
        product.setImageData(image.getBytes());

        productRepo.save(product);

        return product;
    }

    public void deleteProduct(Integer productId) {
        productRepo.deleteById(productId);
    }

    public List<Product> searchProducts(String keyword) {
        return  productRepo.searchProducts(keyword);
    }
}
