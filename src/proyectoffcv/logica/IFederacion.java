/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package proyectoffcv.logica;

import entidades.*;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author jmontanerm
 */
public interface IFederacion {
    /* CLUBES */
    public Club buscarClub(String nombre);
    public Equipo nuevoEquipo(String letra, Instalacion instalacion, Grupo grupo, Club club);
    public Club nuevoClub(String nombre, LocalDate fechaFundacion, Persona presidente);
    
    /* CATEGORÍAS */
    public Categoria nuevaCategoria(String nombre, int orden, double precioLicencia);
    public List<Categoria> obtenerCategorias();
    public List<Grupo> obtenerGrupos(Categoria c);
    public Grupo nuevoGrupo(Categoria c, String nombre);
    
    /* PERSONAS */
    public Persona nuevaPersona(String dni, String nombre, String apellido1, String apellido2, LocalDate fechaNacimiento, String usuario, String password, String poblacion);
    public Empleado nuevoEmpleado(String dni, String nombre, String apellido1, String apellido2, LocalDate fechaNacimiento, String usuario, String password, String poblacion, int numEmpleado, LocalDate inicioContrato, String segSocial);
    public Persona buscaPersona(String dni);
    public List<Persona> buscaPersonas(String nombre, String apellido1, String apellido2);
    
    /* LICENCIAS */
    public Licencia nuevaLicencia(Persona p);
    public Licencia nuevaLicencia(Persona p, Equipo e);
    public void addLicencia(Licencia l, Equipo e);
    public double calcularPrecioLicencia(Equipo e);
    
    /* INSTALACIÓN */
    public Instalacion nuevaInstalacion(String nombre, String direccion, String superficie);
    public List<Instalacion> buscarInstalaciones(String nombre);
}