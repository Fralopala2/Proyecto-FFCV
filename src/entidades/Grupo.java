package entidades;

import java.sql.*;
import proyectoffcv.util.DatabaseConnection;

public class Grupo {
    private int id;
    private Categoria categoria;
    private String nombre;

    // Constructor
    public Grupo(Categoria categoria, String nombre) {
        this.categoria = categoria;
        this.nombre = nombre;
    }

    // Getters y Setters
    public int getId() { return id; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    // Metodo para guardar en la base de datos
    public void guardar() throws SQLException {
        String sql = "INSERT INTO Grupo (categoria_id, nombre) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, categoria.getId());
            stmt.setString(2, nombre);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                this.id = rs.getInt(1);
            }
        }
    }

    // Metodo para actualizar en la base de datos
    public void actualizar() throws SQLException {
        String sql = "UPDATE Grupo SET categoria_id = ?, nombre = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoria.getId());
            stmt.setString(2, nombre);
            stmt.setInt(3, id);
            stmt.executeUpdate();
        }
    }

    // Metodo para eliminar de la base de datos
    public void eliminar() throws SQLException {
        String sql = "DELETE FROM Grupo WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Metodo para buscar por nombre
    public static Grupo buscarPorNombre(String nombre) throws SQLException {
        String sql = "SELECT * FROM Grupo WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Categoria categoria = Categoria.buscarPorId(rs.getInt("categoria_id"));
                Grupo grupo = new Grupo(categoria, rs.getString("nombre"));
                grupo.id = rs.getInt("id");
                return grupo;
            }
        }
        return null;
    }

    // Metodo para buscar por ID
    public static Grupo buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Grupo WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Categoria categoria = Categoria.buscarPorId(rs.getInt("categoria_id"));
                Grupo grupo = new Grupo(categoria, rs.getString("nombre"));
                grupo.id = rs.getInt("id");
                return grupo;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Grupo{nombre='" + nombre + "', categoria=" + categoria.getNombre() + "}";
    }
}