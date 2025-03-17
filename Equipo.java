/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author oscpincer
 */
public class Equipo extends Club{
    private String letra;
    private Grupo grupo;
    private Instalacion instalacion;
    private List<Licencia> licencias;

    public Equipo(String letra, Grupo grupo, Instalacion instalacion, Club club) {
        if (grupo.getEquipos().size() >= 20) {
            throw new IllegalArgumentException("El grupo ya tiene 20 equipos, no se pueden añadir mas.");
        }
        this.letra = letra;
        this.grupo = grupo;
        this.instalacion = instalacion;
        this.licencias = new ArrayList<>();
        grupo.agregarEquipo(this);
        club.agregarEquipo(this);
    }

    public String getLetra() {
        return letra;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    @Override
    public String toString() {
        return "Equipo " + letra + " - Grupo: " + grupo.getNombre();
    }
}
