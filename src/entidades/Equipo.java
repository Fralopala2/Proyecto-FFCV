package entidades;

import proyectoffcv.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    private void persistir() throws SQLException {
        String sql = "INSERT INTO Equipo (letra, instalacion_id, grupo_id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, letra);
            ps.setInt(2, obtenerIdInstalacion());
            ps.setInt(3, obtenerIdGrupo());
            ps.executeUpdate();
        }
    }

    private void actualizarEnBD() throws SQLException {
        String sql = "UPDATE Equipo SET instalacion_id = ?, grupo_id = ? WHERE letra = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, obtenerIdInstalacion());
            ps.setInt(2, obtenerIdGrupo());
            ps.setString(3, letra);
            ps.executeUpdate();
        }
    }

    private void eliminarDeBD() throws SQLException {
        String sql = "DELETE FROM Equipo WHERE letra = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, letra);
            ps.executeUpdate();
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
        String sql = "SELECT * FROM Equipo WHERE letra = ?";
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
        }
    }

    public Persona buscarJugador(String dni) throws SQLException {
        for (Licencia licencia : cargarLicencias()) {
            if (licencia.getPersona().getDni().equals(dni)) {
                return licencia.getPersona();
            }
        }
        return null;
    }

    public double calcularPrecioLicencia() {
        return grupo.getCategoria() != null ? grupo.getCategoria().getPrecioLicencia() : 0.0;
    }

    private List<Licencia> cargarLicencias() throws SQLException {
        licencias.clear();
        String sql = "SELECT * FROM Licencia WHERE equipo_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, obtenerIdEquipo());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Persona persona = Persona.buscarPorDni(rs.getString("dni_persona"));
                Licencia licencia = new Licencia(persona, rs.getString("numeroLicencia"));
                licencia.setAbonada(rs.getBoolean("abonada"));
                licencias.add(licencia);
            }
            return licencias;
        }
    }

    private int obtenerIdInstalacion() throws SQLException {
        String sql = "SELECT id FROM Instalacion WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, instalacion.getNombre());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            throw new SQLException("Instalación no encontrada.");
        }
    }

    private int obtenerIdGrupo() throws SQLException {
        String sql = "SELECT id FROM Grupo WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, grupo.getNombre());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            throw new SQLException("Grupo no encontrado.");
        }
    }

    private int obtenerIdEquipo() throws SQLException {
        String sql = "SELECT id FROM Equipo WHERE letra = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, letra);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            throw new SQLException("Equipo no encontrado.");
        }
    }

    // Getters y setters
    public String getLetra() { return letra; }
    public void setLetra(String letra) { this.letra = letra; }
    public Instalacion getInstalacion() { return instalacion; }
    public void setInstalacion(Instalacion instalacion) { this.instalacion = instalacion; }
    public Grupo getGrupo() { return grupo; }
    public void setGrupo(Grupo grupo) { this.grupo = grupo; }
    public List<Licencia> getLicencias() { return licencias; }

    @Override
    public String toString() {
        return "Equipo{" +
               "letra='" + letra + '\'' +
               ", instalacion=" + instalacion.getNombre() +
               ", grupo=" + grupo.getNombre() +
               ", licencias=" + licencias.size() + " licencias" +
               '}';
    }
}