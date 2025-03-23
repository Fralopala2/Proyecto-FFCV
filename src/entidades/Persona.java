/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbformat/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import java.time.LocalDate;

/**
 *
 * @author NOX
 */
public class Persona {
    private String dni;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private LocalDate fechaNacimiento;
    private String usuario;
    private String password;
    private String poblacion;

    public Persona(String dni, String nombre, String apellido1, String apellido2, LocalDate fechaNacimiento, String usuario, String password, String poblacion) {
        setDni(dni); // Usa el setter para validar desde el constructor
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.fechaNacimiento = fechaNacimiento;
        this.usuario = usuario;
        this.password = password;
        this.poblacion = poblacion;
    }

    // Métodos
    public String getDni() {
        return dni;
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

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
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

    public void setDni(String dni) {
        if (dni == null || dni.trim().isEmpty()) {
            throw new IllegalArgumentException("El DNI no puede ser nulo ni estar vacío.");
        }
        this.dni = dni;
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

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
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

    @Override
    public String toString() {
        return "Persona{" +
                "dni='" + dni + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido1='" + apellido1 + '\'' +
                ", apellido2='" + apellido2 + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", poblacion='" + poblacion + '\'' +
                '}';
    }
}