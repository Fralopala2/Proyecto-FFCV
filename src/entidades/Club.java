package entidades;

import java.sql.*;
import proyectoffcv.util.DatabaseConnection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Club {
    private String nombre;
    private LocalDate fechaFundacion;
    private Persona presidente;
    private Persona secretario;
    private List<Equipo> equipos;

    // Constructor
    public Club(String nombre, LocalDate fechaFundacion, Persona presidente) {
        this.nombre = nombre;
        this.fechaFundacion = fechaFundacion;
        this.presidente = presidente;
        this.secretario = null;
        this.equipos = new ArrayList<>();
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public LocalDate getFechaFundacion() { return fechaFundacion; }
    public void setFechaFundacion(LocalDate fechaFundacion) { this.fechaFundacion = fechaFundacion; }
    public Persona getPresidente() { return presidente; }
    public void setPresidente(Persona presidente) { this.presidente = presidente; }
    public Persona getSecretario() { return secretario; }
    public void setSecretario(Persona secretario) { this.secretario = secretario; }
    public List<Equipo> getEquipos() { return equipos; }

    // Metodo para guardar en la base de datos
    public void guardar() throws SQLException {
        String sql = "INSERT INTO Club (nombre, fechaFundacion, presidente_dni, secretario_dni) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setDate(2, java.sql.Date.valueOf(fechaFundacion));
            ps.setString(3, presidente.getDni());
            ps.setString(4, secretario != null ? secretario.getDni() : null);
            System.out.println("Persistiendo club: " + nombre + ", presidente: " + presidente.getDni() + ", secretario: " + (secretario != null ? secretario.getDni() : "null"));
            ps.executeUpdate();
        }
    }

    // Metodo para actualizar en la base de datos
    public void actualizar() throws SQLException {
        String sql = "UPDATE Club SET fechaFundacion = ?, presidente_dni = ?, secretario_dni = ? WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(fechaFundacion));
            ps.setString(2, presidente.getDni());
            ps.setString(3, secretario != null ? secretario.getDni() : null);
            ps.setString(4, nombre);
            ps.executeUpdate();
        }
    }

    // Metodo para eliminar de la base de datos
    public void eliminar() throws SQLException {
        String sql = "DELETE FROM Club WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.executeUpdate();
        }
    }

    // Metodo para buscar por nombre
    public static Club buscarPorNombre(String nombre) throws SQLException {
        String sql = "SELECT * FROM Club WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Persona presidente = Persona.buscarPorDni(rs.getString("presidente_dni"));
                Persona secretario = rs.getString("secretario_dni") != null ? Persona.buscarPorDni(rs.getString("secretario_dni")) : null;
                Club club = new Club(rs.getString("nombre"), rs.getDate("fechaFundacion").toLocalDate(), presidente);
                club.setSecretario(secretario);
                // Cargar equipos
                club.equipos = obtenerEquipos(club);
                return club;
            }
        }
        return null;
    }

    // Metodo para buscar por ID
    public static Club buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Club WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Persona presidente = Persona.buscarPorDni(rs.getString("presidente_dni"));
                Persona secretario = rs.getString("secretario_dni") != null ? Persona.buscarPorDni(rs.getString("secretario_dni")) : null;
                Club club = new Club(rs.getString("nombre"), rs.getDate("fechaFundacion").toLocalDate(), presidente);
                club.setSecretario(secretario);
                // Cargar equipos
                club.equipos = obtenerEquipos(club);
                return club;
            }
        }
        return null;
    }

    // Metodo para obtener los equipos de un club
    private static List<Equipo> obtenerEquipos(Club club) throws SQLException {
        List<Equipo> equipos = new ArrayList<>();
        String sql = "SELECT * FROM Equipo WHERE club_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, club.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Instalacion instalacion = Instalacion.buscarPorId(rs.getInt("instalacion_id"));
                Grupo grupo = Grupo.buscarPorId(rs.getInt("grupo_id"));
                Equipo equipo = new Equipo(rs.getString("letra"), instalacion, grupo, club);
                equipos.add(equipo);
            }
        }
        return equipos;
    }

    // Metodo para anadir un equipo al club
    public void anadirEquipo(String letra, Instalacion instalacion, Grupo grupo) throws SQLException {
        Equipo equipo = new Equipo(letra, instalacion, grupo, this);
        equipo.guardar();
        equipos.add(equipo);
    }

    // Metodo para obtener el ID del club (suponiendo que la tabla Club tiene una columna id)
    public int getId() throws SQLException {
        String sql = "SELECT id FROM Club WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        throw new SQLException("No se encontro el ID del club: " + nombre);
    }

    @Override
    public String toString() {
        return "Club{nombre='" + nombre + "', fechaFundacion=" + fechaFundacion + ", presidente=" + presidente.getDni() + ", secretario=" + (secretario != null ? secretario.getDni() : "null") + ", equipos=" + equipos.size() + "}";
    }
}