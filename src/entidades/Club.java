package entidades;

import proyectoffcv.util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*
@author Oscar
*/


public class Club {
    private int id;
    private String nombre;
    private LocalDate fechaFundacion;
    private List<Equipo> equipos;
    private Persona presidente;

    public Club(String nombre, LocalDate fechaFundacion, Persona presidente) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacío.");
        }
        if (fechaFundacion == null) {
            throw new IllegalArgumentException("La fecha de fundación no puede ser nula.");
        }
        if (presidente == null) {
            throw new IllegalArgumentException("El presidente no puede ser nulo.");
        }
        this.nombre = nombre;
        this.fechaFundacion = fechaFundacion;
        this.equipos = new ArrayList<>();
        this.presidente = presidente;
    }

    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    private void persistir() throws SQLException {
        String sql = "INSERT INTO club (nombre, fechaFundacion, presidente_dni) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setDate(2, java.sql.Date.valueOf(fechaFundacion));
            ps.setString(3, presidente.getDNI());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException("Error al persistir el club: " + ex.getMessage(), ex);
        }
    }

    private void actualizarEnBD() throws SQLException {
        String sql = "UPDATE club SET fechaFundacion = ?, presidente_dni = ? WHERE nombre = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(fechaFundacion));
            ps.setString(2, presidente.getDNI());
            ps.setString(3, nombre);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se encontró el club con nombre: " + nombre);
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al actualizar el club: " + ex.getMessage(), ex);
        }
    }

    private void eliminarDeBD() throws SQLException {
        String sql = "DELETE FROM club WHERE nombre = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se encontró el club con nombre: " + nombre);
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar el club: " + ex.getMessage(), ex);
        }
    }

    public void guardar() throws SQLException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacío.");
        }
        if (presidente == null) {
            throw new IllegalArgumentException("El presidente no puede ser nulo.");
        }
        if (Persona.buscaPersona(presidente.getDNI()) == null) {
            throw new IllegalArgumentException("El presidente con DNI " + presidente.getDNI() + " no existe en la base de datos.");
        }
        if (buscarPorNombre(nombre) == null) {
            persistir();
        } else {
            throw new IllegalStateException("El club ya existe en la base de datos.");
        }
    }

    public void actualizar() throws SQLException {
        if (buscarPorNombre(nombre) != null) {
            actualizarEnBD();
        } else {
            throw new IllegalStateException("El club no existe en la base de datos.");
        }
    }

    public void eliminar() throws SQLException {
        if (buscarPorNombre(nombre) != null) {
            eliminarDeBD();
        } else {
            throw new IllegalStateException("El club no existe en la base de datos.");
        }
    }

    public static Club buscarPorNombre(String nombre) throws SQLException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacío.");
        }
        String sql = "SELECT * FROM club WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Persona presidente = Persona.buscaPersona(rs.getString("presidente_dni"));
                if (presidente == null) {
                    throw new SQLException("Presidente con DNI " + rs.getString("presidente_dni") + " no encontrado.");
                }
                return new Club(rs.getString("nombre"), rs.getDate("fechaFundacion").toLocalDate(), presidente);
            }
            return null;
        } catch (SQLException ex) {
            throw new SQLException("Error al buscar club por nombre: " + ex.getMessage(), ex);
        }
    }

    public void addEquipo(Equipo equipo) throws SQLException {
        if (equipo == null) {
            throw new IllegalArgumentException("El equipo no puede ser nulo.");
        }
        if (equipos.contains(equipo)) {
            throw new IllegalArgumentException("El equipo ya está en el club.");
        }
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            if (Equipo.buscarPorLetra(equipo.getLetra()) == null) {
                throw new SQLException("El equipo con letra " + equipo.getLetra() + " no existe en la base de datos.");
            }
            String checkSql = "SELECT club_id FROM club_equipo WHERE equipo_id = ?";
            try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setInt(1, obtenerIdEquipo(equipo));
                ResultSet rs = checkPs.executeQuery();
                if (rs.next()) {
                    throw new SQLException("El equipo ya está asociado a otro club.");
                }
            }
            String sql = "INSERT INTO club_equipo (club_id, equipo_id) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, obtenerIdClub());
                ps.setInt(2, obtenerIdEquipo(equipo));
                ps.executeUpdate();
            }
            equipos.add(equipo);
            conn.commit();
        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    throw new SQLException("Error al revertir la transacción: " + rollbackEx.getMessage(), rollbackEx);
                }
            }
            throw new SQLException("Error al agregar equipo al club: " + ex.getMessage(), ex);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    throw new SQLException("Error al cerrar la conexión: " + closeEx.getMessage(), closeEx);
                }
            }
        }
    }

    public void removeEquipo(Equipo equipo) throws SQLException {
        if (equipo == null) {
            throw new IllegalArgumentException("El equipo no puede ser nulo.");
        }
        if (!equipos.contains(equipo)) {
            throw new IllegalArgumentException("El equipo no está en el club.");
        }
        String sql = "DELETE FROM club_equipo WHERE club_id = ? AND equipo_id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, obtenerIdClub());
            ps.setInt(2, obtenerIdEquipo(equipo));
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se encontró la relación club-equipo para eliminar.");
            }
            equipos.remove(equipo);
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar equipo del club: " + ex.getMessage(), ex);
        }
    }

    private void cargarEquipos() throws SQLException {
        equipos.clear();
        String sql = "SELECT e.* FROM equipo e JOIN club_equipo ce ON e.id = ce.equipo_id WHERE ce.club_id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, obtenerIdClub());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Instalacion instalacion = Instalacion.buscarPorId(rs.getInt("instalacion_id"));
                Grupo grupo = Grupo.buscarPorId(rs.getInt("grupo_id"));
                if (instalacion == null || grupo == null) {
                    throw new SQLException("Instalación o grupo no encontrados para el equipo con ID: " + rs.getInt("id"));
                }
                Equipo equipo = new Equipo(rs.getString("letra"), instalacion, grupo);
                equipos.add(equipo);
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al cargar equipos del club: " + ex.getMessage(), ex);
        }
    }

    public int obtenerIdClub() throws SQLException {
        String sql = "SELECT id FROM club WHERE nombre = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, this.getNombre());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            throw new SQLException("Club con nombre " + nombre + " no encontrado.");
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener ID del club: " + ex.getMessage(), ex);
        }
    }

    private int obtenerIdEquipo(Equipo equipo) throws SQLException {
        String sql = "SELECT id FROM equipo WHERE letra = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, equipo.getLetra());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            throw new SQLException("Equipo con letra " + equipo.getLetra() + " no encontrado.");
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener ID del equipo: " + ex.getMessage(), ex);
        }
    }

    public static List<Club> obtenerTodos() throws SQLException {
        List<Club> clubes = new ArrayList<>();
        String sql = "SELECT c.nombre, c.fechaFundacion, c.presidente_dni FROM club c";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Persona presidente = Persona.buscaPersona(rs.getString("presidente_dni"));
                if (presidente == null) {
                    throw new SQLException("Presidente no encontrado para el club: " + rs.getString("nombre"));
                }
                Club club = new Club(
                    rs.getString("nombre"),
                    rs.getDate("fechaFundacion").toLocalDate(),
                    presidente
                );
                club.cargarEquipos();
                clubes.add(club);
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener todos los clubes: " + ex.getMessage(), ex);
        }
        return clubes;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaFundacion() {
        return fechaFundacion;
    }

    public void setFechaFundacion(LocalDate fechaFundacion) {
        this.fechaFundacion = fechaFundacion;
    }

    public List<Equipo> getEquipos() {
        return equipos;
    }

    public void setEquipos(List<Equipo> equipos) {
        this.equipos = equipos;
    }

    public Persona getPresidente() {
        return presidente;
    }

    public void setPresidente(Persona presidente) {
        this.presidente = presidente;
    }

    @Override
    public String toString() {
        return "Club{nombre='" + nombre + "', fechaFundacion=" + fechaFundacion + ", presidente=" + presidente.getDNI() + ", equipos=" + equipos.size() + "}";
    }
}