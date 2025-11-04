package Model;

import java.math.BigDecimal;

public class Inventory {
    private int inventory_id;
    private int product_id;
    private int inventory_quantity;
    private BigDecimal unit_price;
    private BigDecimal inventory_price;

    public Inventory(int inventory_id, int product_id, int inventory_quantity, BigDecimal unit_price, BigDecimal inventory_price) {
        this.inventory_id = inventory_id;
        this.product_id = product_id;
        this.inventory_quantity = inventory_quantity;
        this.unit_price = unit_price;
        this.inventory_price = inventory_price;
    }

    public Inventory() {}

    // Getters and Setters
    public int getInventoryId() { return inventory_id; }
    public void setInventoryId(int inventory_id) { this.inventory_id = inventory_id; }

    public int getProductId() { return product_id; }
    public void setProductId(int product_id) { this.product_id = product_id; }

    public int getInventoryQuantity() { return inventory_quantity; }
    public void setInventoryQuantity(int inventory_quantity) { this.inventory_quantity = inventory_quantity; }

    public BigDecimal getUnitPrice() { return unit_price; }
    public void setUnitPrice(BigDecimal unit_price) { this.unit_price = unit_price; }

    public BigDecimal getInventoryPrice() { return inventory_price; }
    public void setInventoryPrice(BigDecimal inventory_price) { this.inventory_price = inventory_price; }
}
