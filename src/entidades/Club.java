package entidades;

import java.sql.*;
import java.time.LocalDate;
import proyectoffcv.util.DatabaseConnection;

public class Club {
    private int id;
    private String nombre;
    private LocalDate fechaFundacion;
    private Persona presidente;

    // Constructor para crear un club nuevo
    public Club(String nombre, LocalDate fechaFundacion, Persona presidente) {
        this.nombre = nombre;
        this.fechaFundacion = fechaFundacion;
        this.presidente = presidente;
    }

    // Constructor para busqueda de clubes
    public Club(int id, String nombre, LocalDate fechaFundacion, Persona presidente) {
        this.id = id;
        this.nombre = nombre;
        this.fechaFundacion = fechaFundacion;
        this.presidente = presidente;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public LocalDate getFechaFundacion() { return fechaFundacion; }
    public Persona getPresidente() { return presidente; }

    // Setters
    public void setFechaFundacion(LocalDate fechaFundacion) { this.fechaFundacion = fechaFundacion; }
    public void setPresidente(Persona presidente) { this.presidente = presidente; }

    // Metodo para guardar en la base de datos
    public void guardar() throws SQLException {
        String sql = "INSERT INTO Club (nombre, fechaFundacion, presidente_dni) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nombre);
            stmt.setDate(2, java.sql.Date.valueOf(fechaFundacion));
            stmt.setString(3, presidente.getDni());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                this.id = rs.getInt(1);
            }
        }
    }

    // Metodo para actualizar en la base de datos
    public void actualizar() throws SQLException {
        String sql = "UPDATE Club SET fechaFundacion = ?, presidente_dni = ? WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, java.sql.Date.valueOf(fechaFundacion));
            stmt.setString(2, presidente.getDni());
            stmt.setString(3, nombre);
            stmt.executeUpdate();
        }
    }

    // Metodo para eliminar de la base de datos
    public void eliminar() throws SQLException {
        String sql = "DELETE FROM Club WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.executeUpdate();
        }
    }

    // Metodo para buscar por nombre
    public static Club buscarPorNombre(String nombre) throws SQLException {
        String sql = "SELECT * FROM Club WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                LocalDate fechaFundacion = rs.getDate("fechaFundacion").toLocalDate();
                Persona presidente = Persona.buscarPorDni(rs.getString("presidente_dni"));
                Club club = new Club(nombre, fechaFundacion, presidente);
                club.id = rs.getInt("id");
                return club;
            }
        }
        return null;
    }

    // Metodo para buscar por ID
    public static Club buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Club WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String nombre = rs.getString("nombre");
                LocalDate fechaFundacion = rs.getDate("fechaFundacion").toLocalDate();
                Persona presidente = Persona.buscarPorDni(rs.getString("presidente_dni"));
                Club club = new Club(nombre, fechaFundacion, presidente);
                club.id = id;
                return club;
            }
        }
        return null;
    }

    // Metodo para mostrar informacion del club
    @Override
    public String toString() {
        return "Club: " + nombre + ", Fecha Fundacion: " + fechaFundacion + ", Presidente: " + 
               presidente.getNombre() + " " + presidente.getApellido1();
    }
}