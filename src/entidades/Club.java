package entidades;

import java.sql.*;
import java.time.LocalDate;
import proyectoffcv.util.DatabaseConnection;

public class Club {
    private int id;
    private String nombre;
    private LocalDate fechaAlta;
    private Persona presidente;
    private String direccion;

    // Constructor
    public Club(String nombre, LocalDate fechaAlta, Persona presidente, String direccion) {
        this.nombre = nombre;
        this.fechaAlta = fechaAlta;
        this.presidente = presidente;
        this.direccion = direccion;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public LocalDate getFechaAlta() { return fechaAlta; }
    public Persona getPresidente() { return presidente; }
    public String getDireccion() { return direccion; }

    // Setters
    public void setFechaAlta(LocalDate fechaAlta) { this.fechaAlta = fechaAlta; }
    public void setPresidente(Persona presidente) { this.presidente = presidente; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    // Metodo para guardar en la base de datos
    public void guardar() throws SQLException {
        String sql = "INSERT INTO Club (nombre, fecha_alta, presidente_dni, direccion) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nombre);
            stmt.setDate(2, java.sql.Date.valueOf(fechaAlta));
            stmt.setString(3, presidente.getDni());
            stmt.setString(4, direccion);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                this.id = rs.getInt(1);
            }
        }
    }

    // Metodo para actualizar en la base de datos
    public void actualizar() throws SQLException {
        String sql = "UPDATE Club SET fecha_alta = ?, presidente_dni = ?, direccion = ? WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, java.sql.Date.valueOf(fechaAlta));
            stmt.setString(2, presidente.getDni());
            stmt.setString(3, direccion);
            stmt.setString(4, nombre);
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
                LocalDate fechaAlta = rs.getDate("fecha_alta").toLocalDate();
                Persona presidente = Persona.buscarPorDni(rs.getString("presidente_dni"));
                String direccion = rs.getString("direccion");
                Club club = new Club(nombre, fechaAlta, presidente, direccion);
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
                LocalDate fechaAlta = rs.getDate("fecha_alta").toLocalDate();
                Persona presidente = Persona.buscarPorDni(rs.getString("presidente_dni"));
                String direccion = rs.getString("direccion");
                Club club = new Club(nombre, fechaAlta, presidente, direccion);
                club.id = id;
                return club;
            }
        }
        return null;
    }
}