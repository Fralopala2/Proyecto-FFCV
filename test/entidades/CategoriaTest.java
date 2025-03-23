package entidades;

import java.util.InputMismatchException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class CategoriaTest {

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