/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoffcv.logica;

import entidades.Categoria;
import entidades.Club;
import entidades.Empleado;
import entidades.Equipo;
import entidades.Grupo;
import entidades.Instalacion;
import entidades.Licencia;
import entidades.Persona;
import java.time.LocalDate;
import java.util.*;

/**
 *
 * @author jmontanerm
 */
public final class Federacion implements IFederacion {
    private static Federacion instancia;
    private List<Categoria> categorias;
    private List<Empleado> empleados;
    private List<Persona> afiliados;
    private List<Club> clubes;
    private Federacion() {
        this.categorias = new ArrayList<>();
        this.empleados = new ArrayList<>();
        this.afiliados = new ArrayList<>();
        this.clubes = new ArrayList<>();
    }
    
    public static Federacion getInstance() {
        if(instancia == null)
            instancia = new Federacion();
        return instancia;
    }
    
    @Override
    public Categoria nuevaCategoria(String nombre, int orden, double precioLicencia) throws InputMismatchException {
        
        Categoria c = new Categoria(nombre, orden, precioLicencia);
        this.categorias.add(c);
        return c;
    }
}
