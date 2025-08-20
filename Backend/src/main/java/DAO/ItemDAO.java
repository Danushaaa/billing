package DAO;

import DBUtil.DatabaseUtil;
import Model.Item;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {
    public boolean addItem(Item item) throws SQLException {
        String sql = "INSERT INTO items (item_code, item_name, unit_price, quantity_in_stock) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, item.getItemCode());
            stmt.setString(2, item.getItemName());
            stmt.setDouble(3, item.getUnitPrice());
            stmt.setInt(4, item.getQuantityInStock());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public Item getItem(String itemCode) throws SQLException {
        String sql = "SELECT * FROM items WHERE item_code = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, itemCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Item(
                    rs.getString("item_code"),
                    rs.getString("item_name"),
                    rs.getDouble("unit_price"),
                    rs.getInt("quantity_in_stock")
                );
            }
        }
        return null;
    }

    public List<Item> getAllItems() throws SQLException {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                items.add(new Item(
                    rs.getString("item_code"),
                    rs.getString("item_name"),
                    rs.getDouble("unit_price"),
                    rs.getInt("quantity_in_stock")
                ));
            }
        }
        return items;
    }

    public boolean updateItem(Item item) throws SQLException {
        String sql = "UPDATE items SET item_name = ?, unit_price = ?, quantity_in_stock = ? WHERE item_code = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, item.getItemName());
            stmt.setDouble(2, item.getUnitPrice());
            stmt.setInt(3, item.getQuantityInStock());
            stmt.setString(4, item.getItemCode());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteItem(String itemCode) throws SQLException {
        String sql = "DELETE FROM items WHERE item_code = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, itemCode);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}