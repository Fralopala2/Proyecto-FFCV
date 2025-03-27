package entidades;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

public class GrupoTest {
    private void cleanup(String grupoNombre, String catNombre) throws SQLException {
        Grupo grupo = Grupo.buscarPorNombre(grupoNombre);
        if (grupo != null) grupo.eliminar();
        Categoria categoria = Categoria.buscarPorNombre(catNombre);
        if (categoria != null) categoria.eliminar();
    }

    @Test
    public void testPersistenciaGrupo() throws SQLException {
        String grupoNombre = "Grupo Test";
        String catNombre = "Categoria Test";
        cleanup(grupoNombre, catNombre);
        
        Categoria categoria = new Categoria(catNombre, 1, 100.0);
        Grupo grupo = new Grupo(grupoNombre);
        grupo.setCategoria(categoria);
        
        try {
            categoria.guardar();
            grupo.guardar();
            
            Grupo recuperado = Grupo.buscarPorNombre(grupoNombre);
            assertNotNull(recuperado);
            assertEquals(grupoNombre, recuperado.getNombre());
            assertEquals(catNombre, recuperado.getCategoria().getNombre());
        } finally {
            cleanup(grupoNombre, catNombre);
        }
    }

    @Test
    public void testObtenerPorCategoria() throws SQLException {
        String grupoNombre = "Grupo Test Cat";
        String catNombre = "Categoria Test Cat";
        cleanup(grupoNombre, catNombre);
        
        Categoria categoria = new Categoria(catNombre, 1, 100.0);
        Grupo grupo = new Grupo(grupoNombre);
        grupo.setCategoria(categoria);
        
        try {
            categoria.guardar();
            grupo.guardar();
            
            var resultados = Grupo.obtenerPorCategoria(categoria);
            assertFalse(resultados.isEmpty());
            assertEquals(grupoNombre, resultados.get(0).getNombre());
        } finally {
            cleanup(grupoNombre, catNombre);
        }
    }

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