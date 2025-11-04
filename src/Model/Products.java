package Model;

import java.math.BigDecimal;

public class Products {
    private int product_id;
    private String product_name;
    private String product_brand;
    private BigDecimal product_price;
    private BigDecimal product_markup;
    private int product_stock;

    public Products(int product_id, String product_name, String product_brand, BigDecimal product_price, BigDecimal product_markup, int product_stock) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_brand = product_brand;
        this.product_price = product_price;
        this.product_markup = product_markup;
        this.product_stock = product_stock;
    }

    public Products() {}

    // Getters and Setters
    public int getProductId() { return product_id; }
    public void setProductId(int product_id) { this.product_id = product_id; }

    public String getProductName() { return product_name; }
    public void setProductName(String product_name) { this.product_name = product_name; }

    public String getProductBrand() { return product_brand; }
    public void setProductBrand(String product_brand) { this.product_brand = product_brand; }

    public BigDecimal getProductPrice() { return product_price; }
    public void setProductPrice(BigDecimal product_price) { this.product_price = product_price; }

    public BigDecimal getProductMarkup() { return product_markup; }
    public void setProductMarkup(BigDecimal product_markup) { this.product_markup = product_markup; }

    public int getProductStock() { return product_stock; }
    public void setProductStock(int product_stock) { this.product_stock = product_stock; }
}
