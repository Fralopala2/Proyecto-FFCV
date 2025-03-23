package entidades;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class EquipoTest {

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