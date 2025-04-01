package entidades;

import proyectoffcv.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * Hecho por Daniel González Cebrián 
 */

public class Instalacion {
    private String nombre;
    private String direccion;
    private TipoSuperficie superficie;

    public enum TipoSuperficie {
        TIERRA, CESPED_NATURAL, CESPED_SINTETICO
    }

    public Instalacion(String nombre, String direccion, TipoSuperficie superficie) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.superficie = superficie;
    }

    private void persistir() throws SQLException {
        String sql = "INSERT INTO Instalacion (nombre, direccion, superficie) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, direccion);
            ps.setString(3, superficie.name());
            ps.executeUpdate();
        }
    }

    private void actualizarEnBD() throws SQLException {
        String sql = "UPDATE Instalacion SET direccion = ?, superficie = ? WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, direccion);
            ps.setString(2, superficie.name());
            ps.setString(3, nombre);
            ps.executeUpdate();
        }
    }

    private void eliminarDeBD() throws SQLException {
        String sql = "DELETE FROM Instalacion WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.executeUpdate();
        }
    }

    public void guardar() throws SQLException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacío.");
        }
        if (buscarPorNombre(nombre) == null) {
            persistir();
        } else {
            throw new IllegalStateException("La instalación ya existe en la base de datos.");
        }
    }

    public void actualizar() throws SQLException {
        if (buscarPorNombre(nombre) != null) {
            actualizarEnBD();
        } else {
            throw new IllegalStateException("La instalación no existe en la base de datos.");
        }
    }

    public void eliminar() throws SQLException {
        if (buscarPorNombre(nombre) != null) {
            eliminarDeBD();
        } else {
            throw new IllegalStateException("La instalación no existe en la base de datos.");
        }
    }

    public static Instalacion buscarPorNombre(String nombre) throws SQLException {
        String sql = "SELECT * FROM Instalacion WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Instalacion(rs.getString("nombre"), rs.getString("direccion"),
                        TipoSuperficie.valueOf(rs.getString("superficie")));
            }
            return null;
        }
    }

    public static List<Instalacion> buscarPorNombreParcial(String nombre) throws SQLException {
        List<Instalacion> instalaciones = new ArrayList<>();
        String sql = "SELECT * FROM Instalacion WHERE nombre LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + nombre + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                instalaciones.add(new Instalacion(rs.getString("nombre"), rs.getString("direccion"),
                        TipoSuperficie.valueOf(rs.getString("superficie"))));
            }
            return instalaciones;
        }
    }
    
    public static Instalacion buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Instalacion WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Instalacion(rs.getString("nombre"), rs.getString("direccion"),
                        TipoSuperficie.valueOf(rs.getString("superficie")));
            }
            return null;
        }
    }

    // Getters y setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public TipoSuperficie getSuperficie() { return superficie; }
    public void setSuperficie(TipoSuperficie superficie) { this.superficie = superficie; }

    @Override
    public String toString() {
        return "Instalacion{nombre='" + nombre + "', direccion='" + direccion + "', superficie='" + superficie + "'}";
    }
}