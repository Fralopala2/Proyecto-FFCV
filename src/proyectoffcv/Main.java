package proyectoffcv;

import proyectoffcv.logica.Federacion;
import entidades.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Federacion federacion = Federacion.getInstance();
        
        System.out.println("=== INICIO DE PRUEBAS ===");
        
        try {
            
            // 0. Limpiar toda la base de datos antes de empezar
            System.out.println("Preparando base de datos para pruebas...");
            federacion.limpiarTablas();
            
            // 1. Prueba de Personas
            System.out.println("\n--- Prueba de Personas ---");
            Persona persona1 = federacion.nuevaPersona("12345678A", "Juan", "Pérez", "Gómez", 
                    LocalDate.of(1990, 1, 1), "juanp", "pass123", "Valencia");
            System.out.println("Persona creada: " + persona1);
            
            Persona persona2 = federacion.nuevaPersona("87654321B", "María", "López", null, 
                    LocalDate.of(1985, 5, 15), "marial", "pass456", "Alicante");
            System.out.println("Persona creada: " + persona2);
            
            // Buscar persona
            Persona personaEncontrada = federacion.buscaPersona("12345678A");
            System.out.println("Persona encontrada por DNI: " + personaEncontrada);
            
            // 2. Prueba de Empleados
            System.out.println("\n--- Prueba de Empleados ---");
            Empleado empleado = federacion.nuevoEmpleado("11223344H", "Carlos", "Martínez", "Sánchez",
                    LocalDate.of(1980, 3, 20), "carlosm", "pass789", "Castellón", 
                    1001, LocalDate.now(), "123456789012");
            empleado.setPuesto("Entrenador");
            System.out.println("Empleado creado: " + empleado);
            
            // 3. Prueba de Clubes
            System.out.println("\n--- Prueba de Clubes ---");
            Club club1 = federacion.nuevoClub("Valencia CF", LocalDate.of(1919, 3, 18), persona1);
            System.out.println("Club creado: " + club1);
            
            Club club2 = federacion.nuevoClub("Villarreal CF", LocalDate.of(1923, 3, 10), persona2);
            System.out.println("Club creado: " + club2);
            
            // Buscar club
            Club clubEncontrado = federacion.buscarClub("Valencia CF");
            System.out.println("Club encontrado: " + clubEncontrado);
            
            // 4. Prueba de Categorías
            System.out.println("\n--- Prueba de Categorías ---");
            Categoria senior = federacion.nuevaCategoria("Senior", 1, 100.0);
            System.out.println("Categoría creada: " + senior);
            
            Categoria juvenil = federacion.nuevaCategoria("Juvenil", 2, 75.0);
            System.out.println("Categoría creada: " + juvenil);
            
            // 5. Prueba de Grupos
            System.out.println("\n--- Prueba de Grupos ---");
            Grupo grupoA = federacion.nuevoGrupo(senior, "Grupo A");
            System.out.println("Grupo creado: " + grupoA);
            
            Grupo grupoB = federacion.nuevoGrupo(senior, "Grupo B");
            System.out.println("Grupo creado: " + grupoB);
            
            // Listar grupos por categoría
            List<Grupo> gruposSenior = federacion.obtenerGrupos(senior);
            System.out.println("\nGrupos en categoría Senior:");
            gruposSenior.forEach(System.out::println);
            
            // 6. Prueba de Instalaciones
            System.out.println("\n--- Prueba de Instalaciones ---");
            Instalacion mestalla = federacion.nuevaInstalacion("Mestalla", "Av. de Suecia, Valencia", 
                    "CESPED_NATURAL");
            System.out.println("Instalación creada: " + mestalla);
            
            Instalacion ciutat = federacion.nuevaInstalacion("Ciutat de València", "C/ de Sant Vicent, Valencia", 
                    "CESPED_ARTIFICIAL");
            System.out.println("Instalación creada: " + ciutat);
            
            // 7. Prueba de Equipos
            System.out.println("\n--- Prueba de Equipos ---");
            Equipo equipo1 = federacion.nuevoEquipo("A", mestalla, grupoA, club1);
            System.out.println("Equipo creado: " + equipo1);
            
            Equipo equipo2 = federacion.nuevoEquipo("B", ciutat, grupoB, club1);
            System.out.println("Equipo creado: " + equipo2);
            
            // Buscar jugador en equipo (debería fallar porque no hay jugadores aún)
            System.out.println("\nBuscando jugador en equipo (debería ser null):");
            Persona jugador = equipo1.buscarJugador("12345678A");
            System.out.println("Jugador encontrado: " + jugador);
            
            // 8. Prueba de Licencias
            System.out.println("\n--- Prueba de Licencias ---");
            Licencia licencia1 = federacion.nuevaLicencia(persona1, equipo1);
            System.out.println("Licencia con equipo creada: " + licencia1);
            
            Licencia licencia2 = federacion.nuevaLicencia(persona2);
            System.out.println("Licencia simple creada: " + licencia2);
            
            // Calcular precio de licencia
            double precioLicencia = federacion.calcularPrecioLicencia(equipo1);
            System.out.println("Precio de licencia para equipo1: " + precioLicencia);
            
            // 9. Pruebas de búsquedas
            System.out.println("\n--- Pruebas de Búsquedas ---");
            List<Persona> personas = federacion.buscaPersonas("Juan", "Pérez", "Gómez");
            System.out.println("Personas encontradas (Juan Pérez Gómez):");
            personas.forEach(System.out::println);
            
            List<Instalacion> instalaciones = federacion.buscarInstalaciones("Mestalla");
            System.out.println("\nInstalaciones encontradas (Mestalla):");
            instalaciones.forEach(System.out::println);
            
           // 10. Pruebas de eliminación
            System.out.println("\n--- Pruebas de Eliminación ---");
            try {
                equipo2.eliminar();
                System.out.println("Equipo2 eliminado");

                grupoB.eliminar();
                System.out.println("GrupoB eliminado");

                ciutat.eliminar();
                System.out.println("Instalación Ciutat eliminada");
            } catch (SQLException e) {
                System.err.println("Error al eliminar: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.err.println("Error de base de datos:");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Error de validación:");
            e.printStackTrace();
        }
        
        System.out.println("\n=== FIN DE PRUEBAS ===");
    }
}