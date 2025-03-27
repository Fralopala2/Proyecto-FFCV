package entidades;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class EquipoTest {
    private void cleanup(String letra, String grupoNombre, String catNombre, String instalacionNombre) throws SQLException {
        Equipo equipo = Equipo.buscarPorLetra(letra);
        if (equipo != null) equipo.eliminar();
        Grupo grupo = Grupo.buscarPorNombre(grupoNombre);
        if (grupo != null) grupo.eliminar();
        Categoria categoria = Categoria.buscarPorNombre(catNombre);
        if (categoria != null) categoria.eliminar();
        Instalacion instalacion = Instalacion.buscarPorNombre(instalacionNombre);
        if (instalacion != null) instalacion.eliminar();
    }

    @Test
    public void testPersistenciaEquipo() throws SQLException {
        String letra = "X";
        String grupoNombre = "Grupo Test";
        String catNombre = "Categoria Test";
        String instalacionNombre = "Instalacion Test";
        cleanup(letra, grupoNombre, catNombre, instalacionNombre);
        
        Categoria categoria = new Categoria(catNombre, 1, 100.0);
        Grupo grupo = new Grupo(grupoNombre);
        grupo.setCategoria(categoria);
        Instalacion instalacion = new Instalacion(instalacionNombre, "Direccion", Instalacion.TipoSuperficie.CESPED_NATURAL);
        Equipo equipo = new Equipo(letra, instalacion, grupo);
        
        try {
            categoria.guardar();
            grupo.guardar();
            instalacion.guardar();
            equipo.guardar();
            
            Equipo recuperado = Equipo.buscarPorLetra(letra);
            assertNotNull(recuperado);
            assertEquals(letra, recuperado.getLetra());
            assertEquals(grupoNombre, recuperado.getGrupo().getNombre());
        } finally {
            cleanup(letra, grupoNombre, catNombre, instalacionNombre);
        }
    }

    @Test
    public void testBuscarJugador() throws SQLException {
        String letra = "Y";
        String grupoNombre = "Grupo Test Jug";
        String catNombre = "Categoria Test Jug";
        String instalacionNombre = "Instalacion Test Jug";
        String dni = "12345678Z";
        cleanup(letra, grupoNombre, catNombre, instalacionNombre);
        
        Persona persona = new Persona(dni, "Jugador", "Test", "Apellido", 
        LocalDate.of(1990,1,1), "user", "pass", "Ciudad");
        Licencia licencia = new Licencia(persona, "LIC-123");
        
        Categoria categoria = new Categoria(catNombre, 1, 100.0);
        Grupo grupo = new Grupo(grupoNombre);
        grupo.setCategoria(categoria);
        Instalacion instalacion = new Instalacion(instalacionNombre, "Direccion", Instalacion.TipoSuperficie.CESPED_NATURAL);
        Equipo equipo = new Equipo(letra, instalacion, grupo);
        
        try {
            persona.guardar();
            licencia.guardar();
            categoria.guardar();
            grupo.guardar();
            instalacion.guardar();
            equipo.guardar();
            
            licencia.asignarAEquipo(equipo);
            
            Persona jugador = equipo.buscarJugador(dni);
            assertNotNull(jugador);
            assertEquals(dni, jugador.getDni());
        } finally {
            licencia.eliminar();
            persona.eliminar();
            cleanup(letra, grupoNombre, catNombre, instalacionNombre);
        }
    }

    @Test
    public void testEquipoConstructorValid() {
        Instalacion instalacion = new Instalacion("Estadio Principal", "Calle Falsa 123", Instalacion.TipoSuperficie.CESPED_NATURAL);
        Grupo grupo = new Grupo("Grupo A");
        Equipo equipo = new Equipo("A", instalacion, grupo);
        assertEquals("A", equipo.getLetra());
        assertEquals(instalacion, equipo.getInstalacion());
        assertEquals(grupo, equipo.getGrupo());
        assertNotNull(equipo.getLicencias());
        assertTrue(equipo.getLicencias().isEmpty());
    }

    @Test
    public void testSetLetra() {
        Instalacion instalacion = new Instalacion("Estadio Principal", "Calle Falsa 123", Instalacion.TipoSuperficie.CESPED_NATURAL);
        Grupo grupo = new Grupo("Grupo A");
        Equipo equipo = new Equipo("A", instalacion, grupo);
        equipo.setLetra("B");
        assertEquals("B", equipo.getLetra());
    }

    @Test
    public void testSetInstalacion() {
        Instalacion instalacion1 = new Instalacion("Estadio Principal", "Calle Falsa 123", Instalacion.TipoSuperficie.CESPED_NATURAL);
        Instalacion instalacion2 = new Instalacion("Cancha Secundaria", "Avenida Real 456", Instalacion.TipoSuperficie.CESPED_SINTETICO);
        Grupo grupo = new Grupo("Grupo A");
        Equipo equipo = new Equipo("A", instalacion1, grupo);
        equipo.setInstalacion(instalacion2);
        assertEquals(instalacion2, equipo.getInstalacion());
    }

    @Test
    public void testSetGrupo() {
        Instalacion instalacion = new Instalacion("Estadio Principal", "Calle Falsa 123", Instalacion.TipoSuperficie.CESPED_NATURAL);
        Grupo grupo1 = new Grupo("Grupo A");
        Grupo grupo2 = new Grupo("Grupo B");
        Equipo equipo = new Equipo("A", instalacion, grupo1);
        equipo.setGrupo(grupo2);
        assertEquals(grupo2, equipo.getGrupo());
    }

    @Test
    public void testCalcularPrecioLicencia() {
        Instalacion instalacion = new Instalacion("Estadio Principal", "Calle Falsa 123", Instalacion.TipoSuperficie.CESPED_NATURAL);
        Grupo grupo = new Grupo("Grupo A");
        Categoria categoria = new Categoria("Categoria1", 1, 100.0);
        grupo.setCategoria(categoria);
        Equipo equipo = new Equipo("A", instalacion, grupo);
        assertEquals(100.0, equipo.calcularPrecioLicencia(), 0.001);
    }

    @Test
    public void testCalcularPrecioLicenciaNoCategoria() {
        Instalacion instalacion = new Instalacion("Estadio Principal", "Calle Falsa 123", Instalacion.TipoSuperficie.CESPED_NATURAL);
        Grupo grupo = new Grupo("Grupo A");
        Equipo equipo = new Equipo("A", instalacion, grupo);
        assertEquals(0.0, equipo.calcularPrecioLicencia(), 0.001);
    }

    @Test
    public void testToString() {
        Instalacion instalacion = new Instalacion("Estadio Principal", "Calle Falsa 123", Instalacion.TipoSuperficie.CESPED_NATURAL);
        Grupo grupo = new Grupo("Grupo A");
        Equipo equipo = new Equipo("A", instalacion, grupo);
        String expected = "Equipo{letra='A', instalacion=Estadio Principal, grupo=Grupo A, licencias=0 licencias}";
        assertEquals(expected, equipo.toString());
    }
}