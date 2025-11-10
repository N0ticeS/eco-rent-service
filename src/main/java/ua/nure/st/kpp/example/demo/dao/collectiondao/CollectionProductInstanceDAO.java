package ua.nure.st.kpp.example.demo.dao.collectiondao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.IdGenerator;
import ua.nure.st.kpp.example.demo.dao.daointerface.ProductInstanceDAO;
import ua.nure.st.kpp.example.demo.entity.Product;
import ua.nure.st.kpp.example.demo.entity.ProductInstance;

import java.util.ArrayList;
import java.util.List;

public class CollectionProductInstanceDAO implements ProductInstanceDAO {

    private static final Logger log = LoggerFactory.getLogger(CollectionProductInstanceDAO.class);

    private static final List<ProductInstance> pInstances = new ArrayList<>();
    private static final IdGeneratorService idGenerator = new IdGeneratorService();
    private static final CollectionProductDAO productDAO = new CollectionProductDAO();

    static {

        Product product1 = productDAO.getProductById(1);
        Product product2 = productDAO.getProductById(2);
        Product product3 = productDAO.getProductById(3);

        ProductInstance instance1 = new ProductInstance(product1);
        instance1.setId(idGenerator.nextId());
        instance1.setRentalPrice(calculateRentalPrice(product1));
        pInstances.add(instance1);

        ProductInstance instance2 = new ProductInstance(product2);
        instance2.setId(idGenerator.nextId());
        instance2.setRentalPrice(calculateRentalPrice(product2));
        pInstances.add(instance2);

        ProductInstance instance3 = new ProductInstance(product2);
        instance3.setId(idGenerator.nextId());
        instance3.setRentalPrice(calculateRentalPrice(product2));
        pInstances.add(instance3);

        ProductInstance instance4 = new ProductInstance(product3);
        instance4.setId(idGenerator.nextId());
        instance4.setRentalPrice(calculateRentalPrice(product3));
        pInstances.add(instance4);

        ProductInstance instance5 = new ProductInstance(product3);
        instance5.setId(idGenerator.nextId());
        instance5.setRentalPrice(calculateRentalPrice(product3));
        pInstances.add(instance5);
    }

    private static double calculateRentalPrice(Product product) {
        return product.getPrice() * 0.005;
    }

    @Override
    public synchronized void createProductInstance(ProductInstance productInstance) {
        productInstance.setId(idGenerator.nextId());
        productInstance.setRentalPrice(calculateRentalPrice(productInstance.getProduct()));
        pInstances.add(productInstance);
        log.debug("ProductInstance added: {}", productInstance);
    }

    @Override
    public synchronized void updateProductInstance(ProductInstance productInstance) {
        for (ProductInstance pInstance : pInstances) {
            if (pInstance.getId() == productInstance.getId()) {
                pInstance.setProduct(productInstance.getProduct());
                pInstance.setRentalPrice(calculateRentalPrice(productInstance.getProduct()));
                pInstance.setRentalRate(productInstance.getRentalRate());
                log.debug("ProductInstance updated: {}", productInstance);
                return;
            }
        }
        log.warn("ProductInstance with ID {} not found for update", productInstance.getId());
    }

    @Override
    public synchronized void deleteProductInstance(int id) {
        pInstances.removeIf(productInstance -> productInstance.getId() == id);
        log.debug("ProductInstance with ID {} deleted", id);
    }

    @Override
    public synchronized ProductInstance getProductInstanceById(int id) {
        for (ProductInstance pI : pInstances) {
            if (pI.getId() == id) {
                return pI;
            }
        }
        log.warn("ProductInstance with ID {} not found", id);
        return null;
    }

    @Override
    public synchronized List<ProductInstance> getProductInstanceByName(String name) {
        List<ProductInstance> result = new ArrayList<>();
        for (ProductInstance pI : pInstances) {
            if (pI.getProduct().getProductName().equalsIgnoreCase(name)) {
                result.add(pI);
            }
        }
        return result;
    }

    @Override
    public synchronized List<ProductInstance> getProductInstanceByProductId(int productId) {
        List<ProductInstance> result = new ArrayList<>();
        for (ProductInstance pI : pInstances) {
            if (pI.getProduct().getId() == productId) {
                result.add(pI);
            }
        }
        return result;
    }

    @Override
    public synchronized List<ProductInstance> getAllProductInstances() {
        log.debug("Found: {} ProductInstances", pInstances.size());
        return new ArrayList<>(pInstances);
    }

}
