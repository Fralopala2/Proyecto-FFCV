package entidades;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

public class EmpleadoTest {
    private void cleanup(String dni) throws SQLException {
        Empleado empleado = Empleado.buscarPorDni(dni);
        if (empleado != null) empleado.eliminar();
        Persona persona = Persona.buscarPorDni(dni);
        if (persona != null) persona.eliminar();
    }

    @Test
    public void testPersistenciaEmpleado() throws SQLException {
        String dni = "12345678E";
        cleanup(dni);
        
        Empleado empleado = new Empleado(dni, "Juan", "Pérez", "Gómez", 
            LocalDate.of(1980, 1, 1), "juanp", "pass123", "Madrid",
            "Entrenador", 1001, LocalDate.of(2020, 6, 1), "SS123456");
        
        try {
            empleado.guardar();
            
            Empleado recuperado = Empleado.buscarPorDni(dni);
            assertNotNull(recuperado);
            assertEquals(dni, recuperado.getDni());
            assertEquals("Entrenador", recuperado.getPuesto());
            assertEquals(1001, recuperado.getNumeroEmpleado());
        } finally {
            cleanup(dni);
        }
    }

    @Test
    public void testActualizarEmpleado() throws SQLException {
        String dni = "12345678F";
        cleanup(dni);
        
        Empleado empleado = new Empleado(dni, "Juan", "Pérez", "Gómez", 
            LocalDate.of(1980, 1, 1), "juanp", "pass123", "Madrid",
            "Entrenador", 1001, LocalDate.of(2020, 6, 1), "SS123456");
        
        try {
            empleado.guardar();
            empleado.setPuesto("Manager");
            empleado.setNumeroEmpleado(1002);
            empleado.actualizar();
            
            Empleado recuperado = Empleado.buscarPorDni(dni);
            assertEquals("Manager", recuperado.getPuesto());
            assertEquals(1002, recuperado.getNumeroEmpleado());
        } finally {
            cleanup(dni);
        }
    }

    @Test
    public void testEmpleadoConstructorValid() {
        Empleado empleado = new Empleado("12345678A", "Juan", "Pérez", "Gómez", LocalDate.of(1980, 1, 1), "juanp", "pass123", "Madrid",
        "Entrenador", 1001, LocalDate.of(2020, 6, 1), "SS123456");
        assertEquals("12345678A", empleado.getDni());
        assertEquals("Juan", empleado.getNombre());
        assertEquals("Pérez", empleado.getApellido1());
        assertEquals("Gómez", empleado.getApellido2());
        assertEquals(LocalDate.of(1980, 1, 1), empleado.getFechaNacimiento());
        assertEquals("juanp", empleado.getUsuario());
        assertEquals("pass123", empleado.getPassword());
        assertEquals("Madrid", empleado.getPoblacion());
        assertEquals("Entrenador", empleado.getPuesto());
        assertEquals(1001, empleado.getNumeroEmpleado());
        assertEquals(LocalDate.of(2020, 6, 1), empleado.getInicioContrato());
        assertEquals("SS123456", empleado.getSegSocial());
    }

    @Test
    public void testSetters() {
        Empleado empleado = new Empleado("12345678A", "Juan", "Pérez", "Gómez", LocalDate.of(1980, 1, 1), "juanp", "pass123", "Madrid",
        "Entrenador", 1001, LocalDate.of(2020, 6, 1), "SS123456");
        empleado.setPuesto("Manager");
        empleado.setNumeroEmpleado(1002);
        empleado.setInicioContrato(LocalDate.of(2021, 7, 1));
        empleado.setSegSocial("SS654321");
        assertEquals("Manager", empleado.getPuesto());
        assertEquals(1002, empleado.getNumeroEmpleado());
        assertEquals(LocalDate.of(2021, 7, 1), empleado.getInicioContrato());
        assertEquals("SS654321", empleado.getSegSocial());
    }

    @Test
    public void testToString() {
        Empleado empleado = new Empleado("12345678A", "Juan", "Pérez", "Gómez", LocalDate.of(1980, 1, 1), "juanp", "pass123", "Madrid",
        "Entrenador", 1001, LocalDate.of(2020, 6, 1), "SS123456");
        String expected = "Persona{dni='12345678A', nombre='Juan', apellido1='Pérez', apellido2='Gómez', fechaNacimiento=1980-01-01, poblacion='Madrid'} - Puesto: Entrenador, Numero de Empleado: 1001, Inicio de Contrato: 2020-06-01, Seguridad Social: SS123456";
        assertEquals(expected, empleado.toString());
    }
}