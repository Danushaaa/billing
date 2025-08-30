/*
 * Click nbfs://netbeans/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nfs://netbeans/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.Customer;
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

public class CustomerDAOTest {

    private CustomerDAO customerDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        customerDAO = new CustomerDAO();

        // Clear the orders table first to avoid foreign key constraint violations
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM orders")) {
            stmt.executeUpdate();
        }

        // Clear the customer table
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM customer")) {
            stmt.executeUpdate();
        }

        // Create customer table if it doesn't exist
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS customer (account_number VARCHAR(50) PRIMARY KEY, name VARCHAR(100), email VARCHAR(100), contact VARCHAR(20), address VARCHAR(200), NIC VARCHAR(20), password VARCHAR(50), units_consumed INT)")) {
            stmt.executeUpdate();
        }

        // Insert test customers
        List<Customer> testCustomers = new ArrayList<>();
        testCustomers.add(new Customer("A001", "John Doe", "john@example.com", "1234567890", "123 Main St", "NIC001", "pass123", 100));
        testCustomers.add(new Customer("A002", "Jane Smith", "jane@example.com", "0987654321", "456 Elm St", "NIC002", "secure456", 200));

        // Insert test customers into the database
        for (Customer customer : testCustomers) {
            String sql = "INSERT INTO customer (account_number, name, email, contact, address, NIC, password, units_consumed) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, customer.getAccountNumber());
                stmt.setString(2, customer.getName());
                stmt.setString(3, customer.getEmail());
                stmt.setString(4, customer.getContact());
                stmt.setString(5, customer.getAddress());
                stmt.setString(6, customer.getNIC());
                stmt.setString(7, customer.getPassword());
                stmt.setInt(8, customer.getUnitsConsumed());
                stmt.executeUpdate();
            }
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Clean up: Delete all records from orders table first
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM orders")) {
            stmt.executeUpdate();
        }

        // Clean up: Delete all records from customer table
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM customer")) {
            stmt.executeUpdate();
        }
    }

    @Test
    public void testAddCustomerSuccess() throws SQLException {
        System.out.println("testAddCustomerSuccess");
        Customer newCustomer = new Customer("A003", "Alice Brown", "alice@example.com", "1112223333", "789 Oak St", "NIC003", "newpass789", 150);
        boolean added = customerDAO.addCustomer(newCustomer);
        assertTrue(added, "Customer should be added successfully");

        // Verify the customer was added
        Customer retrieved = customerDAO.getCustomer("A003");
        assertNotNull(retrieved, "Added customer should be retrievable");
        assertEquals("A003", retrieved.getAccountNumber(), "Account number should match");
        assertEquals("Alice Brown", retrieved.getName(), "Name should match");
        assertEquals("alice@example.com", retrieved.getEmail(), "Email should match");
        assertEquals("1112223333", retrieved.getContact(), "Contact should match");
        assertEquals("789 Oak St", retrieved.getAddress(), "Address should match");
        assertEquals("NIC003", retrieved.getNIC(), "NIC should match");
        assertEquals("newpass789", retrieved.getPassword(), "Password should match");
        assertEquals(150, retrieved.getUnitsConsumed(), "Units consumed should match");
    }

    @Test
    public void testAddCustomerDuplicateAccountNumber() throws SQLException {
        System.out.println("testAddCustomerDuplicateAccountNumber");
        Customer duplicateCustomer = new Customer("A001", "Duplicate Name", "duplicate@example.com", "5555555555", "999 Pine St", "NIC999", "pass999", 300);
        assertThrows(SQLException.class, () -> customerDAO.addCustomer(duplicateCustomer), "Adding customer with duplicate account number should throw SQLException");
    }

    @Test
    public void testGetCustomerSuccess() throws SQLException {
        System.out.println("testGetCustomerSuccess");
        Customer customer = customerDAO.getCustomer("A001");
        assertNotNull(customer, "Customer should be retrieved successfully");
        assertEquals("A001", customer.getAccountNumber(), "Account number should match");
        assertEquals("John Doe", customer.getName(), "Name should match");
        assertEquals("john@example.com", customer.getEmail(), "Email should match");
        assertEquals("1234567890", customer.getContact(), "Contact should match");
        assertEquals("123 Main St", customer.getAddress(), "Address should match");
        assertEquals("NIC001", customer.getNIC(), "NIC should match");
        assertEquals("pass123", customer.getPassword(), "Password should match");
        assertEquals(100, customer.getUnitsConsumed(), "Units consumed should match");
    }

    @Test
    public void testGetCustomerInvalidAccountNumber() throws SQLException {
        System.out.println("testGetCustomerInvalidAccountNumber");
        Customer customer = customerDAO.getCustomer("A999");
        assertNull(customer, "Should return null for invalid account number");
    }

    @Test
    public void testGetAllCustomers() throws SQLException {
        System.out.println("testGetAllCustomers");
        List<Customer> customers = customerDAO.getAllCustomers();
        assertEquals(2, customers.size(), "Should retrieve exactly 2 customers");
        assertTrue(customers.stream().anyMatch(c -> c.getAccountNumber().equals("A001")), "Customer A001 should be in the list");
        assertTrue(customers.stream().anyMatch(c -> c.getAccountNumber().equals("A002")), "Customer A002 should be in the list");
    }

    @Test
    public void testUpdateCustomerSuccess() throws SQLException {
        System.out.println("testUpdateCustomerSuccess");
        Customer updatedCustomer = new Customer("A001", "John Updated", "john.updated@example.com", "9998887777", "456 New St", "NIC001", "newpass123", 250);
        boolean updated = customerDAO.updateCustomer(updatedCustomer);
        assertTrue(updated, "Customer should be updated successfully");

        // Verify the update
        Customer retrieved = customerDAO.getCustomer("A001");
        assertNotNull(retrieved, "Updated customer should be retrievable");
        assertEquals("John Updated", retrieved.getName(), "Name should match");
        assertEquals("john.updated@example.com", retrieved.getEmail(), "Email should match");
        assertEquals("9998887777", retrieved.getContact(), "Contact should match");
        assertEquals("456 New St", retrieved.getAddress(), "Address should match");
        assertEquals("newpass123", retrieved.getPassword(), "Password should match");
        assertEquals(250, retrieved.getUnitsConsumed(), "Units consumed should match");
    }

    @Test
    public void testUpdateCustomerNonExistent() throws SQLException {
        System.out.println("testUpdateCustomerNonExistent");
        Customer nonExistentCustomer = new Customer("A999", "Non Existent", "non@example.com", "0000000000", "No Address", "NIC999", "nopass", 400);
        boolean updated = customerDAO.updateCustomer(nonExistentCustomer);
        assertFalse(updated, "Updating non-existent customer should return false");
    }

    @Test
    public void testDeleteCustomerSuccess() throws SQLException {
        System.out.println("testDeleteCustomerSuccess");
        // Ensure no related orders exist for A001
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM orders WHERE customer_account_number = ?")) {
            stmt.setString(1, "A001");
            stmt.executeUpdate();
        }

        boolean deleted = customerDAO.deleteCustomer("A001");
        assertTrue(deleted, "Customer should be deleted successfully");

        // Verify deletion
        Customer retrieved = customerDAO.getCustomer("A001");
        assertNull(retrieved, "Deleted customer should not be retrievable");
    }

    @Test
    public void testDeleteCustomerNonExistent() throws SQLException {
        System.out.println("testDeleteCustomerNonExistent");
        boolean deleted = customerDAO.deleteCustomer("A999");
        assertFalse(deleted, "Deleting non-existent customer should return false");
    }
}