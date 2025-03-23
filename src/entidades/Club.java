/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author NOX
 */

public class Club {
    private String nombre;
    private LocalDate fechaFundacion;
    private List<Equipo> equipos;
    private Persona presidente;

    public Club(String nombre, LocalDate fechaFundacion, Persona presidente) {
        this.nombre = nombre;
        this.fechaFundacion = fechaFundacion;
        this.presidente = presidente;
        this.equipos = new ArrayList<>();
    }

    // Metodos
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaFundacion() {
        return fechaFundacion;
    }

    public void setFechaFundacion(LocalDate fechaFundacion) {
        this.fechaFundacion = fechaFundacion;
    }

    public Persona getPresidente() {
        return presidente;
    }

    public void setPresidente(Persona presidente) {
        this.presidente = presidente;
    }

    public List<Equipo> getEquipos() {
        return equipos;
    }

    public void addEquipo(Equipo equipo) {
        if (!equipos.contains(equipo)) {
            equipos.add(equipo);
        } else {
            throw new IllegalArgumentException("El equipo ya esta en el club.");
        }
    }

    public void removeEquipo(Equipo equipo) {
        equipos.remove(equipo);
    }

    public int getNumeroEquipos() {
        return equipos.size();
    }
    
    @Override
    public String toString() {
        return "Club{" +
                "nombre='" + nombre + '\'' +
                ", fechaFundacion=" + fechaFundacion +
                ", presidente=" + presidente +
                ", equipos=" + equipos +
                '}';
    }
}
