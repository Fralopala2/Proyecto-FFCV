package entidades;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import proyectoffcv.util.DatabaseConnection;

public class Equipo {
    private int id;
    private String letra;
    private Instalacion instalacion;
    private Grupo grupo;
    private Club club;
    private List<Licencia> licencias; // Lista de licencias

    // Constructor
    public Equipo(String letra, Instalacion instalacion, Grupo grupo, Club club) {
        this.letra = letra;
        this.instalacion = instalacion;
        this.grupo = grupo;
        this.club = club;
        this.licencias = new ArrayList<>();
    }

    // Constructor para cargar desde BD
    public Equipo(int id, String letra, Instalacion instalacion, Grupo grupo, Club club) {
        this.id = id;
        this.letra = letra;
        this.instalacion = instalacion;
        this.grupo = grupo;
        this.club = club;
        this.licencias = new ArrayList<>();
    }

    // Getters y Setters
    public int getId() { return id; }
    public String getLetra() { return letra; }
    public Instalacion getInstalacion() { return instalacion; }
    public Grupo getGrupo() { return grupo; }
    public void setGrupo(Grupo grupo) { this.grupo = grupo; }
    public Club getClub() { return club; }
    public List<Licencia> getLicencias() { return licencias; }

    // Método para añadir una licencia al equipo
    public void agregarLicencia(Licencia licencia) throws SQLException {
        if (licencia == null) {
            throw new IllegalArgumentException("La licencia no puede ser nula.");
        }
        if (licencia.getEquipo() != null && licencia.getEquipo() != this) {
            throw new IllegalStateException("La licencia ya está asociada a otro equipo");
        }
        licencia.setEquipo(this);
        licencias.add(licencia);
        licencia.actualizarPublic(); // Actualizar en la base de datos
    }

    // Método para crear una nueva licencia para una persona
    public Licencia crearLicencia(Persona jugador, boolean abonada) throws SQLException {
        if (jugador == null) {
            throw new IllegalArgumentException("El jugador no puede ser nulo.");
        }
        String numeroLicencia = String.format("LIC_%02d", (int)(Math.random() * 100));
        Licencia nuevaLicencia = new Licencia(numeroLicencia, jugador, this, abonada);
        agregarLicencia(nuevaLicencia);
        return nuevaLicencia;
    }

    // Método para cargar licencias desde la base de datos
    public void cargarLicencias() throws SQLException {
        this.licencias = Licencia.obtenerPorEquipo(this.id);
    }

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
        String sql = "INSERT INTO Equipo (letra, instalacion_id, grupo_id, club_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, letra);
            stmt.setInt(2, instalacion.getId());
            stmt.setInt(3, grupo.getId());
            stmt.setInt(4, club.getId());
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
        String sql = "UPDATE Equipo SET letra = ?, instalacion_id = ?, grupo_id = ?, club_id = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, letra);
            stmt.setInt(2, instalacion.getId());
            stmt.setInt(3, grupo.getId());
            stmt.setInt(4, club.getId());
            stmt.setInt(5, id);
            stmt.executeUpdate();
        }
    }

    // Método para eliminar de la base de datos (Privado)
    private void eliminarPrivado() throws SQLException {
        String sql = "DELETE FROM Equipo WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Método para buscar un equipo por su ID
    public static Equipo buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, letra, instalacion_id, grupo_id, club_id FROM Equipo WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Instalacion instalacion = Instalacion.buscarPorId(rs.getInt("instalacion_id"));
                Grupo grupo = Grupo.buscarPorId(rs.getInt("grupo_id"));
                Club club = Club.buscarPorId(rs.getInt("club_id"));

                if (instalacion == null || grupo == null || club == null) {
                    throw new SQLException("Error al cargar equipo " + id + ": Una de las relaciones (instalación, grupo, club) no se encontró en la base de datos.");
                }

                return new Equipo(
                    rs.getInt("id"),
                    rs.getString("letra"),
                    instalacion,
                    grupo,
                    club
                );
            }
        }
        return null;
    }

    // Método para obtener todos los equipos
    public static List<Equipo> obtenerTodos() throws SQLException {
        List<Equipo> equipos = new ArrayList<>();
        String sql = "SELECT id FROM Equipo";
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
        return letra + " - " + club.getNombre();
    }
}