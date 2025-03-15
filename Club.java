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
public class Club{
    private String nombre;
    private LocalDate fechaFundacion;
    private Persona presidente;
    private Persona secretario;
    private List<Equipo> equipos;

    public Club(String nombre, LocalDate fechaFundacion, Persona presidente, Persona secretario) {
        if (LogicaDeNegocio.getInstance().existeClub(nombre)) {
            throw new IllegalArgumentException("Ya existe un club con este nombre.");
        }
        this.nombre = nombre;
        this.fechaFundacion = fechaFundacion;
        this.presidente = presidente;
        this.secretario = secretario;
        this.equipos = new ArrayList<>();
        LogicaDeNegocio.getInstance().registrarClub(this);
    }

    public void agregarEquipo(Equipo equipo) {
        for (Equipo e : equipos) {
            if (e.getGrupo().equals(equipo.getGrupo())) {
                throw new IllegalArgumentException("No puede haber dos equipos del mismo club en el mismo grupo.");
            }
        }
        equipos.add(equipo);
    }

    public String getNombre() {
        return nombre;
    }

    public List<Equipo> getEquipos() {
        return equipos;
    }

    @Override
    public String toString() {
        return "Club: " + nombre + ", Fundado en: " + fechaFundacion;
    }
}
