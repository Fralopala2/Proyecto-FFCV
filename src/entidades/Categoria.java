package entidades;

import proyectoffcv.util.DatabaseConnection;
import java.util.InputMismatchException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Categoria {

    // Atributos
    private String nombre;
    private int orden;
    private int id;
    private double precioLicencia;
    private List<Grupo> grupos;

    // Constructores
    public Categoria(String nombre, int orden, double precioLicencia) {
        this.nombre = nombre;
        this.orden = orden;
        if (precioLicencia < 0.0) {
            throw new InputMismatchException("El precio de la licencia no puede ser menor que cero.");
        }
        this.precioLicencia = precioLicencia;
        this.grupos = new ArrayList<>();
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
        try {
            actualizarEnBD();
        } catch (SQLException e) {
            
        }
    }

    public int getOrden() { return orden; }
    public void setOrden(int orden) { 
        this.orden = orden; 
        try {
            actualizarEnBD();
        } catch (SQLException e) {
            
        }
    }

    public double getPrecioLicencia() { return precioLicencia; }
    public void setPrecioLicencia(double precioLicencia) {
        checkPrecioLicencia(precioLicencia);
        this.precioLicencia = precioLicencia;
        try {
            actualizarEnBD();
        } catch (SQLException e) {
            
        }
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public List<Grupo> getGrupos() { return grupos; }

    // Métodos
    private void checkPrecioLicencia(double p) {
        if (p < 0.0) {
            throw new InputMismatchException("El precio de la licencia no puede ser menor que cero.");
        }
    }

    private void persistir() throws SQLException {
        String sql = "INSERT INTO Categoria (nombre, orden, precioLicencia) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre);
            ps.setInt(2, orden);
            ps.setDouble(3, precioLicencia);
            ps.executeUpdate();
            
            // Obtener ID generado
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                this.id = rs.getInt(1);
            }
        }
    }

    private void actualizarEnBD() throws SQLException {
        String sql = "UPDATE Categoria SET orden = ?, precioLicencia = ?, nombre = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orden);
            ps.setDouble(2, precioLicencia);
            ps.setString(3, nombre);
            ps.setInt(4, id);
            ps.executeUpdate();
        }
    }

    private void eliminarDeBD() throws SQLException {
        String sql = "DELETE FROM Categoria WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
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
            throw new IllegalStateException("La categoría ya existe en la base de datos.");
        }
    }

    public void actualizar() throws SQLException {
        if (id > 0) {
            actualizarEnBD();
        } else {
            throw new IllegalStateException("La categoría no tiene un ID válido.");
        }
    }

    public void eliminar() throws SQLException {
        if (id > 0) {
            eliminarDeBD();
        } else {
            throw new IllegalStateException("La categoría no existe en la base de datos.");
        }
    }

    public static Categoria buscarPorNombre(String nombre) throws SQLException {
        String sql = "SELECT * FROM Categoria WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Categoria categoria = new Categoria(rs.getString("nombre"), rs.getInt("orden"), rs.getDouble("precioLicencia"));
                categoria.setId(rs.getInt("id"));
                return categoria;
            }
            return null;
        }
    }

    public static List<Categoria> obtenerTodas() throws SQLException {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT * FROM Categoria";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Categoria categoria = new Categoria(rs.getString("nombre"), rs.getInt("orden"), rs.getDouble("precioLicencia"));
                categoria.setId(rs.getInt("id"));
                
                // Cargar grupos asociados
                List<Grupo> grupos = Grupo.buscarPorCategoria(categoria);
                if (grupos != null) {
                    categoria.grupos = grupos;
                }
                
                categorias.add(categoria);
            }
            return categorias;
        }
    }

    public static Categoria buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Categoria WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Categoria categoria = new Categoria(rs.getString("nombre"), rs.getInt("orden"), rs.getDouble("precioLicencia"));
                categoria.setId(rs.getInt("id"));
                return categoria;
            }
            return null;
        }
    }

    @Override
    public String toString() {
        return "Categoria{nombre=" + nombre + ", orden=" + orden + ", precioLicencia=" + precioLicencia + "}";
    }
}
