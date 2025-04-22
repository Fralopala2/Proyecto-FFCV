package entidades;

import proyectoffcv.util.DatabaseConnection;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author david
 */
public class Persona {

    private String dni;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String usuario;
    private String password;
    private String poblacion;
    private LocalDate fechaNacimiento;

    /**
     * Constructor de la clase Persona.
     */
    public Persona(String dni, String nombre, String apellido1, String apellido2, String usuario,
            String password, String poblacion, LocalDate fechaNacimiento) {
        validateDni(dni);
        validateString(nombre, "Nombre");
        validateString(usuario, "Usuario");
        validateString(password, "Contraseña");
        this.dni = dni;
        this.nombre = nombre;
        this.apellido1 = apellido1 != null ? apellido1 : "";
        this.apellido2 = apellido2 != null ? apellido2 : "";
        this.usuario = usuario;
        this.password = password;
        this.poblacion = poblacion != null ? poblacion : "";
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * Valida que una cadena no sea nula ni esté vacía.
     */
    private void validateString(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " no puede ser nulo ni vacío.");
        }
    }

    /**
     * Valida el formato básico del DNI (8 dígitos + 1 letra).
     */
    private void validateDni(String dni) {
        if (dni == null || dni.trim().isEmpty()) {
            throw new IllegalArgumentException("El DNI no puede ser nulo ni vacío.");
        }
        if (!dni.matches("\\d{8}[A-Za-z]")) {
            throw new IllegalArgumentException("El DNI debe tener 8 dígitos seguidos de una letra.");
        }
    }

    /**
     * Crea una nueva persona y la persiste en la base de datos.
     */
    public static Persona nuevaPersona(String dni, String nombre, String apellido1, String apellido2,
            LocalDate fechaNacimiento, String usuario, String password,
            String poblacion) throws SQLException {
        if (buscaPersona(dni) != null) {
            return null; // Persona ya existe
        }
        Persona persona = new Persona(dni, nombre, apellido1, apellido2, usuario, password, poblacion, fechaNacimiento);
        persona.persistencia();
        return persona;
    }

    /**
     * Busca una persona por su DNI en la base de datos.
     */
    public static Persona buscaPersona(String dni) throws SQLException {
        String sql = "SELECT * FROM Persona WHERE dni = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dni);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Persona(
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getString("apellido1"),
                        rs.getString("apellido2"),
                        rs.getString("usuario"),
                        rs.getString("password"),
                        rs.getString("poblacion"),
                        rs.getDate("fechaNacimiento").toLocalDate()
                );
            }
            return null;
        }
    }
    
    public void eliminar() throws SQLException {
        String sql = "DELETE FROM Persona WHERE dni = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dni);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se encontró la persona con DNI: " + dni);
            }
        }
    }

    /**
     * Busca personas por nombre y apellidos (búsqueda parcial).
     */
    public static ArrayList<Persona> buscaPersonas(String nombre, String apellido1, String apellido2) throws SQLException {
        ArrayList<Persona> busqueda = new ArrayList<>();
        String sql = "SELECT * FROM Persona WHERE nombre LIKE ? AND apellido1 LIKE ? AND apellido2 LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + (nombre != null ? nombre : "") + "%");
            ps.setString(2, "%" + (apellido1 != null ? apellido1 : "") + "%");
            ps.setString(3, "%" + (apellido2 != null ? apellido2 : "") + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                busqueda.add(new Persona(
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getString("apellido1"),
                        rs.getString("apellido2"),
                        rs.getString("usuario"),
                        rs.getString("password"),
                        rs.getString("poblacion"),
                        rs.getDate("fechaNacimiento").toLocalDate()
                ));
            }
        }
        return busqueda;
    }
    
    /**
     * Actualiza en la base de datos los datos de una persona.
     */
    
    public void actualizar() throws SQLException {
        String sql = "UPDATE Persona SET nombre = ?, apellido1 = ?, apellido2 = ?, fechaNacimiento = ?, usuario = ?, password = ?, poblacion = ? WHERE dni = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, apellido1);
            ps.setString(3, apellido2);
            ps.setDate(4, java.sql.Date.valueOf(fechaNacimiento));
            ps.setString(5, usuario);
            ps.setString(6, password);
            ps.setString(7, poblacion);
            ps.setString(8, dni);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se encontró la persona con DNI: " + dni);
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al actualizar la persona: " + ex.getMessage(), ex);
        }
    }

    /**
     * Persiste la persona en la base de datos.
     */
    public void persistencia() throws SQLException {
        String sql = "INSERT INTO Persona (dni, nombre, apellido1, apellido2, fechaNacimiento, usuario, password, poblacion) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dni);
            ps.setString(2, nombre);
            ps.setString(3, apellido1);
            ps.setString(4, apellido2);
            ps.setDate(5, java.sql.Date.valueOf(fechaNacimiento));
            ps.setString(6, usuario);
            ps.setString(7, password);
            ps.setString(8, poblacion);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException("Error al persistir la persona: " + ex.getMessage(), ex);
        }
    }

    // Getters y setters
    public String getDNI() {
        return dni;
    }

    public void setDNI(String dni) {
        validateDni(dni);
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        validateString(nombre, "Nombre");
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1 != null ? apellido1 : "";
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2 != null ? apellido2 : "";
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        validateString(usuario, "Usuario");
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        validateString(password, "Contraseña");
        this.password = password;
    }

    public String getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion != null ? poblacion : "";
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser nula.");
        }
        this.fechaNacimiento = fechaNacimiento;
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre + "\nPrimer Apellido: " + apellido1 + "\nSegundo Apellido: "
                + apellido2 + "\nDNI: " + dni + "\nFecha de nacimiento: " + fechaNacimiento
                + "\nPoblación: " + poblacion + "\nUsuario: " + usuario + "\nContraseña: " + password;
    }
}
