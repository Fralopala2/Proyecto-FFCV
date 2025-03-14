/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persona;
import java.time.LocalDate;
import persona.Persona;
/**
 *
 * @author david
 */
public class Empleado extends Persona{
    
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
    
    @Override
    
    public String toString(){
    
    return "Nombre: "+ super.getNombre() +" Primer Apellido: "+ super.getApellido1()+" Segundo Apellido: "+ super.getApellido2() +" con DNI: "+ super.getDNI()+" y con fecha de nacimiento: "+ super.getFechaNacimiento() +" Usuario: "+ super.getUsuario() +" y contraseña: "+ super.getPassword() +" y con poblacion: "+ super.getPoblacion() +" numero de empleado: "+ numEmpleado +" Fecha de inicio de contrato: "+ inicioContrato +" numero de la seguridad social: "+ segSocial;
    }
    
}
