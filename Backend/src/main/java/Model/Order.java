package Model;

import java.util.List;

public class Order {
    private int orderId;
    private String customerAccountNumber;
    private String customerName;
    private List<OrderItem> orderItems;
    private double totalAmount;

    public Order() {}

    public Order(String customerAccountNumber, String customerName, List<OrderItem> orderItems, double totalAmount) {
        this.customerAccountNumber = customerAccountNumber;
        this.customerName = customerName;
        this.orderItems = orderItems;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public String getCustomerAccountNumber() { return customerAccountNumber; }
    public void setCustomerAccountNumber(String customerAccountNumber) { this.customerAccountNumber = customerAccountNumber; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public List<OrderItem> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    // Inner class for OrderItem
    public static class OrderItem {
        private int orderId;
        private String itemCode;
        private String itemName;
        private int quantity;
        private double unitPrice;
        private double totalPrice;

        public OrderItem() {}

        public OrderItem(String itemCode, String itemName, int quantity, double unitPrice, double totalPrice) {
            this.itemCode = itemCode;
            this.itemName = itemName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.totalPrice = totalPrice;
        }

        // Getters and Setters
        public int getOrderId() { return orderId; }
        public void setOrderId(int orderId) { this.orderId = orderId; }
        public String getItemCode() { return itemCode; }
        public void setItemCode(String itemCode) { this.itemCode = itemCode; }
        public String getItemName() { return itemName; }
        public void setItemName(String itemName) { this.itemName = itemName; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public double getUnitPrice() { return unitPrice; }
        public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
        public double getTotalPrice() { return totalPrice; }
        public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    }
}