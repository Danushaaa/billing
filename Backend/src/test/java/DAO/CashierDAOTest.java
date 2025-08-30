/*
 * Click nbfs://netbeans/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nfs://netbeans/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.Cashier;
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

public class CashierDAOTest {

    private CashierDAO cashierDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        cashierDAO = new CashierDAO();

        // Clear the cashier table to avoid duplicate entries
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM cashier")) {
            stmt.executeUpdate();
        }

        // Create cashier table if it doesn't exist
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS cashier (cashier_id VARCHAR(50) PRIMARY KEY, name VARCHAR(100), email VARCHAR(100), contact VARCHAR(20), address VARCHAR(200), NIC VARCHAR(20), password VARCHAR(50), shift_hours INT)")) {
            stmt.executeUpdate();
        }

        // Insert test cashiers
        List<Cashier> testCashiers = new ArrayList<>();
        testCashiers.add(new Cashier("C001", "John Doe", "john@example.com", "1234567890", "123 Main St", "NIC001", "pass123", 8));
        testCashiers.add(new Cashier("C002", "Jane Smith", "jane@example.com", "0987654321", "456 Elm St", "NIC002", "secure456", 6));

        // Insert test cashiers into the database
        for (Cashier cashier : testCashiers) {
            String sql = "INSERT INTO cashier (cashier_id, name, email, contact, address, NIC, password, shift_hours) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, cashier.getCashierId());
                stmt.setString(2, cashier.getName());
                stmt.setString(3, cashier.getEmail());
                stmt.setString(4, cashier.getContact());
                stmt.setString(5, cashier.getAddress());
                stmt.setString(6, cashier.getNIC());
                stmt.setString(7, cashier.getPassword());
                stmt.setInt(8, cashier.getShiftHours());
                stmt.executeUpdate();
            }
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Clean up: Delete all test cashiers without dropping the table
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM cashier")) {
            stmt.executeUpdate();
        }
    }

    @Test
    public void testAddCashierSuccess() throws SQLException {
        System.out.println("testAddCashierSuccess");
        Cashier newCashier = new Cashier("C003", "Alice Brown", "alice@example.com", "1112223333", "789 Oak St", "NIC003", "newpass789", 7);
        boolean added = cashierDAO.addCashier(newCashier);
        assertTrue(added, "Cashier should be added successfully");

        // Verify the cashier was added
        Cashier retrieved = cashierDAO.getCashier("C003");
        assertNotNull(retrieved, "Added cashier should be retrievable");
        assertEquals("C003", retrieved.getCashierId(), "Cashier ID should match");
        assertEquals("Alice Brown", retrieved.getName(), "Name should match");
        assertEquals("alice@example.com", retrieved.getEmail(), "Email should match");
        assertEquals("1112223333", retrieved.getContact(), "Contact should match");
        assertEquals("789 Oak St", retrieved.getAddress(), "Address should match");
        assertEquals("NIC003", retrieved.getNIC(), "NIC should match");
        assertEquals("newpass789", retrieved.getPassword(), "Password should match");
        assertEquals(7, retrieved.getShiftHours(), "Shift hours should match");
    }

    @Test
    public void testAddCashierDuplicateId() throws SQLException {
        System.out.println("testAddCashierDuplicateId");
        Cashier duplicateCashier = new Cashier("C001", "Duplicate Name", "duplicate@example.com", "5555555555", "999 Pine St", "NIC999", "pass999", 5);
        assertThrows(SQLException.class, () -> cashierDAO.addCashier(duplicateCashier), "Adding cashier with duplicate ID should throw SQLException");
    }

    @Test
    public void testGetCashierSuccess() throws SQLException {
        System.out.println("testGetCashierSuccess");
        Cashier cashier = cashierDAO.getCashier("C001");
        assertNotNull(cashier, "Cashier should be retrieved successfully");
        assertEquals("C001", cashier.getCashierId(), "Cashier ID should match");
        assertEquals("John Doe", cashier.getName(), "Name should match");
        assertEquals("john@example.com", cashier.getEmail(), "Email should match");
        assertEquals("1234567890", cashier.getContact(), "Contact should match");
        assertEquals("123 Main St", cashier.getAddress(), "Address should match");
        assertEquals("NIC001", cashier.getNIC(), "NIC should match");
        assertEquals("pass123", cashier.getPassword(), "Password should match");
        assertEquals(8, cashier.getShiftHours(), "Shift hours should match");
    }

    @Test
    public void testGetCashierInvalidId() throws SQLException {
        System.out.println("testGetCashierInvalidId");
        Cashier cashier = cashierDAO.getCashier("C999");
        assertNull(cashier, "Should return null for invalid cashier ID");
    }

    @Test
    public void testGetAllCashiers() throws SQLException {
        System.out.println("testGetAllCashiers");
        List<Cashier> cashiers = cashierDAO.getAllCashiers();
        assertEquals(2, cashiers.size(), "Should retrieve exactly 2 cashiers");
        assertTrue(cashiers.stream().anyMatch(c -> c.getCashierId().equals("C001")), "Cashier C001 should be in the list");
        assertTrue(cashiers.stream().anyMatch(c -> c.getCashierId().equals("C002")), "Cashier C002 should be in the list");
    }

    @Test
    public void testUpdateCashierSuccess() throws SQLException {
        System.out.println("testUpdateCashierSuccess");
        Cashier updatedCashier = new Cashier("C001", "John Updated", "john.updated@example.com", "9998887777", "456 New St", "NIC001", "newpass123", 10);
        boolean updated = cashierDAO.updateCashier(updatedCashier);
        assertTrue(updated, "Cashier should be updated successfully");

        // Verify the update
        Cashier retrieved = cashierDAO.getCashier("C001");
        assertNotNull(retrieved, "Updated cashier should be retrievable");
        assertEquals("John Updated", retrieved.getName(), "Name should match");
        assertEquals("john.updated@example.com", retrieved.getEmail(), "Email should match");
        assertEquals("9998887777", retrieved.getContact(), "Contact should match");
        assertEquals("456 New St", retrieved.getAddress(), "Address should match");
        assertEquals("newpass123", retrieved.getPassword(), "Password should match");
        assertEquals(10, retrieved.getShiftHours(), "Shift hours should match");
    }

    @Test
    public void testUpdateCashierNonExistent() throws SQLException {
        System.out.println("testUpdaterelliCashierNonExistent");
        Cashier nonExistentCashier = new Cashier("C999", "Non Existent", "non@example.com", "0000000000", "No Address", "NIC999", "nopass", 4);
        boolean updated = cashierDAO.updateCashier(nonExistentCashier);
        assertFalse(updated, "Updating non-existent cashier should return false");
    }

    @Test
    public void testDeleteCashierSuccess() throws SQLException {
        System.out.println("testDeleteCashierSuccess");
        boolean deleted = cashierDAO.deleteCashier("C001");
        assertTrue(deleted, "Cashier should be deleted successfully");

        // Verify deletion
        Cashier retrieved = cashierDAO.getCashier("C001");
        assertNull(retrieved, "Deleted cashier should not be retrievable");
    }

    @Test
    public void testDeleteCashierNonExistent() throws SQLException {
        System.out.println("testDeleteCashierNonExistent");
        boolean deleted = cashierDAO.deleteCashier("C999");
        assertFalse(deleted, "Deleting non-existent cashier should return false");
    }

    @Test
    public void testAuthenticateSuccess() throws SQLException {
        System.out.println("testAuthenticateSuccess");
        Cashier cashier = cashierDAO.authenticate("C001", "pass123");
        assertNotNull(cashier, "Cashier should be authenticated successfully");
        assertEquals("C001", cashier.getCashierId(), "Cashier ID should match");
        assertEquals("pass123", cashier.getPassword(), "Password should match");
    }

    @Test
    public void testAuthenticateInvalidCashierId() throws SQLException {
        System.out.println("testAuthenticateInvalidCashierId");
        Cashier cashier = cashierDAO.authenticate("C999", "pass123");
        assertNull(cashier, "Authentication should fail for invalid cashier ID");
    }

    @Test
    public void testAuthenticateInvalidPassword() throws SQLException {
        System.out.println("testAuthenticateInvalidPassword");
        Cashier cashier = cashierDAO.authenticate("C001", "wrongpass");
        assertNull(cashier, "Authentication should fail for invalid password");
    }

    @Test
    public void testAuthenticateEmptyCredentials() throws SQLException {
        System.out.println("testAuthenticateEmptyCredentials");
        Cashier cashier = cashierDAO.authenticate("", "");
        assertNull(cashier, "Authentication should fail for empty credentials");
    }

    @Test
    public void testAuthenticateNullCredentials() throws SQLException {
        System.out.println("testAuthenticateNullCredentials");
        Cashier cashier = cashierDAO.authenticate(null, null);
        assertNull(cashier, "Authentication should fail for null credentials");
    }
}