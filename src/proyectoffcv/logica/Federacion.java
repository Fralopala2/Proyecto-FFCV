package proyectoffcv.logica;

import entidades.*;
import entidades.Instalacion.TipoSuperficie;
import proyectoffcv.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public final class Federacion implements IFederacion {
    private static Federacion instancia;
    private static int contadorLicencias = 1;
    private List<Categoria> categorias;
    private List<Empleado> empleados;
    private List<Persona> personas;
    private List<Club> clubes;
    private List<Instalacion> instalaciones;
    private List<Licencia> licencias;
    
    private Federacion() {
        this.categorias = new ArrayList<>();
        this.empleados = new ArrayList<>();
        this.personas = new ArrayList<>();
        this.clubes = new ArrayList<>();
        this.instalaciones = new ArrayList<>();
        this.licencias = new ArrayList<>();

        try {
            System.out.println("Cargando personas...");
            this.personas = Persona.obtenerTodas();
            System.out.println("Cargando empleados...");
            this.empleados = Empleado.obtenerTodos();
            System.out.println("Cargando instalaciones...");
            this.instalaciones = Instalacion.obtenerTodas();
            System.out.println("Cargando categorias...");
            this.categorias = Categoria.cargarCategoriasDesdeBD();
            System.out.println("Cargando clubes...");
            this.clubes = Club.cargarClubsDesdeBD();
            System.out.println("Cargando licencias...");
            this.licencias = Licencia.obtenerTodas();
        } catch (SQLException e) {
            System.err.println("Error al cargar datos desde la base de datos: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error al cargar datos iniciales de la base de datos.", "Error de Carga", JOptionPane.ERROR_MESSAGE);
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
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del club no puede estar vacio.");
        }
        for (Club club : clubes) {
            if (club.getNombre().equalsIgnoreCase(nombre)) {
                return club;
            }
        }
        throw new RuntimeException("Error no se encontro ningun club con el nombre: " + nombre);
    }

    @Override
    public Equipo nuevoEquipo(String letra, Instalacion instalacion, Grupo grupo, Club club) throws IllegalArgumentException, RuntimeException {
        try {
            if (letra == null || letra.isBlank()) {
                throw new IllegalArgumentException("La letra del equipo no puede estar vacía.");
            }
            if (instalacion == null || grupo == null || club == null) {
                throw new IllegalArgumentException("Instalación, grupo o club no pueden ser nulos.");
            }
            Equipo nuevoEquipo = new Equipo(letra, instalacion, grupo, club);
            nuevoEquipo.guardarPublic();
            club.agregarEquipo(nuevoEquipo);
            grupo.agregarEquipo(nuevoEquipo);
            return nuevoEquipo;
        } catch (SQLException e) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, "Error al crear un nuevo equipo: " + e.getMessage(), e);
            throw new RuntimeException("Error al crear un nuevo equipo: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Club nuevoClub(String nombre, LocalDate fechaFundacion, Persona presidente) {
        try {
            Club club = new Club(nombre, fechaFundacion, presidente);
            club.guardarPublic(); // Este método intentará guardar en la BD y lanzará SQLException si hay duplicado.
            clubes.add(club); // Si se guarda correctamente, añadir a la lista en memoria.
            return club;
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry") && e.getMessage().contains("for key 'nombre'")) {
                Logger.getLogger(Federacion.class.getName()).log(Level.WARNING, "Intento de crear un club con nombre duplicado: {0}", nombre);
                throw new RuntimeException("Ya existe un club con el nombre: " + nombre, e);
            } else {
                Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, "Error al crear un nuevo club: " + e.getMessage(), e);
                throw new RuntimeException("Error al crear un nuevo club: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public Categoria nuevaCategoria(String nombre, int orden, double precioLicencia) {
        try {
            Categoria nuevaCategoria = new Categoria(nombre, orden, precioLicencia);
            nuevaCategoria.guardarPublic();
            categorias.add(nuevaCategoria);
            return nuevaCategoria;
        } catch (SQLException e) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, "Error al crear una nueva categoría: " + e.getMessage(), e);
            throw new RuntimeException("Error al crear una nueva categoría: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Categoria> obtenerCategorias() {
        return Collections.unmodifiableList(categorias);
    }

    @Override
    public List<Grupo> obtenerGrupos(Categoria c) {
        try {
            return Grupo.buscarGruposPorCategoria(c);
        } catch (SQLException e) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, "Error al obtener grupos para la categoría " + c.getNombre() + ": " + e.getMessage(), e);
            throw new RuntimeException("Error al obtener grupos para la categoría " + c.getNombre() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public Grupo nuevoGrupo(Categoria c, String nombre) {
        try {
            Grupo nuevoGrupo = new Grupo(c, nombre);
            nuevoGrupo.guardarPublic();
            c.getGrupos().add(nuevoGrupo);
            return nuevoGrupo;
        } catch (SQLException e) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, "Error al crear un nuevo grupo: " + e.getMessage(), e);
            throw new RuntimeException("Error al crear un nuevo grupo: " + e.getMessage(), e);
        }
    }

    @Override
    public Persona nuevaPersona(String dni, String nombre, String apellido1, String apellido2, LocalDate fechaNacimiento, String usuario, String password, String poblacion) {
        try {
            Persona persona = new Persona(dni, nombre, apellido1, apellido2, fechaNacimiento, usuario, password, poblacion);
            persona.guardarPublic();
            personas.add(persona);
            return persona;
        } catch (SQLException e) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, "Error al crear una nueva persona: " + e.getMessage(), e);
            throw new RuntimeException("Error al crear una nueva persona: " + e.getMessage(), e);
        }
    }

    @Override
    public Empleado nuevoEmpleado(String dni, String nombre, String apellido1, String apellido2, LocalDate fechaNacimiento, String usuario, String password, String poblacion, int numEmpleado, LocalDate inicioContrato, String segSocial) {
        try {
            Empleado empleado = new Empleado(dni, nombre, apellido1, apellido2, fechaNacimiento, usuario, password, poblacion, numEmpleado, inicioContrato, segSocial);
            empleado.guardarPublic();
            this.empleados.add(empleado);
            this.personas.add(empleado);
            return empleado;
        } catch (SQLException e) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, "Error al crear un nuevo empleado: " + e.getMessage(), e);
            throw new RuntimeException("Error al crear un nuevo empleado: " + e.getMessage(), e);
        }
    }

    @Override
    public Persona buscaPersona(String dni) {
        for (Persona i : personas) {
            if (i.getDni().equals(dni)) {
                return i;
            }
        }
        return null;
    }

    @Override
    public List<Persona> buscaPersonas(String nombre, String apellido1, String apellido2) {
        List<Persona> resultados = new ArrayList<>();
        for (Persona p : personas) {
            boolean coincideNombre = (nombre == null || nombre.isEmpty()) || p.getNombre().equalsIgnoreCase(nombre);
            boolean coincideApellido1 = (apellido1 == null || apellido1.isEmpty()) || p.getApellido1().equalsIgnoreCase(apellido1);
            boolean coincideApellido2 = (apellido2 == null || apellido2.isEmpty()) || (p.getApellido2() != null && p.getApellido2().equalsIgnoreCase(apellido2));
            if (coincideNombre && coincideApellido1 && coincideApellido2) {
                resultados.add(p);
            }
        }
        return resultados;
    }

    @Override
    public Licencia nuevaLicencia(Persona p) {
        try {
            String numeroLicencia = String.format("LIC-%06d", contadorLicencias++);
            Licencia licencia = new Licencia(numeroLicencia, p, null, false);
            licencia.guardarPublic();
            licencias.add(licencia);
            return licencia;
        } catch (SQLException e) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, "Error al crear nueva licencia para persona: " + e.getMessage(), e);
            throw new RuntimeException("Error al crear nueva licencia para persona: " + e.getMessage(), e);
        }
    }

    @Override
    public Licencia nuevaLicencia(Persona p, Equipo e) {
        try {
            String numeroLicencia = String.format("LIC-%06d", contadorLicencias++);
            Licencia licencia = new Licencia(numeroLicencia, p, e, false);
            licencia.guardarPublic();
            licencias.add(licencia);
            return licencia;
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, "Error al crear nueva licencia con equipo: " + ex.getMessage(), ex);
            throw new RuntimeException("Error al crear nueva licencia con equipo: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void addLicencia(Licencia l, Equipo e) {
        try {
            if (l.getEquipo() == null) {
                l.setEquipo(e);
                l.actualizarPublic();
            } else if (!l.getEquipo().equals(e)) {
                l.setEquipo(e);
                l.actualizarPublic();
            }
            if (!licencias.contains(l)) {
                licencias.add(l);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, "Error al añadir/actualizar licencia con equipo: " + ex.getMessage(), ex);
            throw new RuntimeException("Error al añadir/actualizar licencia con equipo: " + ex.getMessage(), ex);
        }
    }

    @Override
        public double calcularPrecioLicencia(Equipo e) {
        if (e == null || e.getGrupo() == null || e.getGrupo().getCategoria() == null) {
            throw new IllegalArgumentException("Equipo, grupo o categoría no pueden ser nulos.");
        }
        return e.getGrupo().getCategoria().getPrecioLicencia();
    }

    @Override
    public Instalacion nuevaInstalacion(String nombre, String direccion, String superficie) {
        try {
            TipoSuperficie tipoSuperficie;
            try {
                tipoSuperficie = TipoSuperficie.valueOf(superficie.toUpperCase());
            } catch (IllegalArgumentException e) {
                Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, "Superficie inválida: " + superficie + ". " + e.getMessage(), e);
                throw new RuntimeException("Superficie inválida: " + superficie + ". " + e.getMessage(), e);
            }
            Instalacion nuevaInstalacion = new Instalacion(nombre, direccion, tipoSuperficie);
            nuevaInstalacion.guardarPublic();
            instalaciones.add(nuevaInstalacion);
            return nuevaInstalacion;
        } catch (SQLException e) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, "Error al crear nueva instalación: " + e.getMessage(), e);
            throw new RuntimeException("Error al crear nueva instalación: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Instalacion> buscarInstalaciones(String nombre) {
        List<Instalacion> resultados = new ArrayList<>();
        for (Instalacion i : instalaciones) {
            if (i.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                resultados.add(i);
            }
        }
        return resultados;
    }
    
    // Métodos auxiliares para usabilidad Interfaz grafica
    public List<Club> obtenerClubes() {
        return Collections.unmodifiableList(clubes);
    }

    public List<Equipo> obtenerEquipos() {
        List<Equipo> todosLosEquipos = new ArrayList<>();
        for (Club club : clubes) {
            todosLosEquipos.addAll(club.obtenerEquipos());
        }
        return todosLosEquipos;
    }

    public List<Persona> obtenerPersonas() {
        return Collections.unmodifiableList(personas);
    }

    public List<Empleado> obtenerEmpleados() {
        return Collections.unmodifiableList(empleados);
    }

    public List<Licencia> obtenerLicencias() {
        return Collections.unmodifiableList(licencias);
    }

    public void limpiarTablas() throws FederacionException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 0")) {
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM Licencia")) { stmt.executeUpdate(); }
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM Equipo")) { stmt.executeUpdate(); }
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM Instalacion")) { stmt.executeUpdate(); }
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM Grupo")) { stmt.executeUpdate(); }
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM Categoria")) { stmt.executeUpdate(); }
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM Club")) { stmt.executeUpdate(); }
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM Empleado")) { stmt.executeUpdate(); }
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM Persona")) { stmt.executeUpdate(); }
            try (PreparedStatement stmt = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 1")) {
                stmt.executeUpdate();
            }
            conn.commit();
            limpiarListas();
        } catch (SQLException e) {
            try {
                if (DatabaseConnection.getConnection() != null) {
                    DatabaseConnection.getConnection().rollback();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, "Error al hacer rollback.", ex);
            }
            throw new FederacionException("Error al limpiar tablas de la base de datos.", e);
        }
    }

    public void limpiarListas() {
        categorias.clear();
        empleados.clear();
        personas.clear();
        clubes.clear();
        instalaciones.clear();
        licencias.clear();
        contadorLicencias = 1;
        instancia = null;
    }
}