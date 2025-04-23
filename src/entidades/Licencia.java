package entidades;

import proyectoffcv.util.DatabaseConnection;
import java.sql.*;

/*
Hecho por Daniel
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
        } catch (SQLException ex) {
            throw new SQLException("Error al persistir la licencia: " + ex.getMessage(), ex);
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
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se encontró la licencia con número: " + numeroLicencia);
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al actualizar la licencia: " + ex.getMessage(), ex);
        }
    }

    private void eliminarDeBD() throws SQLException {
        String sql = "DELETE FROM Licencia WHERE numeroLicencia = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, numeroLicencia);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se encontró la licencia con número: " + numeroLicencia);
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar la licencia: " + ex.getMessage(), ex);
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
        if (numeroLicencia == null || numeroLicencia.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de licencia no puede ser nulo ni vacío.");
        }
        String sql = "SELECT * FROM Licencia WHERE numeroLicencia = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, numeroLicencia);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Persona persona = Persona.buscaPersona(rs.getString("persona_dni"));
                if (persona == null) {
                    throw new SQLException("Persona asociada con DNI " + rs.getString("persona_dni") + " no encontrada.");
                }
                Licencia licencia = new Licencia(persona, rs.getString("numeroLicencia"));
                licencia.setAbonada(rs.getBoolean("abonada"));
                int equipoId = rs.getInt("equipo_id");
                if (!rs.wasNull()) {
                    String equipoSql = "SELECT * FROM Equipo WHERE id = ?";
                    try (PreparedStatement equipoPs = conn.prepareStatement(equipoSql);
                         ResultSet equipoRs = equipoPs.executeQuery()) {
                        equipoPs.setInt(1, equipoId);
                        if (equipoRs.next()) {
                            Instalacion instalacion = Instalacion.buscarPorId(equipoRs.getInt("instalacion_id"));
                            Grupo grupo = Grupo.buscarPorId(equipoRs.getInt("grupo_id"));
                            if (instalacion == null || grupo == null) {
                                throw new SQLException("Instalación o grupo no encontrados para el equipo con ID: " + equipoId);
                            }
                            Equipo equipo = new Equipo(equipoRs.getString("letra"), instalacion, grupo);
                            licencia.setEquipo(equipo);
                        }
                    }
                }
                return licencia;
            }
            return null;
        } catch (SQLException ex) {
            throw new SQLException("Error al buscar licencia por número: " + ex.getMessage(), ex);
        }
    }

    public void asignarAEquipo(Equipo equipo) throws SQLException {
        if (equipo == null) {
            throw new IllegalArgumentException("El equipo no puede ser nulo.");
        }
        if (Equipo.buscarPorLetra(equipo.getLetra()) == null) {
            throw new SQLException("El equipo con letra " + equipo.getLetra() + " no existe en la base de datos.");
        }
        if (equipo.getGrupo() == null || equipo.getGrupo().getCategoria() == null) {
            throw new IllegalArgumentException("El equipo debe tener un grupo y una categoría válidos.");
        }
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            this.equipo = equipo;
            actualizarEnBD();
            equipo.getLicencias().add(this);
            conn.commit();
        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    throw new SQLException("Error al revertir la transacción: " + rollbackEx.getMessage(), rollbackEx);
                }
            }
            throw new SQLException("Error al asignar licencia al equipo: " + ex.getMessage(), ex);
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

    private int obtenerIdEquipo(Equipo equipo) throws SQLException {
        String sql = "SELECT id FROM Equipo WHERE letra = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
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