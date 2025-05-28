package entidades;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import proyectoffcv.util.DatabaseConnection;

public class Empleado extends Persona {
    private int numEmpleado;
    private LocalDate inicioContrato;
    private String segSocial;
    private String puesto;

    // Constructor
    public Empleado(String dni, String nombre, String apellido1, String apellido2, LocalDate fechaNacimiento,
                    String usuario, String password, String poblacion, int numEmpleado, LocalDate inicioContrato,
                    String segSocial) {
        super(dni, nombre, apellido1, apellido2, fechaNacimiento, usuario, password, poblacion);
        this.numEmpleado = numEmpleado;
        this.inicioContrato = inicioContrato;
        this.segSocial = segSocial;
        this.puesto = puesto == null ? "" : puesto;
    }

    // Getters
    public int getNumEmpleado() { return numEmpleado; }
    public LocalDate getInicioContrato() { return inicioContrato; }
    public String getSegSocial() { return segSocial; }

    // Métodos públicos que llaman a los privados
    @Override
    public void guardarPublic() throws SQLException {
        guardarPrivado();
    }

    @Override
    public void actualizarPublic() throws SQLException {
        actualizarPrivado();
    }

    @Override
    public void eliminarPublic() throws SQLException {
        eliminarPrivado();
    }

    // Método para guardar en la base de datos (Privado)
    private void guardarPrivado() throws SQLException {
        super.guardarPublic();
        String sql = "INSERT INTO Empleado (dni, numeroEmpleado, inicioContrato, segSocial, puesto) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, getDni());
            stmt.setInt(2, numEmpleado);
            stmt.setDate(3, java.sql.Date.valueOf(inicioContrato));
            stmt.setString(4, segSocial);
            stmt.setString(5, puesto);
            stmt.executeUpdate();
        }
    }

    // Método para actualizar en la base de datos (Privado)
    private void actualizarPrivado() throws SQLException {
        super.actualizarPublic();
        String sql = "UPDATE Empleado SET numeroEmpleado = ?, inicioContrato = ?, segSocial = ?, puesto = ? WHERE dni = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, numEmpleado);
            stmt.setDate(2, java.sql.Date.valueOf(inicioContrato));
            stmt.setString(3, segSocial);
            stmt.setString(4, puesto);
            stmt.setString(5, dni);
            stmt.executeUpdate();
        }
    }

    // Método para eliminar de la base de datos (Privado)
    private void eliminarPrivado() throws SQLException {
        String sqlEmpleado = "DELETE FROM Empleado WHERE dni = ?";
        String sqlPersona = "DELETE FROM Persona WHERE dni = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmtEmpleado = conn.prepareStatement(sqlEmpleado);
             PreparedStatement stmtPersona = conn.prepareStatement(sqlPersona)) {
            stmtEmpleado.setString(1, getDni());
            stmtEmpleado.executeUpdate();
            stmtPersona.setString(1, getDni());
            stmtPersona.executeUpdate();
        }
    }

    // Método para cargar todos los empleados desde la base de datos
    public static List<Empleado> obtenerTodos() throws SQLException {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT p.dni, p.nombre, p.apellido1, p.apellido2, p.fechaNacimiento, p.usuario, p.password, p.poblacion, e.numeroEmpleado, e.inicioContrato, e.segSocial, e.puesto FROM Persona p JOIN Empleado e ON p.dni = e.dni";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                empleados.add(new Empleado(
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getString("apellido1"),
                    rs.getString("apellido2"),
                    rs.getDate("fechaNacimiento").toLocalDate(),
                    rs.getString("usuario"),
                    rs.getString("password"),
                    rs.getString("poblacion"),
                    rs.getInt("numeroEmpleado"),
                    rs.getDate("inicioContrato").toLocalDate(),
                    rs.getString("segSocial")
                ));
            }
        }
        return empleados;
    }

    @Override
    public String toString() {
        return "Empleado{dni='" + dni + "', nombre='" + nombre + "', numEmpleado=" + numEmpleado + "}";
    }
}