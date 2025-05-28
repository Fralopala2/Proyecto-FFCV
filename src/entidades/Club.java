package entidades;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import proyectoffcv.util.DatabaseConnection;

public class Club {
    private int id;
    private String nombre;
    private LocalDate fechaFundacion;
    private Persona presidente;
    private Persona secretario;
    private ArrayList<Equipo> equipos;

    // Constructor para crear un club nuevo
    public Club(String nombre, LocalDate fechaFundacion, Persona presidente) throws SQLException {
        validarDatos(nombre, fechaFundacion, presidente);
        this.nombre = nombre;
        this.fechaFundacion = fechaFundacion;
        this.presidente = presidente;
        this.secretario = null;
        this.equipos = new ArrayList<>();
    }

    // Constructor para cargar desde BD
    public Club(int id, String nombre, LocalDate fechaFundacion, Persona presidente, Persona secretario) {
        this.id = id;
        this.nombre = nombre;
        this.fechaFundacion = fechaFundacion;
        this.presidente = presidente;
        this.secretario = secretario;
        this.equipos = new ArrayList<>();
    }

    public void validarDatos(String nombre, LocalDate fechaFundacion, Persona presidente) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Nombre inválido: no puede ser nulo o vacío.");
        }
        if (fechaFundacion == null) {
            throw new IllegalArgumentException("Fecha de fundación inválida: no puede ser nula.");
        }
        if (presidente == null) {
            throw new IllegalArgumentException("Presidente inválido: no puede ser nulo.");
        }
    }

    // Getters y Setters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public LocalDate getFechaFundacion() { return fechaFundacion; }
    public void setFechaFundacion(LocalDate fechaFundacion) { this.fechaFundacion = fechaFundacion; }
    public Persona getPresidente() { return presidente; }
    public void setPresidente(Persona presidente) { this.presidente = presidente; }
    public ArrayList<Equipo> getEquipos() { return equipos; }

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

    // Método para agregar equipo
    public void agregarEquipo(Equipo equipo) throws SQLException {
        if (equipo != null && !this.equipos.contains(equipo)) {
            this.equipos.add(equipo);
            // Eliminado equipo.setClub(this) ya que Equipo no tiene setClub
        }
    }

    // Método para guardar en la base de datos (Privado)
    private void guardarPrivado() throws SQLException {
        String sql = "INSERT INTO Club (nombre, fechaFundacion, presidente_dni, secretario_dni) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nombre);
            stmt.setDate(2, java.sql.Date.valueOf(fechaFundacion));
            stmt.setString(3, presidente.getDni());
            stmt.setString(4, (secretario != null) ? secretario.getDni() : null);
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    this.id = generatedKeys.getInt(1);
                }
            }
        }
    }

    // Método para actualizar en la base de datos (Privado)
    private void actualizarPrivado() throws SQLException {
        String sql = "UPDATE Club SET nombre = ?, fechaFundacion = ?, presidente_dni = ?, secretario_dni = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setDate(2, java.sql.Date.valueOf(fechaFundacion));
            stmt.setString(3, presidente.getDni());
            stmt.setString(4, (secretario != null) ? secretario.getDni() : null);
            stmt.setInt(5, id);
            stmt.executeUpdate();
        }
    }

    // Método para eliminar de la base de datos (Privado)
    private void eliminarPrivado() throws SQLException {
        String sql = "DELETE FROM Club WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Método para buscar un club por su ID (estático)
    public static Club buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Club WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Persona presidente = Persona.buscarPorDni(rs.getString("presidente_dni"));
                Persona secretario = rs.getString("secretario_dni") != null ? Persona.buscarPorDni(rs.getString("secretario_dni")) : null;
                return new Club(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getDate("fechaFundacion").toLocalDate(),
                    presidente,
                    secretario
                );
            }
        }
        return null;
    }

    public static ArrayList<Club> cargarClubsDesdeBD() throws SQLException {
        ArrayList<Club> clubes = new ArrayList<>();
        String sql = "SELECT * FROM Club";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Persona presidente = Persona.buscarPorDni(rs.getString("presidente_dni"));
                Persona secretario = rs.getString("secretario_dni") != null ? Persona.buscarPorDni(rs.getString("secretario_dni")) : null;
                Club club = new Club(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getDate("fechaFundacion").toLocalDate(),
                    presidente,
                    secretario
                );
                clubes.add(club);
            }
        }
        return clubes;
    }

    @Override
    public String toString() {
        return "Club{nombre='" + nombre + "', fechaFundacion=" + fechaFundacion +
               ", presidente=" + (presidente != null ? presidente.getNombre() + " " + presidente.getApellido1() : "N/A") + "}";
    }
}