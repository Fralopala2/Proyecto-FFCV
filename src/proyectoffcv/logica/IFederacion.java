package proyectoffcv.logica;

import entidades.*;
import java.time.LocalDate;
import java.util.List;
import java.sql.*;

public interface IFederacion {
    // Buscar una persona por DNI
    Persona buscaPersona(String dni);
    
    // Buscar personas por nombre y apellidos
    List<Persona> buscaPersonas(String nombre, String apellido1, String apellido2);
    
    // Buscar un club por nombre
    Club buscarClub(String nombre);
    
    // Buscar clubes por nombre con coincidencias parciales
    List<Club> buscarClubes(String nombre) throws SQLException;
    
    // Crear un nuevo equipo
    Equipo nuevoEquipo(String letra, Instalacion instalacion, Grupo grupo, Club club);
    
    // Crear una nueva categoria
    Categoria nuevaCategoria(String nombre, int orden, double precio);
    
    // Crear un nuevo grupo
    Grupo nuevoGrupo(Categoria categoria, String nombre);
    
    // Crear una nueva persona
    Persona nuevaPersona(String dni, String nombre, String apellido1, String apellido2, LocalDate fechaNacimiento, String usuario, String password, String poblacion);
    
    // Crear un nuevo empleado
    Empleado nuevoEmpleado(String dni, String nombre, String apellido1, String apellido2, LocalDate fechaNacimiento, String usuario, String password, String poblacion, int numEmpleado, LocalDate inicioContrato, String segSocial);
    
    // Crear una nueva instalacion
    Instalacion nuevaInstalacion(String nombre, String direccion, String superficie);
    
    // Buscar instalaciones por nombre
    List<Instalacion> buscarInstalaciones(String nombre);
    
    // Crear un nuevo club
    Club nuevoClub(String nombre, LocalDate fechaFundacion, Persona presidente);
    
    // Obtener todas las categorias
    List<Categoria> obtenerCategorias();
    
    // Obtener grupos de una categoria
    List<Grupo> obtenerGrupos(Categoria categoria);
    
    // Crear una nueva licencia
    Licencia nuevaLicencia(Persona jugador, Equipo equipo, LocalDate fechaInicio, LocalDate fechaFin, boolean abonada);
    
    // Obtener licencias de un jugador
    List<Licencia> obtenerLicencias(Persona jugador);
    
    // Calcular precio de una licencia
    double calcularPrecioLicencia(Equipo equipo);
    
    // Anadir un jugador a un equipo
    void anadirJugadorAEquipo(Persona jugador, Equipo equipo) throws SQLException;
}