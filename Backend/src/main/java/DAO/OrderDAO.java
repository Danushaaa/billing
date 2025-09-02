package DAO;

import DBUtil.DatabaseUtil;
import Model.Order;
import Model.Order.OrderItem;
import Model.Item;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    public boolean addOrder(Order order) throws SQLException {
        System.out.println("Starting addOrder for customer: " + order.getCustomerAccountNumber());
        System.out.println("Order items: " + order.getOrderItems().size());

        // Check if customer exists
        CustomerDAO customerDAO = new CustomerDAO();
        if (customerDAO.getCustomer(order.getCustomerAccountNumber()) == null) {
            System.out.println("Customer not found: " + order.getCustomerAccountNumber());
            throw new SQLException("Customer with account number " + order.getCustomerAccountNumber() + " does not exist.");
        }

        String insertOrderSql = "INSERT INTO orders (customer_account_number, customer_name, item_code, item_name, quantity, unit_price, total_price) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String selectItemSql = "SELECT quantity_in_stock FROM items WHERE item_code = ?";
        String updateStockSql = "UPDATE items SET quantity_in_stock = quantity_in_stock - ? WHERE item_code = ? AND quantity_in_stock >= ?";

        Connection conn = DatabaseUtil.getConnection();
        try {
            conn.setAutoCommit(false); // Start transaction
            ItemDAO itemDAO = new ItemDAO();

            // Process each order item
            for (OrderItem orderItem : order.getOrderItems()) {
                System.out.println("Processing item: " + orderItem.getItemCode() + ", Quantity: " + orderItem.getQuantity());

                // Verify item exists and get current stock
                try (PreparedStatement selectStmt = conn.prepareStatement(selectItemSql)) {
                    selectStmt.setString(1, orderItem.getItemCode());
                    ResultSet rs = selectStmt.executeQuery();
                    if (!rs.next()) {
                        System.out.println("Item not found: " + orderItem.getItemCode());
                        throw new SQLException("Item with code " + orderItem.getItemCode() + " does not exist.");
                    }
                    int currentStock = rs.getInt("quantity_in_stock");
                    System.out.println("Current stock for " + orderItem.getItemCode() + ": " + currentStock);

                    // Validate stock
                    if (currentStock < orderItem.getQuantity()) {
                        System.out.println("Insufficient stock for " + orderItem.getItemCode() + ". Available: " + currentStock);
                        throw new SQLException("Insufficient stock for item " + orderItem.getItemCode() + ". Available: " + currentStock);
                    }

                    // Insert order item
                    try (PreparedStatement orderStmt = conn.prepareStatement(insertOrderSql)) {
                        orderStmt.setString(1, order.getCustomerAccountNumber());
                        orderStmt.setString(2, order.getCustomerName());
                        orderStmt.setString(3, orderItem.getItemCode());
                        orderStmt.setString(4, orderItem.getItemName());
                        orderStmt.setInt(5, orderItem.getQuantity());
                        orderStmt.setDouble(6, orderItem.getUnitPrice());
                        orderStmt.setDouble(7, orderItem.getTotalPrice());
                        int orderRows = orderStmt.executeUpdate();
                        System.out.println("Order inserted for item " + orderItem.getItemCode() + ": " + orderRows + " row(s) affected");
                    }

                    // Update stock
                    try (PreparedStatement stockStmt = conn.prepareStatement(updateStockSql)) {
                        stockStmt.setInt(1, orderItem.getQuantity());
                        stockStmt.setString(2, orderItem.getItemCode());
                        stockStmt.setInt(3, orderItem.getQuantity());
                        int stockRows = stockStmt.executeUpdate();
                        System.out.println("Stock update for item " + orderItem.getItemCode() + ": " + stockRows + " row(s) affected");
                        if (stockRows != 1) {
                            System.out.println("Stock update failed for item: " + orderItem.getItemCode());
                            throw new SQLException("Failed to update stock for item: " + orderItem.getItemCode());
                        }
                    }
                }
            }

            conn.commit(); // Commit transaction
            System.out.println("Transaction committed successfully.");
            return true;
        } catch (SQLException e) {
            System.err.println("Error during transaction: " + e.getMessage());
            conn.rollback(); // Rollback on error
            throw e;
        } finally {
            conn.setAutoCommit(true); // Restore auto-commit
            if (conn != null) conn.close();
        }
    }

    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders ORDER BY order_date DESC";
        Connection conn = DatabaseUtil.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            Order order = null;
            String currentCustomerAccountNumber = "";
            while (rs.next()) {
                if (!rs.getString("customer_account_number").equals(currentCustomerAccountNumber)) {
                    if (order != null) orders.add(order);
                    order = new Order();
                    order.setOrderId(rs.getInt("order_id"));
                    order.setCustomerAccountNumber(rs.getString("customer_account_number"));
                    order.setCustomerName(rs.getString("customer_name"));
                    order.setTotalAmount(rs.getDouble("total_price"));
                    order.setOrderItems(new ArrayList<>());
                    currentCustomerAccountNumber = rs.getString("customer_account_number");
                }
                OrderItem item = new OrderItem();
                item.setOrderId(rs.getInt("order_id"));
                item.setItemCode(rs.getString("item_code"));
                item.setItemName(rs.getString("item_name"));
                item.setQuantity(rs.getInt("quantity"));
                item.setUnitPrice(rs.getDouble("unit_price"));
                item.setTotalPrice(rs.getDouble("total_price"));
                order.getOrderItems().add(item);
                order.setTotalAmount(order.getTotalAmount() + item.getTotalPrice());
            }
            if (order != null) orders.add(order);
        }
        return orders;
    }
}