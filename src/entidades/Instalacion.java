package entidades;

import proyectoffcv.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
Hecho por Daniel
*/


public class Instalacion {
    private String nombre;
    private String direccion;
    private TipoSuperficie superficie;

    public enum TipoSuperficie {
        TIERRA, CESPED_NATURAL, CESPED_ARTIFICIAL
    }

    public Instalacion(String nombre, String direccion, TipoSuperficie superficie) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacío.");
        }
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección no puede ser nula ni vacía.");
        }
        if (superficie == null) {
            throw new IllegalArgumentException("La superficie no puede ser nula.");
        }
        this.nombre = nombre;
        this.direccion = direccion;
        this.superficie = superficie;
    }

    private void ejecutarConsulta(boolean isInsert) throws SQLException {
        String sql = isInsert
                ? "INSERT INTO Instalacion (nombre, direccion, superficie) VALUES (?, ?, ?)"
                : "UPDATE Instalacion SET direccion = ?, superficie = ? WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (isInsert) {
                ps.setString(1, nombre);
                ps.setString(2, direccion);
                ps.setString(3, superficie.name());
            } else {
                ps.setString(1, direccion);
                ps.setString(2, superficie.name());
                ps.setString(3, nombre);
            }
            int rowsAffected = ps.executeUpdate();
            if (!isInsert && rowsAffected == 0) {
                throw new SQLException("No se encontró la instalación con nombre: " + nombre);
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al " + (isInsert ? "persistir" : "actualizar") + " la instalación: " + ex.getMessage(), ex);
        }
    }

    private void persistir() throws SQLException {
        ejecutarConsulta(true);
    }

    private void actualizarEnBD() throws SQLException {
        ejecutarConsulta(false);
    }

    private void eliminarDeBD() throws SQLException {
        String sql = "DELETE FROM Instalacion WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se encontró la instalación con nombre: " + nombre);
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar la instalación: " + ex.getMessage(), ex);
        }
    }

    public void guardar() throws SQLException {
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
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacío.");
        }
        String sql = "SELECT * FROM Instalacion WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Instalacion(
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        TipoSuperficie.valueOf(rs.getString("superficie"))
                );
            }
            return null;
        } catch (SQLException ex) {
            throw new SQLException("Error al buscar instalación por nombre: " + ex.getMessage(), ex);
        }
    }

    public static Instalacion buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Instalacion WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Instalacion(
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        TipoSuperficie.valueOf(rs.getString("superficie"))
                );
            }
            return null;
        } catch (SQLException ex) {
            throw new SQLException("Error al buscar instalación por ID: " + ex.getMessage(), ex);
        }
    }

    public static List<Instalacion> buscarPorNombreParcial(String nombre) throws SQLException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacío.");
        }
        List<Instalacion> instalaciones = new ArrayList<>();
        String sql = "SELECT * FROM Instalacion WHERE nombre LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + nombre + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                instalaciones.add(new Instalacion(
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        TipoSuperficie.valueOf(rs.getString("superficie"))
                ));
            }
            return instalaciones;
        } catch (SQLException ex) {
            throw new SQLException("Error al buscar instalaciones por nombre parcial: " + ex.getMessage(), ex);
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacío.");
        }
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección no puede ser nula ni vacía.");
        }
        this.direccion = direccion;
    }

    public TipoSuperficie getSuperficie() {
        return superficie;
    }

    public void setSuperficie(TipoSuperficie superficie) {
        if (superficie == null) {
            throw new IllegalArgumentException("La superficie no puede ser nula.");
        }
        this.superficie = superficie;
    }

    @Override
    public String toString() {
        return "Instalacion{nombre='" + nombre + "', direccion='" + direccion + "', superficie=" + superficie + "}";
    }
}