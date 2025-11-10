package ua.nure.st.kpp.example.demo.dao.collectiondao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.st.kpp.example.demo.dao.daointerface.CustomerDAO;
import ua.nure.st.kpp.example.demo.entity.Customer;

import java.util.ArrayList;
import java.util.List;

public class CollectionCustomerDAO implements CustomerDAO {

    private static final Logger log = LoggerFactory.getLogger(CollectionCustomerDAO.class);

    private static final List<Customer> customers = new ArrayList<>();
    private static final IdGeneratorService idGenerator = new IdGeneratorService();

    static {

        Customer customer1 = new Customer("John", "Doe", "123456781", "4440001234");
        Customer customer2 = new Customer("Jane", "Smith", "765432132", "5550005678");
        Customer customer3 = new Customer("Mike", "Johnson", "112233431", "5550008765");
        Customer customer4 = new Customer("Emily", "Davis", "443322142", "5550004321");
        Customer customer5 = new Customer("David", "Wilson", "556677852", "5550006789");

        customer1.setId(idGenerator.nextId());
        customer2.setId(idGenerator.nextId());
        customer3.setId(idGenerator.nextId());
        customer4.setId(idGenerator.nextId());
        customer5.setId(idGenerator.nextId());

        customers.add(customer1);
        customers.add(customer2);
        customers.add(customer3);
        customers.add(customer4);
        customers.add(customer5);
    }

    @Override
    public synchronized void createCustomer(Customer customer) {
        customer.setId(idGenerator.nextId());
        customers.add(customer);
        log.debug("Created Customer: {}", customer);
    }

    @Override
    public synchronized void updateCustomer(Customer customer) {
        for (Customer c : customers) {
            if (c.getId() == customer.getId()) {
                c.setFirstName(customer.getFirstName());
                c.setLastName(customer.getLastName());
                c.setPassportNumber(customer.getPassportNumber());
                c.setPhoneNumber(customer.getPhoneNumber());
            }
        }
        log.warn("Customer with id: {} not found for update", customer.getId());
    }

    @Override
    public synchronized void deleteCustomer(int id) {
        customers.removeIf(c -> c.getId() == id);
        log.debug("Customer with id: {} deleted", id);
    }

    @Override
    public synchronized Customer getCustomerById(int id) {
        for (Customer customer : customers) {
            if (customer.getId() == id) {
                return customer;
            }
        }
        log.warn("Customer with id: {} not found", id);
        return null;
    }

    @Override
    public synchronized List<Customer> getCustomersByLastName(String lastName) {
        List<Customer> result = new ArrayList<>();
        for (Customer customer : customers) {
            if (customer.getLastName().equalsIgnoreCase(lastName)) {
                result.add(customer);
            }
        }
        return result;
    }

    @Override
    public synchronized List<Customer> getAllCustomers() {
        log.debug("Found: {} customers", customers.size());
        return new ArrayList<>(customers);
    }

}
