package entidades;

import java.sql.*;
import proyectoffcv.util.DatabaseConnection;

public class Categoria {
    private String nombre;
    private int orden;
    private double precioLicencia;

    // Constructor
    public Categoria(String nombre, int orden, double precioLicencia) {
        this.nombre = nombre;
        this.orden = orden;
        this.precioLicencia = precioLicencia;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getOrden() { return orden; }
    public void setOrden(int orden) { this.orden = orden; }
    public double getPrecioLicencia() { return precioLicencia; }
    public void setPrecioLicencia(double precioLicencia) { this.precioLicencia = precioLicencia; }

    // Metodo para obtener el ID
    public int getId() throws SQLException {
        String sql = "SELECT id FROM Categoria WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        throw new SQLException("No se encontro el ID de la categoria: " + nombre);
    }

    // Metodo para guardar en la base de datos
    public void guardar() throws SQLException {
        String sql = "INSERT INTO Categoria (nombre, orden, precioLicencia) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setInt(2, orden);
            stmt.setDouble(3, precioLicencia);
            stmt.executeUpdate();
        }
    }

    // Metodo para actualizar en la base de datos
    public void actualizar() throws SQLException {
        String sql = "UPDATE Categoria SET orden = ?, precioLicencia = ? WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orden);
            stmt.setDouble(2, precioLicencia);
            stmt.setString(3, nombre);
            stmt.executeUpdate();
        }
    }

    // Metodo para eliminar de la base de datos
    public void eliminar() throws SQLException {
        String sql = "DELETE FROM Categoria WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.executeUpdate();
        }
    }

    // Metodo para buscar por nombre
    public static Categoria buscarPorNombre(String nombre) throws SQLException {
        String sql = "SELECT * FROM Categoria WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Categoria(
                    rs.getString("nombre"),
                    rs.getInt("orden"),
                    rs.getDouble("precioLicencia")
                );
            }
        }
        return null;
    }

    // Metodo para buscar por ID
    public static Categoria buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Categoria WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Categoria(
                    rs.getString("nombre"),
                    rs.getInt("orden"),
                    rs.getDouble("precioLicencia")
                );
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Categoria{nombre='" + nombre + "', orden=" + orden + ", precioLicencia=" + precioLicencia + "}";
    }
}