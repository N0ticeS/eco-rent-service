package ua.nure.st.kpp.example.demo.dao.mysqldao;

import ua.nure.st.kpp.example.demo.dao.ConnectionManager;
import ua.nure.st.kpp.example.demo.dao.DataAccessException;
import ua.nure.st.kpp.example.demo.dao.DataNotFoundException;
import ua.nure.st.kpp.example.demo.dao.daointerface.ProductDAO;
import ua.nure.st.kpp.example.demo.entity.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlProductDAO implements ProductDAO {

    private final ConnectionManager cm;

    public MySqlProductDAO(ConnectionManager cm) {
        this.cm = cm;
    }

    private static final String BASE_SELECT_QUERY = """
            SELECT product_id, product_name, description, price
            FROM products
            """;

    @Override
    public void createProduct(Product product) {
        String query = "INSERT INTO products (product_name, description, price) VALUES (?, ?, ?)";
        Connection con = null;
        try {
            con = cm.getConnection(false);
            try (PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                int k = 0;
                stmt.setString(++k, product.getProductName());
                stmt.setString(++k, product.getDescription());
                stmt.setDouble(++k, product.getPrice());
                stmt.executeUpdate();

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        product.setId(generatedKeys.getInt(1));
                    } else {
                        throw new DataAccessException("Creating product failed, no ID obtained.");
                    }
                }
                con.commit();
            } catch (SQLException e) {
                SqlUtil.rollback(con);
                throw new DataAccessException("Error creating product", e);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error creating product", e);
        } finally {
            SqlUtil.close(con);
        }
    }


    @Override
    public void updateProduct(Product product) {
        String query = "UPDATE products SET product_name = ?, description = ?, price = ? WHERE product_id = ?";
        Connection con = null;
        try {
            con = cm.getConnection(false);
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                int k = 0;
                stmt.setString(++k, product.getProductName());
                stmt.setString(++k, product.getDescription());
                stmt.setDouble(++k, product.getPrice());
                stmt.setInt(++k, product.getId());
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new DataNotFoundException("Product with ID " + product.getId() + " not found for update.");
                }
                con.commit();
            } catch (SQLException e) {
                SqlUtil.rollback(con);
                throw new DataAccessException("Error updating product", e);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error updating product", e);
        } finally {
            SqlUtil.close(con);
        }
    }


    @Override
    public void deleteProduct(int id) {
        String query = "DELETE FROM products WHERE product_id = ?";
        Connection con = null;
        try {
            con = cm.getConnection(false);
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setInt(1, id);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new DataNotFoundException("Product with ID " + id + " not found for deletion.");
                }
                con.commit();
            } catch (SQLException e) {
                SqlUtil.rollback(con);
                throw new DataAccessException("Error deleting product", e);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting product", e);
        } finally {
            SqlUtil.close(con);
        }
    }


    @Override
    public Product getProductById(int id) {
        String query = BASE_SELECT_QUERY + " WHERE product_id = ?";
        Connection con = null;
        try {
            con = cm.getConnection(false);
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        con.commit();
                        return mapRowToProduct(rs);
                    } else {
                        throw new DataNotFoundException("Product with ID " + id + " not found.");
                    }
                }

            } catch (SQLException e) {
                SqlUtil.rollback(con);
                throw new DataAccessException("Error fetching product by ID", e);
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error fetching product by ID", e);
        } finally {
            SqlUtil.close(con);
        }
    }


    @Override
    public List<Product> getProductsByName(String name) {
        String query = BASE_SELECT_QUERY + " WHERE product_name LIKE ?";
        List<Product> products = new ArrayList<>();
        Connection con = null;
        try {
            con = cm.getConnection(false);
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, "%" + name + "%");
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        products.add(mapRowToProduct(rs));
                    }
                }
            } catch (SQLException e) {
                SqlUtil.rollback(con);
                throw new DataAccessException("Error fetching products by name", e);
            }
            con.commit();
        } catch (SQLException e) {
            throw new DataAccessException("Error fetching products by name", e);
        } finally {
            SqlUtil.close(con);
        }
        return products;
    }


    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        Connection con = null;
        try {
            con = cm.getConnection(false);
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(BASE_SELECT_QUERY)) {
                while (rs.next()) {
                    products.add(mapRowToProduct(rs));
                }
            } catch (SQLException e) {
                SqlUtil.rollback(con);
                throw new DataAccessException("Error fetching all products", e);
            }
            con.commit();
        } catch (SQLException e) {
            throw new DataAccessException("Error fetching all products", e);
        } finally {
            SqlUtil.close(con);
        }
        return products;
    }


    Product mapRowToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("product_id"));
        product.setProductName(rs.getString("product_name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getDouble("price"));
        return product;
    }
}
