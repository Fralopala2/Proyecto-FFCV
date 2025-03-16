/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package persona;
import java.time.LocalDate;
/**
 *
 * @author david
 */
public class Persona {
   
 private String DNI, nombre, apellido1, apellido2, usuario, password, poblacion;
 private LocalDate fechaNacimiento;
        
  public Persona(String DNI, String nombre, String apellido1, String apellido2, String usuario, String password, String poblacion, LocalDate fechanacimiento){
  this.DNI = DNI;
  this.apellido1 = apellido1;
  this.apellido2 = apellido2;
  this.fechaNacimiento = fechaNacimiento;
  this.nombre = nombre;
  this.password = password;
  this.poblacion = poblacion;
  this.usuario = usuario;
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
    
   @Override
    public String toString() {
        return nombre + " " + apellido1 + " " + apellido2 + " (" + DNI + ")";
    }   
        
   
   
   
    
    
}
