package ua.nure.st.kpp.example.demo.service;

import ua.nure.st.kpp.example.demo.entity.Customer;

import java.util.List;

public interface CustomerService {
    void createCustomer(Customer customer);

    void updateCustomer(Customer customer);

    void deleteCustomer(int id);

    Customer getCustomerById(int id);

    List<Customer> getCustomersByLastName(String name);

    List<Customer> getAllCustomers();
}
