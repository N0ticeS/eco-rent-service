package ua.nure.st.kpp.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.st.kpp.example.demo.dao.DataAccessException;
import ua.nure.st.kpp.example.demo.dao.daointerface.ProductInstanceDAO;
import ua.nure.st.kpp.example.demo.entity.ProductInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class ProductInstanceServiceImpl implements ProductInstanceService {

    private static final Logger log = LoggerFactory.getLogger(ProductInstanceServiceImpl.class);
    private final ProductInstanceDAO dao;

    @Autowired
    public ProductInstanceServiceImpl(ProductInstanceDAO dao) {
        this.dao = dao;
    }

    @Override
    public void createProductInstance(ProductInstance instance) {
        try {
            dao.createProductInstance(instance);
        } catch (Exception e) {
            log.error("Error creating product instance: {}", e.getMessage(), e);
            throw new DataAccessException("Error creating product instance. Please try again", e);
        }
    }

    @Override
    public void updateProductInstance(ProductInstance instance) {
        try {
            dao.updateProductInstance(instance);
        } catch (Exception e) {
            log.error("Error updating product instance: {}", e.getMessage(), e);
            throw new DataAccessException("Error updating product instance. Please try again", e);
        }
    }

    @Override
    public void deleteProductInstance(int id) {
        try {
            dao.deleteProductInstance(id);
        } catch (Exception e) {
            log.error("Error deleting product instance: {}", e.getMessage(), e);
            throw new DataAccessException("Error deleting product instance. Please try again", e);
        }
    }

    @Override
    public ProductInstance getProductInstanceById(int id) {
        try {
            return dao.getProductInstanceById(id);
        } catch (Exception e) {
            log.error("Error getting product instance by id: {}", e.getMessage(), e);
            throw new DataAccessException("Error getting product instance by id. Please try again", e);
        }
    }

    @Override
    public List<ProductInstance> getProductInstanceByName(String name) {
        try {
            return dao.getProductInstanceByName(name);
        } catch (Exception e) {
            log.error("Error getting product instance by name: {}", e.getMessage(), e);
            throw new DataAccessException("Error getting product instance by name. Please try again", e);
        }
    }

    @Override
    public List<ProductInstance> getProductInstanceByProductId(int productId) {
        try {
            return dao.getProductInstanceByProductId(productId);
        } catch (Exception e) {
            log.error("Error getting product instance by productId: {}", e.getMessage(), e);
            throw new DataAccessException("Error getting product instance by productId. Please try again", e);
        }
    }

    @Override
    public List<ProductInstance> getAllProductInstances() {
        return dao.getAllProductInstances();
    }
}
