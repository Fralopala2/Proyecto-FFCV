package entidades;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

public class LicenciaTest {
    private void cleanup(String licenciaNum, String dni, String letraEquipo, 
                        String grupoNombre, String catNombre, String instalacionNombre) throws SQLException {
        // Limpiar licencia
        Licencia licencia = Licencia.buscarPorNumero(licenciaNum);
        if (licencia != null) licencia.eliminar();
        
        // Limpiar persona
        Persona persona = Persona.buscarPorDni(dni);
        if (persona != null) persona.eliminar();
        
        // Limpiar equipo y dependencias
        Equipo equipo = Equipo.buscarPorLetra(letraEquipo);
        if (equipo != null) equipo.eliminar();
        
        Grupo grupo = Grupo.buscarPorNombre(grupoNombre);
        if (grupo != null) grupo.eliminar();
        
        Categoria categoria = Categoria.buscarPorNombre(catNombre);
        if (categoria != null) categoria.eliminar();
        
        Instalacion instalacion = Instalacion.buscarPorNombre(instalacionNombre);
        if (instalacion != null) instalacion.eliminar();
    }

    @Test
    public void testLicenciaConstructorValid() {
        Persona persona = new Persona("12345678A", "Juan", "Pérez", "Gómez", 
                                    LocalDate.of(1980, 1, 1), "juanp", "pass123", "Madrid");
        Licencia licencia = new Licencia(persona, "LIC123");
        
        assertEquals(persona, licencia.getPersona());
        assertEquals("LIC123", licencia.getNumeroLicencia());
        assertFalse(licencia.isAbonada());
    }

    @Test
    public void testSetAbonada() {
        Persona persona = new Persona("12345678B", "Ana", "García", "López", 
                                    LocalDate.of(1975, 2, 2), "anag", "pass456", "Barcelona");
        Licencia licencia = new Licencia(persona, "LIC456");
        
        licencia.setAbonada(true);
        assertTrue(licencia.isAbonada());
        
        licencia.setAbonada(false);
        assertFalse(licencia.isAbonada());
    }

    @Test
    public void testPersistenciaLicencia() throws SQLException {
        String dni = "12345678X";
        String licenciaNum = "LIC-12345";
        cleanup(licenciaNum, dni, "", "", "", "");
        
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
            assertFalse(recuperada.isAbonada());
        } finally {
            cleanup(licenciaNum, dni, "", "", "", "");
        }
    }

    @Test
    public void testAsignarAEquipo() throws SQLException {
        String dni = "12345678Y";
        String licenciaNum = "LIC-12346";
        String letraEquipo = "A";
        String grupoNombre = "Grupo Test";
        String catNombre = "Cat Test";
        String instalacionNombre = "Estadio Test";
        
        cleanup(licenciaNum, dni, letraEquipo, grupoNombre, catNombre, instalacionNombre);
        
        // Configuración inicial
        Persona persona = new Persona(dni, "Juan", "Pérez", "Gómez", 
                                    LocalDate.of(1980, 1, 1), "juanp", "pass123", "Madrid");
        Licencia licencia = new Licencia(persona, licenciaNum);
        
        // Configurar equipo
        Instalacion instalacion = new Instalacion(instalacionNombre, "Calle Test", 
                                                Instalacion.TipoSuperficie.CESPED_NATURAL);
        Categoria categoria = new Categoria(catNombre, 1, 100.0);
        Grupo grupo = new Grupo(grupoNombre);
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
            
            // Verificar la asignación
            Licencia recuperada = Licencia.buscarPorNumero(licenciaNum);
            assertNotNull(recuperada);
            
            // Verificar que la licencia está en el equipo
            equipo = Equipo.buscarPorLetra(letraEquipo);
            assertTrue(equipo.getLicencias().stream()
                          .anyMatch(l -> l.getNumeroLicencia().equals(licenciaNum)));
        } finally {
            // Limpieza en orden inverso
            cleanup(licenciaNum, dni, letraEquipo, grupoNombre, catNombre, instalacionNombre);
        }
    }

    @Test
    public void testToString() {
        Persona persona = new Persona("12345678Z", "Carlos", "Ruiz", "Santos", 
                                    LocalDate.of(1985, 5, 15), "carlosr", "pass789", "Valencia");
        Licencia licencia = new Licencia(persona, "LIC789");
        licencia.setAbonada(true);
        
        String expected = "Licencia{numero='LIC789', persona=12345678Z, abonada=true}";
        assertEquals(expected, licencia.toString());
    }
}