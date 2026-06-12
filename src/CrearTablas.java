import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CrearTablas {
    public static void main(String[] args) {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=gestor_esfe;encrypt=true;trustServerCertificate=true";
        String user = "sa";
        String password = "YourStrong!Passw0rd";  // ⚠️ CAMBIA

        String crearTablas = """
            -- Tabla users
            IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='users' AND xtype='U')
            CREATE TABLE users (
                id INT IDENTITY(1,1) PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                email VARCHAR(100) UNIQUE NOT NULL,
                password VARCHAR(255) NOT NULL,
                active BIT DEFAULT 1,
                role VARCHAR(20) DEFAULT 'USER'
            )
            
            -- Tabla estudiantes
            IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='estudiantes' AND xtype='U')
            CREATE TABLE estudiantes (
                id INT IDENTITY(1,1) PRIMARY KEY,
                carnet VARCHAR(20) UNIQUE NOT NULL,
                nombre VARCHAR(100) NOT NULL,
                apellido VARCHAR(100) NOT NULL,
                carrera VARCHAR(50) NOT NULL,
                nivel VARCHAR(20) NOT NULL,
                grupo VARCHAR(10) NOT NULL,
                activo BIT DEFAULT 1
            )
            
            -- Tabla materias
            IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='materias' AND xtype='U')
            CREATE TABLE materias (
                id INT IDENTITY(1,1) PRIMARY KEY,
                nombre VARCHAR(100) NOT NULL,
                carrera VARCHAR(50) NOT NULL,
                nivel VARCHAR(20) NOT NULL,
                grupo VARCHAR(10) NOT NULL
            )
            
            -- Tabla notas
            IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='notas' AND xtype='U')
            CREATE TABLE notas (
                id INT IDENTITY(1,1) PRIMARY KEY,
                estudiante_id INT NOT NULL,
                materia_id INT NOT NULL,
                valor DECIMAL(3,1) NOT NULL CHECK (valor >= 0 AND valor <= 10),
                fecha DATE DEFAULT GETDATE(),
                observacion VARCHAR(255),
                FOREIGN KEY (estudiante_id) REFERENCES estudiantes(id) ON DELETE CASCADE
            )
        """;

        String insertAdmin = """
            IF NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@demo.com')
            INSERT INTO users (name, email, password, active, role) 
            VALUES ('Administrador', 'admin@demo.com', 'admin123', 1, 'ADMIN')
        """;

        String insertMaterias = """
            IF NOT EXISTS (SELECT 1 FROM materias)
            INSERT INTO materias (nombre, carrera, nivel, grupo) VALUES
            ('Módulo de Software', 'Software', '1°', '2'),
            ('Inglés', 'Software', '1°', '2'),
            ('Módulo de Software', 'Software', '2°', '1'),
            ('Inglés', 'Software', '2°', '1'),
            ('Módulo de Software', 'Software', '4° Articulado', '3'),
            ('Inglés', 'Software', '4° Articulado', '3')
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            // Ejecutar cada comando por separado
            for (String comando : crearTablas.split("(?=CREATE TABLE)")) {
                if (!comando.trim().isEmpty()) {
                    try {
                        stmt.execute(comando);
                    } catch (SQLException e) {
                        // Ignorar si la tabla ya existe
                        if (!e.getMessage().contains("already exists")) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    }
                }
            }

            stmt.execute(insertAdmin);
            stmt.execute(insertMaterias);

            System.out.println("✅ Tablas creadas correctamente");
            System.out.println("✅ Usuario admin creado: admin@demo.com / admin123");
            System.out.println("✅ Materias predefinidas agregadas");

        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}