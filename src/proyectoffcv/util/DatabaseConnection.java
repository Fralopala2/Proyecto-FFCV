package proyectoffcv.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://192.168.1.140:3306/federacion_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            conn.setAutoCommit(true); // Asegura autoCommit
            System.out.println("Conexión a la base de datos establecida.");
            return conn;
        } catch (SQLException ex) {
            System.err.println("Error de conexión: " + ex.getMessage());
            throw ex;
        }
    }
}
