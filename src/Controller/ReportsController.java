package Controller;

import java.sql.*;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.*;

public class ReportsController {
    
    public Map<String, Object> getDashboardSummary() {
        Map<String, Object> summary = new HashMap<>();
        DecimalFormat df = new DecimalFormat("#,##0.00");
        
        try {
            // Total Products
            int totalProducts = getTotalProducts();
            summary.put("totalProducts", totalProducts);
            
            // Total Inventory Value
            double totalInventoryValue = getTotalInventoryValue();
            summary.put("totalInventoryValue", df.format(totalInventoryValue));
            
            // Total Sales Revenue
            double totalSalesRevenue = getTotalSalesRevenue();
            summary.put("totalSalesRevenue", df.format(totalSalesRevenue));
            
            // Total Sales Count
            int totalSalesCount = getTotalSalesCount();
            summary.put("totalSalesCount", totalSalesCount);
            
            // Low Stock Products (less than 10 items)
            int lowStockProducts = getLowStockProductsCount();
            summary.put("lowStockProducts", lowStockProducts);
            
            // Out of Stock Products
            int outOfStockProducts = getOutOfStockProductsCount();
            summary.put("outOfStockProducts", outOfStockProducts);
            
            // Total Users
            int totalUsers = getTotalUsers();
            summary.put("totalUsers", totalUsers);
            
            // Top Selling Product
            String topSellingProduct = getTopSellingProduct();
            summary.put("topSellingProduct", topSellingProduct);
            
            // Recent Sales (last 7 days)
            double recentSalesRevenue = getRecentSalesRevenue(7);
            summary.put("recentSalesRevenue", df.format(recentSalesRevenue));
            
        } catch (Exception e) {
            System.err.println("Error generating dashboard summary: " + e.getMessage());
            e.printStackTrace();
        }
        
        return summary;
    }
    
    public List<Map<String, Object>> getAllProductsReport() {
        List<Map<String, Object>> products = new ArrayList<>();
        String sql = "SELECT product_id, product_name, product_brand, product_price, " +
                     "product_markup, product_stock, " +
                     "(product_price + (product_price * product_markup / 100)) as selling_price " +
                     "FROM Product ORDER BY product_id";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBContext.getConnection();
            if (conn == null) return products;
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Map<String, Object> product = new HashMap<>();
                product.put("product_id", rs.getInt("product_id"));
                product.put("product_name", rs.getString("product_name"));
                product.put("product_brand", rs.getString("product_brand"));
                product.put("product_price", rs.getDouble("product_price"));
                product.put("product_markup", rs.getDouble("product_markup"));
                product.put("product_stock", rs.getInt("product_stock"));
                product.put("selling_price", rs.getDouble("selling_price"));
                products.add(product);
            }

        } catch (SQLException e) {
            System.err.println("Failed to fetch products report: " + e.getMessage());
        } finally {
            closeResources(stmt, conn, rs);
        }
        return products;
    }
    
    public List<Map<String, Object>> getAllInventoryReport() {
        List<Map<String, Object>> inventory = new ArrayList<>();
        String sql = "SELECT i.inventory_id, i.or_number, i.product_id, p.product_name, p.product_brand, " +
                     "i.inventory_quantity, i.unit_price, i.inventory_price, " +
                     "i.created_at " +
                     "FROM Inventory i " +
                     "JOIN Product p ON i.product_id = p.product_id " +
                     "ORDER BY i.inventory_id DESC";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBContext.getConnection();
            if (conn == null) return inventory;
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("inventory_id", rs.getInt("inventory_id"));
                item.put("or_number", rs.getString("or_number"));
                item.put("product_id", rs.getInt("product_id"));
                item.put("product_name", rs.getString("product_name"));
                item.put("product_brand", rs.getString("product_brand"));
                item.put("inventory_quantity", rs.getInt("inventory_quantity"));
                item.put("unit_price", rs.getDouble("unit_price"));
                item.put("inventory_price", rs.getDouble("inventory_price"));
                item.put("created_at", rs.getTimestamp("created_at"));
                inventory.add(item);
            }

        } catch (SQLException e) {
            System.err.println("Failed to fetch inventory report: " + e.getMessage());
        } finally {
            closeResources(stmt, conn, rs);
        }
        return inventory;
    }
    
    public List<Map<String, Object>> getAllSalesReport() {
        List<Map<String, Object>> sales = new ArrayList<>();
        String sql = "SELECT s.sale_id, s.product_id, p.product_name, p.product_brand, " +
                     "s.sale_item, s.sale_price, s.sale_quantity, s.sale_total, " +
                     "s.sale_user, s.sale_date " +
                     "FROM Sales s " +
                     "JOIN Product p ON s.product_id = p.product_id " +
                     "ORDER BY s.sale_id DESC";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBContext.getConnection();
            if (conn == null) return sales;
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Map<String, Object> sale = new HashMap<>();
                sale.put("sale_id", rs.getInt("sale_id"));
                sale.put("product_id", rs.getInt("product_id"));
                sale.put("product_name", rs.getString("product_name"));
                sale.put("product_brand", rs.getString("product_brand"));
                sale.put("sale_item", rs.getString("sale_item"));
                sale.put("sale_price", rs.getDouble("sale_price"));
                sale.put("sale_quantity", rs.getInt("sale_quantity"));
                sale.put("sale_total", rs.getDouble("sale_total"));
                sale.put("sale_user", rs.getString("sale_user"));
                sale.put("sale_date", rs.getTimestamp("sale_date"));
                sales.add(sale);
            }

        } catch (SQLException e) {
            System.err.println("Failed to fetch sales report: " + e.getMessage());
        } finally {
            closeResources(stmt, conn, rs);
        }
        return sales;
    }
    
    public List<Map<String, Object>> getTopSellingProducts(int limit) {
        List<Map<String, Object>> topProducts = new ArrayList<>();
        String sql = "SELECT p.product_id, p.product_name, p.product_brand, " +
                     "SUM(s.sale_quantity) as total_quantity, " +
                     "SUM(s.sale_total) as total_revenue, " +
                     "COUNT(s.sale_id) as sale_count " +
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
            if (conn == null) return topProducts;
            
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
                product.put("sale_count", rs.getInt("sale_count"));
                topProducts.add(product);
            }

        } catch (SQLException e) {
            System.err.println("Failed to fetch top selling products: " + e.getMessage());
        } finally {
            closeResources(stmt, conn, rs);
        }
        return topProducts;
    }
    
    // New method to get inventory report with OR numbers filtered by date range
    public List<Map<String, Object>> getInventoryReportByDateRange(Date startDate, Date endDate) {
        List<Map<String, Object>> inventory = new ArrayList<>();
        String sql = "SELECT i.inventory_id, i.or_number, i.product_id, p.product_name, p.product_brand, " +
                     "i.inventory_quantity, i.unit_price, i.inventory_price, " +
                     "i.created_at " +
                     "FROM Inventory i " +
                     "JOIN Product p ON i.product_id = p.product_id " +
                     "WHERE i.created_at BETWEEN ? AND ? " +
                     "ORDER BY i.created_at DESC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBContext.getConnection();
            if (conn == null) return inventory;
            
            stmt = conn.prepareStatement(sql);
            stmt.setTimestamp(1, new Timestamp(startDate.getTime()));
            stmt.setTimestamp(2, new Timestamp(endDate.getTime()));
            rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("inventory_id", rs.getInt("inventory_id"));
                item.put("or_number", rs.getString("or_number"));
                item.put("product_id", rs.getInt("product_id"));
                item.put("product_name", rs.getString("product_name"));
                item.put("product_brand", rs.getString("product_brand"));
                item.put("inventory_quantity", rs.getInt("inventory_quantity"));
                item.put("unit_price", rs.getDouble("unit_price"));
                item.put("inventory_price", rs.getDouble("inventory_price"));
                item.put("created_at", rs.getTimestamp("created_at"));
                inventory.add(item);
            }

        } catch (SQLException e) {
            System.err.println("Failed to fetch inventory report by date range: " + e.getMessage());
        } finally {
            closeResources(stmt, conn, rs);
        }
        return inventory;
    }
    
    // New method to get inventory summary by OR number
    public List<Map<String, Object>> getInventorySummaryByOrNumber() {
        List<Map<String, Object>> summary = new ArrayList<>();
        String sql = "SELECT i.or_number, COUNT(i.inventory_id) as item_count, " +
                     "SUM(i.inventory_quantity) as total_quantity, " +
                     "SUM(i.inventory_price) as total_value, " +
                     "MIN(i.created_at) as first_entry, MAX(i.created_at) as last_entry " +
                     "FROM Inventory i " +
                     "GROUP BY i.or_number " +
                     "ORDER BY last_entry DESC";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBContext.getConnection();
            if (conn == null) return summary;
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("or_number", rs.getString("or_number"));
                item.put("item_count", rs.getInt("item_count"));
                item.put("total_quantity", rs.getInt("total_quantity"));
                item.put("total_value", rs.getDouble("total_value"));
                item.put("first_entry", rs.getTimestamp("first_entry"));
                item.put("last_entry", rs.getTimestamp("last_entry"));
                summary.add(item);
            }

        } catch (SQLException e) {
            System.err.println("Failed to fetch inventory summary by OR number: " + e.getMessage());
        } finally {
            closeResources(stmt, conn, rs);
        }
        return summary;
    }
    
    // Helper methods for summary calculations
    private int getTotalProducts() {
        String sql = "SELECT COUNT(*) as total FROM Product";
        return executeCountQuery(sql);
    }
    
    private double getTotalInventoryValue() {
        String sql = "SELECT SUM(inventory_price) as total FROM Inventory";
        return executeSumQuery(sql);
    }
    
    private double getTotalSalesRevenue() {
        String sql = "SELECT SUM(sale_total) as total FROM Sales";
        return executeSumQuery(sql);
    }
    
    private int getTotalSalesCount() {
        String sql = "SELECT COUNT(*) as total FROM Sales";
        return executeCountQuery(sql);
    }
    
    private int getLowStockProductsCount() {
        String sql = "SELECT COUNT(*) as total FROM Product WHERE product_stock < 10 AND product_stock > 0";
        return executeCountQuery(sql);
    }
    
    private int getOutOfStockProductsCount() {
        String sql = "SELECT COUNT(*) as total FROM Product WHERE product_stock = 0";
        return executeCountQuery(sql);
    }
    
    private int getTotalUsers() {
        // The actual users table is named `users` in the database (see migrations/SimsSchema.py)
        String sql = "SELECT COUNT(*) as total FROM users";
        return executeCountQuery(sql);
    }
    
    private String getTopSellingProduct() {
        String sql = "SELECT p.product_name " +
                     "FROM Sales s " +
                     "JOIN Product p ON s.product_id = p.product_id " +
                     "GROUP BY p.product_id, p.product_name " +
                     "ORDER BY SUM(s.sale_quantity) DESC " +
                     "LIMIT 1";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBContext.getConnection();
            if (conn == null) return "N/A";
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                return rs.getString("product_name");
            }

        } catch (SQLException e) {
            System.err.println("Failed to fetch top selling product: " + e.getMessage());
        } finally {
            closeResources(stmt, conn, rs);
        }
        return "N/A";
    }
    
    private double getRecentSalesRevenue(int days) {
        String sql = "SELECT SUM(sale_total) as total FROM Sales WHERE sale_date >= DATE_SUB(NOW(), INTERVAL ? DAY)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBContext.getConnection();
            if (conn == null) return 0.0;
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, days);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("total");
            }

        } catch (SQLException e) {
            System.err.println("Failed to fetch recent sales revenue: " + e.getMessage());
        } finally {
            closeResources(stmt, conn, rs);
        }
        return 0.0;
    }
    
    private int executeCountQuery(String sql) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBContext.getConnection();
            if (conn == null) return 0;
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            System.err.println("Failed to execute count query: " + e.getMessage());
        } finally {
            closeResources(stmt, conn, rs);
        }
        return 0;
    }
    
    private double executeSumQuery(String sql) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBContext.getConnection();
            if (conn == null) return 0.0;
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                return rs.getDouble("total");
            }

        } catch (SQLException e) {
            System.err.println("Failed to execute sum query: " + e.getMessage());
        } finally {
            closeResources(stmt, conn, rs);
        }
        return 0.0;
    }
    
    // Helper methods for resource management
    private void closeResources(PreparedStatement stmt, Connection conn, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
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