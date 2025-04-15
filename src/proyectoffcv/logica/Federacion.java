/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoffcv.logica;

import entidades.*;
import java.time.LocalDate;
import java.util.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.UUID;

/**
 *
 * @author Paco
 */
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
            club.addEquipo(equipo);
            return equipo;
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("No se pudo crear el equipo.");
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
            throw new IllegalStateException("No se pudo crear el club.");
        }
    }

    @Override
    public Categoria nuevaCategoria(String nombre, int orden, double precioLicencia) {
        try {
            Categoria c = new Categoria(nombre, orden, precioLicencia);
            categorias.add(c);
            return c;
        } catch (Exception ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("No se pudo crear la categoría.");
        }
    }

    @Override
    public List<Categoria> obtenerCategorias() {
        return new ArrayList<>(categorias);
    }

    @Override
    public List<Grupo> obtenerGrupos(Categoria c) {
        return new ArrayList<>(c.getGrupos());
    }

    @Override
    public Grupo nuevoGrupo(Categoria c, String nombre) {
        Grupo grupo = new Grupo(nombre);
        grupo.setCategoria(c);
        c.agregarGrupo(grupo);
        return grupo;
    }

    @Override
    public Persona nuevaPersona(String dni, String nombre, String apellido1, String apellido2, LocalDate fechaNacimiento, String usuario, String password, String poblacion) {
        Persona persona = Persona.nuevaPersona(dni, nombre, apellido1, apellido2, fechaNacimiento, usuario, password, poblacion);
        if (persona != null) {
            try {
                persona.Persistencia();
                afiliados.add(persona);
            } catch (SQLException ex) {
                Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, null, ex);
                throw new IllegalStateException("No se pudo persistir la persona.");
            }
        }
        return persona;
    }

    @Override
    public Empleado nuevoEmpleado(String dni, String nombre, String apellido1, String apellido2, LocalDate fechaNacimiento, String usuario, String password, String poblacion, int numEmpleado, LocalDate inicioContrato, String segSocial) {
        Empleado empleado = Empleado.nuevoEmpleado(dni, nombre, apellido1, apellido2, fechaNacimiento, usuario, password, poblacion, numEmpleado, inicioContrato, segSocial);
        if (empleado != null) {
            try {
                empleado.Persistencia();
                empleados.add(empleado);
            } catch (SQLException ex) {
                Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, null, ex);
                throw new IllegalStateException("No se pudo persistir el empleado.");
            }
        }
        return empleado;
    }

    @Override
    public Persona buscaPersona(String dni) {
        for (Persona p : afiliados) {
            if (p.getDNI().equals(dni)) {
                return p;
            }
        }
        Persona persona = Persona.buscaPersona(dni);
        if (persona != null) {
            afiliados.add(persona);
        }
        return persona;
    }

    @Override
    public List<Persona> buscaPersonas(String nombre, String apellido1, String apellido2) {
        return Persona.buscaPersonas(nombre, apellido1, apellido2);
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
            throw new IllegalStateException("No se pudo crear la licencia.");
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
            throw new IllegalStateException("No se pudo asignar la licencia al equipo.");
        }
    }

    @Override
    public void addLicencia(Licencia l, Equipo e) {
        try {
            l.asignarAEquipo(e);
        } catch (SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("No se pudo asignar la licencia al equipo.");
        }
    }

    @Override
    public double calcularPrecioLicencia(Equipo e) {
        Grupo grupo = e.getGrupo();
        if (grupo != null && grupo.getCategoria() != null) {
            return grupo.getCategoria().getPrecioLicencia();
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
        } catch (IllegalArgumentException | SQLException ex) {
            Logger.getLogger(Federacion.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("No se pudo crear la instalación.");
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