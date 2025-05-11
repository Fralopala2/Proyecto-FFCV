package entidades;

import java.sql.*;
import proyectoffcv.util.DatabaseConnection;

public class Licencia {
    private String numeroLicencia;
    private Persona jugador;
    private Equipo equipo;
    private boolean abonada;

    // Constructor
    public Licencia(String numeroLicencia, Persona jugador, Equipo equipo, boolean abonada) {
        this.numeroLicencia = numeroLicencia;
        this.jugador = jugador;
        this.equipo = equipo;
        this.abonada = abonada;
    }

    // Getters y Setters
    public String getNumeroLicencia() { return numeroLicencia; }
    public Persona getJugador() { return jugador; }
    public Equipo getEquipo() { return equipo; }
    public void setEquipo(Equipo equipo) { this.equipo = equipo; }
    public boolean isAbonada() { return abonada; }
    public void setAbonada(boolean abonada) { this.abonada = abonada; }

    // Metodo para guardar en la base de datos
    public void guardar() throws SQLException {
        String sql = "INSERT INTO Licencia (numeroLicencia, persona_dni, equipo_id, abonada) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, numeroLicencia);
            stmt.setString(2, jugador.getDni());
            if (equipo != null) {
                stmt.setInt(3, equipo.getId());
            } else {
                stmt.setNull(3, java.sql.Types.INTEGER);
            }
            stmt.setBoolean(4, abonada);
            stmt.executeUpdate();
        }
    }

    // Metodo para actualizar en la base de datos
    public void actualizar() throws SQLException {
        String sql = "UPDATE Licencia SET persona_dni = ?, equipo_id = ?, abonada = ? WHERE numeroLicencia = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, jugador.getDni());
            if (equipo != null) {
                stmt.setInt(2, equipo.getId());
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }
            stmt.setBoolean(3, abonada);
            stmt.setString(4, numeroLicencia);
            stmt.executeUpdate();
        }
    }

    // Metodo para eliminar de la base de datos
    public void eliminar() throws SQLException {
        String sql = "DELETE FROM Licencia WHERE numeroLicencia = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, numeroLicencia);
            stmt.executeUpdate();
        }
    }

    // Metodo para buscar por numeroLicencia
    public static Licencia buscarPorNumeroLicencia(String numeroLicencia) throws SQLException {
        String sql = "SELECT * FROM Licencia WHERE numeroLicencia = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, numeroLicencia);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Persona jugador = Persona.buscarPorDni(rs.getString("persona_dni"));
                Equipo equipo = rs.getInt("equipo_id") != 0 ? Equipo.buscarPorId(rs.getInt("equipo_id")) : null;
                return new Licencia(
                    rs.getString("numeroLicencia"),
                    jugador,
                    equipo,
                    rs.getBoolean("abonada")
                );
            }
        }
        return null;
    }

    // Nuevo metodo para buscar por jugador y equipo
    public static Licencia buscarPorJugadorYEquipo(String dniJugador, int equipoId) throws SQLException {
        String sql = "SELECT * FROM Licencia WHERE persona_dni = ? AND equipo_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dniJugador);
            stmt.setInt(2, equipoId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Persona jugador = Persona.buscarPorDni(rs.getString("persona_dni"));
                Equipo equipo = Equipo.buscarPorId(rs.getInt("equipo_id"));
                return new Licencia(
                    rs.getString("numeroLicencia"),
                    jugador,
                    equipo,
                    rs.getBoolean("abonada")
                );
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Licencia{numeroLicencia='" + numeroLicencia + "', jugador=" + jugador.getNombre() + ", equipo=" + (equipo != null ? equipo.getLetra() : "null") + ", abonada=" + abonada + "}";
    }
}