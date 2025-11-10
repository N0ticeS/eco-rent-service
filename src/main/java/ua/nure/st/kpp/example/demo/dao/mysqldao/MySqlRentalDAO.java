package ua.nure.st.kpp.example.demo.dao.mysqldao;

import ua.nure.st.kpp.example.demo.dao.ConnectionManager;
import ua.nure.st.kpp.example.demo.dao.DataAccessException;
import ua.nure.st.kpp.example.demo.dao.DataNotFoundException;
import ua.nure.st.kpp.example.demo.dao.daointerface.RentalDAO;
import ua.nure.st.kpp.example.demo.entity.Customer;
import ua.nure.st.kpp.example.demo.entity.ProductInstance;
import ua.nure.st.kpp.example.demo.entity.Rental;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlRentalDAO implements RentalDAO {

    private final ConnectionManager cm;

    public MySqlRentalDAO(ConnectionManager cm) {
        this.cm = cm;
    }

    private static final String BASE_SELECT_QUERY = """
            SELECT 
            r.rental_id, r.rental_date, r.return_date, r.rental_price,
            pi.instance_id, pi.rental_price AS instance_rental_price, pi.rental_rate,
            c.customer_id, c.first_name, c.last_name, c.passport_number, c.phone_number, c.rental_status,
            p.product_id, p.product_name, p.description, p.price
            FROM rentals r
            JOIN product_instances pi ON r.instance_id = pi.instance_id
            JOIN customers c ON r.customer_id = c.customer_id
            JOIN products p ON pi.product_id = p.product_id
            """;

    @Override
    public void createRental(Rental rental) {
        String query = "INSERT INTO rentals (instance_id, customer_id, rental_date, return_date, rental_price) VALUES (?, ?, ?, ?, ?)";
        Connection con = null;
        try {
            con = cm.getConnection(false);
            try (PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                int k = 0;
                stmt.setInt(++k, rental.getProductInstance().getId());
                stmt.setInt(++k, rental.getCustomer().getId());
                stmt.setDate(++k, rental.getRentalDate());
                stmt.setDate(++k, rental.getReturnDate());
                stmt.setDouble(++k, rental.getRentalPrice());
                stmt.executeUpdate();

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        rental.setId(generatedKeys.getInt(1));
                    } else {
                        throw new DataAccessException("Creating rental failed, no ID obtained.");
                    }
                }
                con.commit();
            } catch (SQLException e) {
                SqlUtil.rollback(con);
                throw new DataAccessException("Error creating rental", e);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error creating rental", e);
        } finally {
            SqlUtil.close(con);
        }
    }


    @Override
    public void updateRental(Rental rental) {
        String query = "UPDATE rentals SET instance_id = ?, customer_id = ?, rental_date = ?, return_date = ?, rental_price = ? WHERE rental_id = ?";
        Connection con = null;
        try {
            con = cm.getConnection(false);
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                int k = 0;
                stmt.setInt(++k, rental.getProductInstance().getId());
                stmt.setInt(++k, rental.getCustomer().getId());
                stmt.setDate(++k, rental.getRentalDate());
                stmt.setDate(++k, rental.getReturnDate());
                stmt.setDouble(++k, rental.getRentalPrice());
                stmt.setInt(++k, rental.getId());
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new DataNotFoundException("Rental with ID " + rental.getId() + " not found for update.");
                }
                con.commit();
            } catch (SQLException e) {
                SqlUtil.rollback(con);
                throw new DataAccessException("Error updating rental", e);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error updating rental", e);
        } finally {
            SqlUtil.close(con);
        }
    }

    @Override
    public void deleteRental(int id) {
        String query = "DELETE FROM rentals WHERE rental_id = ?";
        Connection con = null;
        try {
            con = cm.getConnection(false);
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setInt(1, id);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new DataNotFoundException("Rental with ID " + id + " not found for deletion.");
                }
                con.commit();
            } catch (SQLException e) {
                SqlUtil.rollback(con);
                throw new DataAccessException("Error deleting rental", e);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting rental", e);
        } finally {
            SqlUtil.close(con);
        }
    }


    @Override
    public Rental getRentalById(int id) {
        String query = BASE_SELECT_QUERY + " WHERE r.rental_id = ?";
        Connection con = null;
        try {
            con = cm.getConnection(false);
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Rental rental = mapRowToRental(rs);
                        con.commit();
                        return rental;
                    } else {
                        SqlUtil.rollback(con);
                        throw new DataNotFoundException("Rental with ID " + id + " not found.");
                    }
                }
            } catch (SQLException e) {
                SqlUtil.rollback(con);
                throw new DataAccessException("Error fetching rental by ID", e);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error fetching rental by ID", e);
        } finally {
            SqlUtil.close(con);
        }
    }


    @Override
    public List<Rental> getRentalsByCustomerId(int customerId) {
        String query = BASE_SELECT_QUERY + " WHERE c.customer_id = ?";
        List<Rental> rentals = new ArrayList<>();
        Connection con = null;
        try {
            con = cm.getConnection(false);
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setInt(1, customerId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        rentals.add(mapRowToRental(rs));
                    }
                }
            } catch (SQLException e) {
                SqlUtil.rollback(con);
                throw new DataAccessException("Error fetching rentals by customer ID", e);
            }
            con.commit();
        } catch (SQLException e) {
            throw new DataAccessException("Error fetching rentals by customer ID", e);
        } finally {
            SqlUtil.close(con);
        }
        return rentals;
    }


    @Override
    public List<Rental> getRentalsByInstanceId(int instanceId) {
        String query = BASE_SELECT_QUERY + " WHERE pi.instance_id = ?";
        List<Rental> rentals = new ArrayList<>();
        Connection con = null;
        try {
            con = cm.getConnection(false);
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setInt(1, instanceId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        rentals.add(mapRowToRental(rs));
                    }
                }
            } catch (SQLException e) {
                SqlUtil.rollback(con);
                throw new DataAccessException("Error fetching rentals by instance ID", e);
            }
            con.commit();
        } catch (SQLException e) {
            throw new DataAccessException("Error fetching rentals by instance ID", e);
        } finally {
            SqlUtil.close(con);
        }
        return rentals;
    }


    @Override
    public List<Rental> getAllRentals() {
        List<Rental> rentals = new ArrayList<>();
        Connection con = null;
        try {
            con = cm.getConnection(false);
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(BASE_SELECT_QUERY)) {
                while (rs.next()) {
                    rentals.add(mapRowToRental(rs));
                }
            } catch (SQLException e) {
                SqlUtil.rollback(con);
                throw new DataAccessException("Error fetching all rentals", e);
            }
            con.commit();
        } catch (SQLException e) {
            throw new DataAccessException("Error fetching all rentals", e);
        } finally {
            SqlUtil.close(con);
        }
        return rentals;
    }


    Rental mapRowToRental(ResultSet rs) throws SQLException {
        Rental rental = new Rental();
        ProductInstance instance = new MySqlProductInstanceDAO(cm).mapRowToProductInstance(rs);
        Customer customer = new MySqlCustomerDAO(cm).mapRowToCustomer(rs);
        rental.setProductInstance(instance);
        rental.setCustomer(customer);
        rental.setId(rs.getInt("rental_id"));
        rental.setRentalDate(rs.getDate("rental_date"));
        rental.setReturnDate(rs.getDate("return_date"));
        rental.setRentalPrice(rs.getDouble("rental_price"));
        return rental;
    }
}
