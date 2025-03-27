package entidades;

import java.time.LocalDate;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClubTest {

    private void cleanup(String letra, String grupoNombre, String catNombre, String instalacionNombre, String clubNombre, String dni) throws SQLException {
        Equipo equipo = Equipo.buscarPorLetra(letra);
        if (equipo != null) equipo.eliminar();
        Grupo grupo = Grupo.buscarPorNombre(grupoNombre);
        if (grupo != null) grupo.eliminar();
        Categoria categoria = Categoria.buscarPorNombre(catNombre);
        if (categoria != null) categoria.eliminar();
        Instalacion instalacion = Instalacion.buscarPorNombre(instalacionNombre);
        if (instalacion != null) instalacion.eliminar();
        Club club = Club.buscarPorNombre(clubNombre); // Assumes this exists
        if (club != null) club.eliminar();
        Persona persona = Persona.buscarPorDni(dni);
        if (persona != null) persona.eliminar();
    }

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
        Persona presidente = new Persona("87654321B", "Juan", "Pérez", "Gómez", LocalDate.of(1980, 1, 1), "juan2", "pass123", "Madrid");
        Club club = new Club("Club Deportivo", LocalDate.of(1990, 5, 15), presidente);
        club.setNombre("Club Atlético");
        assertEquals("Club Atlético", club.getNombre());
    }

    @Test
    public void testSetFechaFundacion() {
        Persona presidente = new Persona("98765432C", "Juan", "Pérez", "Gómez", LocalDate.of(1980, 1, 1), "juan3", "pass123", "Madrid");
        Club club = new Club("Club Deportivo", LocalDate.of(1990, 5, 15), presidente);
        club.setFechaFundacion(LocalDate.of(2000, 10, 20));
        assertEquals(LocalDate.of(2000, 10, 20), club.getFechaFundacion());
    }

    @Test
    public void testSetPresidente() {
        Persona presidente1 = new Persona("12345678D", "Juan", "Pérez", "Gómez", LocalDate.of(1980, 1, 1), "juan4", "pass123", "Madrid");
        Persona presidente2 = new Persona("87654321E", "Ana", "García", "López", LocalDate.of(1975, 2, 2), "anag", "pass456", "Barcelona");
        Club club = new Club("Club Deportivo", LocalDate.of(1990, 5, 15), presidente1);
        club.setPresidente(presidente2);
        assertEquals(presidente2, club.getPresidente());
    }

    @Test
    public void testAddEquipoValid() throws SQLException {
        cleanup("A1", "Grupo A1", "Cat1", "Estadio Principal1", "Club Deportivo Test1", "11111111F");
        Persona presidente = new Persona("11111111F", "Juan", "Pérez", "Gómez", LocalDate.of(1980, 1, 1), "juan5", "pass123", "Madrid");
        Club club = new Club("Club Deportivo Test1", LocalDate.of(1990, 1, 1), presidente);
        Instalacion instalacion = new Instalacion("Estadio Principal1", "Calle Falsa 123", Instalacion.TipoSuperficie.CESPED_NATURAL);
        Categoria categoria = new Categoria("Cat1", 1, 100.0);
        Grupo grupo = new Grupo("Grupo A1");
        grupo.setCategoria(categoria);
        Equipo equipo = new Equipo("A1", instalacion, grupo);
        presidente.guardar();
        club.guardar();
        equipo.setClubId(club.obtenerIdClub()); // Assumes obtenerIdClub() exists
        instalacion.guardar();
        categoria.guardar();
        grupo.guardar();
        equipo.guardar();
        club.addEquipo(equipo);
        try {
            assertEquals(1, club.getNumeroEquipos());
            assertTrue(club.getEquipos().contains(equipo));
        } finally {
            equipo.eliminar();
            grupo.eliminar();
            categoria.eliminar();
            instalacion.eliminar();
            club.eliminar();
            presidente.eliminar();
        }
    }

    @Test
    public void testAddEquipoDuplicate() throws SQLException {
        cleanup("A2", "Grupo A2", "Cat2", "Estadio Principal2", "Club Deportivo Test2", "22222222G");
        Persona presidente = new Persona("22222222G", "Juan", "Pérez", "Gómez", LocalDate.of(1980, 1, 1), "juan6", "pass123", "Madrid");
        Club club = new Club("Club Deportivo Test2", LocalDate.of(1990, 1, 1), presidente);
        Instalacion instalacion = new Instalacion("Estadio Principal2", "Calle Falsa 123", Instalacion.TipoSuperficie.CESPED_NATURAL);
        Categoria categoria = new Categoria("Cat2", 2, 200.0);
        Grupo grupo = new Grupo("Grupo A2");
        grupo.setCategoria(categoria);
        Equipo equipo = new Equipo("A2", instalacion, grupo);
        presidente.guardar();
        club.guardar();
        equipo.setClubId(club.obtenerIdClub()); // Assumes obtenerIdClub() exists
        instalacion.guardar();
        categoria.guardar();
        grupo.guardar();
        equipo.guardar();
        club.addEquipo(equipo);
        try {
            assertThrows(IllegalArgumentException.class, () -> club.addEquipo(equipo));
        } finally {
            equipo.eliminar();
            grupo.eliminar();
            categoria.eliminar();
            instalacion.eliminar();
            club.eliminar();
            presidente.eliminar();
        }
    }

    @Test
    public void testRemoveEquipo() throws SQLException {
        cleanup("A3", "Grupo A3", "Cat3", "Estadio Principal3", "Club Deportivo Test3", "33333333H");
        Persona presidente = new Persona("33333333H", "Juan", "Pérez", "Gómez", LocalDate.of(1980, 1, 1), "juan7", "pass123", "Madrid");
        Club club = new Club("Club Deportivo Test3", LocalDate.of(1990, 1, 1), presidente);
        Instalacion instalacion = new Instalacion("Estadio Principal3", "Calle Falsa 123", Instalacion.TipoSuperficie.CESPED_NATURAL);
        Categoria categoria = new Categoria("Cat3", 3, 300.0);
        Grupo grupo = new Grupo("Grupo A3");
        grupo.setCategoria(categoria);
        Equipo equipo = new Equipo("A3", instalacion, grupo);
        presidente.guardar();
        club.guardar();
        equipo.setClubId(club.obtenerIdClub()); // Assumes obtenerIdClub() exists
        instalacion.guardar();
        categoria.guardar();
        grupo.guardar();
        equipo.guardar();
        club.addEquipo(equipo);
        club.removeEquipo(equipo);
        try {
            assertEquals(0, club.getNumeroEquipos());
            assertTrue(club.getEquipos().isEmpty());
        } finally {
            equipo.eliminar();
            grupo.eliminar();
            categoria.eliminar();
            instalacion.eliminar();
            club.eliminar();
            presidente.eliminar();
        }
    }

    @Test
    public void testToString() {
        Persona presidente = new Persona("12345678I", "Juan", "Pérez", "Gómez", LocalDate.of(1980, 1, 1), "juan8", "pass123", "Madrid");
        Club club = new Club("Club Deportivo", LocalDate.of(1990, 5, 15), presidente);
        String expected = "Club{nombre='Club Deportivo', fechaFundacion=1990-05-15, presidente=12345678I, equipos=0}";
        assertEquals(expected, club.toString());
    }
}