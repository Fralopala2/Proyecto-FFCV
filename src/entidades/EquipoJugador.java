package entidades;
import proyectoffcv.util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;

public class EquipoJugador {
    private Equipo equipo;
    private Persona jugador;
    private LocalDateTime fechaRegistro;

    public EquipoJugador(Equipo equipo, Persona jugador, LocalDateTime fechaRegistro) {
        this.equipo = equipo;
        this.jugador = jugador;
        this.fechaRegistro = fechaRegistro;
    }

    public void guardar() throws SQLException {
        String sql = "INSERT INTO equipo_jugador (equipo_id, dni_jugador, fecha_registro) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, equipo.getId());
            stmt.setString(2, jugador.getDni());
            stmt.setTimestamp(3, Timestamp.valueOf(fechaRegistro));
            stmt.executeUpdate();
        }
    }
}