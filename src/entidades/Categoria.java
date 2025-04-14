package entidades;
import proyectoffcv.util.DatabaseConnection;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.ArrayList;
import java.util.List;

public class Categoria {
    private String nombre;
    private int orden;
    private double precioLicencia;
    private List<Grupo> grupos;

    public Categoria(String nombre, int orden, double precioLicencia) {
        this.nombre = nombre;
        this.orden = orden;
        if (precioLicencia < 0.0) {
            throw new InputMismatchException("El precio de la licencia no puede ser menor que cero.");
        }
        this.precioLicencia = precioLicencia;
        this.grupos = new ArrayList<>();
    }

    private void checkPrecioLicencia(double p) {
        if (p < 0.0) {
            throw new InputMismatchException("El precio de la licencia no puede ser menor que cero.");
        }
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getOrden() { return orden; }
    public void setOrden(int orden) { this.orden = orden; }
    public double getPrecioLicencia() { return precioLicencia; }
    public void setPrecioLicencia(double precioLicencia) { 
        checkPrecioLicencia(precioLicencia); 
        this.precioLicencia = precioLicencia; 
    }
    public List<Grupo> getGrupos() { return grupos; }

    public void agregarGrupo(Grupo grupo) {
        if (grupo == null) {
            throw new IllegalArgumentException("El grupo no puede ser nulo.");
        }
        grupos.add(grupo);
    }

    public static Categoria buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Categoria WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Categoria categoria = new Categoria(
                        rs.getString("nombre"),
                        rs.getInt("orden"),
                        rs.getDouble("precioLicencia")
                );
                return categoria;
            }
            return null;
        }
    }
    
    public void eliminarGrupo(Grupo grupo) {
        grupos.remove(grupo);
    }

    @Override
    public String toString() {
        return "Categoria{nombre=" + nombre + ", orden=" + orden + ", precioLicencia=" + precioLicencia + "}";
    }
}