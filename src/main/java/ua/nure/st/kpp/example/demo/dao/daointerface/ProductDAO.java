package ua.nure.st.kpp.example.demo.dao.daointerface;

import org.springframework.stereotype.Repository;
import ua.nure.st.kpp.example.demo.entity.Product;

import java.sql.SQLException;
import java.util.List;

@Repository
public interface ProductDAO {
    void createProduct(Product product);

    void updateProduct(Product product);

    void deleteProduct(int id);

    Product getProductById(int id);

    List<Product> getProductsByName(String name);

    List<Product> getAllProducts();
}