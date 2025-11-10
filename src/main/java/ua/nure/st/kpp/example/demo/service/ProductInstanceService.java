package ua.nure.st.kpp.example.demo.service;

import ua.nure.st.kpp.example.demo.entity.ProductInstance;

import java.util.List;

public interface ProductInstanceService {
    void createProductInstance(ProductInstance instance);

    void updateProductInstance(ProductInstance instance);

    void deleteProductInstance(int id);

    ProductInstance getProductInstanceById(int id);

    List<ProductInstance> getProductInstanceByName(String name);

    List<ProductInstance> getProductInstanceByProductId(int productId);

    List<ProductInstance> getAllProductInstances();
}
