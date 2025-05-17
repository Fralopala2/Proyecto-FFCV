package entidades;

import java.sql.*;
import java.time.LocalDate;
import proyectoffcv.util.DatabaseConnection;

public class Licencia {
    private String numeroLicencia;
    private Persona jugador;
    private Equipo equipo;
    private boolean abonada;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public Licencia(String numeroLicencia, Persona jugador, Equipo equipo, boolean abonada) {
        this.numeroLicencia = numeroLicencia;
        this.jugador = jugador;
        this.equipo = equipo;
        this.abonada = abonada;
    }

    // Getters y Setters
    public String getNumeroLicencia() { return numeroLicencia; }
    public Persona getJugador() { return jugador; }
    public void setJugador(Persona jugador) { this.jugador = jugador; }
    public Equipo getEquipo() { return equipo; }
    public void setEquipo(Equipo equipo) { this.equipo = equipo; }
    public boolean isAbonada() { return abonada; }
    public void setAbonada(boolean abonada) { this.abonada = abonada; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    // Metodo para guardar en la base de datos
    public void guardar() throws SQLException {
        String sql = "INSERT INTO Licencia (numeroLicencia, persona_dni, equipo_id, fecha_inicio, fecha_fin, abonada) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, numeroLicencia);
            stmt.setString(2, jugador.getDni());
            if (equipo != null) {
                stmt.setInt(3, equipo.getId());
            } else {
                stmt.setNull(3, java.sql.Types.INTEGER);
            }
            stmt.setDate(4, fechaInicio != null ? Date.valueOf(fechaInicio) : null);
            stmt.setDate(5, fechaFin != null ? Date.valueOf(fechaFin) : null);
            stmt.setBoolean(6, abonada);
            stmt.executeUpdate();
        }
    }

    // Metodo para actualizar en la base de datos
    public void actualizar() throws SQLException {
        String sql = "UPDATE Licencia SET persona_dni = ?, equipo_id = ?, fecha_inicio = ?, fecha_fin = ?, abonada = ? WHERE numeroLicencia = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, jugador.getDni());
            if (equipo != null) {
                stmt.setInt(2, equipo.getId());
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }
            stmt.setDate(3, fechaInicio != null ? Date.valueOf(fechaInicio) : null);
            stmt.setDate(4, fechaFin != null ? Date.valueOf(fechaFin) : null);
            stmt.setBoolean(5, abonada);
            stmt.setString(6, numeroLicencia);
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
                Licencia licencia = new Licencia(
                    rs.getString("numeroLicencia"),
                    jugador,
                    equipo,
                    rs.getBoolean("abonada")
                );
                licencia.setFechaInicio(rs.getDate("fecha_inicio") != null ? rs.getDate("fecha_inicio").toLocalDate() : null);
                licencia.setFechaFin(rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toLocalDate() : null);
                return licencia;
            }
        }
        return null;
    }

    // Metodo para buscar por jugador y equipo
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
                Licencia licencia = new Licencia(
                    rs.getString("numeroLicencia"),
                    jugador,
                    equipo,
                    rs.getBoolean("abonada")
                );
                licencia.setFechaInicio(rs.getDate("fecha_inicio") != null ? rs.getDate("fecha_inicio").toLocalDate() : null);
                licencia.setFechaFin(rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toLocalDate() : null);
                return licencia;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Licencia{numeroLicencia='" + numeroLicencia + "', jugador=" + jugador.getNombre() + 
               ", equipo=" + (equipo != null ? equipo.getLetra() : "null") + 
               ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + ", abonada=" + abonada + "}";
    }
}