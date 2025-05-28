package entidades;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import proyectoffcv.util.DatabaseConnection;

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

    // Método para actualizar en la base de datos (Privado)
    private void actualizarPrivado() throws SQLException {
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

    // Método para eliminar de la base de datos (Privado)
    private void eliminarPrivado() throws SQLException {
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

    // Método para buscar personas por criterios de nombre y apellidos
    public static List<Persona> buscarPorCriterios(String nombre, String apellido1, String apellido2) throws SQLException {
        List<Persona> personas = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM Persona WHERE 1=1");
        List<String> params = new ArrayList<>();

        if (nombre != null && !nombre.isEmpty()) {
            sqlBuilder.append(" AND nombre LIKE ?");
            params.add("%" + nombre + "%");
        }
        if (apellido1 != null && !apellido1.isEmpty()) {
            sqlBuilder.append(" AND apellido1 LIKE ?");
            params.add("%" + apellido1 + "%");
        }
        if (apellido2 != null && !apellido2.isEmpty()) {
            sqlBuilder.append(" AND apellido2 LIKE ?");
            params.add("%" + apellido2 + "%");
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setString(i + 1, params.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                personas.add(new Persona(
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getString("apellido1"),
                    rs.getString("apellido2"),
                    rs.getDate("fechaNacimiento").toLocalDate(),
                    rs.getString("usuario"),
                    rs.getString("password"),
                    rs.getString("poblacion")
                ));
            }
        }
        return personas;
    }

    // Método para cargar todas las personas desde la base de datos
    public static List<Persona> obtenerTodas() throws SQLException {
        List<Persona> personas = new ArrayList<>();
        String sql = "SELECT dni, nombre, apellido1, apellido2, fechaNacimiento, usuario, password, poblacion FROM Persona";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                personas.add(new Persona(
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getString("apellido1"),
                    rs.getString("apellido2"),
                    rs.getDate("fechaNacimiento").toLocalDate(),
                    rs.getString("usuario"),
                    rs.getString("password"),
                    rs.getString("poblacion")
                ));
            }
        }
        return personas;
    }

    // Método para buscar por DNI
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
        return "Persona{dni='" + dni + "', nombre='" + nombre + "', apellido1='" + apellido1 + "', apellido2='" + apellido2 + "'}";
    }
}