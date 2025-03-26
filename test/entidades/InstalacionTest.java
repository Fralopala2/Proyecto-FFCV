package entidades;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class InstalacionTest {

    @Test
    public void testInstalacionConstructorValid() {
        Instalacion instalacion = new Instalacion("Estadio Principal", "Calle Falsa 123", Instalacion.TipoSuperficie.CESPED_NATURAL);
        assertEquals("Estadio Principal", instalacion.getNombre());
        assertEquals("Calle Falsa 123", instalacion.getDireccion());
        assertEquals(Instalacion.TipoSuperficie.CESPED_NATURAL, instalacion.getSuperficie());
    }

    @Test
    public void testSetters() {
        Instalacion instalacion = new Instalacion("Estadio Principal", "Calle Falsa 123", Instalacion.TipoSuperficie.CESPED_NATURAL);
        instalacion.setNombre("Cancha Secundaria");
        instalacion.setDireccion("Avenida Real 456");
        instalacion.setSuperficie(Instalacion.TipoSuperficie.CESPED_SINTETICO);
        assertEquals("Cancha Secundaria", instalacion.getNombre());
        assertEquals("Avenida Real 456", instalacion.getDireccion());
        assertEquals(Instalacion.TipoSuperficie.CESPED_SINTETICO, instalacion.getSuperficie());
    }

    @Test
    public void testToString() {
        Instalacion instalacion = new Instalacion("Estadio Principal", "Calle Falsa 123", Instalacion.TipoSuperficie.CESPED_NATURAL);
        String expected = "Instalacion{nombre='Estadio Principal', direccion='Calle Falsa 123', superficie='CESPED_NATURAL'}";
        assertEquals(expected, instalacion.toString());
    }
}