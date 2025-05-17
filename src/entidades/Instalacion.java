package entidades;

import java.sql.*;
import proyectoffcv.util.DatabaseConnection;

public class Instalacion {
    private int id;
    private String nombre;
    private String direccion;
    private TipoSuperficie superficie;

    public enum TipoSuperficie {
        CESPED_NATURAL, CESPED_ARTIFICIAL, TIERRA
    }

    // Constructor
    public Instalacion(int id, String nombre, String direccion, TipoSuperficie superficie) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.superficie = superficie;
    }

    public Instalacion(String nombre, String direccion, TipoSuperficie superficie) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.superficie = superficie;
    }

    // Getters y Setters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public TipoSuperficie getSuperficie() { return superficie; }
    public void setSuperficie(TipoSuperficie superficie) { this.superficie = superficie; }

    // Metodo para guardar en la base de datos
    public void guardar() throws SQLException {
        String sql = "INSERT INTO Instalacion (nombre, direccion, superficie) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nombre);
            stmt.setString(2, direccion);
            stmt.setString(3, superficie.toString());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                this.id = rs.getInt(1);
            }
        }
    }

    // Metodo para actualizar de la base de datos
    public void actualizar() throws SQLException {
        String sql = "UPDATE instalacion SET direccion = ?, superficie = ? WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, direccion);
            stmt.setString(2, superficie.toString());
            stmt.setString(3, nombre);
            stmt.executeUpdate();
        }
    }

    // Metodo para eliminar de la base de datos
    public void eliminar() throws SQLException {
        String sql = "DELETE FROM instalacion WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.executeUpdate();
        }
    }

    // Metodo para buscar por nombre
    public static Instalacion buscarPorNombre(String nombre) throws SQLException {
        String sql = "SELECT * FROM Instalacion WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Instalacion(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("direccion"),
                    TipoSuperficie.valueOf(rs.getString("superficie"))
                );
            }
        }
        return null;
    }

    // Metodo para buscar por ID
    public static Instalacion buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Instalacion WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Instalacion(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("direccion"),
                    TipoSuperficie.valueOf(rs.getString("superficie"))
                );
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Instalacion{nombre='" + nombre + "', direccion='" + direccion + "', superficie=" + superficie + "}";
    }
}