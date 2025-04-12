/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persona;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.DriverManager;
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
    
    private void Persistencia()throws SQLException{
     
     String consulta = "INSERT INTO Persona (DNI, puesto, numEmpleado, inicioContrato, segSocial)"+ " VALUES (?, ?, ?, ?, ?)";
     
    try(Connection conn = conexionBaseDatos.getConnection();
    
     PreparedStatement valor = conn.prepareStatement(consulta)){
     
     valor.setString(1, getDNI());       
     valor.setString(2, puesto);        
     valor.setInt(3, numEmpleado);
     valor.setDate(4, java.sql.Date.valueOf(inicioContrato));
     valor.setString(5, segSocial);
    
     valor.executeUpdate();
    }
    }
    
    @Override
    public String toString() {
        return super.toString() + " - Puesto: " + puesto + ", Número de Empleado: " + numEmpleado + ", Inicio de Contrato: " + inicioContrato + ", Seguridad Social: " + segSocial;
    }
    
}
