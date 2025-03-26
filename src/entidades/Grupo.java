package entidades;

import proyectoffcv.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Grupo {
    private String nombre;
    private Categoria categoria;
    private List<Equipo> equipos;

    public Grupo(String nombre) {
        this.nombre = nombre;
        this.equipos = new ArrayList<>();
    }

    private void persistir() throws SQLException {
        String sql = "INSERT INTO Grupo (nombre, categoria_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setInt(2, obtenerIdCategoria()); // Método de instancia
            ps.executeUpdate();
        }
    }

    private void actualizarEnBD() throws SQLException {
        String sql = "UPDATE Grupo SET categoria_id = ? WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, obtenerIdCategoria()); // Método de instancia
            ps.setString(2, nombre);
            ps.executeUpdate();
        }
    }

    private void eliminarDeBD() throws SQLException {
        String sql = "DELETE FROM Grupo WHERE nombre = ?";
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
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría no puede ser nula.");
        }
        if (buscarPorNombre(nombre) == null) {
            persistir();
        } else {
            throw new IllegalStateException("El grupo ya existe en la base de datos.");
        }
    }

    public void actualizar() throws SQLException {
        if (buscarPorNombre(nombre) != null) {
            actualizarEnBD();
        } else {
            throw new IllegalStateException("El grupo no existe en la base de datos.");
        }
    }

    public void eliminar() throws SQLException {
        if (buscarPorNombre(nombre) != null) {
            eliminarDeBD();
        } else {
            throw new IllegalStateException("El grupo no existe en la base de datos.");
        }
    }

    public static Grupo buscarPorNombre(String nombre) throws SQLException {
        String sql = "SELECT * FROM Grupo WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Grupo grupo = new Grupo(rs.getString("nombre"));
                grupo.setCategoria(Categoria.buscarPorId(rs.getInt("categoria_id")));
                return grupo;
            }
            return null;
        }
    }

    public static Grupo buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Grupo WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Grupo grupo = new Grupo(rs.getString("nombre"));
                grupo.setCategoria(Categoria.buscarPorId(rs.getInt("categoria_id")));
                return grupo;
            }
            return null;
        }
    }

    public static List<Grupo> obtenerPorCategoria(Categoria c) throws SQLException {
        List<Grupo> grupos = new ArrayList<>();
        String sql = "SELECT * FROM Grupo WHERE categoria_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, obtenerIdCategoriaStatic(c)); // Método estático
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Grupo grupo = new Grupo(rs.getString("nombre"));
                grupo.setCategoria(c);
                grupos.add(grupo);
            }
            return grupos;
        }
    }

    private int obtenerIdCategoria() throws SQLException {
        if (categoria == null) {
            throw new IllegalStateException("La categoría no está asignada.");
        }
        String sql = "SELECT id FROM Categoria WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, categoria.getNombre());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            throw new SQLException("Categoría no encontrada.");
        }
    }

    private static int obtenerIdCategoriaStatic(Categoria c) throws SQLException {
        if (c == null) {
            throw new IllegalArgumentException("La categoría no puede ser nula.");
        }
        String sql = "SELECT id FROM Categoria WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            throw new SQLException("Categoría no encontrada.");
        }
    }

    // Getters y setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) {
        if (categoria == null) throw new IllegalArgumentException("La categoría no puede ser nula.");
        this.categoria = categoria;
    }
    public List<Equipo> getEquipos() { return equipos; }

    @Override
    public String toString() {
        return "Grupo{nombre='" + nombre + "', categoria=" + (categoria != null ? categoria.getNombre() : "null") +
               ", equipos=" + equipos.size() + " equipos}";
    }
}