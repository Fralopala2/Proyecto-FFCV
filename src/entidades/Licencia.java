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
    public boolean isAbonada() { return abonada; }
    
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

    // Método corregido para guardar en la base de datos (Privado)
    private void guardarPrivado() throws SQLException {
        String sql = "INSERT INTO Licencia (numeroLicencia, persona_dni, equipo_id, abonada) VALUES (?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE persona_dni = ?, equipo_id = ?, abonada = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, numeroLicencia);
            stmt.setString(2, jugador.getDni());
            stmt.setObject(3, (equipo != null) ? equipo.getId() : null); // Manejar null
            stmt.setBoolean(4, abonada);
            stmt.setString(5, jugador.getDni());
            stmt.setObject(6, (equipo != null) ? equipo.getId() : null); // Manejar null
            stmt.setBoolean(7, abonada);
            stmt.executeUpdate();
        }
    }
    
    // Método para obtener licencias asociadas a un equipo
    public static List<Licencia> obtenerPorEquipo(int equipoId) throws SQLException {
        List<Licencia> licencias = new ArrayList<>();
        String sql = "SELECT * FROM Licencia WHERE equipo_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, equipoId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Persona jugador = Persona.buscarPorDni(rs.getString("persona_dni"));
                Equipo equipo = Equipo.buscarPorId(rs.getInt("equipo_id"));
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

    // Método corregido para actualizar en la base de datos (Privado)
    private void actualizarPrivado() throws SQLException {
        String sql = "UPDATE Licencia SET persona_dni = ?, equipo_id = ?, abonada = ? WHERE numeroLicencia = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, jugador.getDni());
            stmt.setObject(2, (equipo != null) ? equipo.getId() : null); // Manejar null
            stmt.setBoolean(3, abonada);
            stmt.setString(4, numeroLicencia);
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

    // Métodos estáticos para obtener licencias de la base de datos
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
    
    public static Licencia buscarPorNumero(String numeroLicencia) throws SQLException {
        String sql = "SELECT * FROM Licencia WHERE numeroLicencia = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, numeroLicencia);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Persona jugador = Persona.buscarPorDni(rs.getString("persona_dni"));
                Equipo equipo = null;
                if (rs.getObject("equipo_id") != null) {
                    equipo = Equipo.buscarPorId(rs.getInt("equipo_id"));
                }
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