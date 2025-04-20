/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import java.time.LocalDate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import proyectoffcv.util.DatabaseConnection;

/**
 *
 * @author david
 */
public class Empleado extends Persona {
    
    private String puesto;
    private int numEmpleado;
    private LocalDate inicioContrato;
    private String segSocial;
    
    /**
     * Constructor de Empleado.
     */
    public Empleado(int numEmpleado, LocalDate inicioContrato, String segSocial, String dni, 
                    String nombre, String apellido1, String apellido2, String usuario, 
                    String password, String poblacion, LocalDate fechaNacimiento) {
        super(dni, nombre, apellido1, apellido2, usuario, password, poblacion, fechaNacimiento);
        validateNumEmpleado(numEmpleado);
        validateInicioContrato(inicioContrato);
        validateSegSocial(segSocial);
        this.numEmpleado = numEmpleado;
        this.inicioContrato = inicioContrato;
        this.segSocial = segSocial;
        this.puesto = "";
    }

    /**
     * Valida que el número de empleado sea positivo.
     */
    private void validateNumEmpleado(int numEmpleado) {
        if (numEmpleado <= 0) {
            throw new IllegalArgumentException("El número de empleado debe ser mayor a cero.");
        }
    }

    /**
     * Valida que la fecha de inicio de contrato no sea nula.
     */
    private void validateInicioContrato(LocalDate inicioContrato) {
        if (inicioContrato == null) {
            throw new IllegalArgumentException("La fecha de inicio de contrato no puede ser nula.");
        }
    }

    /**
     * Valida que el número de seguridad social no sea nulo ni vacío.
     */
    private void validateSegSocial(String segSocial) {
        if (segSocial == null || segSocial.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de seguridad social no puede ser nulo ni vacío.");
        }
    }

    /**
     * Crea un nuevo empleado y lo persiste si no existe.
     */
    public static Empleado nuevoEmpleado(String dni, String nombre, String apellido1, String apellido2, 
                                        LocalDate fechaNacimiento, String usuario, String password, 
                                        String poblacion, int numEmpleado, LocalDate inicioContrato, 
                                        String segSocial) throws SQLException {
        if (Persona.buscaPersona(dni) != null) {
            return null; // Persona ya existe
        }
        Empleado empleado = new Empleado(numEmpleado, inicioContrato, segSocial, dni, nombre, 
                                        apellido1, apellido2, usuario, password, poblacion, fechaNacimiento);
        empleado.Persistencia();
        return empleado;
    }

    /**
     * Persiste el empleado en la base de datos.
     */
    @Override
    public void Persistencia() throws SQLException {
        // Primero persiste la información de Persona
        super.Persistencia();
        
        String sql = "INSERT INTO empleado (dni, puesto, numeroEmpleado, inicioContrato, segSocial) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, getDNI());
            ps.setString(2, puesto);
            ps.setInt(3, numEmpleado);
            ps.setDate(4, java.sql.Date.valueOf(inicioContrato));
            ps.setString(5, segSocial);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException("Error al persistir el empleado: " + ex.getMessage(), ex);
        }
    }

    /**
     * Actualiza los datos del empleado en la base de datos.
     */
    public void actualizarBaseDatos() throws SQLException {
        String sql = "UPDATE empleado SET puesto = ?, numeroEmpleado = ?, inicioContrato = ?, segSocial = ? WHERE dni = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, puesto);
            ps.setInt(2, numEmpleado);
            ps.setDate(3, java.sql.Date.valueOf(inicioContrato));
            ps.setString(4, segSocial);
            ps.setString(5, getDNI());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se encontró el empleado con DNI: " + getDNI());
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al actualizar el empleado: " + ex.getMessage(), ex);
        }
    }

    /**
     * Elimina el empleado de la base de datos.
     */
    public void eliminarBaseDatos() throws SQLException {
        String sql = "DELETE FROM empleado WHERE dni = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, getDNI());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se encontró el empleado con DNI: " + getDNI());
            }
            // También elimina la persona asociada
            sql = "DELETE FROM Persona WHERE dni = ?";
            try (PreparedStatement psPersona = conn.prepareStatement(sql)) {
                psPersona.setString(1, getDNI());
                psPersona.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar el empleado: " + ex.getMessage(), ex);
        }
    }

    /**
     * Guarda el empleado en la base de datos si no existe.
     */
    public void guardar() throws SQLException {
        validateNumEmpleado(numEmpleado);
        if (Persona.buscaPersona(getDNI()) == null) {
            Persistencia();
        } else {
            throw new IllegalStateException("El empleado ya existe en la base de datos.");
        }
    }

    /**
     * Actualiza el empleado en la base de datos si existe.
     */
    public void actualizar() throws SQLException {
        validateNumEmpleado(numEmpleado);
        if (Persona.buscaPersona(getDNI()) != null) {
            super.Persistencia(); // Actualiza la información de Persona
            actualizarBaseDatos();
        } else {
            throw new IllegalStateException("El empleado no existe en la base de datos.");
        }
    }

    /**
     * Elimina el empleado de la base de datos si existe.
     */
    public void eliminar() throws SQLException {
        validateNumEmpleado(numEmpleado);
        if (Persona.buscaPersona(getDNI()) != null) {
            eliminarBaseDatos();
        } else {
            throw new IllegalStateException("El empleado no existe en la base de datos.");
        }
    }

    // Getters y setters
    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto != null ? puesto : "";
    }

    public int getNumEmpleado() {
        return numEmpleado;
    }

    public void setNumEmpleado(int numEmpleado) {
        validateNumEmpleado(numEmpleado);
        this.numEmpleado = numEmpleado;
    }

    public LocalDate getInicioContrato() {
        return inicioContrato;
    }

    public void setInicioContrato(LocalDate inicioContrato) {
        validateInicioContrato(inicioContrato);
        this.inicioContrato = inicioContrato;
    }

    public String getSegSocial() {
        return segSocial;
    }

    public void setSegSocial(String segSocial) {
        validateSegSocial(segSocial);
        this.segSocial = segSocial;
    }

    @Override
    public String toString() {
        return super.toString() + "\nPuesto: " + puesto + "\nNúmero de Empleado: " + numEmpleado + 
               "\nInicio de Contrato: " + inicioContrato + "\nSeguridad Social: " + segSocial;
    }
}