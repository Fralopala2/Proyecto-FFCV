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
        // Añadir este grupo a la lista de grupos de la categoría
        if (!categoria.getGrupos().contains(this)) {
            categoria.agregarGrupo(this);
        }
    }
    
    
    
    public List<Equipo> getEquipos() { return equipos; }

    public void agregarEquipo(Equipo equipo) {
        if (equipo == null) {
            throw new IllegalArgumentException("El equipo no puede ser nulo.");
        }
        equipos.add(equipo);
    }

    public void eliminarEquipo(Equipo equipo) {
        equipos.remove(equipo);
    }
    
    public static Grupo buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Grupo WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Grupo grupo = new Grupo(rs.getString("nombre"));
                int categoriaId = rs.getInt("categoria_id");
                Categoria categoria = Categoria.buscarPorId(categoriaId);
                grupo.setCategoria(categoria);

                return grupo;
            }
            return null;
        }
    }

    @Override
    public String toString() {
        return "Grupo{nombre='" + nombre + "', categoria=" + (categoria != null ? categoria.getNombre() : "null") +", equipos=" + equipos.size() + " equipos}";
    }
}