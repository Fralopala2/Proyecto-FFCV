package entidades;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author NOX
 */

public class Grupo {
    private String nombre;
    private Categoria categoria;
    private List<Equipo> equipos;

    public Grupo(String nombre) {
        this.nombre = nombre;
        this.equipos = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("La categoria no puede ser null");
        }
        this.categoria = categoria;
    }

    public List<Equipo> getEquipos() {
        return equipos;
    }

    public void setEquipos(List<Equipo> equipos) {
        this.equipos = equipos;
    }

    @Override
    public String toString() {
        return "Grupo{" + "nombre='" + nombre + '\'' + ", categoria=" + (categoria != null ? categoria.getNombre() : "null") +
                ", equipos=" + equipos.size() + " equipos" +'}';
    }
}