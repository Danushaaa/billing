/*
 * Click nbfs://netbeans/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nfs://netbeans/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.Item;
import DBUtil.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ItemDAOTest {

    private ItemDAO itemDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        itemDAO = new ItemDAO();

        // Clear the items table
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM items")) {
            stmt.executeUpdate();
        }

        // Create items table if it doesn't exist
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS items (item_code VARCHAR(50) PRIMARY KEY, item_name VARCHAR(100), unit_price DOUBLE, quantity_in_stock INT)")) {
            stmt.executeUpdate();
        }

        // Insert test items
        List<Item> testItems = new ArrayList<>();
        testItems.add(new Item("I001", "Laptop", 999.99, 10));
        testItems.add(new Item("I002", "Mouse", 29.99, 50));

        // Insert test items into the database
        for (Item item : testItems) {
            String sql = "INSERT INTO items (item_code, item_name, unit_price, quantity_in_stock) VALUES (?, ?, ?, ?)";
            try (Connection conn = DatabaseUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, item.getItemCode());
                stmt.setString(2, item.getItemName());
                stmt.setDouble(3, item.getUnitPrice());
                stmt.setInt(4, item.getQuantityInStock());
                stmt.executeUpdate();
            }
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Clean up: Delete all records from items table
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM items")) {
            stmt.executeUpdate();
        }
    }

    @Test
    public void testAddItemSuccess() throws SQLException {
        System.out.println("testAddItemSuccess");
        Item newItem = new Item("I003", "Keyboard", 49.99, 20);
        boolean added = itemDAO.addItem(newItem);
        assertTrue(added, "Item should be added successfully");

        // Verify the item was added
        Item retrieved = itemDAO.getItem("I003");
        assertNotNull(retrieved, "Added item should be retrievable");
        assertEquals("I003", retrieved.getItemCode(), "Item code should match");
        assertEquals("Keyboard", retrieved.getItemName(), "Item name should match");
        assertEquals(49.99, retrieved.getUnitPrice(), 0.01, "Unit price should match");
        assertEquals(20, retrieved.getQuantityInStock(), "Quantity in stock should match");
    }

    @Test
    public void testAddItemDuplicateItemCode() throws SQLException {
        System.out.println("testAddItemDuplicateItemCode");
        Item duplicateItem = new Item("I001", "Duplicate Laptop", 1999.99, 5);
        assertThrows(SQLException.class, () -> itemDAO.addItem(duplicateItem), "Adding item with duplicate item code should throw SQLException");
    }

    @Test
    public void testGetItemSuccess() throws SQLException {
        System.out.println("testGetItemSuccess");
        Item item = itemDAO.getItem("I001");
        assertNotNull(item, "Item should be retrieved successfully");
        assertEquals("I001", item.getItemCode(), "Item code should match");
        assertEquals("Laptop", item.getItemName(), "Item name should match");
        assertEquals(999.99, item.getUnitPrice(), 0.01, "Unit price should match");
        assertEquals(10, item.getQuantityInStock(), "Quantity in stock should match");
    }

    @Test
    public void testGetItemInvalidItemCode() throws SQLException {
        System.out.println("testGetItemInvalidItemCode");
        Item item = itemDAO.getItem("I999");
        assertNull(item, "Should return null for invalid item code");
    }

    @Test
    public void testGetAllItems() throws SQLException {
        System.out.println("testGetAllItems");
        List<Item> items = itemDAO.getAllItems();
        assertEquals(2, items.size(), "Should retrieve exactly 2 items");
        assertTrue(items.stream().anyMatch(i -> i.getItemCode().equals("I001")), "Item I001 should be in the list");
        assertTrue(items.stream().anyMatch(i -> i.getItemCode().equals("I002")), "Item I002 should be in the list");
    }

    @Test
    public void testUpdateItemSuccess() throws SQLException {
        System.out.println("testUpdateItemSuccess");
        Item updatedItem = new Item("I001", "Updated Laptop", 1099.99, 15);
        boolean updated = itemDAO.updateItem(updatedItem);
        assertTrue(updated, "Item should be updated successfully");

        // Verify the update
        Item retrieved = itemDAO.getItem("I001");
        assertNotNull(retrieved, "Updated item should be retrievable");
        assertEquals("Updated Laptop", retrieved.getItemName(), "Item name should match");
        assertEquals(1099.99, retrieved.getUnitPrice(), 0.01, "Unit price should match");
        assertEquals(15, retrieved.getQuantityInStock(), "Quantity in stock should match");
    }

    @Test
    public void testUpdateItemNonExistent() throws SQLException {
        System.out.println("testUpdateItemNonExistent");
        Item nonExistentItem = new Item("I999", "Non Existent", 99.99, 100);
        boolean updated = itemDAO.updateItem(nonExistentItem);
        assertFalse(updated, "Updating non-existent item should return false");
    }

    @Test
    public void testDeleteItemSuccess() throws SQLException {
        System.out.println("testDeleteItemSuccess");
        boolean deleted = itemDAO.deleteItem("I001");
        assertTrue(deleted, "Item should be deleted successfully");

        // Verify deletion
        Item retrieved = itemDAO.getItem("I001");
        assertNull(retrieved, "Deleted item should not be retrievable");
    }

    @Test
    public void testDeleteItemNonExistent() throws SQLException {
        System.out.println("testDeleteItemNonExistent");
        boolean deleted = itemDAO.deleteItem("I999");
        assertFalse(deleted, "Deleting non-existent item should return false");
    }
}