/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;
import java.time.LocalDate;
import java.sql.Connection;
import proyectoffcv.util.DatabaseConnection;
import java.sql.*;

/**
 *
 * @author david
 */
public class Empleado extends Persona{
    
    private String puesto;
    private int numEmpleado;
    private LocalDate inicioContrato;
    private String segSocial;
    
    
    public Empleado(int numEmpleado, LocalDate inicioContrato, String segSocial, String DNI, String nombre, String apellido1, String apellido2, String usuario, String password, String poblacion, LocalDate fechanacimiento){
    super(DNI, nombre, apellido1, apellido2, usuario, password, poblacion, fechanacimiento);
    this.inicioContrato = inicioContrato;
    this.numEmpleado = numEmpleado;
    this.segSocial = segSocial;
    this.puesto = puesto;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }
    
    public int getNumEmpleado() {
        return numEmpleado;
    }

    public LocalDate getInicioContrato() {
        return inicioContrato;
    }

    public String getSegSocial() {
        return segSocial;
    }

    public void setNumEmpleado(int numEmpleado) {
        this.numEmpleado = numEmpleado;
    }

    public void setInicioContrato(LocalDate inicioContrato) {
        this.inicioContrato = inicioContrato;
    }

    public void setSegSocial(String segSocial) {
        this.segSocial = segSocial;
    }
    
    public static Empleado nuevoEmpleado(String dni, String nombre, String apellido1, String apellido2, LocalDate fechaNacimiento, String usuario, String password, String poblacion, int numEmpleado, LocalDate inicioContrato, String segSocial){
    
        if (Persona.buscaPersona(dni) == null) {
            return new Empleado(numEmpleado, inicioContrato, segSocial, dni, nombre, apellido1, apellido2, usuario, password, poblacion, fechaNacimiento);
        }
        return null;
    
    }
    @Override
    public void Persistencia()throws SQLException{
     
     String consulta = "INSERT INTO empleado dni, puesto, numeroEmpleado, inicioContrato, segSocial)"+ " VALUES (?, ?, ?, ?, ?)";
     
    try(Connection conn = DatabaseConnection.getConnection();
    
     PreparedStatement valor = conn.prepareStatement(consulta)){
     
     valor.setString(1, getDNI());       
     valor.setString(2, puesto);        
     valor.setInt(3, numEmpleado);
     valor.setDate(4, java.sql.Date.valueOf(inicioContrato));
     valor.setString(5, segSocial);
    
     valor.executeUpdate();
    }
    }

    public void actualizarBaseDatos() throws SQLException {
        String sql = "UPDATE empleado SET dni = ?, numeroEmpleado = ? WHERE dni = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, getDNI());
            ps.setString(2, puesto);
            ps.setInt(3, numEmpleado);
            ps.setDate(4, java.sql.Date.valueOf(inicioContrato));
            ps.setString(5, segSocial);
            
            ps.executeUpdate();
        }
    }

    public void eliminarBaseDatos() throws SQLException {
        String sql = "DELETE FROM empleado WHERE dni = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, getDNI());
            ps.setString(2, puesto);
            ps.setInt(3, numEmpleado);
            ps.setDate(4, java.sql.Date.valueOf(inicioContrato));
            ps.setString(5, segSocial);
            
            ps.executeUpdate();
        }
    }
    
        public void guardar() throws SQLException {
    if (numEmpleado <= 0) {
        throw new IllegalArgumentException("El número de empleado no puede ser menor o igual a cero");
    }else {
        throw new IllegalStateException("Este numero de empleado ya existe en la base de datos");
    }
}

    public void actualizar() throws SQLException {
    if (numEmpleado <= 0) {
        throw new IllegalArgumentException("El número de empleado debe ser mayor a cero.");
    }

    if (buscaPersona(String.valueOf(numEmpleado)) != null) {
        actualizarBaseDatos();
    } else {
        throw new IllegalStateException("La categoría no existe en la base de datos.");
    }
}
    
    public void eliminar() throws SQLException {
    if (numEmpleado <= 0) {
        throw new IllegalArgumentException("El número de empleado debe ser mayor a cero.");
    }

    if (buscaPersona(String.valueOf(numEmpleado)) != null) {
        eliminarBaseDatos();
    } else {
        throw new IllegalStateException("La categoría no existe en la base de datos.");
    }
}
    
    @Override
    public String toString() {
        return super.toString() + " - Puesto: " + puesto + ", Número de Empleado: " + numEmpleado + ", Inicio de Contrato: " + inicioContrato + ", Seguridad Social: " + segSocial;
    }
    
    
}
