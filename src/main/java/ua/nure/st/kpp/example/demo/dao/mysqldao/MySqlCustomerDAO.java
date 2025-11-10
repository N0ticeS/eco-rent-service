package ua.nure.st.kpp.example.demo.dao.mysqldao;

import ua.nure.st.kpp.example.demo.dao.ConnectionManager;
import ua.nure.st.kpp.example.demo.dao.DataAccessException;
import ua.nure.st.kpp.example.demo.dao.DataNotFoundException;
import ua.nure.st.kpp.example.demo.dao.daointerface.CustomerDAO;
import ua.nure.st.kpp.example.demo.entity.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlCustomerDAO implements CustomerDAO {

    private final ConnectionManager cm;

    public MySqlCustomerDAO(ConnectionManager cm) {
        this.cm = cm;
    }

    private static final String BASE_SELECT_QUERY = """
            SELECT customer_id, first_name, last_name, passport_number, phone_number, rental_status
            FROM customers
            """;

    @Override
    public void createCustomer(Customer customer) {
        String query = "INSERT INTO customers (first_name, last_name, passport_number, phone_number, rental_status) VALUES (?, ?, ?, ?, ?)";
        Connection con = null;
        try {
            con = cm.getConnection(false);
            try (PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                int k = 0;
                stmt.setString(++k, customer.getFirstName());
                stmt.setString(++k, customer.getLastName());
                stmt.setString(++k, customer.getPassportNumber());
                stmt.setString(++k, customer.getPhoneNumber());
                stmt.setInt(++k, customer.getRentalStatus());
                stmt.executeUpdate();

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        customer.setId(generatedKeys.getInt(1));
                    } else {
                        throw new DataAccessException("Creating customer failed, no ID obtained.");
                    }
                }
                con.commit();
            } catch (SQLException e) {
                SqlUtil.rollback(con);
                throw new DataAccessException(e);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        } finally {
            SqlUtil.close(con);
        }
    }

    @Override
    public void updateCustomer(Customer customer) {
        String query = "UPDATE customers SET first_name = ?, last_name = ?, passport_number = ?, phone_number = ? WHERE customer_id = ?";
        Connection con = null;
        try {
            con = cm.getConnection(false);
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                int k = 0;
                stmt.setString(++k, customer.getFirstName());
                stmt.setString(++k, customer.getLastName());
                stmt.setString(++k, customer.getPassportNumber());
                stmt.setString(++k, customer.getPhoneNumber());
                stmt.setInt(++k, customer.getId());
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new DataNotFoundException("Customer with ID " + customer.getId() + " not found for update.");
                }
                con.commit();
            } catch (SQLException e) {
                SqlUtil.rollback(con);
                throw new DataAccessException("Error updating customer", e);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        } finally {
            SqlUtil.close(con);
        }
    }

    @Override
    public void deleteCustomer(int id) {
        String query = "DELETE FROM customers WHERE customer_id = ?";
        Connection con = null;
        try {
            con = cm.getConnection(false);
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setInt(1, id);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new DataNotFoundException("Customer with ID " + id + " not found for deletion.");
                }
                con.commit();
            } catch (SQLException e) {
                SqlUtil.rollback(con);
                throw new DataAccessException("Error deleting customer", e);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting customer", e);
        } finally {
            SqlUtil.close(con);
        }
    }


    @Override
    public Customer getCustomerById(int id) {
        String query = BASE_SELECT_QUERY + " WHERE customer_id = ?";
        try (Connection connection = cm.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToCustomer(rs);
                } else {
                    throw new DataNotFoundException("Customer with ID " + id + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error fetching customer by ID", e);
        }
    }

    @Override
    public List<Customer> getCustomersByLastName(String name) {
        String query = BASE_SELECT_QUERY + " WHERE last_name LIKE ?";
        List<Customer> customers = new ArrayList<>();
        Connection con = null;
        try {
            con = cm.getConnection(false);
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, "%" + name + "%");
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        customers.add(mapRowToCustomer(rs));
                    }
                }
            } catch (SQLException e) {
                SqlUtil.rollback(con);
                throw new DataAccessException("Error fetching customers by name", e);
            }
            con.commit();
        } catch (SQLException e) {
            throw new DataAccessException("Error fetching customers by name", e);
        } finally {
            SqlUtil.close(con);
        }
        return customers;
    }
    
    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        Connection con = null;
        try {
            con = cm.getConnection(false);
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(BASE_SELECT_QUERY)) {
                while (rs.next()) {
                    customers.add(mapRowToCustomer(rs));
                }
            } catch (SQLException e) {
                SqlUtil.rollback(con);
                throw new DataAccessException("Error fetching all customers", e);
            }
            con.commit();
        } catch (SQLException e) {
            throw new DataAccessException("Error fetching all customers", e);
        } finally {
            SqlUtil.close(con);
        }
        return customers;
    }


    Customer mapRowToCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getInt("customer_id"));
        customer.setFirstName(rs.getString("first_name"));
        customer.setLastName(rs.getString("last_name"));
        customer.setPassportNumber(rs.getString("passport_number"));
        customer.setPhoneNumber(rs.getString("phone_number"));
        customer.setRentalStatus(rs.getInt("rental_status"));
        return customer;
    }
}
