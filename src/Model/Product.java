package Model;

public class Product {
    private int product_id;
    private String product_name;
    private String product_brand;
    private double product_price;
    private double product_markup;
    private int product_stock;

    public Product(int product_id, String product_name, String product_brand, double product_price, double product_markup, int product_stock) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_brand = product_brand;
        this.product_price = product_price;
        this.product_markup = product_markup;
        this.product_stock = product_stock;
    }

    public Product() {}

    // Getters and Setters
    public int getProductId() { return product_id; }
    public void setProductId(int product_id) { this.product_id = product_id; }

    public String getProductName() { return product_name; }
    public void setProductName(String product_name) { this.product_name = product_name; }

    public String getProductBrand() { return product_brand; }
    public void setProductBrand(String product_brand) { this.product_brand = product_brand; }

    public double getProductPrice() { return product_price; }
    public void setProductPrice(double product_price) { this.product_price = product_price; }

    public double getProductMarkup() { return product_markup; }
    public void setProductMarkup(double product_markup) { this.product_markup = product_markup; }

    public int getProductStock() { return product_stock; }
    public void setProductStock(int product_stock) { this.product_stock = product_stock; }
}
