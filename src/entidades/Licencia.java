package entidades;

import proyectoffcv.util.DatabaseConnection;
import java.sql.*;
import java.util.UUID;

public class Licencia {
    private Persona persona;
    private String numeroLicencia;
    private boolean abonada;

    public Licencia(Persona persona, String numeroLicencia) {
        this.persona = persona;
        this.numeroLicencia = numeroLicencia;
        this.abonada = false;
    }

    private void persistir() throws SQLException {
        String sql = "INSERT INTO Licencia (numeroLicencia, dni_persona, abonada) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, numeroLicencia);
            ps.setString(2, persona.getDni());
            ps.setBoolean(3, abonada);
            ps.executeUpdate();
        }
    }

    private void actualizarEnBD() throws SQLException {
        String sql = "UPDATE Licencia SET dni_persona = ?, abonada = ? WHERE numeroLicencia = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, persona.getDni());
            ps.setBoolean(2, abonada);
            ps.setString(3, numeroLicencia);
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
        if (numeroLicencia == null || numeroLicencia.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de licencia no puede ser nulo ni vacío.");
        }
        if (persona == null) {
            throw new IllegalArgumentException("La persona no puede ser nula.");
        }
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
                Persona persona = Persona.buscarPorDni(rs.getString("dni_persona"));
                Licencia licencia = new Licencia(persona, rs.getString("numeroLicencia"));
                licencia.setAbonada(rs.getBoolean("abonada"));
                return licencia;
            }
            return null;
        }
    }

    public void asignarAEquipo(Equipo equipo) throws SQLException {
        String sql = "UPDATE Licencia SET equipo_id = ? WHERE numeroLicencia = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, obtenerIdEquipo(equipo));
            ps.setString(2, numeroLicencia);
            ps.executeUpdate();
            equipo.getLicencias().add(this);
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
            throw new SQLException("Equipo no encontrado.");
        }
    }

    // Getters y setters
    public Persona getPersona() { return persona; }
    public void setPersona(Persona persona) { this.persona = persona; }
    public String getNumeroLicencia() { return numeroLicencia; }
    public boolean isAbonada() { return abonada; }
    public void setAbonada(boolean abonada) { this.abonada = abonada; }

    @Override
    public String toString() {
        return "Licencia{numero='" + numeroLicencia + "', persona=" + persona.getDni() + ", abonada=" + abonada + "}";
    }
}