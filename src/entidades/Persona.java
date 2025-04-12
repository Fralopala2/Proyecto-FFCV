/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package persona;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
/**
 *
 * @author david
 */
public class Persona {
   
 private String DNI, nombre, apellido1, apellido2, usuario, password, poblacion;
 private LocalDate fechaNacimiento;
 private static ArrayList<Persona> personas;
        
  public Persona(String DNI, String nombre, String apellido1, String apellido2, String usuario, String password, String poblacion, LocalDate fechanacimiento){
  this.DNI = DNI;
  this.apellido1 = apellido1;
  this.apellido2 = apellido2;
  this.fechaNacimiento = fechanacimiento;
  this.nombre = nombre;
  this.password = password;
  this.poblacion = poblacion;
  this.usuario = usuario;
  this.personas = new ArrayList<>();
  }      

    public String getDNI() {
        return DNI;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getPassword() {
        return password;
    }

    public String getPoblacion() {
        return poblacion;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setDNI(String DNI) {
        
    if(DNI == null || DNI.trim().isEmpty()){
        System.out.println("El campo esta vacío o es nulo");
    }    
      this.DNI = DNI;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public static ArrayList<Persona> getPersonas() {
        return personas;
    }

    public static void setPersonas(ArrayList<Persona> personas) {
        Persona.personas = personas;
    }
    
    
    
    
    
    public static Persona nuevaPersona(String dni, String nombre, String apellido1, String apellido2, LocalDate fechaNacimiento, String usuario, String password, String poblacion){
        
    if (buscaPersona(dni) == null){
            return new Persona(dni, nombre, apellido1, apellido2, usuario, password, poblacion, fechaNacimiento);
        }
    
        return null; 
    }
    
    public static Persona buscaPersona(String dni){
        
        for (int i = 0; i < personas.size(); i++){
            
            if (personas.get(i).DNI.equals(dni)) {
                return personas.get(i);
            }
        }
        return null;
    }
    
     public static ArrayList<Persona> buscaPersonas(String nombre, String apellido1, String apellido2) {
        ArrayList<Persona> busqueda = new ArrayList<>();
        for (int i = 0; i < personas.size(); i++) {
            Persona persona = personas.get(i);
            if (persona.nombre != null && persona.apellido1 != null && persona.apellido2 != null) {
                busqueda.add(persona);
            }
        }
        return busqueda;
    }
     
     private void Persistencia()throws SQLException{
     
     String consulta = "INSERT INTO Persona (DNI, apellido1, apellido2, fechaNacimiento, nombre, password, poblacion, usuario)"+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
     
    try(Connection conn = conexionBaseDatos.getConnection();
    
     PreparedStatement valor = conn.prepareStatement(consulta)){
     
     valor.setString(1, DNI);       
     valor.setString(2, nombre);        
     valor.setString(3, apellido1);
     valor.setString(4, apellido2);
     valor.setDate(5, java.sql.Date.valueOf(fechaNacimiento));
     valor.setString(6, usuario);
     valor.setString(7, password);
     valor.setString(8, poblacion);
    
     valor.executeUpdate();
    }
    }
    
   @Override
    public String toString() {
        return nombre + " " + apellido1 + " " + apellido2 + " (" + DNI + ")";
    }   
        
   
   
   
    
    
}
