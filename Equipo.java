/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author edgar
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

    @Override
    public String toString() {
        return "Equipo{" +
                "letra='" + letra + '\'' +
                ", instalacion=" + instalacion +
                ", grupo=" + grupo +
                ", licencias=" + licencias +
                '}';
    }

    public double calcularPrecioLicencia() {
        // Suponiendo que la categoría del equipo tiene un método para obtener el precio de la licencia
        Categoria categoria = grupo.getCategoria(); // Obtener la categoría del grupo al que pertenece el equipo
        if (categoria != null) {
            return categoria.getPrecioLicencia(); // Retornar el precio de la licencia de la categoría
        }
        return 0.0; // Si no hay categoría, retornar 0.0
    }
}