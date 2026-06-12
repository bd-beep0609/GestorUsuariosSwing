package controller;

import model.Nota;
import db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotaController {

    // Obtener todas las notas
    public List<Nota> getAllNotas() {
        List<Nota> notas = new ArrayList<>();
        String sql = "SELECT * FROM notas ORDER BY fecha DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                notas.add(mapResultSetToNota(rs));
            }
            System.out.println("📝 Cargadas " + notas.size() + " notas");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notas;
    }

    // Obtener notas por estudiante
    public List<Nota> getNotasByEstudiante(int estudianteId) {
        List<Nota> notas = new ArrayList<>();
        String sql = "SELECT * FROM notas WHERE estudiante_id = ? ORDER BY fecha DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, estudianteId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                notas.add(mapResultSetToNota(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notas;
    }

    // Calcular promedio por estudiante
    public double getPromedioByEstudiante(int estudianteId) {
        String sql = "SELECT AVG(CAST(valor AS DECIMAL(3,1))) as promedio FROM notas WHERE estudiante_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, estudianteId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                double promedio = rs.getDouble("promedio");
                return Math.round(promedio * 100.0) / 100.0; // Redondear a 2 decimales
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Agregar nueva nota
    public boolean addNota(int estudianteId, int materiaId, double valor, String observacion) {
        // Validar nota
        if (valor < 0 || valor > 10) {
            System.err.println("❌ Nota inválida: " + valor + " (debe ser entre 0 y 10)");
            return false;
        }

        String sql = "INSERT INTO notas (estudiante_id, materia_id, valor, observacion) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, estudianteId);
            pstmt.setInt(2, materiaId);
            pstmt.setDouble(3, valor);
            pstmt.setString(4, observacion != null ? observacion : "");
            int resultado = pstmt.executeUpdate();
            if (resultado > 0) {
                System.out.println("✅ Nota registrada: Estudiante ID=" + estudianteId + " | Nota=" + valor);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Eliminar nota
    public boolean deleteNota(int id) {
        String sql = "DELETE FROM notas WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int resultado = pstmt.executeUpdate();
            if (resultado > 0) {
                System.out.println("✅ Nota eliminada ID: " + id);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Mapear ResultSet a objeto Nota
    private Nota mapResultSetToNota(ResultSet rs) throws SQLException {
        return new Nota(
                rs.getInt("id"),
                rs.getInt("estudiante_id"),
                rs.getInt("materia_id"),
                rs.getDouble("valor"),
                rs.getDate("fecha"),
                rs.getString("observacion")
        );
    }
}