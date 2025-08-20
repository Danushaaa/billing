package Model;

public class Item {
    private String itemCode;
    private String itemName;
    private double unitPrice;
    private int quantityInStock;

    public Item() {}

    public Item(String itemCode, String itemName, double unitPrice, int quantityInStock) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.unitPrice = unitPrice;
        this.quantityInStock = quantityInStock;
    }

    // Getters and Setters
    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    public int getQuantityInStock() { return quantityInStock; }
    public void setQuantityInStock(int quantityInStock) { this.quantityInStock = quantityInStock; }
}