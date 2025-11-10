package ua.nure.st.kpp.example.demo.dao.daointerface;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ua.nure.st.kpp.example.demo.entity.Customer;

import java.util.List;

@Repository
public interface CustomerDAO {

    void createCustomer(Customer customer);

    void updateCustomer(Customer customer);

    void deleteCustomer(int id);

    Customer getCustomerById(int id);

    List<Customer> getCustomersByLastName(String name);

    List<Customer> getAllCustomers();
}
