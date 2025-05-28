package entidades;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import proyectoffcv.util.DatabaseConnection;

public class Licencia {
    private String numeroLicencia;
    private Persona jugador;
    private Equipo equipo;
    private boolean abonada;
    private java.time.LocalDate fechaInicio;
    private java.time.LocalDate fechaFin;

    public Licencia(String numeroLicencia, Persona jugador, Equipo equipo, boolean abonada) {
        this.numeroLicencia = numeroLicencia;
        this.jugador = jugador;
        this.equipo = equipo;
        this.abonada = abonada;
    }

    // Getters
    public String getNumeroLicencia() { return numeroLicencia; }
    public Persona getJugador() { return jugador; }
    public Equipo getEquipo() { return equipo; }

    // Setters
    public void setEquipo(Equipo equipo) { this.equipo = equipo; }

    // Métodos públicos que llaman a los privados
    public void guardarPublic() throws SQLException {
        guardarPrivado();
    }

    public void actualizarPublic() throws SQLException {
        actualizarPrivado();
    }

    public void eliminarPublic() throws SQLException {
        eliminarPrivado();
    }

    // Método para guardar en la base de datos (Privado)
    private void guardarPrivado() throws SQLException {
        String sql = "INSERT INTO Licencia (numeroLicencia, persona_dni, equipo_id, abonada, fecha_inicio, fecha_fin) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, numeroLicencia);
            stmt.setString(2, jugador.getDni());
            stmt.setInt(3, equipo.getId());
            stmt.setBoolean(4, abonada);
            stmt.setDate(5, fechaInicio != null ? java.sql.Date.valueOf(fechaInicio) : null);
            stmt.setDate(6, fechaFin != null ? java.sql.Date.valueOf(fechaFin) : null);
            stmt.executeUpdate();
        }
    }

    // Método para actualizar en la base de datos (Privado)
    private void actualizarPrivado() throws SQLException {
        String sql = "UPDATE Licencia SET persona_dni = ?, equipo_id = ?, abonada = ?, fecha_inicio = ?, fecha_fin = ? WHERE numeroLicencia = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, jugador.getDni());
            stmt.setInt(2, equipo.getId());
            stmt.setBoolean(3, abonada);
            stmt.setDate(4, fechaInicio != null ? java.sql.Date.valueOf(fechaInicio) : null);
            stmt.setDate(5, fechaFin != null ? java.sql.Date.valueOf(fechaFin) : null);
            stmt.setString(6, numeroLicencia);
            stmt.executeUpdate();
        }
    }

    // Método para eliminar de la base de datos (Privado)
    private void eliminarPrivado() throws SQLException {
        String sql = "DELETE FROM Licencia WHERE numeroLicencia = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, numeroLicencia);
            stmt.executeUpdate();
        }
    }

    // Método para obtener todas las licencias
    public static List<Licencia> obtenerTodas() throws SQLException {
        List<Licencia> licencias = new ArrayList<>();
        String sql = "SELECT * FROM Licencia";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Persona jugador = Persona.buscarPorDni(rs.getString("persona_dni"));
                Equipo equipo = null;
                if (rs.getObject("equipo_id") != null) {
                    equipo = Equipo.buscarPorId(rs.getInt("equipo_id"));
                }

                Licencia licencia = new Licencia(
                    rs.getString("numeroLicencia"),
                    jugador,
                    equipo,
                    rs.getBoolean("abonada")
                );
                licencias.add(licencia);
            }
        }
        return licencias;
    }

    // Método para buscar licencias por jugador
    public static Licencia buscarPorJugador(Persona jugador) throws SQLException {
        String sql = "SELECT * FROM Licencia WHERE persona_dni = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, jugador.getDni());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
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
        return "Licencia{numeroLicencia='" + numeroLicencia + "', jugador=" + jugador.getNombre() +
               ", equipo=" + (equipo != null ? equipo.getLetra() : "null") + "}";
    }
}