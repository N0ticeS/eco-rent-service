package ua.nure.st.kpp.example.demo.dao.mysqldao;

import ua.nure.st.kpp.example.demo.dao.ConnectionManager;
import ua.nure.st.kpp.example.demo.dao.DataAccessException;
import ua.nure.st.kpp.example.demo.dao.DataNotFoundException;
import ua.nure.st.kpp.example.demo.dao.daointerface.ProductInstanceDAO;
import ua.nure.st.kpp.example.demo.entity.Product;
import ua.nure.st.kpp.example.demo.entity.ProductInstance;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlProductInstanceDAO implements ProductInstanceDAO {

    private final ConnectionManager cm;

    public MySqlProductInstanceDAO(ConnectionManager cm) {
        this.cm = cm;
    }

    private static final String BASE_SELECT_QUERY = """
            SELECT 
            pi.instance_id, pi.rental_price, pi.rental_rate,
            p.product_id, p.product_name, p.description, p.price
            FROM product_instances pi
            JOIN products p ON pi.product_id = p.product_id
            """;

    @Override
    public void createProductInstance(ProductInstance instance) {
        String query = "INSERT INTO product_instances (product_id) VALUES (?)";
        Connection con = null;
        try {
            con = cm.getConnection(false);
            try (PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                int k = 0;
                stmt.setInt(++k, instance.getProduct().getId());
                stmt.executeUpdate();

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        instance.setId(generatedKeys.getInt(1));
                    } else {
                        throw new DataAccessException("Creating product instance failed, no ID obtained.");
                    }
                }
                con.commit();
            } catch (SQLException e) {
                SqlUtil.rollback(con);
                throw new DataAccessException("Error creating product instance", e);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error creating product instance", e);
        } finally {
            SqlUtil.close(con);
        }
    }


    @Override
    public void updateProductInstance(ProductInstance instance) {
        String query = "UPDATE product_instances SET product_id = ?, rental_price = ?, rental_rate = ? WHERE instance_id = ?";
        Connection con = null;
        try {
            con = cm.getConnection(false);
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                int k = 0;
                stmt.setInt(++k, instance.getProduct().getId());
                stmt.setDouble(++k, instance.getRentalPrice());
                stmt.setInt(++k, instance.getRentalRate());
                stmt.setInt(++k, instance.getId());
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new DataNotFoundException("ProductInstance with ID " + instance.getId() + " not found for update.");
                }
                con.commit();
            } catch (SQLException e) {
                SqlUtil.rollback(con);
                throw new DataAccessException("Error updating product instance", e);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error updating product instance", e);
        } finally {
            SqlUtil.close(con);
        }
    }


    @Override
    public void deleteProductInstance(int id) {
        String query = "DELETE FROM product_instances WHERE instance_id = ?";
        Connection con = null;
        try {
            con = cm.getConnection(false);
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setInt(1, id);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new DataNotFoundException("ProductInstance with ID " + id + " not found for deletion.");
                }
                con.commit();
            } catch (SQLException e) {
                SqlUtil.rollback(con);
                throw new DataAccessException("Error deleting product instance", e);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting product instance", e);
        } finally {
            SqlUtil.close(con);
        }
    }


    @Override
    public ProductInstance getProductInstanceById(int id) {
        String query = BASE_SELECT_QUERY + " WHERE pi.instance_id = ?";
        Connection con = null;
        try {
            con = cm.getConnection(false);
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        ProductInstance instance = mapRowToProductInstance(rs);
                        con.commit();
                        return instance;
                    } else {

                        throw new DataNotFoundException("ProductInstance with ID " + id + " not found.");
                    }
                }
            } catch (SQLException e) {
                SqlUtil.rollback(con);
                throw new DataAccessException("Error fetching product instance by ID", e);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error fetching product instance by ID", e);
        } finally {
            SqlUtil.close(con); // Закрытие подключения
        }
    }


    @Override
    public List<ProductInstance> getProductInstanceByName(String name) {
        String query = BASE_SELECT_QUERY + " WHERE p.product_name = ?";
        List<ProductInstance> productInstances = new ArrayList<>();
        Connection con = null;
        try {
            con = cm.getConnection(false);
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, name);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        productInstances.add(mapRowToProductInstance(rs));
                    }
                }
            } catch (SQLException e) {
                SqlUtil.rollback(con);
                throw new DataAccessException("Error fetching product instance by Product Name", e);
            }
            con.commit();
        } catch (SQLException e) {
            throw new DataAccessException("Error fetching product instance by Product Name", e);
        } finally {
            SqlUtil.close(con);
        }
        return productInstances;
    }


    @Override
    public List<ProductInstance> getProductInstanceByProductId(int productId) {
        String query = BASE_SELECT_QUERY + " WHERE p.product_id = ?";
        List<ProductInstance> instances = new ArrayList<>();
        Connection con = null;
        try {
            con = cm.getConnection(false);
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setInt(1, productId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        instances.add(mapRowToProductInstance(rs));
                    }
                }
            } catch (SQLException e) {
                SqlUtil.rollback(con);
                throw new DataAccessException("Error fetching product instances by product ID", e);
            }
            con.commit();
        } catch (SQLException e) {
            throw new DataAccessException("Error fetching product instances by product ID", e);
        } finally {
            SqlUtil.close(con);
        }
        return instances;
    }


    @Override
    public List<ProductInstance> getAllProductInstances() {
        List<ProductInstance> instances = new ArrayList<>();
        Connection con = null;
        try {
            con = cm.getConnection(false);
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(BASE_SELECT_QUERY)) {
                while (rs.next()) {
                    instances.add(mapRowToProductInstance(rs));
                }
            } catch (SQLException e) {
                SqlUtil.rollback(con);
                throw new DataAccessException("Error fetching all product instances", e);
            }
            con.commit();
        } catch (SQLException e) {
            throw new DataAccessException("Error fetching all product instances", e);
        } finally {
            SqlUtil.close(con);
        }
        return instances;
    }


    ProductInstance mapRowToProductInstance(ResultSet rs) throws SQLException {
        ProductInstance instance = new ProductInstance();
        Product product = new MySqlProductDAO(cm).mapRowToProduct(rs);
        instance.setProduct(product);
        instance.setId(rs.getInt("instance_id"));
        instance.setRentalPrice(rs.getDouble("rental_price"));
        instance.setRentalRate(rs.getInt("rental_rate"));
        return instance;
    }
}
