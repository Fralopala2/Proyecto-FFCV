package entidades;

import java.sql.*;
import proyectoffcv.util.DatabaseConnection;

public class Instalacion {
    private int id;
    private String nombre;
    private String direccion;
    private TipoSuperficie superficie;

    // Enum para tipos de superficie
    public enum TipoSuperficie {
        CESPED_NATURAL,
        CESPED_ARTIFICIAL,
        TIERRA
    }

    // Constructor
    public Instalacion(String nombre, String direccion, TipoSuperficie superficie) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.superficie = superficie;
    }

    // Constructor con ID para cargar desde BD
    public Instalacion(int id, String nombre, String direccion, TipoSuperficie superficie) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.superficie = superficie;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public TipoSuperficie getSuperficie() { return superficie; }

    // Setters (si son necesarios)
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setSuperficie(TipoSuperficie superficie) { this.superficie = superficie; }

    // Métodos públicos que llaman a los privados
    public void guardarPublic() throws SQLException {
        guardarPrivado();
    }

    public void actualizarPublic() throws SQLException {
        actualizarPrivado();
    }

    public void eliminarPublic() throws SQLException {
        eliminarPrivado();
    }

    // Método para guardar en la base de datos (Privado)
    private void guardarPrivado() throws SQLException {
        String sql = "INSERT INTO Instalacion (nombre, direccion, superficie) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nombre);
            stmt.setString(2, direccion);
            stmt.setString(3, superficie.name());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    this.id = generatedKeys.getInt(1);
                }
            }
        }
    }

    // Método para actualizar en la base de datos (Privado)
    private void actualizarPrivado() throws SQLException {
        String sql = "UPDATE Instalacion SET nombre = ?, direccion = ?, superficie = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setString(2, direccion);
            stmt.setString(3, superficie.name());
            stmt.setInt(4, id);
            stmt.executeUpdate();
        }
    }

    // Método para eliminar de la base de datos (Privado)
    private void eliminarPrivado() throws SQLException {
        String sql = "DELETE FROM Instalacion WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Método para buscar instalaciones por nombre
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

    // Método para buscar por ID
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

    // Método para obtener todas las instalaciones
    public static java.util.List<Instalacion> obtenerTodas() throws SQLException {
        java.util.List<Instalacion> instalaciones = new java.util.ArrayList<>();
        String sql = "SELECT * FROM Instalacion";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                instalaciones.add(new Instalacion(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("direccion"),
                    TipoSuperficie.valueOf(rs.getString("superficie"))
                ));
            }
        }
        return instalaciones;
    }

    @Override
    public String toString() {
        return "Instalacion{nombre='" + nombre + "', direccion='" + direccion + "', superficie='" + superficie + "'}";
    }
}