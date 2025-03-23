package entidades;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class GrupoTest {

    @Test
    public void testGrupoConstructorValid() {
        Grupo grupo = new Grupo("Grupo A");
        assertEquals("Grupo A", grupo.getNombre());
        assertNotNull(grupo.getEquipos());
        assertTrue(grupo.getEquipos().isEmpty());
        assertEquals(null, grupo.getCategoria());
    }

    @Test
    public void testSetNombre() {
        Grupo grupo = new Grupo("Grupo A");
        grupo.setNombre("Grupo B");
        assertEquals("Grupo B", grupo.getNombre());
    }

    @Test
    public void testSetCategoriaValid() {
        Grupo grupo = new Grupo("Grupo A");
        Categoria categoria = new Categoria("Categoria1", 1, 100.0);
        grupo.setCategoria(categoria);
        assertEquals(categoria, grupo.getCategoria());
    }

    @Test
    public void testSetCategoriaInvalid() {
        Grupo grupo = new Grupo("Grupo A");
        assertThrows(IllegalArgumentException.class, () -> {
            grupo.setCategoria(null);
        });
    }

    @Test
    public void testToString() {
        Grupo grupo = new Grupo("Grupo A");
        String expected = "Grupo{nombre='Grupo A', categoria=null, equipos=0 equipos}";
        assertEquals(expected, grupo.toString());
    }
}