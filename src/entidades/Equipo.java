package entidades;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import proyectoffcv.util.DatabaseConnection;

public class Equipo {
    private int id; // Nuevo campo para el ID
    private String letra;
    private Instalacion instalacion;
    private Grupo grupo;
    private Club club;

    // Constructor
    public Equipo(String letra, Instalacion instalacion, Grupo grupo, Club club) {
        this.letra = letra;
        this.instalacion = instalacion;
        this.grupo = grupo;
        this.club = club;
    }

    // Getters y Setters
    public int getId() { return id; } // Nuevo getter
    public String getLetra() { return letra; }
    public Instalacion getInstalacion() { return instalacion; }
    public void setInstalacion(Instalacion instalacion) { this.instalacion = instalacion; }
    public Grupo getGrupo() { return grupo; }
    public void setGrupo(Grupo grupo) { this.grupo = grupo; }
    public Club getClub() { return club; }
    public void setClub(Club club) { this.club = club; }

    // Metodo para guardar en la base de datos
    public void guardar() throws SQLException {
        String sql = "INSERT INTO Equipo (letra, instalacion_id, grupo_id, club_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, letra);
            stmt.setInt(2, instalacion.getId());
            stmt.setInt(3, grupo.getId());
            stmt.setInt(4, club.getId());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                this.id = rs.getInt(1); // Asigna el ID generado
            }
        }
    }

    // Metodo para actualizar en la base de datos
    public void actualizar() throws SQLException {
        String sql = "UPDATE Equipo SET instalacion_id = ?, grupo_id = ?, club_id = ? WHERE letra = ? AND club_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, instalacion.getId());
            stmt.setInt(2, grupo.getId());
            stmt.setInt(3, club.getId());
            stmt.setString(4, letra);
            stmt.setInt(5, club.getId());
            stmt.executeUpdate();
        }
    }

    // Metodo para eliminar de la base de datos
    public void eliminar() throws SQLException {
        String sql = "DELETE FROM Equipo WHERE letra = ? AND club_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, letra);
            stmt.setInt(2, club.getId());
            stmt.executeUpdate();
        }
    }

    // Metodo para buscar por letra y club
    public static Equipo buscarPorLetraYClub(String letra, String nombreClub) throws SQLException {
        Club club = Club.buscarPorNombre(nombreClub);
        if (club == null) return null;
        String sql = "SELECT * FROM Equipo WHERE letra = ? AND club_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, letra);
            stmt.setInt(2, club.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Instalacion instalacion = Instalacion.buscarPorId(rs.getInt("instalacion_id"));
                Grupo grupo = Grupo.buscarPorId(rs.getInt("grupo_id"));
                Equipo equipo = new Equipo(letra, instalacion, grupo, club);
                equipo.id = rs.getInt("id"); // Asigna el ID
                return equipo;
            }
        }
        return null;
    }

    // Metodo para buscar por letra
    public static Equipo buscarPorLetra(String letra) throws SQLException {
        String sql = "SELECT * FROM Equipo WHERE letra = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, letra);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Instalacion instalacion = Instalacion.buscarPorId(rs.getInt("instalacion_id"));
                Grupo grupo = Grupo.buscarPorId(rs.getInt("grupo_id"));
                Club club = Club.buscarPorId(rs.getInt("club_id"));
                Equipo equipo = new Equipo(letra, instalacion, grupo, club);
                equipo.id = rs.getInt("id"); // Asigna el ID
                return equipo;
            }
        }
        return null;
    }

    // Metodo para buscar por ID
    public static Equipo buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Equipo WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Instalacion instalacion = Instalacion.buscarPorId(rs.getInt("instalacion_id"));
                Grupo grupo = Grupo.buscarPorId(rs.getInt("grupo_id"));
                Club club = Club.buscarPorId(rs.getInt("club_id"));
                Equipo equipo = new Equipo(rs.getString("letra"), instalacion, grupo, club);
                equipo.id = rs.getInt("id"); // Asigna el ID
                return equipo;
            }
        }
        return null;
    }

    // Metodo para obtener todos los equipos
    public static List<Equipo> obtenerTodos() throws SQLException {
        List<Equipo> equipos = new ArrayList<>();
        String sql = "SELECT * FROM Equipo";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Equipo equipo = buscarPorId(rs.getInt("id"));
                if (equipo != null) {
                    equipos.add(equipo);
                }
            }
        }
        return equipos;
    }

    @Override
    public String toString() {
        return letra + " - " + club.getNombre(); // Mostrar letra y nombre del club en el JComboBox
    }
}