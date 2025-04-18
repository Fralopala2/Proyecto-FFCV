package proyectoffcv.logica;

import proyectoffcv.util.DatabaseConnection;
import entidades.*;
import java.time.LocalDate;
import java.util.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.UUID;
import java.sql.*;

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
    }

    public static Federacion getInstance() {
        if (instancia == null) {
            instancia = new Federacion();
        }
        return instancia;
    }

    @Override
    public Club buscarClub(String nombre) {
        for (Club c : clubes) {
            if (c.getNombre().equals(nombre)) {
                return c;
            }
        }
        try {
            Club club = Club.buscarPorNombre(nombre);
            if (club != null) {
                clubes.add(club);
            }
            return club;
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Equipo nuevoEquipo(String letra, Instalacion instalacion, Grupo grupo, Club club) {
        try {
            Equipo equipo = new Equipo(letra, instalacion, grupo);
            equipo.setClubId(club.obtenerIdClub());
            equipo.guardar();
            club.getEquipos().add(equipo);
            return equipo;
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("No se pudo crear el equipo: " + ex.getMessage());
        }
    }

    @Override
    public Club nuevoClub(String nombre, LocalDate fechaFundacion, Persona presidente) {
        try {
            Club club = new Club(nombre, fechaFundacion, presidente);
            club.guardar();
            clubes.add(club);
            return club;
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("No se pudo crear el club: " + ex.getMessage());
        }
    }

    @Override
    public Categoria nuevaCategoria(String nombre, int orden, double precioLicencia) {
        try {
            Categoria c = new Categoria(nombre, orden, precioLicencia);
            c.guardar();
            categorias.add(c);
            return c;
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<>(categorias);
        }
    }

    @Override
    public List<Grupo> obtenerGrupos(Categoria c) {
        try {
            return Grupo.obtenerTodos().stream()
                    .filter(g -> g.getCategoria() != null && g.getCategoria().getNombre().equals(c.getNombre()))
                    .toList();
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<>();
        }
    }

    @Override
    public Grupo nuevoGrupo(Categoria c, String nombre) {
        try {
            // Generar un nuevo ID único para el grupo
            int nuevoId = obtenerNuevoIdGrupo();
            Grupo grupo = new Grupo(nuevoId, nombre);
            grupo.setCategoria(c);
            grupo.guardar();
            c.getGrupos().add(grupo);
            return grupo;
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("No se pudo crear el grupo: " + ex.getMessage());
        }
    }

    // Método auxiliar para obtener un nuevo ID único para Grupo
    private int obtenerNuevoIdGrupo() throws SQLException {
        String sql = "SELECT MAX(id) as max_id FROM Grupo";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("max_id") + 1;
            }
            return 1; // Si no hay grupos, empezamos con ID 1
        }
    }

    @Override
    public Persona nuevaPersona(String dni, String nombre, String apellido1, String apellido2, 
                              LocalDate fechaNacimiento, String usuario, String password, String poblacion) {
        try {
            Persona persona = Persona.nuevaPersona(dni, nombre, apellido1, apellido2, 
                                                 fechaNacimiento, usuario, password, poblacion);
            if (persona != null) {
                persona.Persistencia();
                afiliados.add(persona);
            }
            return persona;
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("No se pudo crear la persona: " + ex.getMessage());
        }
    }

    @Override
    public Empleado nuevoEmpleado(String dni, String nombre, String apellido1, String apellido2, 
                                LocalDate fechaNacimiento, String usuario, String password, String poblacion, 
                                int numEmpleado, LocalDate inicioContrato, String segSocial) {
        try {
            Empleado empleado = Empleado.nuevoEmpleado(dni, nombre, apellido1, apellido2, fechaNacimiento, 
                                                      usuario, password, poblacion, numEmpleado, 
                                                      inicioContrato, segSocial);
            if (empleado != null) {
                empleado.guardar();
                empleados.add(empleado);
            }
            return empleado;
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("No se pudo crear el empleado: " + ex.getMessage());
        }
    }

    @Override
    public Persona buscaPersona(String dni) {
        for (Persona p : afiliados) {
            if (p.getDNI().equals(dni)) {
                return p;
            }
        }
        Persona persona = null;
        try {
            persona = Persona.buscaPersona(dni);
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (persona != null) {
            afiliados.add(persona);
        }
        return persona;
    }

    @Override
    public List<Persona> buscaPersonas(String nombre, String apellido1, String apellido2) {
        try {
            return Persona.buscaPersonas(nombre, apellido1, apellido2);
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Licencia nuevaLicencia(Persona p) {
        try {
            String numeroLicencia = UUID.randomUUID().toString();
            Licencia licencia = new Licencia(p, numeroLicencia);
            licencia.guardar();
            return licencia;
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("No se pudo crear la licencia: " + ex.getMessage());
        }
    }

    @Override
    public Licencia nuevaLicencia(Persona p, Equipo e) {
        try {
            Licencia licencia = nuevaLicencia(p);
            licencia.asignarAEquipo(e);
            return licencia;
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("No se pudo asignar la licencia al equipo: " + ex.getMessage());
        }
    }

    @Override
    public void addLicencia(Licencia l, Equipo e) {
        try {
            l.asignarAEquipo(e);
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("No se pudo asignar la licencia al equipo: " + ex.getMessage());
        }
    }

    @Override
    public double calcularPrecioLicencia(Equipo e) {
        if (e != null && e.getGrupo() != null && e.getGrupo().getCategoria() != null) {
            return e.getGrupo().getCategoria().getPrecioLicencia();
        }
        return 0.0;
    }

    @Override
    public Instalacion nuevaInstalacion(String nombre, String direccion, String superficie) {
        try {
            Instalacion.TipoSuperficie tipo = Instalacion.TipoSuperficie.valueOf(superficie.toUpperCase());
            Instalacion instalacion = new Instalacion(nombre, direccion, tipo);
            instalacion.guardar();
            instalaciones.add(instalacion);
            return instalacion;
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("Tipo de superficie no válido: " + superficie);
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("No se pudo crear la instalación: " + ex.getMessage());
        }
    }

    @Override
    public List<Instalacion> buscarInstalaciones(String nombre) {
        try {
            List<Instalacion> resultado = Instalacion.buscarPorNombreParcial(nombre);
            instalaciones.addAll(resultado);
            return resultado;
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<>();
        }
    }
}