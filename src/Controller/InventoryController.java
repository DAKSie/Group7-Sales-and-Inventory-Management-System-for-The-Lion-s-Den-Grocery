package Controller;

import Model.Inventory;
import java.sql.*;
import java.util.*;

public class InventoryController {
    
    public boolean addInventory(Inventory inventory) {
        String sql = "INSERT INTO Inventory (or_number, product_id, inventory_quantity, unit_price, inventory_price) VALUES (?, ?, ?, ?, ?)";
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
            
            // Insert inventory record
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, inventory.getOrNumber());
            stmt.setInt(2, inventory.getProductId());
            stmt.setInt(3, inventory.getInventoryQuantity());
            stmt.setDouble(4, inventory.getUnitPrice());
            stmt.setDouble(5, inventory.getInventoryPrice());
            stmt.executeUpdate();

            // Update product stock - ADD the inventory quantity to product stock
            String updateStockSql = "UPDATE Product SET product_stock = product_stock + ? WHERE product_id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateStockSql);
            updateStmt.setInt(1, inventory.getInventoryQuantity());
            updateStmt.setInt(2, inventory.getProductId());
            updateStmt.executeUpdate();
            updateStmt.close();
            
            // Commit transaction
            conn.commit();
            System.out.println("Inventory added successfully and product stock updated!");
            return true;

        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Failed to rollback transaction: " + ex.getMessage());
            }
            System.err.println("Failed to add inventory: " + e.getMessage());
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

    public List<Inventory> getAllInventory() {
        List<Inventory> inventoryList = new ArrayList<>();
        String sql = "SELECT * FROM Inventory";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBContext.getConnection();
            if (conn == null) {
                System.err.println("Failed to establish database connection");
                return inventoryList;
            }
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Inventory inventory = new Inventory(
                    rs.getInt("inventory_id"),
                    rs.getString("or_number"),
                    rs.getInt("product_id"),
                    rs.getInt("inventory_quantity"),
                    rs.getDouble("unit_price"),
                    rs.getDouble("inventory_price")
                );
                inventoryList.add(inventory);
            }

        } catch (SQLException e) {
            System.err.println("Failed to fetch inventory: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(stmt, conn, rs);
        }

        return inventoryList;
    }

    public boolean updateInventory(Inventory inventory) {
        // First get the old inventory record to calculate stock difference
        Inventory oldInventory = getInventoryById(inventory.getInventoryId());
        if (oldInventory == null) {
            System.err.println("Old inventory record not found");
            return false;
        }
        
        int stockDifference = inventory.getInventoryQuantity() - oldInventory.getInventoryQuantity();
        
        String sql = "UPDATE Inventory SET or_number = ?, product_id = ?, inventory_quantity = ?, unit_price = ?, inventory_price = ? WHERE inventory_id = ?";
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
            
            // Update inventory record
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, inventory.getOrNumber());
            stmt.setInt(2, inventory.getProductId());
            stmt.setInt(3, inventory.getInventoryQuantity());
            stmt.setDouble(4, inventory.getUnitPrice());
            stmt.setDouble(5, inventory.getInventoryPrice());
            stmt.setInt(6, inventory.getInventoryId());
            stmt.executeUpdate();

            // Update product stock with the difference
            if (stockDifference != 0) {
                String updateStockSql = "UPDATE Product SET product_stock = product_stock + ? WHERE product_id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateStockSql);
                updateStmt.setInt(1, stockDifference);
                updateStmt.setInt(2, inventory.getProductId());
                updateStmt.executeUpdate();
                updateStmt.close();
            }
            
            // Commit transaction
            conn.commit();
            System.out.println("Inventory updated successfully and product stock adjusted!");
            return true;

        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Failed to rollback transaction: " + ex.getMessage());
            }
            System.err.println("Failed to update inventory: " + e.getMessage());
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

    public boolean deleteInventory(int id) {
        // First get the inventory record to restore stock
        Inventory inventory = getInventoryById(id);
        if (inventory == null) {
            System.err.println("Inventory not found with ID: " + id);
            return false;
        }
        
        String sql = "DELETE FROM Inventory WHERE inventory_id = ?";
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
            
            // Delete inventory record
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();

            // Subtract the inventory quantity from product stock (reverse the addition)
            String updateStockSql = "UPDATE Product SET product_stock = product_stock - ? WHERE product_id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateStockSql);
            updateStmt.setInt(1, inventory.getInventoryQuantity());
            updateStmt.setInt(2, inventory.getProductId());
            updateStmt.executeUpdate();
            updateStmt.close();
            
            // Commit transaction
            conn.commit();
            System.out.println("Inventory deleted successfully and product stock adjusted!");
            return true;

        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Failed to rollback transaction: " + ex.getMessage());
            }
            System.err.println("Failed to delete inventory: " + e.getMessage());
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

    public Inventory getInventoryById(int id) {
        String sql = "SELECT * FROM Inventory WHERE inventory_id = ?";
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
                return new Inventory(
                    rs.getInt("inventory_id"),
                    rs.getString("or_number"),
                    rs.getInt("product_id"),
                    rs.getInt("inventory_quantity"),
                    rs.getDouble("unit_price"),
                    rs.getDouble("inventory_price")
                );
            }

        } catch (SQLException e) {
            System.err.println("Failed to fetch inventory: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(stmt, conn, rs);
        }
        return null;
    }

    public List<Map<String, Object>> getInventoryWithProductDetails() {
        List<Map<String, Object>> inventoryDetails = new ArrayList<>();
        String sql = "SELECT i.*, p.product_name, p.product_brand, p.product_stock " +
                     "FROM Inventory i " +
                     "JOIN Product p ON i.product_id = p.product_id " +
                     "ORDER BY i.inventory_id DESC";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBContext.getConnection();
            if (conn == null) {
                System.err.println("Failed to establish database connection");
                return inventoryDetails;
            }
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Map<String, Object> detail = new HashMap<>();
                detail.put("inventory_id", rs.getInt("inventory_id"));
                detail.put("or_number", rs.getString("or_number"));
                detail.put("product_id", rs.getInt("product_id"));
                detail.put("product_name", rs.getString("product_name"));
                detail.put("product_brand", rs.getString("product_brand"));
                detail.put("product_stock", rs.getInt("product_stock")); // Show current product stock
                detail.put("inventory_quantity", rs.getInt("inventory_quantity"));
                detail.put("unit_price", rs.getDouble("unit_price"));
                detail.put("inventory_price", rs.getDouble("inventory_price"));
                inventoryDetails.add(detail);
            }

        } catch (SQLException e) {
            System.err.println("Failed to fetch inventory details: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(stmt, conn, rs);
        }

        return inventoryDetails;
    }

    // Method to get total inventory value
    public double getTotalInventoryValue() {
        String sql = "SELECT SUM(inventory_price) as total_value FROM Inventory";
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
                return rs.getDouble("total_value");
            }

        } catch (SQLException e) {
            System.err.println("Failed to calculate total inventory value: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(stmt, conn, rs);
        }
        return 0.0;
    }

    // Method to check if product exists before adding inventory
    public boolean productExists(int productId) {
        String sql = "SELECT product_id FROM Product WHERE product_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBContext.getConnection();
            if (conn == null) {
                System.err.println("Failed to establish database connection");
                return false;
            }
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, productId);
            rs = stmt.executeQuery();
            
            return rs.next(); // If there's a result, product exists
            
        } catch (SQLException e) {
            System.err.println("Failed to check product existence: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeResources(stmt, conn, rs);
        }
    }

    // Method to generate a new OR number
    public String generateNewOrNumber() {
        String sql = "SELECT MAX(or_number) as last_or FROM Inventory WHERE or_number LIKE 'SIM-%'";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBContext.getConnection();
            if (conn == null) {
                System.err.println("Failed to establish database connection");
                return "SIM-001"; // Default first number
            }
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            if (rs.next() && rs.getString("last_or") != null) {
                String lastOr = rs.getString("last_or");
                // Extract the number part and increment
                int lastNumber = Integer.parseInt(lastOr.split("-")[1]);
                return String.format("SIM-%03d", lastNumber + 1);
            } else {
                return "SIM-001"; // First OR number
            }

        } catch (SQLException e) {
            System.err.println("Failed to generate OR number: " + e.getMessage());
            e.printStackTrace();
            return "SIM-001";
        } catch (NumberFormatException e) {
            System.err.println("Failed to parse OR number: " + e.getMessage());
            return "SIM-001";
        } finally {
            closeResources(stmt, conn, rs);
        }
    }

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