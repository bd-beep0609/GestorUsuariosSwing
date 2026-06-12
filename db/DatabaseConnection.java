package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase para gestionar la conexión a SQL Server
 *
 * @author Bayron
 * @version 1.0
 */
public class DatabaseConnection {

    // ⚠️ CONFIGURACIÓN DE SQL SERVER - CAMBIA SEGÚN TU PC
    private static final String SERVER = "localhost";      // o la IP de tu PC
    private static final String PORT = "1433";
    private static final String DATABASE = "gestor_esfe";
    private static final String USER = "sa";               // tu usuario
    private static final String PASSWORD = "YourStrong!Passw0rd"; // tu contraseña

    private static final String URL = String.format(
            "jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=true;trustServerCertificate=true",
            SERVER, PORT, DATABASE
    );

    private static Connection connection = null;

    /**
     * Obtiene la conexión a la base de datos
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Conexión a SQL Server establecida");
            } catch (ClassNotFoundException e) {
                System.err.println("❌ Driver SQL Server no encontrado: " + e.getMessage());
                throw new SQLException(e);
            }
        }
        return connection;
    }

    /**
     * Cierra la conexión a la base de datos
     */
    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("🔌 Conexión a SQL Server cerrada");
        }
    }

    /**
     * Inicializa la base de datos (crea tablas y datos por defecto)
     */
    public static void initDatabase() {
        System.out.println("📦 Inicializando base de datos SQL Server...");

        // 1. Crear base de datos si no existe
        String createDatabase = """
            IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'gestor_esfe')
            BEGIN
                CREATE DATABASE gestor_esfe
            END
        """;

        // 2. Cambiar al contexto de la base de datos
        String useDatabase = "USE gestor_esfe";

        // 3. Crear tabla users
        String createUsersTable = """
            IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='users' AND xtype='U')
            CREATE TABLE users (
                id INT IDENTITY(1,1) PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                email VARCHAR(100) UNIQUE NOT NULL,
                password VARCHAR(255) NOT NULL,
                active BIT DEFAULT 1,
                role VARCHAR(20) DEFAULT 'USER'
            )
        """;

        // 4. Crear tabla estudiantes
        String createEstudiantesTable = """
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
        """;

        // 5. Crear tabla materias
        String createMateriasTable = """
            IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='materias' AND xtype='U')
            CREATE TABLE materias (
                id INT IDENTITY(1,1) PRIMARY KEY,
                nombre VARCHAR(100) NOT NULL,
                carrera VARCHAR(50) NOT NULL,
                nivel VARCHAR(20) NOT NULL,
                grupo VARCHAR(10) NOT NULL
            )
        """;

        // 6. Crear tabla notas
        String createNotasTable = """
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

        // 7. Insertar usuario administrador por defecto
        String insertAdmin = """
            USE gestor_esfe
            IF NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@demo.com')
            INSERT INTO users (name, email, password, active, role) 
            VALUES ('Administrador', 'admin@demo.com', 'admin123', 1, 'ADMIN')
        """;

        // 8. Insertar materias por defecto
        String insertMaterias = """
            USE gestor_esfe
            IF NOT EXISTS (SELECT 1 FROM materias)
            INSERT INTO materias (nombre, carrera, nivel, grupo) VALUES
            ('Módulo de Software', 'Software', '1°', '2'),
            ('Inglés', 'Software', '1°', '2'),
            ('Módulo de Software', 'Software', '2°', '1'),
            ('Inglés', 'Software', '2°', '1'),
            ('Módulo de Software', 'Software', '4° Articulado', '3'),
            ('Inglés', 'Software', '4° Articulado', '3')
        """;

        try (Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://" + SERVER + ":" + PORT + ";encrypt=true;trustServerCertificate=true",
                USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            // Crear base de datos
            stmt.execute(createDatabase);
            System.out.println("✅ Base de datos 'gestor_esfe' creada/verificada");

            // Cambiar a la base de datos y crear tablas
            stmt.execute(useDatabase);
            stmt.execute(createUsersTable);
            stmt.execute(createEstudiantesTable);
            stmt.execute(createMateriasTable);
            stmt.execute(createNotasTable);
            System.out.println("✅ Tablas creadas/verificadas");

            // Insertar datos por defecto
            stmt.execute(insertAdmin);
            stmt.execute(insertMaterias);
            System.out.println("✅ Datos por defecto insertados");

        } catch (SQLException e) {
            System.err.println("❌ Error inicializando BD: " + e.getMessage());
            e.printStackTrace();
        }
    }
}