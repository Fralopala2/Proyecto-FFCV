package entidades;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

public class PersonaTest {
    private void cleanup(String dni) throws SQLException {
        Persona persona = Persona.buscarPorDni(dni);
        if (persona != null) persona.eliminar();
    }

    @Test
    public void testPersistenciaPersona() throws SQLException {
        String dni = "12345678X";
        cleanup(dni);
        
        Persona persona = new Persona(dni, "Juan", "Pérez", "Gómez", 
            LocalDate.of(1980, 1, 1), "juanp", "pass123", "Madrid");
        
        try {
            persona.guardar();
            
            Persona recuperada = Persona.buscarPorDni(dni);
            assertNotNull(recuperada);
            assertEquals(dni, recuperada.getDni());
            assertEquals("Juan", recuperada.getNombre());
            assertEquals("Madrid", recuperada.getPoblacion());
        } finally {
            cleanup(dni);
        }
    }

    @Test
    public void testBusquedaPorNombreYApellidos() throws SQLException {
        String dni = "12345678Y";
        cleanup(dni);
        
        Persona persona = new Persona(dni, "Juan", "Pérez", "Gómez", 
            LocalDate.of(1980, 1, 1), "juanp", "pass123", "Madrid");
        
        try {
            persona.guardar();
            
            var resultados = Persona.buscarPorNombreYApellidos("Juan", "Pérez", "Gómez");
            assertFalse(resultados.isEmpty());
            assertEquals(dni, resultados.get(0).getDni());
        } finally {
            cleanup(dni);
        }
    }

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
    public void testSetDniInvalid() {
        Persona persona = new Persona("12345678A", "Juan", "Pérez", "Gómez", LocalDate.of(1980, 1, 1), "juanp", "pass123", "Madrid");
        assertThrows(IllegalArgumentException.class, () -> persona.setDni(null));
        assertThrows(IllegalArgumentException.class, () -> persona.setDni(""));
        assertThrows(IllegalArgumentException.class, () -> persona.setDni("   "));
    }

    @Test
    public void testToString() {
        Persona persona = new Persona("12345678A", "Juan", "Pérez", "Gómez", LocalDate.of(1980, 1, 1), "juanp", "pass123", "Madrid");
        String expected = "Persona{dni='12345678A', nombre='Juan', apellido1='Pérez', apellido2='Gómez', fechaNacimiento=1980-01-01, poblacion='Madrid'}";
        assertEquals(expected, persona.toString());
    }
}