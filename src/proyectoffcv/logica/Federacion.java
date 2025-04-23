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

public final class Federacion implements IFederacion {
    private static Federacion instancia;
    private List<Categoria> categorias;
    private List<Empleado> empleados;
    private List<Persona> afiliados;
    private List<Club> clubes;
    private List<Instalacion> instalaciones;

    private Federacion() {
        this.categorias = new ArrayList<>();
        this.empleados = new ArrayList<>();
        this.afiliados = new ArrayList<>();
        this.clubes = new ArrayList<>();
        this.instalaciones = new ArrayList<>();
        try {
            DatabaseConnection.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, "Error de conexión a la base de datos", ex);
            throw new IllegalStateException("No se pudo conectar a la base de datos: " + ex.getMessage());
        }
    }

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
            throw new IllegalStateException("Error al buscar el club: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Equipo nuevoEquipo(String letra, Instalacion instalacion, Grupo grupo, Club club) {
        if (letra == null || letra.trim().isEmpty() || instalacion == null || grupo == null || club == null) {
            throw new IllegalArgumentException("Ningún parámetro puede ser nulo o vacío.");
        }
        try {
            if (Equipo.buscarPorLetra(letra) != null) {
                throw new IllegalStateException("El equipo con letra " + letra + " ya existe.");
            }
            if (Instalacion.buscarPorNombre(instalacion.getNombre()) == null) {
                throw new IllegalStateException("La instalación " + instalacion.getNombre() + " no existe.");
            }
            if (Grupo.buscarPorNombre(grupo.getNombre()) == null) {
                throw new IllegalStateException("El grupo " + grupo.getNombre() + " no existe.");
            }
            if (Club.buscarPorNombre(club.getNombre()) == null) {
                throw new IllegalStateException("El club " + club.getNombre() + " no existe.");
            }
            Equipo equipo = new Equipo(letra, instalacion, grupo);
            equipo.setClubId(club.obtenerIdClub());
            equipo.guardar();
            club.addEquipo(equipo);
            return equipo;
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo crear el equipo: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Club nuevoClub(String nombre, LocalDate fechaFundacion, Persona presidente) {
        if (nombre == null || nombre.trim().isEmpty() || fechaFundacion == null || presidente == null) {
            throw new IllegalArgumentException("Ningún parámetro puede ser nulo o vacío.");
        }
        try {
            if (Club.buscarPorNombre(nombre) != null) {
                throw new IllegalStateException("El club con nombre " + nombre + " ya existe.");
            }
            if (Persona.buscaPersona(presidente.getDNI()) == null) {
                throw new IllegalStateException("La persona con DNI " + presidente.getDNI() + " no existe.");
            }
            Club club = new Club(nombre, fechaFundacion, presidente);
            club.guardar();
            clubes.add(club);
            return club;
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo crear el club: " + ex.getMessage(), ex);
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
            if (Categoria.buscarPorNombre(nombre) != null) {
                throw new IllegalStateException("La categoría con nombre " + nombre + " ya existe.");
            }
            Categoria categoria = new Categoria(nombre, orden, precioLicencia);
            categoria.guardar();
            categorias.add(categoria);
            return categoria;
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo crear la categoría: " + ex.getMessage(), ex);
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
            throw new IllegalStateException("Error al obtener categorías: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Grupo> obtenerGrupos(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría no puede ser nula.");
        }
        try {
            if (Categoria.buscarPorNombre(categoria.getNombre()) == null) {
                throw new IllegalStateException("La categoría " + categoria.getNombre() + " no existe.");
            }
            return Grupo.buscarPorCategoria(categoria);
        } catch (SQLException ex) {
            throw new IllegalStateException("Error al obtener grupos: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Grupo nuevoGrupo(Categoria categoria, String nombre) {
        if (categoria == null || nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Ningún parámetro puede ser nulo o vacío.");
        }
        try {
            if (Categoria.buscarPorNombre(categoria.getNombre()) == null) {
                throw new IllegalStateException("La categoría " + categoria.getNombre() + " no existe.");
            }
            if (Grupo.buscarPorNombre(nombre) != null) {
                throw new IllegalStateException("El grupo con nombre " + nombre + " ya existe.");
            }
            int nuevoId = obtenerNuevoIdGrupo();
            Grupo grupo = new Grupo(nuevoId, nombre);
            grupo.setCategoria(categoria);
            grupo.guardar();
            categoria.getGrupos().add(grupo);
            return grupo;
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo crear el grupo: " + ex.getMessage(), ex);
        }
    }

    private int obtenerNuevoIdGrupo() throws SQLException {
        String sql = "SELECT MAX(id) as max_id FROM Grupo";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("max_id") + 1;
            }
            return 1;
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
            if (Persona.buscaPersona(dni) != null) {
                throw new IllegalStateException("La persona con DNI " + dni + " ya existe.");
            }
            Persona persona = Persona.nuevaPersona(dni, nombre, apellido1, apellido2, 
                                                 fechaNacimiento, usuario, password, poblacion);
            if (persona != null && !afiliados.contains(persona)) {
                afiliados.add(persona);
            }
            return persona;
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo crear la persona: " + ex.getMessage(), ex);
        }
    }

 
    @Override
    public Empleado nuevoEmpleado(String dni, String nombre, String apellido1, String apellido2,
                            LocalDate fechaNacimiento, String usuario, String password,
                            String poblacion, int numEmpleado, LocalDate inicioContrato,
                            String segSocial) {
        // Validación básica de parámetros
        if (dni == null || dni.trim().isEmpty()) {
            throw new IllegalArgumentException("DNI no puede estar vacío");
        }
        if (numEmpleado <= 0) {
            throw new IllegalArgumentException("Número de empleado debe ser positivo");
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Verificar si el número de empleado ya existe
            String checkNumEmp = "SELECT dni FROM empleado WHERE numeroEmpleado = ?";
            try (PreparedStatement ps = conn.prepareStatement(checkNumEmp)) {
                ps.setInt(1, numEmpleado);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        throw new IllegalStateException("El número de empleado " + numEmpleado + " ya está registrado");
                    }
                }
            }

            // Verificar si el DNI ya existe como empleado
            String checkDni = "SELECT dni FROM empleado WHERE dni = ?";
            try (PreparedStatement ps = conn.prepareStatement(checkDni)) {
                ps.setString(1, dni);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        throw new IllegalStateException("El DNI " + dni + " ya está registrado como empleado");
                    }
                }
            }

            // Crear el empleado usando el método estático de la clase Empleado
            Empleado empleado = Empleado.nuevoEmpleado(
                dni, nombre, apellido1, apellido2,
                fechaNacimiento, usuario, password,
                poblacion, numEmpleado, inicioContrato, segSocial
            );

            if (empleado != null) {
                empleados.add(empleado);
            }
            return empleado;
        } catch (SQLException e) {
            throw new IllegalStateException("Error al crear empleado: " + e.getMessage());
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
            throw new IllegalStateException("Error al buscar persona: " + ex.getMessage(), ex);
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
            throw new IllegalStateException("Error al buscar personas: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Licencia nuevaLicencia(Persona persona) {
        if (persona == null) {
            throw new IllegalArgumentException("La persona no puede ser nula.");
        }
        try {
            if (Persona.buscaPersona(persona.getDNI()) == null) {
                throw new IllegalStateException("La persona con DNI " + persona.getDNI() + " no existe.");
            }
            String sql = "SELECT * FROM Licencia WHERE persona_dni = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, persona.getDNI());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    throw new IllegalStateException("La persona con DNI " + persona.getDNI() + " ya tiene una licencia.");
                }
            }
            String numeroLicencia = UUID.randomUUID().toString();
            Licencia licencia = new Licencia(persona, numeroLicencia);
            licencia.guardar();
            return licencia;
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo crear la licencia: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Licencia nuevaLicencia(Persona persona, Equipo equipo) {
        if (persona == null || equipo == null) {
            throw new IllegalArgumentException("Ningún parámetro puede ser nulo.");
        }
        try {
            if (Persona.buscaPersona(persona.getDNI()) == null) {
                throw new IllegalStateException("La persona con DNI " + persona.getDNI() + " no existe.");
            }
            if (Equipo.buscarPorLetra(equipo.getLetra()) == null) {
                throw new IllegalStateException("El equipo con letra " + equipo.getLetra() + " no existe.");
            }
            Licencia licencia = nuevaLicencia(persona);
            licencia.asignarAEquipo(equipo);
            return licencia;
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo asignar la licencia al equipo: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void addLicencia(Licencia licencia, Equipo equipo) {
        if (licencia == null || equipo == null) {
            throw new IllegalArgumentException("Ningún parámetro puede ser nulo.");
        }
        try {
            if (Licencia.buscarPorNumero(licencia.getNumeroLicencia()) == null) {
                throw new IllegalStateException("La licencia con número " + licencia.getNumeroLicencia() + " no existe.");
            }
            if (Equipo.buscarPorLetra(equipo.getLetra()) == null) {
                throw new IllegalStateException("El equipo con letra " + equipo.getLetra() + " no existe.");
            }
            licencia.asignarAEquipo(equipo);
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo asignar la licencia al equipo: " + ex.getMessage(), ex);
        }
    }

    @Override
    public double calcularPrecioLicencia(Equipo equipo) {
        if (equipo == null || equipo.getGrupo() == null || equipo.getGrupo().getCategoria() == null) {
            throw new IllegalArgumentException("El equipo o sus relaciones no pueden ser nulos.");
        }
        try {
            if (Equipo.buscarPorLetra(equipo.getLetra()) == null) {
                throw new IllegalStateException("El equipo con letra " + equipo.getLetra() + " no existe.");
            }
            return equipo.getGrupo().getCategoria().getPrecioLicencia();
        } catch (SQLException ex) {
            throw new IllegalStateException("Error al calcular precio de la licencia: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Instalacion nuevaInstalacion(String nombre, String direccion, String superficie) {
        if (nombre == null || nombre.trim().isEmpty() || direccion == null || superficie == null) {
            throw new IllegalArgumentException("Ningún parámetro puede ser nulo o vacío.");
        }
        try {
            if (Instalacion.buscarPorNombre(nombre) != null) {
                throw new IllegalStateException("La instalación con nombre " + nombre + " ya existe.");
            }

            // Convertir la superficie a mayúsculas y validar que sea un tipo válido
            superficie = superficie.toUpperCase();
            try {
                Instalacion.TipoSuperficie tipo = Instalacion.TipoSuperficie.valueOf(superficie);
                Instalacion instalacion = new Instalacion(nombre, direccion, tipo);
                instalacion.guardar();
                instalaciones.add(instalacion);
                return instalacion;
            } catch (IllegalArgumentException ex) {
                throw new IllegalStateException("Tipo de superficie no válido: " + superficie + 
                    ". Los valores válidos son: " + Arrays.toString(Instalacion.TipoSuperficie.values()));
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo crear la instalación: " + ex.getMessage(), ex);
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
            throw new IllegalStateException("Error al buscar instalaciones: " + ex.getMessage(), ex);
        }
    }
	
    // Método adicional no definido en la interfaz
    public void limpiarTablas() throws SQLException {
        // Orden de eliminación: Primero tablas con FK, luego independientes
        List<String> tablas = Arrays.asList(
            "Licencia",    // Depende de Persona y Equipo
            "Equipo",     // Depende de Instalacion, Grupo y Club
            "Empleado",   // Depende de Persona
            "Club",       // Depende de Persona (presidente)
            "Grupo",      // Depende de Categoria
            "Instalacion", 
            "Categoria",
            "Persona"     // Se borra al final para evitar errores de FK
        );

        try (Connection conn = DatabaseConnection.getConnection()) {
            // 1. Desactivar verificación de claves foráneas (MySQL/MariaDB)
            try (PreparedStatement ps = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 0")) {
                ps.execute();
            }

            // 2. Borrar datos en orden
            for (String tabla : tablas) {
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM " + tabla)) {
                    System.out.println("[DEBUG] Borrando datos de: " + tabla);
                    ps.executeUpdate();
                }
            }

            // 3. Reactivar verificación de FK
            try (PreparedStatement ps = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 1")) {
                ps.execute();
            }

            // Limpiar las listas en memoria
            categorias.clear();
            empleados.clear();
            afiliados.clear();
            clubes.clear();
            instalaciones.clear();

            System.out.println("[DEBUG] ¡Base de datos limpiada exitosamente!");
        }
    }
}
