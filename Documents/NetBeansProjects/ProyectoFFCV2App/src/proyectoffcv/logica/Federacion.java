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
public class Federacion implements IFederacion {
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
    public Club nuevoClub(String nombre, LocalDate fechaFundacion, Persona presidente) {
        Club club = new Club(nombre, fechaFundacion, presidente);
        this.clubes.add(club);
        return club;
    }

    @Override
    public Categoria nuevaCategoria(String nombre, int orden, double precioLicencia) throws InputMismatchException {
        Categoria c = new Categoria(nombre, orden, precioLicencia);
        this.categorias.add(c);
        return c;
    }

    @Override
    public Club buscarClub(String nombre) {
        for (Club club : clubes) {
            if (club.getNombre().equalsIgnoreCase(nombre)) {
                return club;
            }
        }
        return null;
    }

    @Override
    public Equipo nuevoEquipo(String letra, Instalacion instalacion, Grupo grupo) {
        Equipo equipo = new Equipo(letra, instalacion, grupo);
        grupo.getEquipos().add(equipo);
        return equipo;
    }

    @Override
    public List<Categoria> obtenerCategorias() {
        return new ArrayList<>(categorias);
    }

    @Override
    public List<Grupo> obtenerGrupos(Categoria c) {
        if (c != null && c.getGrupos() != null) {
            return new ArrayList<>(c.getGrupos());
        }
        return new ArrayList<>();
    }

    @Override
    public Grupo nuevoGrupo(Categoria c, String nombre) {
        if (c == null) {
            throw new IllegalArgumentException("La categoria no puede ser null");
        }
        Grupo grupo = new Grupo(nombre);
        grupo.setCategoria(c);
        c.getGrupos().add(grupo);
        return grupo;
    }

    @Override
    public Persona nuevaPersona(String dni, String nombre, String apellido1, String apellido2, LocalDate fechaNacimiento, String usuario, String password, String poblacion) {
        Persona persona = new Persona(dni, nombre, apellido1, apellido2, fechaNacimiento, usuario, password, poblacion);
        this.afiliados.add(persona);
        return persona;
    }

    @Override
    public Empleado nuevoEmpleado(String dni, String nombre, String apellido1, String apellido2, LocalDate fechaNacimiento, String usuario, String password, String poblacion, int numEmpleado, LocalDate inicioContrato, String segSocial) {
        Empleado empleado = new Empleado(dni, nombre, apellido1, apellido2, fechaNacimiento, usuario, password, poblacion,"Empleado", numEmpleado, inicioContrato, segSocial); 
        this.empleados.add(empleado);
        return empleado;
    }

    @Override
    public Persona buscaPersona(String dni) {
        for (Persona persona : afiliados) {
            if (persona.getDni().equals(dni)) {
                return persona;
            }
        }
        return null;
    }

    @Override
    public List<Persona> buscaPersonas(String nombre, String apellido1, String apellido2) {
        List<Persona> resultado = new ArrayList<>();
        for (Persona persona : afiliados) {
            if (persona.getNombre().equalsIgnoreCase(nombre) &&
                persona.getApellido1().equalsIgnoreCase(apellido1) &&
                persona.getApellido2().equalsIgnoreCase(apellido2)) {
                resultado.add(persona);
            }
        }
        return resultado;
    }

    @Override
    public Licencia nuevaLicencia(Persona p) {
        Licencia licencia = new Licencia(p, generarNumeroLicencia());
        return licencia;
    }

    @Override
    public Licencia nuevaLicencia(Persona p, Equipo e) {
        Licencia licencia = new Licencia(p, generarNumeroLicencia());
        addLicencia(licencia, e);
        return licencia;
    }

    @Override
    public void addLicencia(Licencia l, Equipo e) {
        e.getLicencias().add(l);
    }

    @Override
    public double calcularPrecioLicencia(Equipo e) {
        Categoria categoria = e.getGrupo().getCategoria();
        if (categoria != null) {
            return categoria.getPrecioLicencia();
        }
        return 0.0;
    }

    @Override
    public Instalacion nuevaInstalacion(String nombre, String direccion, String superficie) {
        Instalacion.TipoSuperficie tipoSuperficie;
        try {
            tipoSuperficie = Instalacion.TipoSuperficie.valueOf(superficie.toUpperCase());
        } catch (IllegalArgumentException e) {
            tipoSuperficie = Instalacion.TipoSuperficie.CESPED_NATURAL;
        }
        if (buscarInstalaciones(nombre).isEmpty()) {
            Instalacion instalacion = new Instalacion(nombre, direccion, tipoSuperficie);
            this.instalaciones.add(instalacion);
            return instalacion;
        }
        return buscarInstalaciones(nombre).get(0);
    }

    @Override
    public List<Instalacion> buscarInstalaciones(String nombre) {
        List<Instalacion> instalacionesEncontradas = new ArrayList<>();
        for (Instalacion instalacion : instalaciones) {
            if (instalacion.getNombre().equalsIgnoreCase(nombre)) {
                instalacionesEncontradas.add(instalacion);
            }
        }
        return instalacionesEncontradas;
    }

    private String generarNumeroLicencia() {
        return UUID.randomUUID().toString();
    }
}