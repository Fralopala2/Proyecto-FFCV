package entidades;

import java.sql.*;
import java.time.LocalDate;
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
    }

    // Getters y Setters
    public int getNumEmpleado() { return numEmpleado; }
    public void setNumEmpleado(int numEmpleado) { this.numEmpleado = numEmpleado; }
    public LocalDate getInicioContrato() { return inicioContrato; }
    public void setInicioContrato(LocalDate inicioContrato) { this.inicioContrato = inicioContrato; }
    public String getSegSocial() { return segSocial; }
    public void setSegSocial(String segSocial) { this.segSocial = segSocial; }
    public String getPuesto() { return puesto; }
    public void setPuesto(String puesto) { this.puesto = puesto; }

    // Metodo para guardar en la base de datos
    @Override
    public void guardar() throws SQLException {
        super.guardar(); // Guarda los datos de Persona
        String sql = "INSERT INTO Empleado (dni, numeroEmpleado, inicioContrato, segSocial, puesto) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dni);
            stmt.setInt(2, numEmpleado);
            stmt.setDate(3, java.sql.Date.valueOf(inicioContrato));
            stmt.setString(4, segSocial);
            stmt.setString(5, puesto);
            stmt.executeUpdate();
        }
    }

    // Metodo para actualizar en la base de datos
    @Override
    public void actualizar() throws SQLException {
        super.actualizar(); // Actualiza los datos de Persona
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

    // Metodo para eliminar de la base de datos
    @Override
    public void eliminar() throws SQLException {
        String sql = "DELETE FROM Empleado WHERE dni = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dni);
            stmt.executeUpdate();
        }
        super.eliminar(); // Elimina los datos de Persona
    }

    @Override
    public String toString() {
        return super.toString() + " (Num Empleado: " + numEmpleado + ")";
    }
}