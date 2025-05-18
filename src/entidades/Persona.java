package entidades;

import java.sql.*;
import proyectoffcv.util.DatabaseConnection;
import java.time.LocalDate;

public class Persona {
    protected String dni;
    protected String nombre;
    protected String apellido1;
    protected String apellido2;
    protected LocalDate fechaNacimiento;
    protected String usuario;
    protected String password;
    protected String poblacion;

    // Constructor
    public Persona(String dni, String nombre, String apellido1, String apellido2, LocalDate fechaNacimiento, String usuario, String password, String poblacion) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.fechaNacimiento = fechaNacimiento;
        this.usuario = usuario;
        this.password = password;
        this.poblacion = poblacion;
    }

    // Getters
    public String getDni() { return dni; }
    public String getNombre() { return nombre; }
    public String getApellido1() { return apellido1; }
    public String getApellido2() { return apellido2; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public String getUsuario() { return usuario; }
    public String getPassword() { return password; }
    public String getPoblacion() { return poblacion; }

    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido1(String apellido1) { this.apellido1 = apellido1; }
    public void setApellido2(String apellido2) { this.apellido2 = apellido2; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public void setPassword(String password) { this.password = password; }
    public void setPoblacion(String poblacion) { this.poblacion = poblacion; }

    // Metodo para guardar en la base de datos
    public void guardar() throws SQLException {
        String sql = "INSERT INTO Persona (dni, nombre, apellido1, apellido2, fechaNacimiento, usuario, password, poblacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dni);
            stmt.setString(2, nombre);
            stmt.setString(3, apellido1);
            stmt.setString(4, apellido2);
            stmt.setDate(5, java.sql.Date.valueOf(fechaNacimiento));
            stmt.setString(6, usuario);
            stmt.setString(7, password);
            stmt.setString(8, poblacion);
            stmt.executeUpdate();
        }
    }

    // Metodo para actualizar en la base de datos
    public void actualizar() throws SQLException {
        String sql = "UPDATE Persona SET nombre = ?, apellido1 = ?, apellido2 = ?, fechaNacimiento = ?, usuario = ?, password = ?, poblacion = ? WHERE dni = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setString(2, apellido1);
            stmt.setString(3, apellido2);
            stmt.setDate(4, java.sql.Date.valueOf(fechaNacimiento));
            stmt.setString(5, usuario);
            stmt.setString(6, password);
            stmt.setString(7, poblacion);
            stmt.setString(8, dni);
            stmt.executeUpdate();
        }
    }

    // Metodo para eliminar de la base de datos
    public void eliminar() throws SQLException {
        String sqlLicencia = "DELETE FROM Licencia WHERE persona_dni = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmtLicencia = conn.prepareStatement(sqlLicencia)) {
            stmtLicencia.setString(1, dni);
            stmtLicencia.executeUpdate();
        }
        String sql = "DELETE FROM Persona WHERE dni = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dni);
            stmt.executeUpdate();
        }
    }

    // Metodo para buscar por DNI
    public static Persona buscarPorDni(String dni) throws SQLException {
        String sql = "SELECT * FROM Persona WHERE dni = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Persona(
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getString("apellido1"),
                    rs.getString("apellido2"),
                    rs.getDate("fechaNacimiento").toLocalDate(),
                    rs.getString("usuario"),
                    rs.getString("password"),
                    rs.getString("poblacion")
                );
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return nombre + " " + apellido1 + " " + (apellido2 != null ? apellido2 : "") + " (DNI: " + dni + ")";
    }
}