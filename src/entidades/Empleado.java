package entidades;

import java.time.LocalDate;
import java.sql.*;
import proyectoffcv.util.DatabaseConnection;

public class Empleado extends Persona {
    
    private String puesto;
    private int numEmpleado;
    private LocalDate inicioContrato;
    private String segSocial;
    
    public Empleado(int numEmpleado, LocalDate inicioContrato, String segSocial, String dni, 
                    String nombre, String apellido1, String apellido2, String usuario, 
                    String password, String poblacion, LocalDate fechaNacimiento) {
        super(dni, nombre, apellido1, apellido2, usuario, password, poblacion, fechaNacimiento);
        this.numEmpleado = numEmpleado;
        this.inicioContrato = inicioContrato;
        this.segSocial = segSocial;
        this.puesto = "Sin puesto";
        validarDatos();
    }

    private void validarDatos() {
        if (numEmpleado <= 0) throw new IllegalArgumentException("Número de empleado debe ser positivo");
        if (inicioContrato == null) throw new IllegalArgumentException("Fecha de contrato requerida");
        if (segSocial == null || segSocial.trim().isEmpty()) {
            throw new IllegalArgumentException("Número de seguridad social requerido");
        }
        if (inicioContrato.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Fecha de contrato no puede ser futura");
        }
    }

    public static Empleado nuevoEmpleado(String dni, String nombre, String apellido1, String apellido2,
                                       LocalDate fechaNacimiento, String usuario, String password,
                                       String poblacion, int numEmpleado, LocalDate inicioContrato,
                                       String segSocial) throws SQLException {
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                // Verificación en tabla empleado
                if (existeRegistro(conn, "SELECT 1 FROM empleado WHERE dni = ?", dni)) {
                    throw new IllegalStateException("El DNI " + dni + " ya está registrado como empleado");
                }

                // Verificación en tabla persona
                boolean personaExiste = existeRegistro(conn, "SELECT 1 FROM persona WHERE dni = ?", dni);
                
                if (!personaExiste) {
                    // Crear nueva persona
                    Persona.nuevaPersona(
                        dni, nombre, apellido1, apellido2, 
                        fechaNacimiento, usuario, password, poblacion
                    );
                } else {
                    // Verificar si los datos coinciden
                    if (!datosPersonaCoinciden(conn, dni, nombre, apellido1, apellido2)) {
                        throw new IllegalStateException("El DNI " + dni + " pertenece a otra persona");
                    }
                }

                // Crear instancia de empleado
                Empleado empleado = new Empleado(
                    numEmpleado, inicioContrato, segSocial, 
                    dni, nombre, apellido1, apellido2, 
                    usuario, password, poblacion, fechaNacimiento
                );
                
                // Persistir empleado
                empleado.persistirEmpleado(conn);
                conn.commit();
                return empleado;

            } catch (SQLException e) {
                conn.rollback();
                throw new SQLException("Error al crear empleado: " + e.getMessage(), e);
            }
        }
    }

    private static boolean existeRegistro(Connection conn, String sql, String dni) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private static boolean datosPersonaCoinciden(Connection conn, String dni, String nombre, 
                                               String apellido1, String apellido2) throws SQLException {
        String sql = "SELECT nombre, apellido1, apellido2 FROM persona WHERE dni = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nombre").equals(nombre) &&
                           rs.getString("apellido1").equals(apellido1) &&
                           (apellido2 == null || rs.getString("apellido2").equals(apellido2));
                }
                return false;
            }
        }
    }

    private void persistirEmpleado(Connection conn) throws SQLException {
        String sql = "INSERT INTO empleado (dni, puesto, numeroEmpleado, inicioContrato, segSocial) " +
                    "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, getDNI());
            ps.setString(2, puesto);
            ps.setInt(3, numEmpleado);
            ps.setDate(4, Date.valueOf(inicioContrato));
            ps.setString(5, segSocial);
            ps.executeUpdate();
        }
    }

    public void actualizar() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                // Actualizar datos de persona
                super.actualizar();
                
                // Actualizar datos de empleado
                String sql = "UPDATE empleado SET puesto = ?, numeroEmpleado = ?, " +
                            "inicioContrato = ?, segSocial = ? WHERE dni = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, puesto);
                    ps.setInt(2, numEmpleado);
                    ps.setDate(3, Date.valueOf(inicioContrato));
                    ps.setString(4, segSocial);
                    ps.setString(5, getDNI());
                    
                    if (ps.executeUpdate() == 0) {
                        throw new SQLException("Empleado no encontrado para actualizar");
                    }
                }
                
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public void eliminar() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                // Eliminar de empleado primero
                String sql = "DELETE FROM empleado WHERE dni = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, getDNI());
                    ps.executeUpdate();
                }
                
                // Luego eliminar de persona
                sql = "DELETE FROM persona WHERE dni = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, getDNI());
                    ps.executeUpdate();
                }
                
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    // Getters y Setters
    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto != null ? puesto : "Sin puesto";
    }

    public int getNumEmpleado() {
        return numEmpleado;
    }

    public void setNumEmpleado(int numEmpleado) {
        if (numEmpleado <= 0) {
            throw new IllegalArgumentException("Número de empleado debe ser positivo");
        }
        this.numEmpleado = numEmpleado;
    }

    public LocalDate getInicioContrato() {
        return inicioContrato;
    }

    public void setInicioContrato(LocalDate inicioContrato) {
        if (inicioContrato == null) {
            throw new IllegalArgumentException("Fecha de contrato requerida");
        }
        this.inicioContrato = inicioContrato;
    }

    public String getSegSocial() {
        return segSocial;
    }

    public void setSegSocial(String segSocial) {
        if (segSocial == null || segSocial.trim().isEmpty()) {
            throw new IllegalArgumentException("Número de seguridad social requerido");
        }
        this.segSocial = segSocial;
    }

    @Override
    public String toString() {
        return super.toString() + 
               "\nPuesto: " + puesto + 
               "\nNúmero Empleado: " + numEmpleado + 
               "\nInicio Contrato: " + inicioContrato + 
               "\nSeguridad Social: " + segSocial;
    }
}