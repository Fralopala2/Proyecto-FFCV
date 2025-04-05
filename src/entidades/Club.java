package entidades;

import proyectoffcv.util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Club {
    private String nombre;
    private LocalDate fechaFundacion;
    private List<Equipo> equipos;
    private Persona presidente;

    public Club(String nombre, LocalDate fechaFundacion, Persona presidente) {
        this.nombre = nombre;
        this.fechaFundacion = fechaFundacion;
        this.presidente = presidente;
        this.equipos = new ArrayList<>();
    }

    private void persistir() throws SQLException {
        String sql = "INSERT INTO Club (nombre, fechaFundacion, presidente_dni) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setDate(2, java.sql.Date.valueOf(fechaFundacion));
            ps.setString(3, presidente.getDni());
            ps.executeUpdate();
        }
    }

    private void actualizarEnBD() throws SQLException {
        String sql = "UPDATE Club SET fechaFundacion = ?, presidente_dni = ? WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(fechaFundacion));
            ps.setString(2, presidente.getDni());
            ps.setString(3, nombre);
            ps.executeUpdate();
        }
    }

    private void eliminarDeBD() throws SQLException {
        String sql = "DELETE FROM Club WHERE nombre = ?";
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
        if (presidente == null) {
            throw new IllegalArgumentException("El presidente no puede ser nulo.");
        }
        if (buscarPorNombre(nombre) == null) {
            persistir();
        } else {
            throw new IllegalStateException("El club ya existe en la base de datos.");
        }
    }

    public void actualizar() throws SQLException {
        if (buscarPorNombre(nombre) != null) {
            actualizarEnBD();
        } else {
            throw new IllegalStateException("El club no existe en la base de datos.");
        }
    }

    public void eliminar() throws SQLException {
        if (buscarPorNombre(nombre) != null) {
            eliminarDeBD();
        } else {
            throw new IllegalStateException("El club no existe en la base de datos.");
        }
    }

    public static Club buscarPorNombre(String nombre) throws SQLException {
        String sql = "SELECT * FROM Club WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Persona presidente = Persona.buscarPorDni(rs.getString("presidente_dni"));
                return new Club(rs.getString("nombre"), rs.getDate("fechaFundacion").toLocalDate(), presidente);
            }
            return null;
        }
    }

    public void addEquipo(Equipo equipo) throws SQLException {
        if (!equipos.contains(equipo)) {
            String sql = "INSERT INTO Club_Equipo (club_id, equipo_id) VALUES (?, ?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, obtenerIdClub());
                ps.setInt(2, obtenerIdEquipo(equipo));
                ps.executeUpdate();
                equipos.add(equipo);
            }
        } else {
            throw new IllegalArgumentException("El equipo ya está en el club.");
        }
    }

    public void removeEquipo(Equipo equipo) throws SQLException {
        if (equipos.contains(equipo)) {
            String sql = "DELETE FROM Club_Equipo WHERE club_id = ? AND equipo_id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, obtenerIdClub());
                ps.setInt(2, obtenerIdEquipo(equipo));
                ps.executeUpdate();
                equipos.remove(equipo);
            }
        }
    }

    private void cargarEquipos() throws SQLException {
        equipos.clear();
        String sql = "SELECT e.* FROM Equipo e JOIN Club_Equipo ce ON e.id = ce.equipo_id WHERE ce.club_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, obtenerIdClub());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Instalacion instalacion = Instalacion.buscarPorId(rs.getInt("instalacion_id"));
                Grupo grupo = Grupo.buscarPorId(rs.getInt("grupo_id"));
                Equipo equipo = new Equipo(rs.getString("letra"), instalacion, grupo);
                equipos.add(equipo);
            }
        }
    }

    public int obtenerIdClub() throws SQLException {
        String sql = "SELECT id FROM Club WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, this.getNombre());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            throw new SQLException("Club no encontrado.");
        }
    }

    private int obtenerIdEquipo(Equipo equipo) throws SQLException {
        String sql = "SELECT id FROM Equipo WHERE letra = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, equipo.getLetra());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            throw new SQLException("Equipo no encontrado.");
        }
    }
    
    public static List<Club> obtenerTodos() throws SQLException {
        List<Club> clubes = new ArrayList<>();
        String sql = "SELECT c.nombre, c.fechaFundacion, c.presidente_dni " +
                     "FROM Club c";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Persona presidente = Persona.buscarPorDni(rs.getString("presidente_dni"));
                if (presidente == null) {
                    throw new SQLException("Presidente no encontrado para el club: " + rs.getString("nombre"));
                }
                
                Club club = new Club(
                    rs.getString("nombre"),
                    rs.getDate("fechaFundacion").toLocalDate(),
                    presidente
                );
                club.cargarEquipos();
                clubes.add(club);
            }
        }
        return clubes;
    }

    // Getters y setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public LocalDate getFechaFundacion() { return fechaFundacion; }
    public void setFechaFundacion(LocalDate fechaFundacion) { this.fechaFundacion = fechaFundacion; }
    public Persona getPresidente() { return presidente; }
    public void setPresidente(Persona presidente) { this.presidente = presidente; }
    public List<Equipo> getEquipos() { return equipos; }
    public int getNumeroEquipos() { return equipos.size(); }

    @Override
    public String toString() {
        return "Club{nombre='" + nombre + "', fechaFundacion=" + fechaFundacion + ", presidente=" + 
               presidente.getDni() + ", equipos=" + equipos.size() + "}";
    }
}