import db.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestConexionBD {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT DB_NAME() as BD_NAME, @@VERSION as VERSION");

            if (rs.next()) {
                System.out.println("📊 Conectado a BD: " + rs.getString("BD_NAME"));
                System.out.println("📌 Versión SQL Server: " + rs.getString("VERSION").substring(0, 50) + "...");
            }

            // Verificar tablas
            rs = stmt.executeQuery("""
                SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES 
                WHERE TABLE_TYPE='BASE TABLE' ORDER BY TABLE_NAME
            """);

            System.out.println("\n📋 Tablas en la base de datos:");
            while (rs.next()) {
                System.out.println("   - " + rs.getString("TABLE_NAME"));
            }

            rs.close();
            stmt.close();
            DatabaseConnection.closeConnection();

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}