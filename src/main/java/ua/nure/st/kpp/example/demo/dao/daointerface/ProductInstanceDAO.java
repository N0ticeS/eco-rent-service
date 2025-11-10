package ua.nure.st.kpp.example.demo.dao.daointerface;

import org.springframework.stereotype.Repository;
import ua.nure.st.kpp.example.demo.entity.ProductInstance;

import java.sql.SQLException;
import java.util.List;

@Repository
public interface ProductInstanceDAO {

    void createProductInstance(ProductInstance productInstance);

    void updateProductInstance(ProductInstance productInstance);

    void deleteProductInstance(int id);

    ProductInstance getProductInstanceById(int id);

    List<ProductInstance> getProductInstanceByName(String name);

    List<ProductInstance> getProductInstanceByProductId(int productId);

    List<ProductInstance> getAllProductInstances();
}
