package entidades;

import proyectoffcv.util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;

public class Empleado extends Persona {
    private String puesto;
    private int numeroEmpleado;
    private LocalDate inicioContrato;
    private String segSocial;

    public Empleado(String dni, String nombre, String apellido1, String apellido2, LocalDate fechaNacimiento,
                    String usuario, String password, String poblacion, String puesto, int numeroEmpleado,
                    LocalDate inicioContrato, String segSocial) {
        super(dni, nombre, apellido1, apellido2, fechaNacimiento, usuario, password, poblacion);
        this.puesto = puesto;
        this.numeroEmpleado = numeroEmpleado;
        this.inicioContrato = inicioContrato;
        this.segSocial = segSocial;
    }

    private void persistirEmpleado() throws SQLException {
        String sql = "INSERT INTO Empleado (dni, puesto, numeroEmpleado, inicioContrato, segSocial) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, getDni());
            ps.setString(2, puesto);
            ps.setInt(3, numeroEmpleado);
            ps.setDate(4, java.sql.Date.valueOf(inicioContrato));
            ps.setString(5, segSocial);
            ps.executeUpdate();
        }
    }

    private void actualizarEnBD() throws SQLException {
        String sql = "UPDATE Empleado SET puesto = ?, numeroEmpleado = ?, inicioContrato = ?, segSocial = ? " +
                     "WHERE dni = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, puesto);
            ps.setInt(2, numeroEmpleado);
            ps.setDate(3, java.sql.Date.valueOf(inicioContrato));
            ps.setString(4, segSocial);
            ps.setString(5, getDni());
            ps.executeUpdate();
        }
    }

    @Override
    public void guardar() throws SQLException {
        if (Persona.buscarPorDni(getDni()) == null) {
            super.guardar(); 
            persistirEmpleado();
        } else {
            throw new IllegalStateException("El empleado ya existe en la base de datos.");
        }
    }

    @Override
    public void actualizar() throws SQLException {
        if (Persona.buscarPorDni(getDni()) != null) {
            super.actualizar();
            actualizarEnBD();
        } else {
            throw new IllegalStateException("El empleado no existe en la base de datos.");
        }
    }

    @Override
    public void eliminar() throws SQLException {
        if (Persona.buscarPorDni(getDni()) != null) {
            String sql = "DELETE FROM Empleado WHERE dni = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, getDni());
                ps.executeUpdate();
            }
            super.eliminar();
        } else {
            throw new IllegalStateException("El empleado no existe en la base de datos.");
        }
    }

    public static Empleado buscarPorDni(String dni) throws SQLException {
        Persona persona = Persona.buscarPorDni(dni);
        if (persona == null) return null;

        String sql = "SELECT * FROM Empleado WHERE dni = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dni);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Empleado(persona.getDni(), persona.getNombre(), persona.getApellido1(), 
                        persona.getApellido2(), persona.getFechaNacimiento(), persona.getUsuario(), 
                        persona.getPassword(), persona.getPoblacion(), rs.getString("puesto"), 
                        rs.getInt("numeroEmpleado"), rs.getDate("inicioContrato").toLocalDate(), 
                        rs.getString("segSocial"));
            }
            return null;
        }
    }

    // Getters y setters
    public String getPuesto() { return puesto; }
    public void setPuesto(String puesto) { this.puesto = puesto; }
    public int getNumeroEmpleado() { return numeroEmpleado; }
    public void setNumeroEmpleado(int numeroEmpleado) { this.numeroEmpleado = numeroEmpleado; }
    public LocalDate getInicioContrato() { return inicioContrato; }
    public void setInicioContrato(LocalDate inicioContrato) { this.inicioContrato = inicioContrato; }
    public String getSegSocial() { return segSocial; }
    public void setSegSocial(String segSocial) { this.segSocial = segSocial; }

    @Override
    public String toString() {
        return super.toString() + " - Puesto: " + puesto + ", Numero de Empleado: " + 
               numeroEmpleado + ", Inicio de Contrato: " + inicioContrato + 
               ", Seguridad Social: " + segSocial;
    }
}