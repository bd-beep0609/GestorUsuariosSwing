import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CrearBaseDatos {
    public static void main(String[] args) {
        String url = "jdbc:sqlserver://localhost:1433;encrypt=true;trustServerCertificate=true";
        String user = "sa";
        String password = "YourStrong!Passw0rd";  // ⚠️ CAMBIA POR TU CONTRASEÑA

        String crearBD = """
            IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'gestor_esfe')
            BEGIN
                CREATE DATABASE gestor_esfe
                PRINT 'Base de datos gestor_esfe creada'
            END
            ELSE
            BEGIN
                PRINT 'La base de datos gestor_esfe ya existe'
            END
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            stmt.execute(crearBD);
            System.out.println("✅ Base de datos 'gestor_esfe' lista para usar");

        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}