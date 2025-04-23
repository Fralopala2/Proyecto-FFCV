package entidades;

import proyectoffcv.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
Hecho por Oscar
*/

public class Equipo {
    private String letra;
    private Instalacion instalacion;
    private Grupo grupo;
    private int clubId;
    private List<Licencia> licencias;

    public Equipo(String letra, Instalacion instalacion, Grupo grupo) {
        this.letra = letra;
        this.instalacion = instalacion;
        this.grupo = grupo;
        this.licencias = new ArrayList<>();
    }

    private void ejecutarConsulta(boolean isInsert) throws SQLException {
        String sql = isInsert
                ? "INSERT INTO equipo (letra, instalacion_id, grupo_id, club_id) VALUES (?, ?, ?, ?)"
                : "UPDATE equipo SET instalacion_id = ?, grupo_id = ?, club_id = ? WHERE letra = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (isInsert) {
                ps.setString(1, letra);
                ps.setInt(2, obtenerIdInstalacion());
                ps.setInt(3, obtenerIdGrupo());
                ps.setInt(4, clubId);
            } else {
                ps.setInt(1, obtenerIdInstalacion());
                ps.setInt(2, obtenerIdGrupo());
                ps.setInt(3, clubId);
                ps.setString(4, letra);
            }
            int rowsAffected = ps.executeUpdate();
            if (!isInsert && rowsAffected == 0) {
                throw new SQLException("No se encontró el equipo con letra: " + letra);
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al " + (isInsert ? "persistir" : "actualizar") + " el equipo: " + ex.getMessage(), ex);
        }
    }

    private void persistir() throws SQLException {
        ejecutarConsulta(true);
    }

    private void actualizarEnBD() throws SQLException {
        ejecutarConsulta(false);
    }

    private void eliminarDeBD() throws SQLException {
        String sql = "DELETE FROM equipo WHERE letra = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, letra);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se encontró el equipo con letra: " + letra);
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar el equipo: " + ex.getMessage(), ex);
        }
    }

    public void guardar() throws SQLException {
        if (letra == null || letra.trim().isEmpty()) {
            throw new IllegalArgumentException("La letra no puede ser nula ni vacía.");
        }
        if (instalacion == null || grupo == null) {
            throw new IllegalArgumentException("Instalación y grupo no pueden ser nulos.");
        }
        if (buscarPorLetra(letra) == null) {
            persistir();
        } else {
            throw new IllegalStateException("El equipo ya existe en la base de datos.");
        }
    }

    public void actualizar() throws SQLException {
        if (buscarPorLetra(letra) != null) {
            actualizarEnBD();
        } else {
            throw new IllegalStateException("El equipo no existe en la base de datos.");
        }
    }

    public void eliminar() throws SQLException {
        if (buscarPorLetra(letra) != null) {
            eliminarDeBD();
        } else {
            throw new IllegalStateException("El equipo no existe en la base de datos.");
        }
    }

    public void setClubId(int clubId) {
        this.clubId = clubId;
    }

    public static Equipo buscarPorLetra(String letra) throws SQLException {
        String sql = "SELECT * FROM equipo WHERE letra = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, letra);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Instalacion instalacion = Instalacion.buscarPorId(rs.getInt("instalacion_id"));
                Grupo grupo = Grupo.buscarPorId(rs.getInt("grupo_id"));
                return new Equipo(rs.getString("letra"), instalacion, grupo);
            }
            return null;
        } catch (SQLException ex) {
            throw new SQLException("Error al buscar equipo por letra: " + ex.getMessage(), ex);
        }
    }

    public Persona buscarJugador(String dni) throws SQLException {
        for (Licencia licencia : cargarLicencias()) {
            if (licencia.getPersona().getDNI().equals(dni)) {
                return licencia.getPersona();
            }
        }
        return null;
    }

    private List<Licencia> cargarLicencias() throws SQLException {
        licencias.clear();
        String sql = "SELECT * FROM licencia WHERE equipo_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, obtenerIdEquipo());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Persona persona = Persona.buscaPersona(rs.getString("persona_dni"));
                if (persona == null) {
                    throw new SQLException("Persona con DNI " + rs.getString("persona_dni") + " no encontrada.");
                }
                Licencia licencia = new Licencia(persona, rs.getString("numeroLicencia"));
                licencia.setAbonada(rs.getBoolean("abonada"));
                licencias.add(licencia);
            }
            return licencias;
        } catch (SQLException ex) {
            throw new SQLException("Error al cargar licencias: " + ex.getMessage(), ex);
        }
    }

    private int obtenerIdInstalacion() throws SQLException {
        String sql = "SELECT id FROM instalacion WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, instalacion.getNombre());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            throw new SQLException("Instalación no encontrada: " + instalacion.getNombre());
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener ID de instalación: " + ex.getMessage(), ex);
        }
    }

    private int obtenerIdGrupo() throws SQLException {
        String sql = "SELECT id FROM grupo WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, grupo.getNombre());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            throw new SQLException("Grupo no encontrado: " + grupo.getNombre());
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener ID de grupo: " + ex.getMessage(), ex);
        }
    }

    private int obtenerIdEquipo() throws SQLException {
        String sql = "SELECT id FROM equipo WHERE letra = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, letra);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            throw new SQLException("Equipo no encontrado: " + letra);
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener ID de equipo: " + ex.getMessage(), ex);
        }
    }

    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public Instalacion getInstalacion() {
        return instalacion;
    }

    public void setInstalacion(Instalacion instalacion) {
        this.instalacion = instalacion;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public List<Licencia> getLicencias() {
        return licencias;
    }

    public void setLicencias(List<Licencia> licencias) {
        this.licencias = licencias;
    }

    @Override
    public String toString() {
        return "Equipo{letra='" + letra + "', instalacion=" + instalacion.getNombre() + ", grupo=" + grupo.getNombre() + ", licencias=" + licencias.size() + " licencias}";
    }
}