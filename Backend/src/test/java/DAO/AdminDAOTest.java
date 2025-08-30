/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.Admin;
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

public class AdminDAOTest {

    private AdminDAO adminDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        adminDAO = new AdminDAO();

        // Clear the admin table to avoid duplicate entries
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM admin")) {
            stmt.executeUpdate();
        }

        // Set up in-memory database and create admin table
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS admin (username VARCHAR(50) PRIMARY KEY, password VARCHAR(50))")) {
            stmt.executeUpdate();
        }

        // Create test admin users
        List<Admin> testAdmins = new ArrayList<>();
        testAdmins.add(new Admin("admin1", "pass123"));
        testAdmins.add(new Admin("admin2", "secure456"));

        // Insert test admins into the database
        for (Admin admin : testAdmins) {
            String sql = "INSERT INTO admin (username, password) VALUES (?, ?)";
            try (Connection conn = DatabaseUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, admin.getUsername());
                stmt.setString(2, admin.getPassword());
                stmt.executeUpdate();
            }
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Clean up: Delete all test admins without dropping the table
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM admin")) {
            stmt.executeUpdate();
        }
    }

    @Test
    public void testAuthenticateSuccess() throws SQLException {
        System.out.println("testAuthenticateSuccess");
        Admin admin = adminDAO.authenticate("admin1", "pass123");
        assertNotNull(admin, "Admin should be authenticated successfully");
        assertEquals("admin1", admin.getUsername(), "Username should match");
        assertEquals("pass123", admin.getPassword(), "Password should match");
    }

    @Test
    public void testAuthenticateSecondAdminSuccess() throws SQLException {
        System.out.println("testAuthenticateSecondAdminSuccess");
        Admin admin = adminDAO.authenticate("admin2", "secure456");
        assertNotNull(admin, "Second admin should be authenticated successfully");
        assertEquals("admin2", admin.getUsername(), "Username should match");
        assertEquals("secure456", admin.getPassword(), "Password should match");
    }

    @Test
    public void testAuthenticateInvalidUsername() throws SQLException {
        System.out.println("testAuthenticateInvalidUsername");
        Admin admin = adminDAO.authenticate("nonexistent", "pass123");
        assertNull(admin, "Authentication should fail for invalid username");
    }

    @Test
    public void testAuthenticateInvalidPassword() throws SQLException {
        System.out.println("testAuthenticateInvalidPassword");
        Admin admin = adminDAO.authenticate("admin1", "wrongPass");
        assertNull(admin, "Authentication should fail for invalid password");
    }

    @Test
    public void testAuthenticateEmptyCredentials() throws SQLException {
        System.out.println("testAuthenticateEmptyCredentials");
        Admin admin = adminDAO.authenticate("", "");
        assertNull(admin, "Authentication should fail for empty credentials");
    }

    @Test
    public void testAuthenticateNullCredentials() throws SQLException {
        System.out.println("testAuthenticateNullCredentials");
        Admin admin = adminDAO.authenticate(null, null);
        assertNull(admin, "Authentication should fail for null credentials");
    }
}