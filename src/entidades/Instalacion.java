/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

/**
 *
 * @author NOX
 */
public class Instalacion {
    private String nombre;
    private String direccion;
    private TipoSuperficie superficie;

    // Enumerado para los tipos de superficie
    public enum TipoSuperficie {
        TIERRA, CESPED_NATURAL, CESPED_SINTETICO
    }

    public Instalacion(String nombre, String direccion, TipoSuperficie superficie) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.superficie = superficie;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public TipoSuperficie getSuperficie() {
        return superficie;
    }

    public void setSuperficie(TipoSuperficie superficie) {
        this.superficie = superficie;
    }

    @Override
    public String toString() {
        return "Instalacion{" +
                "nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", superficie='" + superficie + '\'' +
                '}';
    }
}