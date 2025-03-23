/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import java.time.LocalDate;

/**
 *
 * @author NOX
 */
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

    public String getPuesto() {
        return puesto;
    }

    public int getNumeroEmpleado() {
        return numeroEmpleado;
    }

    public LocalDate getInicioContrato() {
        return inicioContrato;
    }

    public String getSegSocial() {
        return segSocial;
    }

    // Metodos
    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public void setNumeroEmpleado(int numeroEmpleado) {
        this.numeroEmpleado = numeroEmpleado;
    }

    public void setInicioContrato(LocalDate inicioContrato) {
        this.inicioContrato = inicioContrato;
    }

    public void setSegSocial(String segSocial) {
        this.segSocial = segSocial;
    }

    @Override
    public String toString() {
        return super.toString() + " - Puesto: " + puesto + ", Numero de Empleado: " + numeroEmpleado + ", Inicio de Contrato: " + inicioContrato + ", Seguridad Social: " + segSocial;
    }
}