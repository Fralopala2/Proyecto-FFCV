package proyectoffcv.logica;

import proyectoffcv.util.DatabaseConnection;
import entidades.*;
import java.time.LocalDate;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.UUID;

/**
 * Clase que implementa la lógica de la federación utilizando el patrón Singleton.
 */
public final class Federacion implements IFederacion {
    private static Federacion instancia;
    private List<Categoria> categorias;
    private List<Empleado> empleados;
    private List<Persona> afiliados;
    private List<Club> clubes;
    private List<Instalacion> instalaciones;

    /**
     * Constructor privado para el patrón Singleton.
     */
    private Federacion() {
        this.categorias = new ArrayList<>();
        this.empleados = new ArrayList<>();
        this.afiliados = new ArrayList<>();
        this.clubes = new ArrayList<>();
        this.instalaciones = new ArrayList<>();
        try {
            DatabaseConnection.getConnection(); // Verifica la conexión a la base de datos
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, "Error de conexión a la base de datos", ex);
            throw new IllegalStateException("No se pudo conectar a la base de datos: " + ex.getMessage());
        }
    }

    /**
     * Obtiene la instancia única de la federación.
     */
    public static Federacion getInstance() {
        if (instancia == null) {
            instancia = new Federacion();
        }
        return instancia;
    }

    @Override
    public Club buscarClub(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del club no puede ser nulo ni vacío.");
        }
        try {
            Club club = Club.buscarPorNombre(nombre);
            if (club != null && !clubes.contains(club)) {
                clubes.add(club);
            }
            return club;
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, "Error al buscar club", ex);
            throw new IllegalStateException("Error al buscar el club: " + ex.getMessage());
        }
    }

    @Override
    public Equipo nuevoEquipo(String letra, Instalacion instalacion, Grupo grupo, Club club) {
        if (letra == null || instalacion == null || grupo == null || club == null) {
            throw new IllegalArgumentException("Ningún parámetro puede ser nulo.");
        }
        try {
            Equipo equipo = new Equipo(letra, instalacion, grupo);
            equipo.setClubId(club.obtenerIdClub());
            equipo.guardar();
            club.addEquipo(equipo);
            return equipo;
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, "Error al crear equipo", ex);
            throw new IllegalStateException("No se pudo crear el equipo: " + ex.getMessage());
        }
    }

    @Override
    public Club nuevoClub(String nombre, LocalDate fechaFundacion, Persona presidente) {
        if (nombre == null || nombre.trim().isEmpty() || fechaFundacion == null || presidente == null) {
            throw new IllegalArgumentException("Ningún parámetro puede ser nulo o vacío.");
        }
        try {
            Club club = new Club(nombre, fechaFundacion, presidente);
            club.guardar();
            clubes.add(club);
            return club;
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, "Error al crear club", ex);
            throw new IllegalStateException("No se pudo crear el club: " + ex.getMessage());
        }
    }

    @Override
    public Categoria nuevaCategoria(String nombre, int orden, double precioLicencia) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede ser nulo ni vacío.");
        }
        if (precioLicencia < 0) {
            throw new IllegalArgumentException("El precio de la licencia no puede ser negativo.");
        }
        try {
            Categoria categoria = new Categoria(nombre, orden, precioLicencia);
            categoria.guardar();
            categorias.add(categoria);
            return categoria;
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, "Error al crear categoría", ex);
            throw new IllegalStateException("No se pudo crear la categoría: " + ex.getMessage());
        }
    }

    @Override
    public List<Categoria> obtenerCategorias() {
        try {
            List<Categoria> todasCategorias = Categoria.obtenerTodas();
            categorias.clear();
            categorias.addAll(todasCategorias);
            return new ArrayList<>(categorias);
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, "Error al obtener categorías", ex);
            throw new IllegalStateException("Error al obtener categorías: " + ex.getMessage());
        }
    }

    @Override
    public List<Grupo> obtenerGrupos(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría no puede ser nula.");
        }
        try {
            return Grupo.buscarPorCategoria(categoria);
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, "Error al obtener grupos", ex);
            throw new IllegalStateException("Error al obtener grupos: " + ex.getMessage());
        }
    }

    @Override
    public Grupo nuevoGrupo(Categoria categoria, String nombre) {
        if (categoria == null || nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Ningún parámetro puede ser nulo o vacío.");
        }
        try {
            int nuevoId = obtenerNuevoIdGrupo();
            Grupo grupo = new Grupo(nuevoId, nombre);
            grupo.setCategoria(categoria);
            grupo.guardar();
            categoria.getGrupos().add(grupo);
            return grupo;
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, "Error al crear grupo", ex);
            throw new IllegalStateException("No se pudo crear el grupo: " + ex.getMessage());
        }
    }

    /**
     * Obtiene un nuevo ID único para un grupo.
     */
    private int obtenerNuevoIdGrupo() throws SQLException {
        String sql = "SELECT MAX(id) as max_id FROM Grupo";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("max_id") + 1;
            }
            return 1; // Si no hay grupos, empezamos con ID 1
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener nuevo ID de grupo: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Persona nuevaPersona(String dni, String nombre, String apellido1, String apellido2, 
                               LocalDate fechaNacimiento, String usuario, String password, String poblacion) {
        if (dni == null || nombre == null || usuario == null || password == null || fechaNacimiento == null) {
            throw new IllegalArgumentException("Ningún parámetro obligatorio puede ser nulo.");
        }
        try {
            Persona persona = Persona.nuevaPersona(dni, nombre, apellido1, apellido2, 
                                                 fechaNacimiento, usuario, password, poblacion);
            if (persona != null && !afiliados.contains(persona)) {
                afiliados.add(persona);
            }
            return persona;
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, "Error al crear persona", ex);
            throw new IllegalStateException("No se pudo crear la persona: " + ex.getMessage());
        }
    }

    @Override
        public Empleado nuevoEmpleado(String dni, String nombre, String apellido1, String apellido2, 
                                     LocalDate fechaNacimiento, String usuario, String password, String poblacion, 
                                     int numEmpleado, LocalDate inicioContrato, String segSocial) {
            if (dni == null || nombre == null || usuario == null || password == null || 
                fechaNacimiento == null || inicioContrato == null || segSocial == null) {
                throw new IllegalArgumentException("Ningún parámetro obligatorio puede ser nulo.");
            }
            // Verificar si el DNI ya existe
            if (buscaPersona(dni) != null) {
                throw new IllegalArgumentException("El DNI ya está registrado.");
            }
            try {
                Empleado empleado = Empleado.nuevoEmpleado(dni, nombre, apellido1, apellido2, fechaNacimiento, 
                                                          usuario, password, poblacion, numEmpleado, 
                                                          inicioContrato, segSocial);
                if (empleado != null) {
                    empleado.guardar();
                    empleados.add(empleado);
                    return empleado;
                } else {
                    throw new IllegalStateException("No se pudo crear el empleado: error en Empleado.nuevoEmpleado.");
                }
            } catch (SQLException ex) {
                Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, "Error al crear empleado", ex);
                throw new IllegalStateException("No se pudo crear el empleado: " + ex.getMessage());
            }
        }

    @Override
    public Persona buscaPersona(String dni) {
        if (dni == null || dni.trim().isEmpty()) {
            throw new IllegalArgumentException("El DNI no puede ser nulo ni vacío.");
        }
        try {
            Persona persona = Persona.buscaPersona(dni);
            if (persona != null && !afiliados.contains(persona)) {
                afiliados.add(persona);
            }
            return persona;
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, "Error al buscar persona", ex);
            throw new IllegalStateException("Error al buscar persona: " + ex.getMessage());
        }
    }

    @Override
    public List<Persona> buscaPersonas(String nombre, String apellido1, String apellido2) {
        try {
            List<Persona> personas = Persona.buscaPersonas(nombre, apellido1, apellido2);
            for (Persona persona : personas) {
                if (!afiliados.contains(persona)) {
                    afiliados.add(persona);
                }
            }
            return personas;
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, "Error al buscar personas", ex);
            throw new IllegalStateException("Error al buscar personas: " + ex.getMessage());
        }
    }

    @Override
    public Licencia nuevaLicencia(Persona persona) {
        if (persona == null) {
            throw new IllegalArgumentException("La persona no puede ser nula.");
        }
        try {
            String numeroLicencia = UUID.randomUUID().toString();
            Licencia licencia = new Licencia(persona, numeroLicencia);
            licencia.guardar();
            return licencia;
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, "Error al crear licencia", ex);
            throw new IllegalStateException("No se pudo crear la licencia: " + ex.getMessage());
        }
    }

    @Override
    public Licencia nuevaLicencia(Persona persona, Equipo equipo) {
        if (persona == null || equipo == null) {
            throw new IllegalArgumentException("Ningún parámetro puede ser nulo.");
        }
        try {
            Licencia licencia = nuevaLicencia(persona);
            licencia.asignarAEquipo(equipo);
            return licencia;
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, "Error al asignar licencia", ex);
            throw new IllegalStateException("No se pudo asignar la licencia al equipo: " + ex.getMessage());
        }
    }

    @Override
    public void addLicencia(Licencia licencia, Equipo equipo) {
        if (licencia == null || equipo == null) {
            throw new IllegalArgumentException("Ningún parámetro puede ser nulo.");
        }
        try {
            licencia.asignarAEquipo(equipo);
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, "Error al asignar licencia", ex);
            throw new IllegalStateException("No se pudo asignar la licencia al equipo: " + ex.getMessage());
        }
    }

    @Override
    public double calcularPrecioLicencia(Equipo equipo) {
        if (equipo == null || equipo.getGrupo() == null || equipo.getGrupo().getCategoria() == null) {
            throw new IllegalArgumentException("El equipo o sus relaciones no pueden ser nulos.");
        }
        return equipo.getGrupo().getCategoria().getPrecioLicencia();
    }

    @Override
    public Instalacion nuevaInstalacion(String nombre, String direccion, String superficie) {
        if (nombre == null || nombre.trim().isEmpty() || direccion == null || superficie == null) {
            throw new IllegalArgumentException("Ningún parámetro puede ser nulo o vacío.");
        }
        try {
            Instalacion.TipoSuperficie tipo = Instalacion.TipoSuperficie.valueOf(superficie.toUpperCase());
            Instalacion instalacion = new Instalacion(nombre, direccion, tipo);
            instalacion.guardar();
            instalaciones.add(instalacion);
            return instalacion;
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, "Tipo de superficie no válido", ex);
            throw new IllegalStateException("Tipo de superficie no válido: " + superficie);
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, "Error al crear instalación", ex);
            throw new IllegalStateException("No se pudo crear la instalación: " + ex.getMessage());
        }
    }

    @Override
    public List<Instalacion> buscarInstalaciones(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacío.");
        }
        try {
            List<Instalacion> resultado = Instalacion.buscarPorNombreParcial(nombre);
            for (Instalacion instalacion : resultado) {
                if (!instalaciones.contains(instalacion)) {
                    instalaciones.add(instalacion);
                }
            }
            return resultado;
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, "Error al buscar instalaciones", ex);
            throw new IllegalStateException("Error al buscar instalaciones: " + ex.getMessage());
        }
    }
}