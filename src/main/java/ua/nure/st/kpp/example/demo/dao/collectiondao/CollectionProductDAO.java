package ua.nure.st.kpp.example.demo.dao.collectiondao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.IdGenerator;
import ua.nure.st.kpp.example.demo.dao.daointerface.ProductDAO;
import ua.nure.st.kpp.example.demo.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class CollectionProductDAO implements ProductDAO {

    private static final Logger log = LoggerFactory.getLogger(CollectionProductDAO.class);

    private static final List<Product> products = new ArrayList<>();
    private static final IdGeneratorService idGenerator = new IdGeneratorService();

    static {
        Product product1 = new Product("Bicycle", "Mountain bicycle", 2000);
        Product product2 = new Product("Electric Scooter", "Eco-friendly electric scooter", 1800);
        Product product3 = new Product("Skateboard", "Classic wooden skateboard", 1400);

        product1.setId(idGenerator.nextId());
        product2.setId(idGenerator.nextId());
        product3.setId(idGenerator.nextId());

        products.add(product1);
        products.add(product2);
        products.add(product3);
    }

    @Override
    public synchronized void createProduct(Product product) {
        product.setId(idGenerator.nextId());
        products.add(product);
        log.debug("Product added: {}", product);
    }

    public synchronized void updateProduct(Product product) {
        for (Product p : products) {
            if (p.getId() == product.getId()) {
                p.setProductName(product.getProductName());
                p.setDescription(product.getDescription());
                p.setPrice(product.getPrice());
                log.debug("Product updated: {}", p);
                return;
            }
        }
        log.warn("Product with ID {} not found for update", product.getId());
    }

    public synchronized void deleteProduct(int id) {
        products.removeIf(p -> p.getId() == id);
        log.debug("Product deleted: {}", id);
    }

    @Override
    public synchronized Product getProductById(int id) {
        for (Product p : products) {
            if (p.getId() == id) {
                return p;
            }
        }
        log.warn("Product with ID {} not found", id);
        return null;
    }

    @Override
    public synchronized List<Product> getProductsByName(String name) {
        List<Product> result = new ArrayList<>();
        for (Product p : products) {
            if (p.getProductName().equalsIgnoreCase(name)) {
                result.add(p);
            }
        }
        return result;
    }


    @Override
    public synchronized List<Product> getAllProducts() {
        log.debug("Found {} products", products.size());
        return new ArrayList<>(products);
    }
}



