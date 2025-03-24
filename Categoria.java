/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import java.util.*;

/**
 *
 * @author edgar
 */
public class Categoria {
    private String nombre;
    private int orden;
    private double precioLicencia;
    private List<Grupo> grupos;

    public Categoria(String nombre, int orden, double precioLicencia) throws InputMismatchException {
        this.nombre = nombre;
        this.orden = orden;
        checkPrecioLicencia(precioLicencia);
        this.precioLicencia = precioLicencia;
        this.grupos = new ArrayList<>();
    }
    
    private void checkPrecioLicencia(double p) throws InputMismatchException {
        if(p < 0.0) {
            throw new InputMismatchException("El precio de la licencia no puede ser menor que cero.");
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public double getPrecioLicencia() {
        return precioLicencia;
    }

    public void setPrecioLicencia(double precioLicencia) throws InputMismatchException {
        checkPrecioLicencia(precioLicencia);
        this.precioLicencia = precioLicencia;
    }

    public List<Grupo> getGrupos() {
        return grupos;
    }
    
    @Override
    public String toString() {
        return "Categoria{" + "nombre=" + nombre + ", orden=" + orden + ", precioLicencia=" + precioLicencia + '}';
    }
}