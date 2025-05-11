package proyectoffcv.logica;

import entidades.*;
import java.time.LocalDate;
import java.util.List;
import java.sql.*;

public interface IFederacion {
    Categoria nuevaCategoria(String nombre, int orden, double precioLicencia);
    Club nuevoClub(String nombre, LocalDate fechaFundacion, Persona presidente);
    Persona nuevaPersona(String dni, String nombre, String apellido1, String apellido2, LocalDate fechaNacimiento, String usuario, String password, String poblacion);
    Empleado nuevoEmpleado(String dni, String nombre, String apellido1, String apellido2, LocalDate fechaNacimiento, String usuario, String password, String poblacion, int numEmpleado, LocalDate inicioContrato, String segSocial);
    Instalacion nuevaInstalacion(String nombre, String direccion, String superficie);
    Grupo nuevoGrupo(Categoria categoria, String nombre);
    Equipo nuevoEquipo(String letra, Instalacion instalacion, Grupo grupo, Club club);
    Licencia nuevaLicencia(Persona jugador, Equipo equipo, LocalDate fechaInicio, LocalDate fechaFin, boolean abonada);
    Persona buscaPersona(String dni);
    Club buscarClub(String nombre);
    List<Persona> buscaPersonas(String nombre, String apellido1, String apellido2);
    List<Instalacion> buscarInstalaciones(String nombre);
    List<Categoria> obtenerCategorias();
    List<Grupo> obtenerGrupos(Categoria categoria);
    List<Licencia> obtenerLicencias(Persona jugador);
    void anadirJugadorAEquipo(Persona jugador, Equipo equipo) throws SQLException;
    double calcularPrecioLicencia(Equipo equipo);
}