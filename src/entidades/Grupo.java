package entidades;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import proyectoffcv.util.DatabaseConnection;

public class Grupo {
    private int id;
    private Categoria categoria;
    private String nombre;
    private ArrayList<Equipo> equipos;

    // Constructor para crear un grupo nuevo (Añade a BD)
    public Grupo(Categoria categoria, String nombre) throws SQLException {
        validarDatos(nombre, categoria);
        this.categoria = categoria;
        this.nombre = nombre;
        this.equipos = new ArrayList<>();
    }

    // Constructor para cargar desde la BD (NO Añade a la BD)
    public Grupo(int id, Categoria categoria, String nombre) {
        validarDatos(nombre, categoria);
        this.id = id;
        this.categoria = categoria;
        this.nombre = nombre;
        this.equipos = new ArrayList<>();
    }

    private void validarDatos(String nombre, Categoria categoria) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del grupo no puede estar vacío.");
        }
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría del grupo no puede ser nula.");
        }
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) {
        validarDatos(nombre, categoria);
        this.categoria = categoria;
    }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public ArrayList<Equipo> getEquipos() { return equipos; }
    public void setEquipos(ArrayList<Equipo> equipos) { this.equipos = equipos; }

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
        String sql = "INSERT INTO Grupo (categoria_id, nombre) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, categoria.getId());
            stmt.setString(2, nombre);
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
        String sql = "UPDATE Grupo SET categoria_id = ?, nombre = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoria.getId());
            stmt.setString(2, nombre);
            stmt.setInt(3, id);
            stmt.executeUpdate();
        }
    }

    // Método para eliminar de la base de datos (Privado)
    private void eliminarPrivado() throws SQLException {
        String sql = "DELETE FROM Grupo WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Método para buscar un grupo por su ID
    public static Grupo buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, categoria_id, nombre FROM Grupo WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int categoriaId = rs.getInt("categoria_id");
                Categoria categoria = Categoria.buscarPorId(categoriaId);   
                if (categoria == null) {
                    throw new SQLException("Categoría asociada al grupo con ID " + categoriaId + " no encontrada.");
                }
                return new Grupo(rs.getInt("id"), categoria, rs.getString("nombre"));
            }
        }
        return null;
    }

    // Método para buscar un grupo por nombre
    public static Grupo buscarPorNombre(String nombre) throws SQLException {
        String sql = "SELECT g.id, g.nombre, c.nombre as categoria_nombre, c.orden, c.precioLicencia FROM Grupo g JOIN Categoria c ON g.categoria_id = c.id WHERE g.nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Categoria categoria = Categoria.buscarPorNombre(rs.getString("categoria_nombre"));
                if (categoria == null) {
                    throw new SQLException("Categoría asociada al grupo no encontrada.");
                }
                return new Grupo(rs.getInt("id"), categoria, rs.getString("nombre"));
            }
        }
        return null;
    }

    // Método para buscar grupos por categoría
    public static List<Grupo> buscarGruposPorCategoria(Categoria c) throws SQLException {
        List<Grupo> grupos = new ArrayList<>();
        String sql = "SELECT g.id, g.nombre, g.categoria_id FROM Grupo g WHERE g.categoria_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, c.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                grupos.add(new Grupo(rs.getInt("id"), c, rs.getString("nombre")));
            }
        }
        return grupos;
    }

    // Método para cargar equipos desde la base de datos
    public void cargarEquiposDesdeBD() throws SQLException {
        equipos.clear();
        String sql = "SELECT id FROM Equipo WHERE grupo_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, this.id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Equipo equipo = Equipo.buscarPorId(rs.getInt("id"));
                if (equipo != null) {
                    equipos.add(equipo);
                }
            }
        }
    }

    // Método para agregar equipo
    public boolean agregarEquipo(Equipo equipo) throws SQLException {
        if (equipos.size() >= 20) {
            System.out.println("El grupo ya tiene 20 equipos. Operación denegada.");
            return false;
        }
        if (!validarEquipo(equipo)) {
            System.out.println("Ya hay un equipo del mismo club en el grupo. Operación denegada.");
            return false;
        }
        equipo.setGrupo(this);
        equipo.actualizarPublic();
        equipos.add(equipo);
        System.out.println("Equipo agregado.");
        return true;
    }

    // Método privado para validar equipo
    private boolean validarEquipo(Equipo equipo) {
        for (Equipo e : equipos) {
            if (e.getClub().equals(equipo.getClub())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "Grupo{" + "id=" + id + ", categoria=" + categoria + ", nombre=" + nombre + ", equipos=" + equipos + '}';
    }
}