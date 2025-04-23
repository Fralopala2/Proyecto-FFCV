package entidades;

import proyectoffcv.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Grupo {

    // Atributos
    private int id;
    private String nombre;
    private Categoria categoria;
    private List<Equipo> equipos;

    // Constructor
    public Grupo(int id, String nombre) {
        this.id = id;
        this.setNombre(nombre);
        this.equipos = new ArrayList<>();
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacío.");
        }
        this.nombre = nombre;
    }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría no puede ser nula.");
        }
        this.categoria = categoria;
        if (categoria.getGrupos() != null && !categoria.getGrupos().contains(this)) {
            categoria.getGrupos().add(this);
        }
    }

    public List<Equipo> getEquipos() { return equipos; }

    // Métodos
    public void agregarEquipo(Equipo equipo) {
        if (equipo == null) throw new IllegalArgumentException("El equipo no puede ser nulo.");
        equipos.add(equipo);
    }

    public void eliminarEquipo(Equipo equipo) {
        equipos.remove(equipo);
    }

    public void guardar() throws SQLException {
        String sql = "INSERT INTO Grupo (id, nombre, categoria_id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, nombre);
            ps.setInt(3, categoriaId());
            ps.executeUpdate();
        }
    }

    public void actualizar() throws SQLException {
        String sql = "UPDATE Grupo SET nombre = ?, categoria_id = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setInt(2, categoriaId());
            ps.setInt(3, id);
            ps.executeUpdate();
        }
    }

    public void eliminar() throws SQLException {
        String sql = "DELETE FROM Grupo WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public static Grupo buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Grupo WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Grupo grupo = new Grupo(rs.getInt("id"), rs.getString("nombre"));
                Categoria categoria = Categoria.buscarPorId(rs.getInt("categoria_id"));
                grupo.setCategoria(categoria);
                return grupo;
            }
            return null;
        }
    }

    public static List<Grupo> obtenerTodos() throws SQLException {
        List<Grupo> grupos = new ArrayList<>();
        String sql = "SELECT * FROM Grupo";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Grupo grupo = new Grupo(rs.getInt("id"), rs.getString("nombre"));
                Categoria categoria = Categoria.buscarPorId(rs.getInt("categoria_id"));
                grupo.setCategoria(categoria);
                grupos.add(grupo);
            }
        }
        return grupos;
    }
    
    public static List<Grupo> buscarPorCategoria(Categoria categoria) throws SQLException {
        List<Grupo> grupos = new ArrayList<>();
        String sql = "SELECT * FROM Grupo WHERE categoria_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoria.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Grupo grupo = new Grupo(rs.getInt("id"), rs.getString("nombre"));
                grupo.setCategoria(categoria);
                grupos.add(grupo);
            }
            return grupos;
        }
    }
    
    public static Grupo buscarPorNombre(String nombre) throws SQLException {
        String sql = "SELECT * FROM Grupo WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Grupo grupo = new Grupo(rs.getInt("id"), rs.getString("nombre"));
                Categoria categoria = Categoria.buscarPorId(rs.getInt("categoria_id"));
                grupo.setCategoria(categoria);
                return grupo;
            }
            return null;
        }
    }

    private int categoriaId() throws SQLException {
        if (categoria == null) throw new IllegalStateException("Categoría no establecida.");

        String sql = "SELECT id FROM Categoria WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, categoria.getNombre());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            throw new SQLException("Categoría no encontrada: " + categoria.getNombre());
        }
    }

    @Override
    public String toString() {
        return "Grupo{id=" + id + ", nombre='" + nombre + "', categoria=" +
                (categoria != null ? categoria.getNombre() : "null") +
                ", equipos=" + equipos.size() + "}";
    }
}