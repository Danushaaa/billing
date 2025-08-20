/*
 * Click nbfs://netbeans/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nfs://netbeans/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DBUtil.DatabaseUtil;
import Model.Cashier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CashierDAO {
    public boolean addCashier(Cashier cashier) throws SQLException {
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
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public Cashier getCashier(String cashierId) throws SQLException {
        String sql = "SELECT * FROM cashier WHERE cashier_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cashierId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Cashier(
                    rs.getString("cashier_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("contact"),
                    rs.getString("address"),
                    rs.getString("NIC"),
                    rs.getString("password"),
                    rs.getInt("shift_hours")
                );
            }
        }
        return null;
    }

    public List<Cashier> getAllCashiers() throws SQLException {
        List<Cashier> cashiers = new ArrayList<>();
        String sql = "SELECT * FROM cashier";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                cashiers.add(new Cashier(
                    rs.getString("cashier_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("contact"),
                    rs.getString("address"),
                    rs.getString("NIC"),
                    rs.getString("password"),
                    rs.getInt("shift_hours")
                ));
            }
        }
        return cashiers;
    }

    public boolean updateCashier(Cashier cashier) throws SQLException {
        String sql = "UPDATE cashier SET name = ?, email = ?, contact = ?, address = ?, NIC = ?, password = ?, shift_hours = ? WHERE cashier_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cashier.getName());
            stmt.setString(2, cashier.getEmail());
            stmt.setString(3, cashier.getContact());
            stmt.setString(4, cashier.getAddress());
            stmt.setString(5, cashier.getNIC());
            stmt.setString(6, cashier.getPassword());
            stmt.setInt(7, cashier.getShiftHours());
            stmt.setString(8, cashier.getCashierId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteCashier(String cashierId) throws SQLException {
        String sql = "DELETE FROM cashier WHERE cashier_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cashierId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}