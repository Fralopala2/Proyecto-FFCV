package proyectoffcv.logica;

import entidades.*;
import proyectoffcv.util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Federacion implements IFederacion {
    private static Federacion instance;
    private List<Categoria> categorias;
    private List<Empleado> empleados;
    private List<Persona> afiliados;
    private List<Club> clubes;
    private List<Instalacion> instalaciones;

    private Federacion() {
        categorias = new ArrayList<>();
        empleados = new ArrayList<>();
        afiliados = new ArrayList<>();
        clubes = new ArrayList<>();
        instalaciones = new ArrayList<>();
    }

    public static Federacion getInstance() {
        if (instance == null) {
            instance = new Federacion();
        }
        return instance;
    }

    @Override
    public List<Club> buscarClubes(String nombre) throws SQLException {
        List<Club> clubes = new ArrayList<>();
        String sql = "SELECT c.id, c.nombre, c.fechaFundacion, c.presidente_dni, p.nombre AS presidente_nombre, p.apellido1, p.apellido2 " +
                     "FROM club c JOIN persona p ON c.presidente_dni = p.dni " +
                     "WHERE c.nombre LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nombre + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Persona presidente = new Persona(
                    rs.getString("presidente_dni"),
                    rs.getString("presidente_nombre"),
                    rs.getString("apellido1"),
                    rs.getString("apellido2"),
                    null, null, null, null
                );
                Club club = new Club(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getDate("fechaFundacion").toLocalDate(),
                    presidente
                );
                clubes.add(club);
            }
        } catch (SQLException e) {
            System.err.println("Error en buscarClubes: " + e.getMessage());
            throw e;
        }
        return clubes;
    }
    
    @Override
    public Club nuevoClub(String nombre, LocalDate fechaFundacion, Persona presidente) {
        Club club = new Club(nombre, fechaFundacion, presidente);
        return club;
    }
    
    @Override
    public Categoria nuevaCategoria(String nombre, int orden, double precioLicencia) {
        Categoria categoria = new Categoria(nombre, orden, precioLicencia);
        categorias.add(categoria);
        return categoria;
    }

    @Override
    public Persona nuevaPersona(String dni, String nombre, String apellido1, String apellido2, LocalDate fechaNacimiento, String usuario, String password, String poblacion) {
        Persona persona = new Persona(dni, nombre, apellido1, apellido2, fechaNacimiento, usuario, password, poblacion);
        afiliados.add(persona);
        return persona;
    }

    @Override
    public Empleado nuevoEmpleado(String dni, String nombre, String apellido1, String apellido2, LocalDate fechaNacimiento, String usuario, String password, String poblacion, int numEmpleado, LocalDate inicioContrato, String segSocial) {
        Empleado empleado = new Empleado(dni, nombre, apellido1, apellido2, fechaNacimiento, usuario, password, poblacion, numEmpleado, inicioContrato, segSocial);
        empleados.add(empleado);
        return empleado;
    }

    @Override
    public Instalacion nuevaInstalacion(String nombre, String direccion, String superficie) {
        Instalacion instalacion = new Instalacion(nombre, direccion, Instalacion.TipoSuperficie.valueOf(superficie));
        instalaciones.add(instalacion);
        return instalacion;
    }

    @Override
    public Grupo nuevoGrupo(Categoria categoria, String nombre) {
        return new Grupo(categoria, nombre);
    }

    @Override
    public Equipo nuevoEquipo(String letra, Instalacion instalacion, Grupo grupo, Club club) {
        return new Equipo(letra, instalacion, grupo, club);
    }

    @Override
    public Licencia nuevaLicencia(Persona jugador, Equipo equipo, LocalDate fechaInicio, LocalDate fechaFin, boolean abonada) {
        String numeroLicencia = UUID.randomUUID().toString();
        Licencia licencia = new Licencia(numeroLicencia, jugador, equipo, abonada);
        licencia.setFechaInicio(fechaInicio);
        licencia.setFechaFin(fechaFin);
        return licencia;
    }

    @Override
    public Persona buscaPersona(String dni) {
        try {
            return Persona.buscarPorDni(dni);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Club buscarClub(String nombre) {
        try {
            return Club.buscarPorNombre(nombre);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Persona> buscaPersonas(String nombre, String apellido1, String apellido2) {
        List<Persona> personas = new ArrayList<>();
        String sql = "SELECT * FROM Persona WHERE nombre LIKE ? AND apellido1 LIKE ? AND (apellido2 LIKE ? OR apellido2 IS NULL)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nombre + "%");
            stmt.setString(2, "%" + apellido1 + "%");
            stmt.setString(3, "%" + (apellido2 != null ? apellido2 : "") + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                personas.add(new Persona(
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getString("apellido1"),
                    rs.getString("apellido2"),
                    rs.getDate("fechaNacimiento").toLocalDate(),
                    rs.getString("usuario"),
                    rs.getString("password"),
                    rs.getString("poblacion")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error en buscaPersonas: " + e.getMessage());
            e.printStackTrace();
        }
        return personas;
    }

    @Override
    public List<Instalacion> buscarInstalaciones(String nombre) {
        List<Instalacion> result = new ArrayList<>();
        String sql = "SELECT * FROM Instalacion WHERE nombre LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nombre + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(new Instalacion(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("direccion"),
                    Instalacion.TipoSuperficie.valueOf(rs.getString("superficie"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Categoria> obtenerCategorias() {
        List<Categoria> result = new ArrayList<>();
        String sql = "SELECT * FROM Categoria";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                result.add(new Categoria(
                    rs.getString("nombre"),
                    rs.getInt("orden"),
                    rs.getDouble("precioLicencia")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Grupo> obtenerGrupos(Categoria categoria) {
        List<Grupo> grupos = new ArrayList<>();
        String sql = "SELECT * FROM Grupo WHERE categoria_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoria.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                grupos.add(new Grupo(categoria, rs.getString("nombre")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grupos;
    }

    @Override
    public List<Licencia> obtenerLicencias(Persona jugador) {
        List<Licencia> licencias = new ArrayList<>();
        String sql = "SELECT * FROM Licencia WHERE persona_dni = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, jugador.getDni());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Equipo equipo = rs.getInt("equipo_id") != 0 ? Equipo.buscarPorId(rs.getInt("equipo_id")) : null;
                Licencia licencia = new Licencia(
                    rs.getString("numeroLicencia"),
                    jugador,
                    equipo,
                    rs.getBoolean("abonada")
                );
                licencia.setFechaInicio(rs.getDate("fecha_inicio") != null ? rs.getDate("fecha_inicio").toLocalDate() : null);
                licencia.setFechaFin(rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toLocalDate() : null);
                licencias.add(licencia);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return licencias;
    }

    @Override
    public void anadirJugadorAEquipo(Persona jugador, Equipo equipo) throws SQLException {
        EquipoJugador equipoJugador = new EquipoJugador(equipo, jugador, LocalDateTime.now());
        equipoJugador.guardar();
    }

    @Override
    public double calcularPrecioLicencia(Equipo equipo) {
        Grupo grupo = equipo.getGrupo();
        Categoria categoria = grupo.getCategoria();
        return categoria.getPrecioLicencia();
    }

    public List<Persona> buscarJugadoresEnEquipo(Equipo equipo) throws SQLException {
        List<Persona> jugadores = new ArrayList<>();
        String sql = "SELECT p.* FROM persona p JOIN equipo_jugador ej ON p.dni = ej.dni_jugador WHERE ej.equipo_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, equipo.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                jugadores.add(new Persona(
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getString("apellido1"),
                    rs.getString("apellido2"),
                    rs.getDate("fechaNacimiento").toLocalDate(),
                    rs.getString("usuario"),
                    rs.getString("password"),
                    rs.getString("poblacion")
                ));
            }
        }
        return jugadores;
    }

    public Empleado buscaEmpleadoPorNumero(int numeroEmpleado) throws SQLException {
        String sql = "SELECT p.*, e.numeroEmpleado, e.inicioContrato, e.segSocial FROM persona p " +
                    "JOIN empleado e ON p.dni = e.dni WHERE e.numeroEmpleado = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, numeroEmpleado);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Empleado(
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getString("apellido1"),
                    rs.getString("apellido2"),
                    rs.getDate("fechaNacimiento").toLocalDate(),
                    rs.getString("usuario"),
                    rs.getString("password"),
                    rs.getString("poblacion"),
                    rs.getInt("numeroEmpleado"),
                    rs.getDate("inicioContrato").toLocalDate(),
                    rs.getString("segSocial")
                );
            }
        }
        return null;
    }
    
    @Override
    public Empleado buscaEmpleadoPorDni(String dni) throws SQLException {
        String sql = "SELECT p.*, e.numeroEmpleado, e.inicioContrato, e.segSocial, e.puesto FROM persona p " +
                    "JOIN empleado e ON p.dni = e.dni WHERE e.dni = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Empleado(
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getString("apellido1"),
                    rs.getString("apellido2"),
                    rs.getDate("fechaNacimiento").toLocalDate(),
                    rs.getString("usuario"),
                    rs.getString("password"),
                    rs.getString("poblacion"),
                    rs.getInt("numeroEmpleado"),
                    rs.getDate("inicioContrato").toLocalDate(),
                    rs.getString("segSocial")
                );
            }
        }
        return null;
    }

    public void limpiarTablas() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 0")) {
                stmt.executeUpdate();
            }
            String[] tables = {
                "licencia",
                "equipo_jugador",
                "club_equipo",
                "equipo",
                "grupo",
                "categoria",
                "instalacion",
                "club",
                "empleado",
                "persona"
            };
            for (String table : tables) {
                try (PreparedStatement stmt = conn.prepareStatement("TRUNCATE TABLE " + table)) {
                    stmt.executeUpdate();
                }
            }
            try (PreparedStatement stmt = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 1")) {
                stmt.executeUpdate();
            }
            conn.commit();
            System.out.println("Base de datos limpiada exitosamente.");
        } catch (SQLException e) {
            throw new SQLException("Error al limpiar tablas: " + e.getMessage(), e);
        }
    }

    public void limpiarListas() {
        categorias.clear();
        empleados.clear();
        afiliados.clear();
        clubes.clear();
        instalaciones.clear();
    }
}