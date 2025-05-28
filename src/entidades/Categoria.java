package entidades;

import java.sql.*;
import java.util.ArrayList;
import proyectoffcv.util.DatabaseConnection;

public class Categoria {
    private String nombre;
    private int orden;
    private double precioLicencia;
    private ArrayList<Grupo> grupos;

    // Constructor
    public Categoria(String nombre, int orden, double precioLicencia) {
        this.nombre = nombre;
        this.orden = orden;
        this.precioLicencia = precioLicencia;
        this.grupos = new ArrayList<>();
    }

    // Getters
    public String getNombre() { return nombre; }
    public int getOrden() { return orden; }
    public double getPrecioLicencia() { return precioLicencia; }
    public ArrayList<Grupo> getGrupos() { return grupos; }

    // Método para obtener el ID
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
        return -1;
    }

    // Métodos públicos para guardar, actualizar y eliminar
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
        String sql = "INSERT INTO Categoria (nombre, orden, precioLicencia) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setInt(2, orden);
            stmt.setDouble(3, precioLicencia);
            stmt.executeUpdate();
        }
    }

    // Método para actualizar en la base de datos (Privado)
    private void actualizarPrivado() throws SQLException {
        String sql = "UPDATE Categoria SET orden = ?, precioLicencia = ? WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orden);
            stmt.setDouble(2, precioLicencia);
            stmt.setString(3, nombre);
            stmt.executeUpdate();
        }
    }

    // Método para eliminar de la base de datos (Privado)
    private void eliminarPrivado() throws SQLException {
        String sql = "DELETE FROM Categoria WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.executeUpdate();
        }
    }

    // Método para buscar una categoría por nombre
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

    // Método para buscar por ID
    public static Categoria buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nombre, orden, precioLicencia FROM Categoria WHERE id = ?";
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

    // Método para crear grupo
    public Grupo crearGrupo(String nombreGrupo) {
        try {
            Grupo nuevoGrupo = new Grupo(this, nombreGrupo);
            nuevoGrupo.guardarPublic();
            grupos.add(nuevoGrupo); // Añadir a la lista local
            return nuevoGrupo;
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear el grupo", e);
        }
    }

    // Método para cargar todas las categorías desde la base de datos
    public static ArrayList<Categoria> cargarCategoriasDesdeBD() throws SQLException {
        ArrayList<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT * FROM Categoria";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Categoria c = new Categoria(
                    rs.getString("nombre"),
                    rs.getInt("orden"),
                    rs.getDouble("precioLicencia")
                );
                categorias.add(c);
            }
        }
        return categorias;
    }

    @Override
    public String toString() {
        return "Categoria{nombre='" + nombre + "', orden=" + orden + ", precioLicencia=" + precioLicencia + "}";
    }
}