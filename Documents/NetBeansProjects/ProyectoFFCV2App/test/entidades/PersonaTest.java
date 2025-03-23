package entidades;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class PersonaTest {

    @Test
    public void testPersonaConstructorValid() {
        Persona persona = new Persona("12345678A", "Juan", "Pérez", "Gómez", LocalDate.of(1980, 1, 1), "juanp", "pass123", "Madrid");
        assertEquals("12345678A", persona.getDni());
        assertEquals("Juan", persona.getNombre());
        assertEquals("Pérez", persona.getApellido1());
        assertEquals("Gómez", persona.getApellido2());
        assertEquals(LocalDate.of(1980, 1, 1), persona.getFechaNacimiento());
        assertEquals("juanp", persona.getUsuario());
        assertEquals("pass123", persona.getPassword());
        assertEquals("Madrid", persona.getPoblacion());
    }

    @Test
    public void testSetters() {
        Persona persona = new Persona("12345678A", "Juan", "Pérez", "Gómez", LocalDate.of(1980, 1, 1), "juanp", "pass123", "Madrid");
        persona.setDni("87654321B");
        persona.setNombre("Ana");
        persona.setApellido1("García");
        persona.setApellido2("López");
        persona.setFechaNacimiento(LocalDate.of(1975, 2, 2));
        persona.setUsuario("anag");
        persona.setPassword("pass456");
        persona.setPoblacion("Barcelona");
        assertEquals("87654321B", persona.getDni());
        assertEquals("Ana", persona.getNombre());
        assertEquals("García", persona.getApellido1());
        assertEquals("López", persona.getApellido2());
        assertEquals(LocalDate.of(1975, 2, 2), persona.getFechaNacimiento());
        assertEquals("anag", persona.getUsuario());
        assertEquals("pass456", persona.getPassword());
        assertEquals("Barcelona", persona.getPoblacion());
    }

    @Test
    public void testToString() {
        Persona persona = new Persona("12345678A", "Juan", "Pérez", "Gómez", LocalDate.of(1980, 1, 1), "juanp", "pass123", "Madrid");
        String expected = "Persona{dni='12345678A', nombre='Juan', apellido1='Pérez', apellido2='Gómez', fechaNacimiento=1980-01-01, poblacion='Madrid'}";
        assertEquals(expected, persona.toString());
    }
}