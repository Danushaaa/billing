package DAO;

import DBUtil.DatabaseUtil;
import Model.Order;
import Model.Order.OrderItem;
import Model.Item; // Added missing import for Item class
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    public boolean addOrder(Order order) throws SQLException {
        // Check if customer exists
        CustomerDAO customerDAO = new CustomerDAO();
        if (customerDAO.getCustomer(order.getCustomerAccountNumber()) == null) {
            throw new SQLException("Customer with account number " + order.getCustomerAccountNumber() + " does not exist.");
        }

        String insertOrderSql = "INSERT INTO orders (customer_account_number, customer_name, item_code, item_name, quantity, unit_price, total_price) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = DatabaseUtil.getConnection();
        try {
            conn.setAutoCommit(false); // Start transaction
            try (PreparedStatement stmt = conn.prepareStatement(insertOrderSql)) {
                for (OrderItem orderItem : order.getOrderItems()) {
                    // Verify item exists
                    ItemDAO itemDAO = new ItemDAO();
                    Item item = itemDAO.getItem(orderItem.getItemCode());
                    if (item == null) {
                        throw new SQLException("Item with code " + orderItem.getItemCode() + " does not exist.");
                    }
                    if (item.getQuantityInStock() < orderItem.getQuantity()) {
                        throw new SQLException("Insufficient stock for item " + orderItem.getItemCode());
                    }

                    stmt.setString(1, order.getCustomerAccountNumber());
                    stmt.setString(2, order.getCustomerName());
                    stmt.setString(3, orderItem.getItemCode());
                    stmt.setString(4, orderItem.getItemName());
                    stmt.setInt(5, orderItem.getQuantity());
                    stmt.setDouble(6, orderItem.getUnitPrice());
                    stmt.setDouble(7, orderItem.getTotalPrice());
                    stmt.addBatch();
                }
                stmt.executeBatch();
                conn.commit(); // Commit transaction
                return true;
            } catch (SQLException e) {
                conn.rollback(); // Rollback on error
                throw e;
            }
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