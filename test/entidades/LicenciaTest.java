package entidades;

import java.time.LocalDate;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LicenciaTest {
    private void cleanup(String licenciaNum, String dni) throws SQLException {
        Licencia licencia = Licencia.buscarPorNumero(licenciaNum);
        if (licencia != null) licencia.eliminar();
        Persona persona = Persona.buscarPorDni(dni);
        if (persona != null) persona.eliminar();
    }

    @Test
    public void testPersistenciaLicencia() throws SQLException {
        String dni = "12345678X";
        String licenciaNum = "LIC-12345";
        cleanup(licenciaNum, dni);
        
        Persona persona = new Persona(dni, "Juan", "Pérez", "Gómez", 
            LocalDate.of(1980, 1, 1), "juanp", "pass123", "Madrid");
        Licencia licencia = new Licencia(persona, licenciaNum);
        
        try {
            persona.guardar();
            licencia.guardar();
            
            Licencia recuperada = Licencia.buscarPorNumero(licenciaNum);
            assertNotNull(recuperada);
            assertEquals(dni, recuperada.getPersona().getDni());
            assertEquals(licenciaNum, recuperada.getNumeroLicencia());
        } finally {
            cleanup(licenciaNum, dni);
        }
    }

    @Test
    public void testAsignarAEquipo() throws SQLException {
        String dni = "12345678Y";
        String licenciaNum = "LIC-12346";
        String letraEquipo = "A";
        cleanup(licenciaNum, dni);
        
        // Configuración inicial similar a ClubTest
        Persona persona = new Persona(dni, "Juan", "Pérez", "Gómez", 
            LocalDate.of(1980, 1, 1), "juanp", "pass123", "Madrid");
        Licencia licencia = new Licencia(persona, licenciaNum);
        
        // Configurar equipo (similar a ClubTest)
        Instalacion instalacion = new Instalacion("Estadio Test", "Calle Test", Instalacion.TipoSuperficie.CESPED_NATURAL);
        Categoria categoria = new Categoria("Cat Test", 1, 100.0);
        Grupo grupo = new Grupo("Grupo Test");
        grupo.setCategoria(categoria);
        Equipo equipo = new Equipo(letraEquipo, instalacion, grupo);
        
        try {
            // Persistir todos los objetos
            persona.guardar();
            licencia.guardar();
            instalacion.guardar();
            categoria.guardar();
            grupo.guardar();
            equipo.guardar();
            
            // Probar la asignación
            licencia.asignarAEquipo(equipo);
            
            // Verificar
            Licencia recuperada = Licencia.buscarPorNumero(licenciaNum);
            // Aquí necesitarías un método para verificar la asignación
            // Por ejemplo, buscar el equipo asociado a la licencia
        } finally {
            // Limpieza en orden inverso
            equipo.eliminar();
            grupo.eliminar();
            categoria.eliminar();
            instalacion.eliminar();
            licencia.eliminar();
            persona.eliminar();
        }
    }
}