package proyectoffcv;

import proyectoffcv.logica.Federacion;
import proyectoffcv.util.DatabaseConnection;
import entidades.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Federacion federacion = Federacion.getInstance();
        
        System.out.println("=== INICIO DE PRUEBAS ===");
        
        try {
            // 0. Verificar conexión a la base de datos
            System.out.println("Verificando conexion a la base de datos...");
            if (!checkDatabaseConnection()) {
                throw new RuntimeException("No se pudo conectar a la base de datos.");
            }
            
            // 1. Limpiar toda la base de datos antes de empezar
            System.out.println("Preparando base de datos para pruebas...");
            federacion.limpiarTablas();
            
            // 2. Prueba de Personas
            System.out.println("\n--- Prueba de Personas ---");
            Persona persona1 = federacion.nuevaPersona("12345678A", "Juan", "Perez", "Gomez", 
                    LocalDate.of(1990, 1, 1), "juanp", "pass123", "Valencia");
            System.out.println("Persona creada: " + persona1);
            
            Persona persona2 = federacion.nuevaPersona("87654321B", "María", "Lopez", null, 
                    LocalDate.of(1985, 5, 15), "marial", "pass456", "Alicante");
            System.out.println("Persona creada: " + persona2);
            
            // Buscar persona
            Persona personaEncontrada = federacion.buscaPersona("12345678A");
            System.out.println("Persona encontrada por DNI: " + personaEncontrada);
            
            // Actualizar persona
            persona1.setNombre("Juan Carlos");
            persona1.actualizar();
            System.out.println("Persona actualizada: " + federacion.buscaPersona("12345678A"));
            
            // Prueba de error: DNI duplicado
            try {
                federacion.nuevaPersona("12345678A", "Ana", "García", null, 
                        LocalDate.of(1995, 2, 2), "anag", "pass789", "Valencia");
                System.out.println("Error: Deberia haber fallado (DNI duplicado)");
            } catch (IllegalStateException e) {
                System.out.println("Prueba de DNI duplicado correcta: " + e.getMessage());
            }
            
            // 3. Prueba de Empleados
            System.out.println("\n--- Prueba de Empleados ---");
            Empleado empleado = federacion.nuevoEmpleado("11223344H", "Carlos", "Martinez", "Sanchez",
                    LocalDate.of(1980, 3, 20), "carlosm", "pass789", "Castellon", 
                    1001, LocalDate.now(), "123456789012");
            empleado.setPuesto("Entrenador");
            System.out.println("Empleado creado: " + empleado);
            
            // Actualizar empleado
            empleado.setPuesto("Director Tecnico");
            empleado.actualizar();
            System.out.println("Empleado actualizado: " + federacion.buscaPersona("11223344H"));
            
            // Prueba de error: número de empleado inválido
            try {
                federacion.nuevoEmpleado("22334455I", "Luis", "Gomez", null,
                        LocalDate.of(1982, 4, 10), "luisg", "pass101", "Valencia", 
                        -1, LocalDate.now(), "987654321098");
                System.out.println("Error: Deberia haber fallado (numero de empleado invalido)");
            } catch (IllegalArgumentException e) {
                System.out.println("Prueba de número de empleado invalido correcta: " + e.getMessage());
            }
            
            // 4. Prueba de Clubes
            System.out.println("\n--- Prueba de Clubes ---");
            Club club1 = federacion.nuevoClub("Valencia CF", LocalDate.of(1919, 3, 18), persona1);
            System.out.println("Club creado: " + club1);
            
            Club club2 = federacion.nuevoClub("Villarreal CF", LocalDate.of(1923, 3, 10), persona2);
            System.out.println("Club creado: " + club2);
            
            // Buscar club
            Club clubEncontrado = federacion.buscarClub("Valencia CF");
            System.out.println("Club encontrado: " + clubEncontrado);
            
            // Actualizar club
            club1.setFechaFundacion(LocalDate.of(1919, 3, 19));
            club1.actualizar();
            System.out.println("Club actualizado: " + federacion.buscarClub("Valencia CF"));
            
            // Prueba de error: nombre de club nulo
            try {
                federacion.nuevoClub(null, LocalDate.of(2000, 1, 1), persona1);
                System.out.println("Error: Deberia haber fallado (nombre nulo)");
            } catch (IllegalArgumentException e) {
                System.out.println("Prueba de nombre de club nulo correcta: " + e.getMessage());
            }
            
            // 5. Prueba de Categorías
            System.out.println("\n--- Prueba de Categorias ---");
            Categoria senior = federacion.nuevaCategoria("Senior", 1, 100.0);
            System.out.println("Categoria creada: " + senior);
            
            Categoria juvenil = federacion.nuevaCategoria("Juvenil", 2, 75.0);
            System.out.println("Categoria creada: " + juvenil);
            
            // Listar todas las categorías
            List<Categoria> categorias = federacion.obtenerCategorias();
            System.out.println("\nTodas las categorias:");
            categorias.forEach(System.out::println);
            
            // Prueba de error: precio de licencia negativo
            try {
                federacion.nuevaCategoria("Infantil", 3, -50.0);
                System.out.println("Error: Deberia haber fallado (precio negativo)");
            } catch (IllegalArgumentException e) {
                System.out.println("Prueba de precio de licencia negativo correcta: " + e.getMessage());
            }
            
            // 6. Prueba de Grupos
            System.out.println("\n--- Prueba de Grupos ---");
            Grupo grupoA = federacion.nuevoGrupo(senior, "Grupo A");
            System.out.println("Grupo creado: " + grupoA);
            
            Grupo grupoB = federacion.nuevoGrupo(senior, "Grupo B");
            System.out.println("Grupo creado: " + grupoB);
            
            // Listar grupos por categoría
            List<Grupo> gruposSenior = federacion.obtenerGrupos(senior);
            System.out.println("\nGrupos en categoria Senior:");
            gruposSenior.forEach(System.out::println);
            
            // Actualizar grupo
            grupoA.setNombre("Grupo A Modificado");
            grupoA.actualizar();
            System.out.println("Grupo actualizado: " + federacion.obtenerGrupos(senior).stream()
                    .filter(g -> g.getId() == grupoA.getId()).findFirst().orElse(null));
            
            // Prueba de error: categoría nula
            try {
                federacion.nuevoGrupo(null, "Grupo C");
                System.out.println("Error: Deberia haber fallado (categoria nula)");
            } catch (IllegalArgumentException e) {
                System.out.println("Prueba de categoria nula correcta: " + e.getMessage());
            }
            
            // 7. Prueba de Instalaciones
            System.out.println("\n--- Prueba de Instalaciones ---");
            Instalacion mestalla = federacion.nuevaInstalacion("Mestalla", "Av. de Suecia, Valencia", 
                    "CESPED_NATURAL");
            System.out.println("Instalacion creada: " + mestalla);
            
            Instalacion ciutat = federacion.nuevaInstalacion("Ciutat de Valencia", "C/ de Sant Vicent, Valencia", 
                    "CESPED_ARTIFICIAL");
            System.out.println("Instalacion creada: " + ciutat);
            
            // Actualizar instalación
            mestalla.setDireccion("Av. de Suecia 123, Valencia");
            mestalla.actualizar();
            System.out.println("Instalacion actualizada: " + federacion.buscarInstalaciones("Mestalla").get(0));
            
            // Prueba de error: superficie inválida
            try {
                federacion.nuevaInstalacion("Estadio Nuevo", "Calle Falsa, Valencia", "INVALIDO");
                System.out.println("Error: Deberia haber fallado (superficie invalida)");
            } catch (IllegalStateException e) {
                System.out.println("Prueba de superficie invalida correcta: " + e.getMessage());
            }
            
            // 8. Prueba de Equipos
            System.out.println("\n--- Prueba de Equipos ---");
            // Verificar que las dependencias existen
            if (Instalacion.buscarPorNombre("Mestalla") == null) {
                System.out.println("Error: Instalación Mestalla no encontrada en la base de datos");
            }
            if (Grupo.buscarPorNombre("Grupo A") == null) {
                System.out.println("Error: Grupo A no encontrado en la base de datos");
            }
            if (Club.buscarPorNombre("Valencia CF") == null) {
                System.out.println("Error: Club Valencia CF no encontrado en la base de datos");
            }
            Equipo equipo1 = federacion.nuevoEquipo("A", mestalla, grupoA, club1);
            System.out.println("Equipo creado: " + equipo1);
            
            Equipo equipo2 = federacion.nuevoEquipo("B", ciutat, grupoB, club1);
            System.out.println("Equipo creado: " + equipo2);
            
            // Buscar jugador en equipo (debería fallar porque no hay jugadores aún)
            System.out.println("\nBuscando jugador en equipo (deberia ser null):");
            Persona jugador = equipo1.buscarJugador("12345678A");
            System.out.println("Jugador encontrado: " + jugador);
            
            // Prueba de error: letra de equipo duplicada
            try {
                federacion.nuevoEquipo("A1", mestalla, grupoA, club1);
                System.out.println("Error: Deberia haber fallado (letra duplicada)");
            } catch (IllegalStateException e) {
                System.out.println("Prueba de letra de equipo duplicada correcta: " + e.getMessage());
            }
            
            // 9. Prueba de Licencias
            System.out.println("\n--- Prueba de Licencias ---");
            Licencia licencia1 = federacion.nuevaLicencia(persona1, equipo1);
            System.out.println("Licencia con equipo creada: " + licencia1);
            
            Licencia licencia2 = federacion.nuevaLicencia(persona2);
            System.out.println("Licencia simple creada: " + licencia2);
            
            // Prueba de addLicencia
            federacion.addLicencia(licencia2, equipo2);
            System.out.println("Licencia asignada a equipo2: " + licencia2);
            
            // Calcular precio de licencia
            double precioLicencia = federacion.calcularPrecioLicencia(equipo1);
            System.out.println("Precio de licencia para equipo1: " + precioLicencia);
            
            // Actualizar licencia
            licencia1.setAbonada(true);
            licencia1.actualizar();
            System.out.println("Licencia actualizada: " + Licencia.buscarPorNumero(licencia1.getNumeroLicencia()));
            
            // Prueba de error: licencia duplicada para persona
            try {
                federacion.nuevaLicencia(persona1, equipo2);
                System.out.println("Error: Deberia haber fallado (licencia duplicada)");
            } catch (IllegalStateException e) {
                System.out.println("Prueba de licencia duplicada correcta: " + e.getMessage());
            }
            
            // 10. Pruebas de búsquedas
            System.out.println("\n--- Pruebas de Busquedas ---");
            List<Persona> personas = federacion.buscaPersonas("Juan", "Perez", "Gomez");
            System.out.println("Personas encontradas (Juan Perez Gomez):");
            personas.forEach(System.out::println);
            
            List<Instalacion> instalaciones = federacion.buscarInstalaciones("Mestalla");
            System.out.println("\nInstalaciones encontradas (Mestalla):");
            instalaciones.forEach(System.out::println);
            
            // 11. Pruebas de eliminación
            System.out.println("\n--- Pruebas de Eliminacion ---");
            try {
                // Eliminar en orden inverso para respetar FKs
                licencia1.eliminar();
                System.out.println("Licencia1 eliminada");
                
                licencia2.eliminar();
                System.out.println("Licencia2 eliminada");
                
                equipo2.eliminar();
                System.out.println("Equipo2 eliminado");
                
                equipo1.eliminar();
                System.out.println("Equipo1 eliminado");
                
                grupoB.eliminar();
                System.out.println("GrupoB eliminado");
                
                grupoA.eliminar();
                System.out.println("GrupoA eliminado");
                
                ciutat.eliminar();
                System.out.println("Instalacion Ciutat eliminada");
                
                mestalla.eliminar();
                System.out.println("Instalacion Mestalla eliminada");
                
                club2.eliminar();
                System.out.println("Club2 eliminado");
                
                club1.eliminar();
                System.out.println("Club1 eliminado");
                
                empleado.eliminar();
                System.out.println("Empleado eliminado");
                
                persona2.eliminar();
                System.out.println("Persona2 eliminada");
                
                persona1.eliminar();
                System.out.println("Persona1 eliminada");
            } catch (SQLException e) {
                System.err.println("Error al eliminar: " + e.getMessage());
            }
            
        } catch (SQLException e) {
            System.err.println("Error de base de datos:");
            e.printStackTrace();
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("Error de validacion:");
            e.printStackTrace();
        }
        
        System.out.println("\n=== FIN DE PRUEBAS ===");
    }
    
    private static boolean checkDatabaseConnection() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                System.err.println("Error: La conexion a la base de datos es null.");
                return false;
            }
            return !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Error al verificar conexion a la base de datos: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("Error inesperado al verificar conexion: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}