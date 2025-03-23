package entidades;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class ClubTest {

    @Test
    public void testClubConstructorValid() {
        Persona presidente = new Persona("12345678A", "Juan", "Pérez", "Gómez", LocalDate.of(1980, 1, 1), "juanp", "pass123", "Madrid");
        Club club = new Club("Club Deportivo", LocalDate.of(1990, 5, 15), presidente);
        assertEquals("Club Deportivo", club.getNombre());
        assertEquals(LocalDate.of(1990, 5, 15), club.getFechaFundacion());
        assertEquals(presidente, club.getPresidente());
        assertNotNull(club.getEquipos());
        assertTrue(club.getEquipos().isEmpty());
    }

    @Test
    public void testSetNombre() {
        Persona presidente = new Persona("12345678A", "Juan", "Pérez", "Gómez", LocalDate.of(1980, 1, 1), "juanp", "pass123", "Madrid");
        Club club = new Club("Club Deportivo", LocalDate.of(1990, 5, 15), presidente);
        club.setNombre("Club Atlético");
        assertEquals("Club Atlético", club.getNombre());
    }

    @Test
    public void testSetFechaFundacion() {
        Persona presidente = new Persona("12345678A", "Juan", "Pérez", "Gómez", LocalDate.of(1980, 1, 1), "juanp", "pass123", "Madrid");
        Club club = new Club("Club Deportivo", LocalDate.of(1990, 5, 15), presidente);
        club.setFechaFundacion(LocalDate.of(2000, 10, 20));
        assertEquals(LocalDate.of(2000, 10, 20), club.getFechaFundacion());
    }

    @Test
    public void testSetPresidente() {
        Persona presidente1 = new Persona("12345678A", "Juan", "Pérez", "Gómez", LocalDate.of(1980, 1, 1), "juanp", "pass123", "Madrid");
        Persona presidente2 = new Persona("87654321B", "Ana", "García", "López", LocalDate.of(1975, 2, 2), "anag", "pass456", "Barcelona");
        Club club = new Club("Club Deportivo", LocalDate.of(1990, 5, 15), presidente1);
        club.setPresidente(presidente2);
        assertEquals(presidente2, club.getPresidente());
    }

    @Test
    public void testAddEquipoValid() {
        Persona presidente = new Persona("12345678A", "Juan", "Pérez", "Gómez", LocalDate.of(1980, 1, 1), "juanp", "pass123", "Madrid");
        Club club = new Club("Club Deportivo", LocalDate.of(1990, 5, 15), presidente);
        Instalacion instalacion = new Instalacion("Estadio Principal", "Calle Falsa 123", Instalacion.TipoSuperficie.CESPED_NATURAL);
        Grupo grupo = new Grupo("Grupo A");
        Equipo equipo = new Equipo("A", instalacion, grupo);
        club.addEquipo(equipo);
        assertEquals(1, club.getNumeroEquipos());
        assertTrue(club.getEquipos().contains(equipo));
    }

    @Test
    public void testAddEquipoDuplicate() {
        Persona presidente = new Persona("12345678A", "Juan", "Pérez", "Gómez", LocalDate.of(1980, 1, 1), "juanp", "pass123", "Madrid");
        Club club = new Club("Club Deportivo", LocalDate.of(1990, 5, 15), presidente);
        Instalacion instalacion = new Instalacion("Estadio Principal", "Calle Falsa 123", Instalacion.TipoSuperficie.CESPED_NATURAL);
        Grupo grupo = new Grupo("Grupo A");
        Equipo equipo = new Equipo("A", instalacion, grupo);
        club.addEquipo(equipo);
        assertThrows(IllegalArgumentException.class, () -> {
            club.addEquipo(equipo);
        });
    }

    @Test
    public void testRemoveEquipo() {
        Persona presidente = new Persona("12345678A", "Juan", "Pérez", "Gómez", LocalDate.of(1980, 1, 1), "juanp", "pass123", "Madrid");
        Club club = new Club("Club Deportivo", LocalDate.of(1990, 5, 15), presidente);
        Instalacion instalacion = new Instalacion("Estadio Principal", "Calle Falsa 123", Instalacion.TipoSuperficie.CESPED_NATURAL);
        Grupo grupo = new Grupo("Grupo A");
        Equipo equipo = new Equipo("A", instalacion, grupo);
        club.addEquipo(equipo);
        club.removeEquipo(equipo);
        assertEquals(0, club.getNumeroEquipos());
        assertTrue(club.getEquipos().isEmpty());
    }

    @Test
    public void testToString() {
        Persona presidente = new Persona("12345678A", "Juan", "Pérez", "Gómez", LocalDate.of(1980, 1, 1), "juanp", "pass123", "Madrid");
        Club club = new Club("Club Deportivo", LocalDate.of(1990, 5, 15), presidente);
        String expected = "Club{nombre='Club Deportivo', fechaFundacion=1990-05-15, presidente=" + presidente.toString() + ", equipos=[]}";
        assertEquals(expected, club.toString());
    }
}