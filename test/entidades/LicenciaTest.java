package entidades;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LicenciaTest {

    @Test
    public void testLicenciaConstructorValid() {
        Persona persona = new Persona("12345678A", "Juan", "Pérez", "Gómez", LocalDate.of(1980, 1, 1), "juanp", "pass123", "Madrid");
        Licencia licencia = new Licencia(persona, "LIC123");
        assertEquals(persona, licencia.getPersona());
        assertEquals("LIC123", licencia.getNumeroLicencia());
        assertFalse(licencia.isAbonada());
    }

    @Test
    public void testSetAbonada() {
        Persona persona = new Persona("12345678A", "Juan", "Pérez", "Gómez", LocalDate.of(1980, 1, 1), "juanp", "pass123", "Madrid");
        Licencia licencia = new Licencia(persona, "LIC123");
        licencia.setAbonada(true);
        assertTrue(licencia.isAbonada());
        licencia.setAbonada(false);
        assertFalse(licencia.isAbonada());
    }
}