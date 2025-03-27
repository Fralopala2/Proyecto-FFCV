package entidades;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;
import java.util.InputMismatchException;
import org.junit.jupiter.api.Test;

public class CategoriaTest {
    private void cleanup(String nombre) throws SQLException {
        Categoria categoria = Categoria.buscarPorNombre(nombre);
        if (categoria != null) categoria.eliminar();
    }

    @Test
    public void testPersistenciaCategoria() throws SQLException {
        String nombre = "Categoria Test Pers";
        cleanup(nombre);
        
        Categoria categoria = new Categoria(nombre, 1, 100.0);
        
        try {
            categoria.guardar();
            
            Categoria recuperada = Categoria.buscarPorNombre(nombre);
            assertNotNull(recuperada);
            assertEquals(nombre, recuperada.getNombre());
            assertEquals(1, recuperada.getOrden());
            assertEquals(100.0, recuperada.getPrecioLicencia());
        } finally {
            cleanup(nombre);
        }
    }

    @Test
    public void testObtenerTodas() throws SQLException {
        String nombre = "Categoria Test Todas";
        cleanup(nombre);
        
        Categoria categoria = new Categoria(nombre, 1, 100.0);
        
        try {
            categoria.guardar();
            
            var categorias = Categoria.obtenerTodas();
            assertFalse(categorias.isEmpty());
            assertTrue(categorias.stream().anyMatch(c -> c.getNombre().equals(nombre)));
        } finally {
            cleanup(nombre);
        }
    }

    @Test
    public void testCategoriaConstructorValid() {
        Categoria categoria = new Categoria("Categoria1", 1, 100.0);
        assertEquals("Categoria1", categoria.getNombre());
        assertEquals(1, categoria.getOrden());
        assertEquals(100.0, categoria.getPrecioLicencia());
    }

    @Test
    public void testCategoriaConstructorInvalidPrecioLicencia() {
        assertThrows(InputMismatchException.class, () -> {
            new Categoria("Categoria1", 1, -100.0);
        });
    }

    @Test
    public void testSetNombre() {
        Categoria categoria = new Categoria("Categoria1", 1, 100.0);
        categoria.setNombre("Categoria2");
        assertEquals("Categoria2", categoria.getNombre());
    }

    @Test
    public void testSetOrden() {
        Categoria categoria = new Categoria("Categoria1", 1, 100.0);
        categoria.setOrden(2);
        assertEquals(2, categoria.getOrden());
    }

    @Test
    public void testSetPrecioLicenciaValid() {
        Categoria categoria = new Categoria("Categoria1", 1, 100.0);
        categoria.setPrecioLicencia(200.0);
        assertEquals(200.0, categoria.getPrecioLicencia());
    }

    @Test
    public void testSetPrecioLicenciaInvalid() {
        Categoria categoria = new Categoria("Categoria1", 1, 100.0);
        assertThrows(InputMismatchException.class, () -> {
            categoria.setPrecioLicencia(-200.0);
        });
    }

    @Test
    public void testGetGrupos() {
        Categoria categoria = new Categoria("Categoria1", 1, 100.0);
        assertNotNull(categoria.getGrupos());
        assertTrue(categoria.getGrupos().isEmpty());
    }

    @Test
    public void testToString() {
        Categoria categoria = new Categoria("Categoria1", 1, 100.0);
        String expected = "Categoria{nombre=Categoria1, orden=1, precioLicencia=100.0}";
        assertEquals(expected, categoria.toString());
    }
}