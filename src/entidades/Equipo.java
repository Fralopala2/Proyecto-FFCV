/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author NOX
 */
public class Equipo {
    private String letra;
    private Instalacion instalacion;
    private Grupo grupo;
    private List<Licencia> licencias;

    public Equipo(String letra, Instalacion instalacion, Grupo grupo) {
        this.letra = letra;
        this.instalacion = instalacion;
        this.grupo = grupo;
        this.licencias = new ArrayList<>();
    }

    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public Instalacion getInstalacion() {
        return instalacion;
    }

    public void setInstalacion(Instalacion instalacion) {
        this.instalacion = instalacion;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public List<Licencia> getLicencias() {
        return licencias;
    }

    public void setLicencias(List<Licencia> licencias) {
        this.licencias = licencias;
    }
    
    public Persona buscarJugador(String dni) {
        for (Licencia licencia : licencias) {
            if (licencia.getPersona().getDni().equals(dni)) {
                return licencia.getPersona();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Equipo{" + "letra='" + letra + '\'' +", instalacion=" + (instalacion != null ? instalacion.getNombre() : "null") + ", grupo=" + (grupo != null ? grupo.getNombre() : "null") +
                ", licencias=" + licencias.size() + " licencias" +'}';
    }

    public double calcularPrecioLicencia() {
        Categoria categoria = grupo.getCategoria();
        if (categoria != null) {
            return categoria.getPrecioLicencia();
        }
        return 0.0;
    }
}