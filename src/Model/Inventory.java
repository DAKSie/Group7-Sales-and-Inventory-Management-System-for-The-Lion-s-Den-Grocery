// src/Model/Inventory.java
package Model;

public class Inventory {
    private int inventory_id;
    private String or_number;
    private int product_id;
    private int inventory_quantity;
    private double unit_price;
    private double inventory_price;

    public Inventory(int inventory_id, String or_number, int product_id,  int inventory_quantity, double unit_price, double inventory_price) {
        this.inventory_id = inventory_id;
        this.or_number = or_number;
        this.product_id = product_id;
        this.inventory_quantity = inventory_quantity;
        this.unit_price = unit_price;
        this.inventory_price = inventory_price;
    }

    public Inventory() {}

    // Getters and Setters
    public int getInventoryId() { return inventory_id; }
    public void setInventoryId(int inventory_id) { this.inventory_id = inventory_id; }

    public String getOrNumber() { return or_number; }
    public void setOrNumber(String or_number) { this.or_number = or_number; }

    public int getProductId() { return product_id; }
    public void setProductId(int product_id) { this.product_id = product_id; }

    public int getInventoryQuantity() { return inventory_quantity; }
    public void setInventoryQuantity(int inventory_quantity) { this.inventory_quantity = inventory_quantity; }

    public double getUnitPrice() { return unit_price; }
    public void setUnitPrice(double unit_price) { this.unit_price = unit_price; }

    public double getInventoryPrice() { return inventory_price; }
    public void setInventoryPrice(double inventory_price) { this.inventory_price = inventory_price; }
}