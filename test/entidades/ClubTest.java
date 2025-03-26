package entidades;

import java.time.LocalDate;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClubTest {

    @Test
    public void testClubConstructorValid() {
        Persona presidente = new Persona("12345678A", "Juan", "Pérez", "Gómez", LocalDate.of(1980, 1, 1), "juanp1", "pass123", "Madrid");
        Club club = new Club("Club Deportivo", LocalDate.of(1990, 5, 15), presidente);
        assertEquals("Club Deportivo", club.getNombre());
        assertEquals(LocalDate.of(1990, 5, 15), club.getFechaFundacion());
        assertEquals(presidente, club.getPresidente());
        assertNotNull(club.getEquipos());
        assertTrue(club.getEquipos().isEmpty());
    }

    @Test
    public void testSetNombre() {
        Persona presidente = new Persona("12345678A", "Juan", "Pérez", "Gómez", LocalDate.of(1980, 1, 1), "juan2", "pass123", "Madrid");
        Club club = new Club("Club Deportivo", LocalDate.of(1990, 5, 15), presidente);
        club.setNombre("Club Atlético");
        assertEquals("Club Atlético", club.getNombre());
    }

    @Test
    public void testSetFechaFundacion() {
        Persona presidente = new Persona("12345678A", "Juan", "Pérez", "Gómez", LocalDate.of(1980, 1, 1), "juan3", "pass123", "Madrid");
        Club club = new Club("Club Deportivo", LocalDate.of(1990, 5, 15), presidente);
        club.setFechaFundacion(LocalDate.of(2000, 10, 20));
        assertEquals(LocalDate.of(2000, 10, 20), club.getFechaFundacion());
    }

    @Test
    public void testSetPresidente() {
        Persona presidente1 = new Persona("12345678A", "Juan", "Pérez", "Gómez", LocalDate.of(1980, 1, 1), "juan4", "pass123", "Madrid");
        Persona presidente2 = new Persona("87654321B", "Ana", "García", "López", LocalDate.of(1975, 2, 2), "anag", "pass456", "Barcelona");
        Club club = new Club("Club Deportivo", LocalDate.of(1990, 5, 15), presidente1);
        club.setPresidente(presidente2);
        assertEquals(presidente2, club.getPresidente());
    }

    @Test
    public void testAddEquipoValid() throws SQLException {
        Persona presidente = new Persona("11111111A", "Juan", "Pérez", "Gómez", LocalDate.of(1980, 1, 1), "juan5", "pass123", "Madrid");
        presidente.guardar();
        Club club = new Club("Club Deportivo Test1", LocalDate.of(1990, 5, 15), presidente);
        club.guardar();
        Instalacion instalacion = new Instalacion("Estadio Principal", "Calle Falsa 123", Instalacion.TipoSuperficie.CESPED_NATURAL);
        instalacion.guardar(); 
        Grupo grupo = new Grupo("Grupo A");
        grupo.guardar(); 
        Equipo equipo = new Equipo("A", instalacion, grupo);
        equipo.guardar();
        club.addEquipo(equipo);
        assertEquals(1, club.getNumeroEquipos());
        assertTrue(club.getEquipos().contains(equipo));
        club.eliminar();
        equipo.eliminar();
        grupo.eliminar(); 
        instalacion.eliminar();
        presidente.eliminar();
    }

    @Test
    public void testAddEquipoDuplicate() throws SQLException {
        Persona presidente = new Persona("22222222B", "Juan", "Pérez", "Gómez", LocalDate.of(1980, 1, 1), "juan6", "pass123", "Madrid");
        presidente.guardar();
        Club club = new Club("Club Deportivo Test2", LocalDate.of(1990, 5, 15), presidente);
        club.guardar();
        Instalacion instalacion = new Instalacion("Estadio Principal", "Calle Falsa 123", Instalacion.TipoSuperficie.CESPED_NATURAL);
        instalacion.guardar();
        Grupo grupo = new Grupo("Grupo A");
        grupo.guardar();
        Equipo equipo = new Equipo("A", instalacion, grupo);
        equipo.guardar();
        club.addEquipo(equipo);
        assertThrows(IllegalArgumentException.class, () -> {
            club.addEquipo(equipo);
        });
        club.eliminar();
        equipo.eliminar();
        grupo.eliminar(); 
        instalacion.eliminar();
        presidente.eliminar();
    }

    @Test
    public void testRemoveEquipo() throws SQLException {
        Persona presidente = new Persona("33333333C", "Juan", "Pérez", "Gómez", LocalDate.of(1980, 1, 1), "juan7", "pass123", "Madrid");
        presidente.guardar();
        Club club = new Club("Club Deportivo Test3", LocalDate.of(1990, 5, 15), presidente);
        club.guardar();
        Instalacion instalacion = new Instalacion("Estadio Principal", "Calle Falsa 123", Instalacion.TipoSuperficie.CESPED_NATURAL);
        instalacion.guardar();
        Grupo grupo = new Grupo("Grupo A");
        grupo.guardar();
        Equipo equipo = new Equipo("A", instalacion, grupo);
        equipo.guardar();
        club.addEquipo(equipo);
        club.removeEquipo(equipo);
        assertEquals(0, club.getNumeroEquipos());
        assertTrue(club.getEquipos().isEmpty());
        club.eliminar();
        equipo.eliminar();
        grupo.eliminar();
        instalacion.eliminar();
        presidente.eliminar();
    }

    @Test
    public void testToString() {
        Persona presidente = new Persona("12345678A", "Juan", "Pérez", "Gómez", LocalDate.of(1980, 1, 1), "juan8", "pass123", "Madrid");
        Club club = new Club("Club Deportivo", LocalDate.of(1990, 5, 15), presidente);
        String expected = "Club{nombre='Club Deportivo', fechaFundacion=1990-05-15, presidente=12345678A, equipos=0}";
        assertEquals(expected, club.toString());
    }
}