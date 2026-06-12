package controller;

import model.Estudiante;
import db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstudianteController {

    // Obtener todos los estudiantes
    public List<Estudiante> getAllEstudiantes() {
        List<Estudiante> estudiantes = new ArrayList<>();
        String sql = "SELECT * FROM estudiantes ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                estudiantes.add(mapResultSetToEstudiante(rs));
            }
            System.out.println("📚 Cargados " + estudiantes.size() + " estudiantes");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estudiantes;
    }

    // Obtener estudiantes por carrera
    public List<Estudiante> getEstudiantesByCarrera(String carrera) {
        List<Estudiante> result = new ArrayList<>();
        String sql = "SELECT * FROM estudiantes WHERE carrera = ? AND activo = 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, carrera);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result.add(mapResultSetToEstudiante(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // Obtener solo estudiantes activos
    public List<Estudiante> getEstudiantesActivos() {
        List<Estudiante> activos = new ArrayList<>();
        String sql = "SELECT * FROM estudiantes WHERE activo = 1";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                activos.add(mapResultSetToEstudiante(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return activos;
    }

    // Buscar estudiante por ID
    public Estudiante findById(int id) {
        String sql = "SELECT * FROM estudiantes WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToEstudiante(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Agregar nuevo estudiante
    public boolean addEstudiante(String carnet, String nombre, String apellido,
                                 String carrera, String nivel, String grupo, boolean activo) {
        String sql = "INSERT INTO estudiantes (carnet, nombre, apellido, carrera, nivel, grupo, activo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, carnet);
            pstmt.setString(2, nombre);
            pstmt.setString(3, apellido);
            pstmt.setString(4, carrera);
            pstmt.setString(5, nivel);
            pstmt.setString(6, grupo);
            pstmt.setBoolean(7, activo);
            int resultado = pstmt.executeUpdate();
            if (resultado > 0) {
                System.out.println("✅ Estudiante agregado: " + carnet + " - " + nombre);
                return true;
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE")) {
                System.err.println("❌ Error: El carnet " + carnet + " ya existe");
            } else {
                e.printStackTrace();
            }
        }
        return false;
    }

    // Actualizar estudiante existente
    public boolean updateEstudiante(int id, String carnet, String nombre, String apellido,
                                    String carrera, String nivel, String grupo, boolean activo) {
        String sql = "UPDATE estudiantes SET carnet = ?, nombre = ?, apellido = ?, carrera = ?, nivel = ?, grupo = ?, activo = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, carnet);
            pstmt.setString(2, nombre);
            pstmt.setString(3, apellido);
            pstmt.setString(4, carrera);
            pstmt.setString(5, nivel);
            pstmt.setString(6, grupo);
            pstmt.setBoolean(7, activo);
            pstmt.setInt(8, id);
            int resultado = pstmt.executeUpdate();
            if (resultado > 0) {
                System.out.println("✅ Estudiante actualizado ID: " + id);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Eliminar estudiante
    public boolean deleteEstudiante(int id) {
        String sql = "DELETE FROM estudiantes WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int resultado = pstmt.executeUpdate();
            if (resultado > 0) {
                System.out.println("✅ Estudiante eliminado ID: " + id);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Contar estudiantes por carrera
    public int getCantidadEstudiantesByCarrera(String carrera) {
        String sql = "SELECT COUNT(*) FROM estudiantes WHERE carrera = ? AND activo = 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, carrera);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Mapear ResultSet a objeto Estudiante
    private Estudiante mapResultSetToEstudiante(ResultSet rs) throws SQLException {
        return new Estudiante(
                rs.getInt("id"),
                rs.getString("carnet"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("carrera"),
                rs.getString("nivel"),
                rs.getString("grupo"),
                rs.getBoolean("activo")
        );
    }
}