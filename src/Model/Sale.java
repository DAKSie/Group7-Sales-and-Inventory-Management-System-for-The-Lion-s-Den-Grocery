package Model;

public class Sale {
    private int sale_id;
    private int product_id;
    private String sale_item;
    private double sale_price;
    private int sale_quantity;
    private double sale_total;
    private String sale_user;

    public Sale(int sale_id, int product_id, String sale_item, double sale_price, int sale_quantity, double sale_total, String sale_user) {
        this.sale_id = sale_id;
        this.product_id = product_id;
        this.sale_item = sale_item;
        this.sale_price = sale_price;
        this.sale_quantity = sale_quantity;
        this.sale_total = sale_total;
        this.sale_user = sale_user;
    }

    public Sale() {}

    // Getters and Setters
    public int getSaleId() { return sale_id; }
    public void setSaleId(int sale_id) { this.sale_id = sale_id; }

    public int getProductId() { return product_id; }
    public void setProductId(int product_id) { this.product_id = product_id; }

    public String getSaleItem() { return sale_item; }
    public void setSaleItem(String sale_item) { this.sale_item = sale_item; }

    public double getSalePrice() { return sale_price; }
    public void setSalePrice(double sale_price) { this.sale_price = sale_price; }

    public int getSaleQuantity() { return sale_quantity; }
    public void setSaleQuantity(int sale_quantity) { this.sale_quantity = sale_quantity; }

    public double getSaleTotal() { return sale_total; }
    public void setSaleTotal(double sale_total) { this.sale_total = sale_total; }

    public String getSaleUser() { return sale_user; }
    public void setSaleUser(String sale_user) { this.sale_user = sale_user; }

    // Utility method to calculate total automatically
    public void calculateTotal() {
        this.sale_total = this.sale_price * this.sale_quantity;
    }
}
