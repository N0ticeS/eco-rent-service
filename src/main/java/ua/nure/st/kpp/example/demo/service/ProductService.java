package ua.nure.st.kpp.example.demo.service;

import ua.nure.st.kpp.example.demo.entity.Product;

import java.util.List;

public interface ProductService {
    void createProduct(Product product);

    void updateProduct(Product product);

    void deleteProduct(int id);

    Product getProductById(int id);

    List<Product> getProductsByName(String name);

    List<Product> getAllProducts();
}
