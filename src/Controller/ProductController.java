package Controller;

import Model.Product;
import java.sql.*;
import java.util.*;

public class ProductController {    
    public List<Map<String, Object>> getProductsWithSellingPrice() {
        List<Map<String, Object>> productsWithPrices = new ArrayList<>();
        List<Product> products = getAllProducts();
        
        for (Product product : products) {
            Map<String, Object> productData = new HashMap<>();
            productData.put("product_id", product.getProductId());
            productData.put("product_name", product.getProductName());
            productData.put("product_brand", product.getProductBrand());
            productData.put("product_price", product.getProductPrice());
            productData.put("product_markup", product.getProductMarkup());
            productData.put("product_stock", product.getProductStock());
            productData.put("selling_price", getSellingPrice(product));
            productsWithPrices.add(productData);
        }
        
        return productsWithPrices;
    }
    
    // Method to update product stock when a sale occurs
    public boolean updateProductStock(int productId, int quantitySold) {
        String sql = "UPDATE Product SET product_stock = product_stock - ? WHERE product_id = ? AND product_stock >= ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBContext.getConnection();
            if (conn == null) {
                System.err.println("Failed to establish database connection");
                return false;
            }
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, quantitySold);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantitySold); // Ensure we have enough stock
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product stock updated successfully! Reduced " + quantitySold + " from product ID: " + productId);
                return true;
            } else {
                System.err.println("Failed to update stock: Insufficient stock or product not found for ID: " + productId);
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Failed to update product stock: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeResources(stmt, conn);
        }
    }
    
    // Method to check if there's enough stock for a sale
    public boolean hasSufficientStock(int productId, int requestedQuantity) {
        Product product = getProductById(productId);
        if (product != null) {
            return product.getProductStock() >= requestedQuantity;
        }
        return false;
    }
    
    // Method to get current stock for a product
    public int getCurrentStock(int productId) {
        Product product = getProductById(productId);
        return product != null ? product.getProductStock() : 0;
    }

    public void addProduct(Product product) {
        String sql = "INSERT INTO Product (product_name, product_brand, product_price, product_markup, product_stock) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBContext.getConnection();
            if (conn == null) {
                System.err.println("Failed to establish database connection");
                return;
            }
            
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getProductBrand());
            stmt.setDouble(3, product.getProductPrice());
            stmt.setDouble(4, product.getProductMarkup());
            stmt.setInt(5, product.getProductStock());
            stmt.executeUpdate();

            System.out.println("Product added successfully!");

        } catch (SQLException e) {
            System.err.println("Failed to add product: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(stmt, conn);
        }
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Product";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBContext.getConnection();
            if (conn == null) {
                System.err.println("Failed to establish database connection");
                return products;
            }
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Product product = new Product(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getString("product_brand"),
                    rs.getDouble("product_price"),
                    rs.getDouble("product_markup"),
                    rs.getInt("product_stock")
                );
                products.add(product);
            }

        } catch (SQLException e) {
            System.err.println("Failed to fetch products: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(stmt, conn, rs);
        }

        return products;
    }

    public void updateProduct(Product product) {
        String sql = "UPDATE Product SET product_name = ?, product_brand = ?, product_price = ?, product_markup = ?, product_stock = ? WHERE product_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBContext.getConnection();
            if (conn == null) {
                System.err.println("Failed to establish database connection");
                return;
            }
            
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getProductBrand());
            stmt.setDouble(3, product.getProductPrice());
            stmt.setDouble(4, product.getProductMarkup());
            stmt.setInt(5, product.getProductStock());
            stmt.setInt(6, product.getProductId());
            stmt.executeUpdate();

            System.out.println("Product updated successfully!");

        } catch (SQLException e) {
            System.err.println("Failed to update product: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(stmt, conn);
        }
    }

    public void deleteProduct(int id) {
        String sql = "DELETE FROM Product WHERE product_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBContext.getConnection();
            if (conn == null) {
                System.err.println("Failed to establish database connection");
                return;
            }
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Product deleted successfully.");

        } catch (SQLException e) {
            System.err.println("Failed to delete product: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(stmt, conn);
        }
    }

    public Product getProductById(int id) {
        String sql = "SELECT * FROM Product WHERE product_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBContext.getConnection();
            if (conn == null) {
                System.err.println("Failed to establish database connection");
                return null;
            }
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return new Product(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getString("product_brand"),
                    rs.getDouble("product_price"),
                    rs.getDouble("product_markup"),
                    rs.getInt("product_stock")
                );
            }

        } catch (SQLException e) {
            System.err.println("Failed to fetch product: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(stmt, conn, rs);
        }
        return null;
    }
    
    public double getSellingPrice(Product product) {
        return product.getProductPrice() + (product.getProductPrice() * product.getProductMarkup() / 100);
    }
    
    // Helper methods for resource management
    private void closeResources(PreparedStatement stmt, Connection conn) {
        closeResources(stmt, conn, null);
    }
    
    private void closeResources(Statement stmt, Connection conn, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
}