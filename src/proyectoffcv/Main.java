package proyectoffcv;

import proyectoffcv.logica.Federacion;
import proyectoffcv.util.DatabaseConnection;
import entidades.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Federacion federacion = Federacion.getInstance();
        
        // Se declaran al principio para las pruebas
        Persona persona1 = null;
        Persona persona2 = null;
        Empleado empleado = null;
        Club club1 = null;
        Club club2 = null;
        Categoria senior = null;
        Categoria juvenil = null;
        Grupo grupoA = null;
        Grupo grupoB = null;
        Instalacion mestalla = null;
        Instalacion ciutat = null;
        Equipo equipo1 = null;
        Equipo equipo2 = null;
        Licencia licencia1 = null;
        Licencia licencia2 = null;
        
        System.out.println("=== INICIO DE PRUEBAS ===");
        
        try {
            // 0. Verificar conexion a la base de datos solo una vez
            System.out.println("Verificando conexion a la base de datos...");
            if (!checkDatabaseConnection()) {
                throw new RuntimeException("No se pudo conectar a la base de datos.");
            }

            // 1. Limpiar toda la base de datos antes de empezar
            System.out.println("Limpiando base de datos antes de las pruebas...");
            federacion.limpiarTablas();

            // 2. Prueba de Personas
            System.out.println("\n--- Prueba de Personas ---");
            persona1 = federacion.nuevaPersona("12345678A", "Juan", "Perez", "Gomez", 
                    LocalDate.of(1990, 1, 1), "juanp", "pass123", "Valencia");
            persona1.guardar();
            System.out.println("Persona creada: " + persona1);
            
            persona2 = federacion.nuevaPersona("87654321B", "Maria", "Lopez", null, 
                    LocalDate.of(1985, 5, 15), "marial", "pass456", "Alicante");
            persona2.guardar();
            System.out.println("Persona creada: " + persona2);
            
            Persona personaEncontrada = federacion.buscaPersona("12345678A");
            System.out.println("Persona encontrada por DNI: " + personaEncontrada);
            
            persona1.setNombre("Juan Carlos");
            persona1.actualizar();
            System.out.println("Persona actualizada: " + federacion.buscaPersona("12345678A"));
            
            try {
                federacion.nuevaPersona("12345678A", "Ana", "Garcia", null, 
                        LocalDate.of(1995, 2, 2), "anag", "pass789", "Valencia").guardar();
                System.out.println("Error: Deberia haber fallado (DNI duplicado)");
            } catch (SQLException e) {
                System.out.println("Prueba de DNI duplicado correcta: " + e.getMessage());
            }

            // 3. Prueba de Empleados
            System.out.println("\n--- Prueba de Empleados ---");
            int numeroEmpleado = 1001;
            if (federacion.buscaEmpleadoPorNumero(numeroEmpleado) == null) {
                empleado = federacion.nuevoEmpleado("11223344H", "Carlos", "Martinez", "Sanchez",
                        LocalDate.of(1980, 3, 20), "carlosm", "pass789", "Castellon", 
                        numeroEmpleado, LocalDate.now(), "123456789012");
                empleado.setPuesto("Entrenador");
                empleado.guardar();
                System.out.println("Empleado creado: " + empleado);
                
                empleado.setPuesto("Director Tecnico");
                empleado.actualizar();
                System.out.println("Empleado actualizado: " + federacion.buscaPersona("11223344H"));
            } else {
                System.out.println("Empleado con numero " + numeroEmpleado + " ya existe, omitiendo creacion.");
            }
            
            try {
                federacion.nuevoEmpleado("22334455I", "Luis", "Gomez", null,
                        LocalDate.of(1982, 4, 10), "luisg", "pass101", "Valencia", 
                        -1, LocalDate.now(), "987654321098");
                System.out.println("Error: Deberia haber fallado (numero de empleado invalido)");
            } catch (IllegalArgumentException e) {
                System.out.println("Prueba de numero de empleado invalido correcta: " + e.getMessage());
            }

            // 4. Prueba de Clubes
            System.out.println("\n--- Prueba de Clubes ---");
            if (persona1 != null) {
                club1 = federacion.nuevoClub("Valencia CF Test", LocalDate.of(1919, 3, 18), persona1);
                club1.guardar();
                System.out.println("Club creado: " + club1);
            }
            
            if (persona2 != null) {
                club2 = federacion.nuevoClub("Villarreal CF", LocalDate.of(1923, 3, 10), persona2);
                club2.guardar();
                System.out.println("Club creado: " + club2);
            }
            
            Club clubEncontrado = federacion.buscarClub("Valencia CF Test");
            System.out.println("Club encontrado: " + clubEncontrado);
            
            if (club1 != null) {
                club1.setFechaFundacion(LocalDate.of(1919, 3, 19));
                club1.actualizar();
                System.out.println("Club actualizado: " + federacion.buscarClub("Valencia CF Test"));
            }
            
            try {
                if (persona1 != null) {
                    federacion.nuevoClub(null, LocalDate.of(2000, 1, 1), persona1);
                }
                System.out.println("Error: Deberia haber fallado (nombre nulo)");
            } catch (IllegalArgumentException e) {
                System.out.println("Prueba de nombre de club nulo correcta: " + e.getMessage());
            }

            // 5. Prueba de Categorias
            System.out.println("\n--- Prueba de Categorias ---");
            senior = federacion.nuevaCategoria("Senior", 1, 100.0);
            senior.guardar();
            System.out.println("Categoria creada: " + senior);
            
            juvenil = federacion.nuevaCategoria("Juvenil", 2, 75.0);
            juvenil.guardar();
            System.out.println("Categoria creada: " + juvenil);
            
            List<Categoria> categorias = federacion.obtenerCategorias();
            System.out.println("\nTodas las categorias:");
            categorias.forEach(System.out::println);
            
            try {
                federacion.nuevaCategoria("Infantil", 3, -50.0);
                System.out.println("Error: Deberia haber fallado (precio negativo)");
            } catch (IllegalArgumentException e) {
                System.out.println("Prueba de precio de licencia negativo correcta: " + e.getMessage());
            }

            // 6. Prueba de Grupos
            System.out.println("\n--- Prueba de Grupos ---");
            if (senior != null) {
                grupoA = federacion.nuevoGrupo(senior, "Grupo A");
                grupoA.guardar();
                System.out.println("Grupo creado: " + grupoA);
                
                grupoB = federacion.nuevoGrupo(senior, "Grupo B");
                grupoB.guardar();
                System.out.println("Grupo creado: " + grupoB);
                
                List<Grupo> gruposSenior = federacion.obtenerGrupos(senior);
                System.out.println("\nGrupos en categoria Senior:");
                gruposSenior.forEach(System.out::println);
                
                if (grupoA != null) {
                    grupoA.setNombre("Grupo A Modificado");
                    grupoA.actualizar();
                    final int grupoAId = grupoA.getId();
                    System.out.println("Grupo actualizado: " + federacion.obtenerGrupos(senior).stream()
                            .filter(g -> g.getId() == grupoAId).findFirst().orElse(null));
                }
            }
            
            try {
                federacion.nuevoGrupo(null, "Grupo C");
                System.out.println("Error: Deberia haber fallado (categoria nula)");
            } catch (IllegalArgumentException e) {
                System.out.println("Prueba de categoria nula correcta: " + e.getMessage());
            }

            // 7. Prueba de Instalaciones
            System.out.println("\n--- Prueba de Instalaciones ---");
            mestalla = federacion.nuevaInstalacion("Mestalla", "Av. de Suecia, Valencia", 
                    "CESPED_NATURAL");
            mestalla.guardar();
            System.out.println("Instalacion creada: " + mestalla);
            
            ciutat = federacion.nuevaInstalacion("Ciutat de Valencia", "C/ de Sant Vicent, Valencia", 
                    "CESPED_ARTIFICIAL");
            ciutat.guardar();
            System.out.println("Instalacion creada: " + ciutat);
            
            if (mestalla != null) {
                mestalla.setDireccion("Av. de Suecia 123, Valencia");
                mestalla.actualizar();
                System.out.println("Instalacion actualizada: " + federacion.buscarInstalaciones("Mestalla").get(0));
            }
            
            try {
                federacion.nuevaInstalacion("Estadio Nuevo", "Calle Falsa, Valencia", "INVALIDO");
                System.out.println("Error: Deberia haber fallado (superficie invalida)");
            } catch (IllegalArgumentException e) {
                System.out.println("Prueba de superficie invalida correcta: " + e.getMessage());
            }

            // 8. Prueba de Equipos
            System.out.println("\n--- Prueba de Equipos ---");
            if (mestalla != null && grupoA != null && club1 != null) {
                equipo1 = federacion.nuevoEquipo("A", mestalla, grupoA, club1);
                equipo1.guardar();
                System.out.println("Equipo creado: " + equipo1);
            }
            
            if (ciutat != null && grupoB != null && club2 != null) {
                equipo2 = federacion.nuevoEquipo("B", ciutat, grupoB, club2); // Corregido a grupoB
                equipo2.guardar();
                System.out.println("Equipo creado: " + equipo2);
            }

            // 9. Prueba de Anadir Jugador
            System.out.println("\n--- Prueba de Anadir Jugador ---");
            if (equipo1 != null && persona1 != null) {
                EquipoJugador equipoJugador1 = new EquipoJugador(equipo1, persona1, LocalDateTime.now());
                equipoJugador1.guardar();
                System.out.println("Jugador anadido al equipo A: " + persona1);
                
                List<Persona> jugadores = federacion.buscarJugadoresEnEquipo(equipo1);
                Persona jugadorEncontrado = jugadores.stream()
                        .filter(p -> p.getDni().equals("12345678A"))
                        .findFirst()
                        .orElse(null);
                System.out.println("Jugador encontrado en equipo A: " + jugadorEncontrado);
            }
            
            try {
                Equipo equipoInvalido = Equipo.buscarPorLetra("Z");
                if (equipoInvalido == null) {
                    throw new IllegalArgumentException("Equipo inexistente");
                }
                if (persona1 != null) {
                    new EquipoJugador(equipoInvalido, persona1, LocalDateTime.now()).guardar();
                }
                System.out.println("Error: Deberia haber fallado (equipo inexistente)");
            } catch (SQLException | IllegalArgumentException e) {
                System.out.println("Prueba de equipo inexistente correcta: " + e.getMessage());
            }
            
            try {
                if (equipo1 != null && persona1 != null) {
                    EquipoJugador equipoJugadorDuplicado = new EquipoJugador(equipo1, persona1, LocalDateTime.now());
                    equipoJugadorDuplicado.guardar();
                }
                System.out.println("Error: Deberia haber fallado (jugador duplicado)");
            } catch (SQLException e) {
                System.out.println("Prueba de jugador duplicado correcta: " + e.getMessage());
            }

            // 10. Prueba de Licencias
            System.out.println("\n--- Prueba de Licencias ---");
            if (persona1 != null && equipo1 != null) {
                licencia1 = federacion.nuevaLicencia(persona1, equipo1, LocalDate.now(), LocalDate.now().plusYears(1), false);
                licencia1.guardar();
                System.out.println("Licencia con equipo creada: " + licencia1);
            }
            
            if (persona2 != null && equipo2 != null) {
                licencia2 = federacion.nuevaLicencia(persona2, equipo2, LocalDate.now(), LocalDate.now().plusYears(1), false);
                licencia2.guardar();
                System.out.println("Licencia con equipo creada: " + licencia2);
            }
            
            if (licencia1 != null) {
                licencia1.setAbonada(true);
                licencia1.actualizar();
                System.out.println("Licencia actualizada: " + licencia1);
            }
            
            if (persona1 != null) {
                List<Licencia> licencias = federacion.obtenerLicencias(persona1);
                System.out.println("Licencias de " + persona1.getNombre() + ":");
                licencias.forEach(System.out::println);
            }
            
            try {
                if (persona1 != null && equipo1 != null) {
                    federacion.nuevaLicencia(persona1, equipo1, LocalDate.now(), LocalDate.now().plusYears(1), false).guardar();
                }
                System.out.println("Error: Deberia haber fallado (licencia duplicada)");
            } catch (SQLException e) {
                System.out.println("Prueba de licencia duplicada correcta: " + e.getMessage());
            }

            // 11. Prueba de Busquedas
            System.out.println("\n--- Prueba de Busquedas ---");
            List<Persona> personas = federacion.buscaPersonas("Juan", "Perez", "Gomez");
            System.out.println("Personas encontradas (Juan Perez Gomez):");
            personas.forEach(System.out::println);
            List<Instalacion> instalaciones = federacion.buscarInstalaciones("Mestalla");
            System.out.println("\nInstalaciones encontradas (Mestalla):");
            instalaciones.forEach(System.out::println);

            // 12. Pruebas de Eliminacion
            System.out.println("\n--- Pruebas de Eliminacion ---");
            if (persona1 != null) {
                List<Licencia> licenciasPersona1 = federacion.obtenerLicencias(persona1);
                for (Licencia licencia : licenciasPersona1) {
                    licencia.eliminar();
                    System.out.println("Licencia eliminada para persona1: " + licencia);
                }
            }
            if (persona2 != null) {
                List<Licencia> licenciasPersona2 = federacion.obtenerLicencias(persona2);
                for (Licencia licencia : licenciasPersona2) {
                    licencia.eliminar();
                    System.out.println("Licencia eliminada para persona2: " + licencia);
                }
            }
            
            if (equipo1 != null) equipo1.eliminar();
            if (equipo2 != null) equipo2.eliminar();
            if (grupoB != null) grupoB.eliminar();
            if (grupoA != null) grupoA.eliminar();
            if (juvenil != null) juvenil.eliminar();
            if (senior != null) senior.eliminar();
            if (ciutat != null) ciutat.eliminar();
            if (mestalla != null) mestalla.eliminar();
            if (club2 != null) club2.eliminar();
            if (club1 != null) club1.eliminar();
            if (empleado != null) empleado.eliminar();
            if (persona2 != null) persona2.eliminar();
            if (persona1 != null) persona1.eliminar();

            System.out.println("Todos los objetos eliminados exitosamente.");

        } catch (SQLException e) {
            System.err.println("Error general de base de datos: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("Error de validacion: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("Limpiando base de datos despues de las pruebas...");
            try {
                federacion.limpiarTablas();
            } catch (SQLException e) {
                System.err.println("Error al limpiar la base de datos: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        System.out.println("\n=== FIN DE PRUEBAS ===");
    }
    
    private static boolean checkDatabaseConnection() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                System.err.println("Error: La conexion a la base de datos es null.");
                return false;
            }
            System.out.println("Conexion a la base de datos establecida.");
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