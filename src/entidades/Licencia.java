/*
 * Hecho por Daniel González Cebrián 
 */
package entidades;

import proyectoffcv.util.DatabaseConnection;
import java.sql.*;
import java.util.UUID;

/**
 * Clase que representa una licencia en el sistema de la federación.
 */
public class Licencia {
    private Persona persona;
    private String numeroLicencia;
    private boolean abonada;
    private Equipo equipo;

    public Licencia(Persona persona, String numeroLicencia) {
        if (persona == null) {
            throw new IllegalArgumentException("La persona no puede ser nula.");
        }
        if (numeroLicencia == null || numeroLicencia.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de licencia no puede ser nulo ni vacío.");
        }
        this.persona = persona;
        this.numeroLicencia = numeroLicencia;
        this.abonada = false;
        this.equipo = null;
    }

    private void persistir() throws SQLException {
        String sql = "INSERT INTO Licencia (numeroLicencia, persona_dni, abonada, equipo_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, numeroLicencia);
            ps.setString(2, persona.getDNI());
            ps.setBoolean(3, abonada);
            ps.setObject(4, equipo != null ? obtenerIdEquipo(equipo) : null);
            ps.executeUpdate();
        }
    }

    private void actualizarEnBD() throws SQLException {
        String sql = "UPDATE Licencia SET persona_dni = ?, abonada = ?, equipo_id = ? WHERE numeroLicencia = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, persona.getDNI());
            ps.setBoolean(2, abonada);
            ps.setObject(3, equipo != null ? obtenerIdEquipo(equipo) : null);
            ps.setString(4, numeroLicencia);
            ps.executeUpdate();
        }
    }

    private void eliminarDeBD() throws SQLException {
        String sql = "DELETE FROM Licencia WHERE numeroLicencia = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, numeroLicencia);
            ps.executeUpdate();
        }
    }

    public void guardar() throws SQLException {
        if (buscarPorNumero(numeroLicencia) == null) {
            persistir();
        } else {
            throw new IllegalStateException("La licencia ya existe en la base de datos.");
        }
    }

    public void actualizar() throws SQLException {
        if (buscarPorNumero(numeroLicencia) != null) {
            actualizarEnBD();
        } else {
            throw new IllegalStateException("La licencia no existe en la base de datos.");
        }
    }

    public void eliminar() throws SQLException {
        if (buscarPorNumero(numeroLicencia) != null) {
            eliminarDeBD();
        } else {
            throw new IllegalStateException("La licencia no existe en la base de datos.");
        }
    }

    public static Licencia buscarPorNumero(String numeroLicencia) throws SQLException {
        String sql = "SELECT * FROM Licencia WHERE numeroLicencia = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, numeroLicencia);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Persona persona = Persona.buscaPersona(rs.getString("persona_dni"));
                if (persona == null) {
                    throw new SQLException("Persona asociada no encontrada.");
                }
                Licencia licencia = new Licencia(persona, rs.getString("numeroLicencia"));
                licencia.setAbonada(rs.getBoolean("abonada"));
                int equipoId = rs.getInt("equipo_id");
                if (!rs.wasNull()) {
                    Equipo equipo = buscarPorId(equipoId);
                    if (equipo != null) {
                        licencia.setEquipo(equipo);
                    }
                }
                return licencia;
            }
            return null;
        }
    }

    public void asignarAEquipo(Equipo equipo) throws SQLException {
        if (equipo == null) {
            throw new IllegalArgumentException("El equipo no puede ser nulo.");
        }
        this.equipo = equipo;
        actualizarEnBD();
        equipo.getLicencias().add(this);
    }

    private int obtenerIdEquipo(Equipo equipo) throws SQLException {
        String sql = "SELECT id FROM Equipo WHERE letra = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, equipo.getLetra());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            throw new SQLException("Equipo no encontrado.");
        }
    }

    // Método auxiliar para buscar equipo por ID
    private static Equipo buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Equipo WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Instalacion instalacion = Instalacion.buscarPorId(rs.getInt("instalacion_id"));
                Grupo grupo = Grupo.buscarPorId(rs.getInt("grupo_id"));
                if (instalacion == null || grupo == null) {
                    return null;
                }
                Equipo equipo = new Equipo(rs.getString("letra"), instalacion, grupo);
                equipo.setClubId(rs.getInt("club_id"));
                return equipo;
            }
            return null;
        }
    }

    // Getters y setters
    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        if (persona == null) {
            throw new IllegalArgumentException("La persona no puede ser nula.");
        }
        this.persona = persona;
    }

    public String getNumeroLicencia() {
        return numeroLicencia;
    }

    public boolean isAbonada() {
        return abonada;
    }

    public void setAbonada(boolean abonada) {
        this.abonada = abonada;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    @Override
    public String toString() {
        return "Licencia{numero='" + numeroLicencia + "', persona=" + persona.getDNI() + 
               ", abonada=" + abonada + ", equipo=" + (equipo != null ? equipo.getLetra() : "sin equipo") + "}";
    }
}