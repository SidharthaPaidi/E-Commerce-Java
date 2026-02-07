package com.sidhu.ecomApp.controller;

import com.sidhu.ecomApp.model.Product;
import com.sidhu.ecomApp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProductController {
    @Autowired
    ProductService productServie;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts() {

        return new ResponseEntity<>(productServie.getProducts(),HttpStatus.ACCEPTED);
    }

    @GetMapping("/product/{productId}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Integer productId) {
        Product product = productServie.getProductById(productId);
        return new ResponseEntity<>(product.getImageData(),HttpStatus.OK);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable int productId) {
        Product product = productServie.getProductById(productId);
        if(product != null)
            return new ResponseEntity<>(product,HttpStatus.OK);
        else
            return new ResponseEntity<>(product,HttpStatus.NOT_FOUND);
    }

    @PutMapping("/product/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer productId,@RequestPart Product product, @RequestPart MultipartFile imageFile) {
        Product updatedProduct = null;
        try{
            updatedProduct = productServie.addOrUpdateProduct(product,imageFile);
            return new ResponseEntity<>("updated the product successfully",HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile) {
        Product savedProduct = null;
        try {
            savedProduct = productServie.addOrUpdateProduct(product,imageFile);
            return new ResponseEntity<>(savedProduct,HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("product/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId) {
        Product product = productServie.getProductById(productId);
        if(product != null) {
            productServie.deleteProduct(productId);
            return new ResponseEntity<>("Deleted product successfully",HttpStatus.OK);
        }else
            return new ResponseEntity<>("Invalid Request",HttpStatus.NOT_FOUND);

    }

    @GetMapping("products/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam String keyword) {
        List<Product> products = productServie.searchProducts(keyword);
//        System.out.println("searching with :" + keyword);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }

}
