package controller;

import model.User;
import db.DatabaseConnection;
import javax.swing.JOptionPane;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserController {
    private User currentUser;

    public User authenticate(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ? AND active = 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                currentUser = mapResultSetToUser(rs);
                System.out.println("✅ Usuario autenticado: " + currentUser.getName());
                return currentUser;
            } else {
                System.out.println("❌ Login fallido para: " + email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        currentUser = null;
        System.out.println("🔓 Sesión cerrada");
    }

    public User findUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean addUser(String name, String email, String password, boolean active) {
        String sql = "INSERT INTO users (name, email, password, active, role) VALUES (?, ?, ?, ?, 'USER')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setBoolean(4, active);
            int result = pstmt.executeUpdate();
            if (result > 0) {
                System.out.println("✅ Usuario agregado: " + email);
                return true;
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE")) {
                JOptionPane.showMessageDialog(null, "El email ya existe");
            } else {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean updateUser(int id, String name, String email, boolean active, String role) {
        String sql = "UPDATE users SET name = ?, email = ?, active = ?, role = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setBoolean(3, active);
            pstmt.setString(4, role);
            pstmt.setInt(5, id);
            int result = pstmt.executeUpdate();
            if (result > 0) {
                System.out.println("✅ Usuario actualizado: " + email);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteUser(int id) {
        User userToDelete = findUserById(id);
        if (userToDelete != null && userToDelete.isAdmin() && userToDelete.getEmail().equals("admin@demo.com")) {
            JOptionPane.showMessageDialog(null, "No se puede eliminar al administrador principal");
            return false;
        }
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int result = pstmt.executeUpdate();
            if (result > 0) {
                System.out.println("✅ Usuario eliminado ID: " + id);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean changePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, userId);
            int result = pstmt.executeUpdate();
            if (result > 0) {
                System.out.println("✅ Contraseña cambiada para usuario ID: " + userId);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getBoolean("active"),
                rs.getString("role")
        );
    }
}