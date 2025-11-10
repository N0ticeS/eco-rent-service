package ua.nure.st.kpp.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.st.kpp.example.demo.dao.DataAccessException;
import ua.nure.st.kpp.example.demo.dao.daointerface.ProductDAO;
import ua.nure.st.kpp.example.demo.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductDAO dao;

    @Autowired
    public ProductServiceImpl(ProductDAO dao) {
        this.dao = dao;
    }

    @Override
    public void createProduct(Product product) {
        try {
            dao.createProduct(product);
        } catch (Exception e) {
            log.error("Error creating product: + {} ", e.getMessage(), e);
            throw new DataAccessException("Unable to create product. Please try again.");
        }
    }

    @Override
    public void updateProduct(Product product) {
        try {
            dao.updateProduct(product);
        } catch (Exception e) {
            log.error("Error updating product: + {} ", e.getMessage(), e);
            throw new DataAccessException("Unable to update product. Please try again.");
        }
    }

    @Override
    public void deleteProduct(int id) {
        try {
            dao.deleteProduct(id);
        } catch (Exception e) {
            log.error("Error deleting product: + {} ", e.getMessage(), e);
            throw new DataAccessException("Unable to delete product. Please try again.");
        }
    }

    @Override
    public Product getProductById(int id) {
        try {
            return dao.getProductById(id);
        } catch (Exception e) {
            log.error("Error getting product by id: + {} ", e.getMessage(), e);
            throw new DataAccessException("Unable to get product. Please try again.");
        }
    }

    @Override
    public List<Product> getProductsByName(String name) {
        try {
            return dao.getProductsByName(name);
        } catch (Exception e) {
            log.error("Error getting products by name: + {} ", e.getMessage(), e);
            throw new DataAccessException("Unable to get products by name. Please try again.");
        }
    }

    @Override
    public List<Product> getAllProducts() {
        try {
            return dao.getAllProducts();
        } catch (Exception e) {
            log.error("Error getting all products. Please try again.");
            throw new DataAccessException("Unable to get all products. Please try again.");
        }
    }
}
