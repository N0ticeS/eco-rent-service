package ua.nure.st.kpp.example.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.st.kpp.example.demo.dao.DataAccessException;
import ua.nure.st.kpp.example.demo.dao.daointerface.CustomerDAO;
import ua.nure.st.kpp.example.demo.entity.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);
    private final CustomerDAO dao;

    @Autowired
    public CustomerServiceImpl(CustomerDAO dao) {
        this.dao = dao;
    }

    @Override
    public void createCustomer(Customer customer) {
        try {
            dao.createCustomer(customer);
        } catch (Exception e) {
            log.error("Error creating customer: {} ", e.getMessage(), e);
            throw new DataAccessException("Unable to create customer. Please try again", e);
        }
    }

    @Override
    public void updateCustomer(Customer customer) {
        try {
            dao.updateCustomer(customer);
        } catch (Exception e) {
            log.error("Error updating customer: {} ", e.getMessage(), e);
            throw new DataAccessException("Unable to update customer. Please try again", e);
        }
    }

    @Override
    public void deleteCustomer(int id) {
        try {
            dao.deleteCustomer(id);
        } catch (Exception e) {
            log.error("Error deleting customer: {} ", e.getMessage(), e);
            throw new DataAccessException("Unable to delete customer. Please try again", e);
        }
    }

    @Override
    public Customer getCustomerById(int id) {
        try {
            return dao.getCustomerById(id);
        } catch (Exception e) {
            log.error("Error getting customer by Id: {} ", e.getMessage(), e);
            throw new DataAccessException("Unable to get customer by id. Please try again", e);
        }
    }

    @Override
    public List<Customer> getCustomersByLastName(String name) {
        try {
            return dao.getCustomersByLastName(name);
        } catch (Exception e) {
            log.error("Error getting customers by last name: {} ", e.getMessage(), e);
            throw new DataAccessException("Unable to get customers by last name. Please try again", e);
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        return dao.getAllCustomers();
    }
}

