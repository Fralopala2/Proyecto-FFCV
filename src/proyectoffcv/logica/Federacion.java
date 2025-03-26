package proyectoffcv.logica;

import entidades.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class Federacion implements IFederacion {
    private static Federacion instancia;

    private Federacion() {
        // Constructor privado para el patron Singleton
    }

    public static Federacion getInstance() {
        if (instancia == null) {
            instancia = new Federacion();
        }
        return instancia;
    }

    /* CLUBES */
    @Override
    public Club buscarClub(String nombre) {
        try {
            return Club.buscarPorNombre(nombre);
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar club: " + e.getMessage(), e);
        }
    }

    @Override
    public Equipo nuevoEquipo(String letra, Instalacion instalacion, Grupo grupo) {
        if (letra == null || letra.trim().isEmpty()) {
            throw new IllegalArgumentException("La letra no puede ser nula ni vacía.");
        }
        if (instalacion == null || grupo == null) {
            throw new IllegalArgumentException("Instalación y grupo no pueden ser nulos.");
        }
        Equipo equipo = new Equipo(letra, instalacion, grupo);
        try {
            equipo.setClubId(1);
            equipo.guardar();
            grupo.getEquipos().add(equipo);
            return equipo;
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear equipo: " + e.getMessage(), e);
        }
    }

    @Override
    public Club nuevoClub(String nombre, LocalDate fechaFundacion, Persona presidente) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacío.");
        }
        if (presidente == null) {
            throw new IllegalArgumentException("El presidente no puede ser nulo.");
        }
        Club club = new Club(nombre, fechaFundacion, presidente);
        try {
            club.guardar();
            return club;
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear club: " + e.getMessage(), e);
        }
    }

    /* CATEGORÍAS */
    @Override
    public Categoria nuevaCategoria(String nombre, int orden, double precioLicencia) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacío.");
        }
        if (precioLicencia < 0) {
            throw new IllegalArgumentException("El precio de la licencia no puede ser negativo.");
        }
        Categoria categoria = new Categoria(nombre, orden, precioLicencia);
        try {
            categoria.guardar();
            return categoria;
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear categoría: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Categoria> obtenerCategorias() {
        try {
            return Categoria.obtenerTodas();
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener categorías: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Grupo> obtenerGrupos(Categoria c) {
        if (c == null) {
            throw new IllegalArgumentException("La categoría no puede ser nula.");
        }
        try {
            return Grupo.obtenerPorCategoria(c);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener grupos: " + e.getMessage(), e);
        }
    }

    @Override
    public Grupo nuevoGrupo(Categoria c, String nombre) {
        if (c == null) {
            throw new IllegalArgumentException("La categoría no puede ser nula.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacío.");
        }
        Grupo grupo = new Grupo(nombre);
        grupo.setCategoria(c);
        try {
            grupo.guardar();
            c.getGrupos().add(grupo);
            return grupo;
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear grupo: " + e.getMessage(), e);
        }
    }

    /* PERSONAS */
    @Override
    public Persona nuevaPersona(String dni, String nombre, String apellido1, String apellido2, 
                                LocalDate fechaNacimiento, String usuario, String password, String poblacion) {
        if (dni == null || dni.trim().isEmpty()) {
            throw new IllegalArgumentException("El DNI no puede ser nulo ni vacío.");
        }
        Persona persona = new Persona(dni, nombre, apellido1, apellido2, fechaNacimiento, usuario, password, poblacion);
        try {
            persona.guardar();
            return persona;
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear persona: " + e.getMessage(), e);
        }
    }

    @Override
    public Empleado nuevoEmpleado(String dni, String nombre, String apellido1, String apellido2, 
                                  LocalDate fechaNacimiento, String usuario, String password, String poblacion, 
                                  int numEmpleado, LocalDate inicioContrato, String segSocial) {
        if (dni == null || dni.trim().isEmpty()) {
            throw new IllegalArgumentException("El DNI no puede ser nulo ni vacío.");
        }
        Empleado empleado = new Empleado(dni, nombre, apellido1, apellido2, fechaNacimiento, usuario, password, 
                                         poblacion, "Empleado", numEmpleado, inicioContrato, segSocial);
        try {
            empleado.guardar();
            return empleado;
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear empleado: " + e.getMessage(), e);
        }
    }

    @Override
    public Persona buscaPersona(String dni) {
        if (dni == null || dni.trim().isEmpty()) {
            throw new IllegalArgumentException("El DNI no puede ser nulo ni vacío.");
        }
        try {
            return Persona.buscarPorDni(dni);
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar persona: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Persona> buscaPersonas(String nombre, String apellido1, String apellido2) {
        if (nombre == null || apellido1 == null || apellido2 == null) {
            throw new IllegalArgumentException("Los parámetros de búsqueda no pueden ser nulos.");
        }
        try {
            return Persona.buscarPorNombreYApellidos(nombre, apellido1, apellido2);
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar personas: " + e.getMessage(), e);
        }
    }

    /* LICENCIAS */
    @Override
    public Licencia nuevaLicencia(Persona p) {
        if (p == null) {
            throw new IllegalArgumentException("La persona no puede ser nula.");
        }
        String numeroLicencia = generarNumeroLicencia();
        Licencia licencia = new Licencia(p, numeroLicencia);
        try {
            licencia.guardar();
            return licencia;
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear licencia: " + e.getMessage(), e);
        }
    }

    @Override
    public Licencia nuevaLicencia(Persona p, Equipo e) {
        if (p == null || e == null) {
            throw new IllegalArgumentException("La persona y el equipo no pueden ser nulos.");
        }
        String numeroLicencia = generarNumeroLicencia();
        Licencia licencia = new Licencia(p, numeroLicencia);
        try {
            licencia.guardar();
            licencia.asignarAEquipo(e);
            return licencia;
        } catch (SQLException ex) {
            throw new RuntimeException("Error al crear licencia con equipo: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void addLicencia(Licencia l, Equipo e) {
        if (l == null || e == null) {
            throw new IllegalArgumentException("La licencia y el equipo no pueden ser nulos.");
        }
        try {
            l.asignarAEquipo(e);
        } catch (SQLException ex) {
            throw new RuntimeException("Error al añadir licencia al equipo: " + ex.getMessage(), ex);
        }
    }

    @Override
    public double calcularPrecioLicencia(Equipo e) {
        if (e == null) {
            throw new IllegalArgumentException("El equipo no puede ser nulo.");
        }
        return e.calcularPrecioLicencia();
    }

    /* INSTALACIÓN */
    @Override
    public Instalacion nuevaInstalacion(String nombre, String direccion, String superficie) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacío.");
        }
        Instalacion.TipoSuperficie tipoSuperficie;
        try {
            tipoSuperficie = Instalacion.TipoSuperficie.valueOf(superficie.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Superficie inválida: " + superficie);
        }
        Instalacion instalacion = new Instalacion(nombre, direccion, tipoSuperficie);
        try {
            instalacion.guardar();
            return instalacion;
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear instalación: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Instalacion> buscarInstalaciones(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacío.");
        }
        try {
            return Instalacion.buscarPorNombreParcial(nombre);
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar instalaciones: " + e.getMessage(), e);
        }
    }

    /* MÉTODO AUXILIAR */
    private String generarNumeroLicencia() {
        return UUID.randomUUID().toString();
    }
}