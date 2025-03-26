package entidades;

import proyectoffcv.util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Persona {
    private String dni;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private LocalDate fechaNacimiento;
    private String usuario;
    private String password;
    private String poblacion;

    public Persona(String dni, String nombre, String apellido1, String apellido2, LocalDate fechaNacimiento, 
                   String usuario, String password, String poblacion) {
        setDni(dni);
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.fechaNacimiento = fechaNacimiento;
        this.usuario = usuario;
        this.password = password;
        this.poblacion = poblacion;
    }

    // Métodos de persistencia
    private void persistir() throws SQLException {
        String sql = "INSERT INTO Persona (dni, nombre, apellido1, apellido2, fechaNacimiento, usuario, password, poblacion) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dni);
            ps.setString(2, nombre);
            ps.setString(3, apellido1);
            ps.setString(4, apellido2);
            ps.setDate(5, java.sql.Date.valueOf(fechaNacimiento));
            ps.setString(6, usuario);
            ps.setString(7, password);
            ps.setString(8, poblacion);
            ps.executeUpdate();
        }
    }

    private void actualizarEnBD() throws SQLException {
        String sql = "UPDATE Persona SET nombre = ?, apellido1 = ?, apellido2 = ?, fechaNacimiento = ?, " +
                     "usuario = ?, password = ?, poblacion = ? WHERE dni = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, apellido1);
            ps.setString(3, apellido2);
            ps.setDate(4, java.sql.Date.valueOf(fechaNacimiento));
            ps.setString(5, usuario);
            ps.setString(6, password);
            ps.setString(7, poblacion);
            ps.setString(8, dni);
            ps.executeUpdate();
        }
    }

    private void eliminarDeBD() throws SQLException {
        String sql = "DELETE FROM Persona WHERE dni = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dni);
            ps.executeUpdate();
        }
    }

    public void guardar() throws SQLException {
        if (dni == null || dni.trim().isEmpty()) {
            throw new IllegalArgumentException("El DNI no puede ser nulo ni estar vacío.");
        }
        if (buscarPorDni(dni) == null) {
            persistir();
        } else {
            throw new IllegalStateException("La persona ya existe en la base de datos.");
        }
    }

    public void actualizar() throws SQLException {
        if (buscarPorDni(dni) != null) {
            actualizarEnBD();
        } else {
            throw new IllegalStateException("La persona no existe en la base de datos.");
        }
    }

    public void eliminar() throws SQLException {
        if (buscarPorDni(dni) != null) {
            eliminarDeBD();
        } else {
            throw new IllegalStateException("La persona no existe en la base de datos.");
        }
    }

    public static Persona buscarPorDni(String dni) throws SQLException {
        String sql = "SELECT * FROM Persona WHERE dni = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dni);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Persona(rs.getString("dni"), rs.getString("nombre"), rs.getString("apellido1"),
                        rs.getString("apellido2"), rs.getDate("fechaNacimiento").toLocalDate(),
                        rs.getString("usuario"), rs.getString("password"), rs.getString("poblacion"));
            }
            return null;
        }
    }

    public static List<Persona> buscarPorNombreYApellidos(String nombre, String apellido1, String apellido2) throws SQLException {
        List<Persona> resultado = new ArrayList<>();
        String sql = "SELECT * FROM Persona WHERE nombre = ? AND apellido1 = ? AND apellido2 = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, apellido1);
            ps.setString(3, apellido2);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resultado.add(new Persona(rs.getString("dni"), rs.getString("nombre"), rs.getString("apellido1"),
                        rs.getString("apellido2"), rs.getDate("fechaNacimiento").toLocalDate(),
                        rs.getString("usuario"), rs.getString("password"), rs.getString("poblacion")));
            }
            return resultado;
        }
    }

    // Getters y setters
    public String getDni() { return dni; }
    public void setDni(String dni) {
        if (dni == null || dni.trim().isEmpty()) {
            throw new IllegalArgumentException("El DNI no puede ser nulo ni estar vacío.");
        }
        this.dni = dni;
    }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido1() { return apellido1; }
    public void setApellido1(String apellido1) { this.apellido1 = apellido1; }
    public String getApellido2() { return apellido2; }
    public void setApellido2(String apellido2) { this.apellido2 = apellido2; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPoblacion() { return poblacion; }
    public void setPoblacion(String poblacion) { this.poblacion = poblacion; }

    @Override
    public String toString() {
        return "Persona{dni='" + dni + "', nombre='" + nombre + "', apellido1='" + apellido1 + "', " +
               "apellido2='" + apellido2 + "', fechaNacimiento=" + fechaNacimiento + ", poblacion='" + poblacion + "'}";
    }
}