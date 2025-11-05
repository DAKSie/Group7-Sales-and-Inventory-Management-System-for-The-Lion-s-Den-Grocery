// src/Controllers/SalesController.java
package Controller;

import Model.Sale;
import java.sql.*;
import java.util.*;

public class SalesController {
    
    public boolean addSale(Sale sale) {
        ProductController productController = new ProductController();
        
        // First check if there's sufficient stock
        if (!productController.hasSufficientStock(sale.getProductId(), sale.getSaleQuantity())) {
            System.err.println("Insufficient stock for product ID: " + sale.getProductId());
            return false;
        }
        
        String sql = "INSERT INTO Sales (product_id, sale_item, sale_price, sale_quantity, sale_total, sale_user) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBContext.getConnection();
            if (conn == null) {
                System.err.println("Failed to establish database connection");
                return false;
            }
            
            // Start transaction
            conn.setAutoCommit(false);
            
            // Insert sale record
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, sale.getProductId());
            stmt.setString(2, sale.getSaleItem());
            stmt.setDouble(3, sale.getSalePrice());
            stmt.setInt(4, sale.getSaleQuantity());
            stmt.setDouble(5, sale.getSaleTotal());
            stmt.setString(6, sale.getSaleUser());
            stmt.executeUpdate();

            // Update product stock
            String updateStockSql = "UPDATE Product SET product_stock = product_stock - ? WHERE product_id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateStockSql);
            updateStmt.setInt(1, sale.getSaleQuantity());
            updateStmt.setInt(2, sale.getProductId());
            updateStmt.executeUpdate();
            updateStmt.close();
            
            // Commit transaction
            conn.commit();
            System.out.println("Sale added successfully and stock updated!");
            return true;

        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Failed to rollback transaction: " + ex.getMessage());
            }
            System.err.println("Failed to add sale: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                System.err.println("Failed to reset auto-commit: " + e.getMessage());
            }
            closeResources(stmt, conn);
        }
    }

    public List<Sale> getAllSales() {
        List<Sale> salesList = new ArrayList<>();
        String sql = "SELECT * FROM Sales ORDER BY sale_id DESC";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBContext.getConnection();
            if (conn == null) {
                System.err.println("Failed to establish database connection");
                return salesList;
            }
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Sale sale = new Sale(
                    rs.getInt("sale_id"),
                    rs.getInt("product_id"),
                    rs.getString("sale_item"),
                    rs.getDouble("sale_price"),
                    rs.getInt("sale_quantity"),
                    rs.getDouble("sale_total"),
                    rs.getString("sale_user")
                );
                salesList.add(sale);
            }

        } catch (SQLException e) {
            System.err.println("Failed to fetch sales: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(stmt, conn, rs);
        }

        return salesList;
    }

    public void updateSale(Sale sale) {
        String sql = "UPDATE Sales SET product_id = ?, sale_item = ?, sale_price = ?, sale_quantity = ?, sale_total = ?, sale_user = ? WHERE sale_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBContext.getConnection();
            if (conn == null) {
                System.err.println("Failed to establish database connection");
                return;
            }
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, sale.getProductId());
            stmt.setString(2, sale.getSaleItem());
            stmt.setDouble(3, sale.getSalePrice());
            stmt.setInt(4, sale.getSaleQuantity());
            stmt.setDouble(5, sale.getSaleTotal());
            stmt.setString(6, sale.getSaleUser());
            stmt.setInt(7, sale.getSaleId());
            stmt.executeUpdate();

            System.out.println("Sale updated successfully!");

        } catch (SQLException e) {
            System.err.println("Failed to update sale: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(stmt, conn);
        }
    }

    public void deleteSale(int id) {
        // First get the sale details to restore stock
        Sale sale = getSaleById(id);
        if (sale == null) {
            System.err.println("Sale not found with ID: " + id);
            return;
        }
        
        String sql = "DELETE FROM Sales WHERE sale_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBContext.getConnection();
            if (conn == null) {
                System.err.println("Failed to establish database connection");
                return;
            }
            
            // Start transaction
            conn.setAutoCommit(false);
            
            // Delete sale record
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();

            // Restore product stock
            String restoreStockSql = "UPDATE Product SET product_stock = product_stock + ? WHERE product_id = ?";
            PreparedStatement restoreStmt = conn.prepareStatement(restoreStockSql);
            restoreStmt.setInt(1, sale.getSaleQuantity());
            restoreStmt.setInt(2, sale.getProductId());
            restoreStmt.executeUpdate();
            restoreStmt.close();
            
            // Commit transaction
            conn.commit();
            System.out.println("Sale deleted successfully and stock restored!");

        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Failed to rollback transaction: " + ex.getMessage());
            }
            System.err.println("Failed to delete sale: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                System.err.println("Failed to reset auto-commit: " + e.getMessage());
            }
            closeResources(stmt, conn);
        }
    }

    public Sale getSaleById(int id) {
        String sql = "SELECT * FROM Sales WHERE sale_id = ?";
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
                return new Sale(
                    rs.getInt("sale_id"),
                    rs.getInt("product_id"),
                    rs.getString("sale_item"),
                    rs.getDouble("sale_price"),
                    rs.getInt("sale_quantity"),
                    rs.getDouble("sale_total"),
                    rs.getString("sale_user")
                );
            }

        } catch (SQLException e) {
            System.err.println("Failed to fetch sale: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(stmt, conn, rs);
        }
        return null;
    }

    public List<Map<String, Object>> getSalesWithProductDetails() {
        List<Map<String, Object>> salesDetails = new ArrayList<>();
        String sql = "SELECT s.*, p.product_name, p.product_brand " +
                     "FROM Sales s " +
                     "JOIN Product p ON s.product_id = p.product_id " +
                     "ORDER BY s.sale_id DESC";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBContext.getConnection();
            if (conn == null) {
                System.err.println("Failed to establish database connection");
                return salesDetails;
            }
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Map<String, Object> detail = new HashMap<>();
                detail.put("sale_id", rs.getInt("sale_id"));
                detail.put("product_id", rs.getInt("product_id"));
                detail.put("product_name", rs.getString("product_name"));
                detail.put("product_brand", rs.getString("product_brand"));
                detail.put("sale_item", rs.getString("sale_item"));
                detail.put("sale_price", rs.getDouble("sale_price"));
                detail.put("sale_quantity", rs.getInt("sale_quantity"));
                detail.put("sale_total", rs.getDouble("sale_total"));
                detail.put("sale_user", rs.getString("sale_user"));
                salesDetails.add(detail);
            }

        } catch (SQLException e) {
            System.err.println("Failed to fetch sales details: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(stmt, conn, rs);
        }

        return salesDetails;
    }

    public double getTotalSales() {
        String sql = "SELECT SUM(sale_total) as total_sales FROM Sales";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBContext.getConnection();
            if (conn == null) {
                System.err.println("Failed to establish database connection");
                return 0.0;
            }
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                return rs.getDouble("total_sales");
            }

        } catch (SQLException e) {
            System.err.println("Failed to calculate total sales: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(stmt, conn, rs);
        }
        return 0.0;
    }

    public List<Map<String, Object>> getSalesByDateRange(java.util.Date startDate, java.util.Date endDate) {
        List<Map<String, Object>> salesDetails = new ArrayList<>();
        String sql = "SELECT s.*, p.product_name, p.product_brand " +
                     "FROM Sales s " +
                     "JOIN Product p ON s.product_id = p.product_id " +
                     "WHERE DATE(s.sale_date) BETWEEN ? AND ? " +
                     "ORDER BY s.sale_id DESC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBContext.getConnection();
            if (conn == null) {
                System.err.println("Failed to establish database connection");
                return salesDetails;
            }
            
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, new java.sql.Date(startDate.getTime()));
            stmt.setDate(2, new java.sql.Date(endDate.getTime()));
            rs = stmt.executeQuery();
    
            while (rs.next()) {
                Map<String, Object> detail = new HashMap<>();
                detail.put("sale_id", rs.getInt("sale_id"));
                detail.put("product_id", rs.getInt("product_id"));
                detail.put("product_name", rs.getString("product_name"));
                detail.put("product_brand", rs.getString("product_brand"));
                detail.put("sale_item", rs.getString("sale_item"));
                detail.put("sale_price", rs.getDouble("sale_price"));
                detail.put("sale_quantity", rs.getInt("sale_quantity"));
                detail.put("sale_total", rs.getDouble("sale_total"));
                detail.put("sale_user", rs.getString("sale_user"));
                detail.put("sale_date", rs.getTimestamp("sale_date"));
                salesDetails.add(detail);
            }
    
        } catch (SQLException e) {
            System.err.println("Failed to fetch sales by date range: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(stmt, conn, rs);
        }
    
        return salesDetails;
    }

    public List<Map<String, Object>> getTopSellingProducts(int limit) {
        List<Map<String, Object>> topProducts = new ArrayList<>();
        String sql = "SELECT p.product_id, p.product_name, p.product_brand, " +
                     "SUM(s.sale_quantity) as total_quantity, " +
                     "SUM(s.sale_total) as total_revenue " +
                     "FROM Sales s " +
                     "JOIN Product p ON s.product_id = p.product_id " +
                     "GROUP BY p.product_id, p.product_name, p.product_brand " +
                     "ORDER BY total_quantity DESC " +
                     "LIMIT ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBContext.getConnection();
            if (conn == null) {
                System.err.println("Failed to establish database connection");
                return topProducts;
            }
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, limit);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> product = new HashMap<>();
                product.put("product_id", rs.getInt("product_id"));
                product.put("product_name", rs.getString("product_name"));
                product.put("product_brand", rs.getString("product_brand"));
                product.put("total_quantity", rs.getInt("total_quantity"));
                product.put("total_revenue", rs.getDouble("total_revenue"));
                topProducts.add(product);
            }

        } catch (SQLException e) {
            System.err.println("Failed to fetch top selling products: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(stmt, conn, rs);
        }

        return topProducts;
    }

    public Map<String, Object> getSalesSummary() {
        Map<String, Object> summary = new HashMap<>();
        String sql = "SELECT " +
                     "COUNT(*) as total_sales, " +
                     "SUM(sale_total) as total_revenue, " +
                     "AVG(sale_total) as average_sale, " +
                     "MAX(sale_total) as highest_sale, " +
                     "MIN(sale_total) as lowest_sale " +
                     "FROM Sales";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBContext.getConnection();
            if (conn == null) {
                System.err.println("Failed to establish database connection");
                return summary;
            }
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                summary.put("total_sales", rs.getInt("total_sales"));
                summary.put("total_revenue", rs.getDouble("total_revenue"));
                summary.put("average_sale", rs.getDouble("average_sale"));
                summary.put("highest_sale", rs.getDouble("highest_sale"));
                summary.put("lowest_sale", rs.getDouble("lowest_sale"));
            }

        } catch (SQLException e) {
            System.err.println("Failed to fetch sales summary: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(stmt, conn, rs);
        }

        return summary;
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