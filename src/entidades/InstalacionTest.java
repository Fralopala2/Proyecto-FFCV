package entidades;

import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InstalacionTest {
    private void cleanup(String nombreInstalacion) throws SQLException {
        Instalacion instalacion = Instalacion.buscarPorNombre(nombreInstalacion);
        if (instalacion != null) instalacion.eliminar();
    }

    @Test
    public void testPersistenciaInstalacion() throws SQLException {
        String nombre = "Estadio Test";
        cleanup(nombre);
        
        Instalacion instalacion = new Instalacion(nombre, "Calle Test", 
            Instalacion.TipoSuperficie.CESPED_NATURAL);
        
        try {
            instalacion.guardar();
            
            Instalacion recuperada = Instalacion.buscarPorNombre(nombre);
            assertNotNull(recuperada);
            assertEquals(nombre, recuperada.getNombre());
            assertEquals(Instalacion.TipoSuperficie.CESPED_NATURAL, recuperada.getSuperficie());
        } finally {
            cleanup(nombre);
        }
    }

    @Test
    public void testBusquedaParcial() throws SQLException {
        String nombreBase = "Estadio Parcial Test";
        cleanup(nombreBase + "1");
        cleanup(nombreBase + "2");
        
        Instalacion i1 = new Instalacion(nombreBase + "1", "Calle 1", 
            Instalacion.TipoSuperficie.CESPED_NATURAL);
        Instalacion i2 = new Instalacion(nombreBase + "2", "Calle 2", 
            Instalacion.TipoSuperficie.CESPED_SINTETICO);
        
        try {
            i1.guardar();
            i2.guardar();
            
            List<Instalacion> resultados = Instalacion.buscarPorNombreParcial(nombreBase);
            assertEquals(2, resultados.size());
        } finally {
            cleanup(nombreBase + "1");
            cleanup(nombreBase + "2");
        }
    }
}