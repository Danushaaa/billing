/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/*
 * Click nfs://netbeans/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nfs://netbeans/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DBUtil.DatabaseUtil;
import Model.Customer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    public boolean addCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO customer (account_number, nme, email, contact, address, NIC, password, units_consumed) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
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
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public Customer getCustomer(String accountNumber) throws SQLException {
        String sql = "SELECT * FROM customer WHERE account_number = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Customer(
                    rs.getString("account_number"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("contact"),
                    rs.getString("address"),
                    rs.getString("NIC"),
                    rs.getString("password"),
                    rs.getInt("units_consumed")
                );
            }
        }
        return null;
    }

    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customer";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                customers.add(new Customer(
                    rs.getString("account_number"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("contact"),
                    rs.getString("address"),
                    rs.getString("NIC"),
                    rs.getString("password"),
                    rs.getInt("units_consumed")
                ));
            }
        }
        return customers;
    }

    public boolean updateCustomer(Customer customer) throws SQLException {
        String sql = "UPDATE customer SET name = ?, email = ?, contact = ?, address = ?, NIC = ?, password = ?, units_consumed = ? WHERE account_number = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getEmail());
            stmt.setString(3, customer.getContact());
            stmt.setString(4, customer.getAddress());
            stmt.setString(5, customer.getNIC());
            stmt.setString(6, customer.getPassword());
            stmt.setInt(7, customer.getUnitsConsumed());
            stmt.setString(8, customer.getAccountNumber());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteCustomer(String accountNumber) throws SQLException {
        String sql = "DELETE FROM customer WHERE account_number = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}