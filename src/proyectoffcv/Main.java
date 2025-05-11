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
        
        // Declare all variables at method scope
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
            // 0. Verificar conexion a la base de datos
            System.out.println("Verificando conexion a la base de datos...");
            if (!checkDatabaseConnection()) {
                throw new RuntimeException("No se pudo conectar a la base de datos.");
            }
            
            // 1. Limpiar toda la base de datos antes de empezar
            System.out.println("Limpiando base de datos antes de las pruebas...");
            federacion.limpiarTablas();
            
            // 2. Prueba de Personas
            try {
                System.out.println("\n--- Prueba de Personas ---");
                persona1 = federacion.nuevaPersona("12345678A", "Juan", "Perez", "Gomez", 
                        LocalDate.of(1990, 1, 1), "juanp", "pass123", "Valencia");
                persona1.guardar();
                System.out.println("Persona creada: " + persona1);
                
                persona2 = federacion.nuevaPersona("87654321B", "Maria", "Lopez", null, 
                        LocalDate.of(1985, 5, 15), "marial", "pass456", "Alicante");
                persona2.guardar();
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
                    federacion.nuevaPersona("12345678A", "Ana", "Garcia", null, 
                            LocalDate.of(1995, 2, 2), "anag", "pass789", "Valencia").guardar();
                    System.out.println("Error: Deberia haber fallado (DNI duplicado)");
                } catch (SQLException e) {
                    System.out.println("Prueba de DNI duplicado correcta: " + e.getMessage());
                }
            } catch (SQLException e) {
                System.err.println("Error en Prueba de Personas: " + e.getMessage());
                e.printStackTrace();
            }
            
            // 3. Prueba de Empleados
            try {
                System.out.println("\n--- Prueba de Empleados ---");
                int numeroEmpleado = 1001;
                // Check if numeroEmpleado already exists
                if (federacion.buscaEmpleadoPorNumero(numeroEmpleado) == null) {
                    empleado = federacion.nuevoEmpleado("11223344H", "Carlos", "Martinez", "Sanchez",
                            LocalDate.of(1980, 3, 20), "carlosm", "pass789", "Castellon", 
                            numeroEmpleado, LocalDate.now(), "123456789012");
                    empleado.setPuesto("Entrenador");
                    empleado.guardar();
                    System.out.println("Empleado creado: " + empleado);
                    
                    // Actualizar empleado
                    empleado.setPuesto("Director Tecnico");
                    empleado.actualizar();
                    System.out.println("Empleado actualizado: " + federacion.buscaPersona("11223344H"));
                } else {
                    System.out.println("Empleado con numero " + numeroEmpleado + " ya existe, omitiendo creacion.");
                }
                
                // Prueba de error: numero de empleado invalido
                try {
                    federacion.nuevoEmpleado("22334455I", "Luis", "Gomez", null,
                            LocalDate.of(1982, 4, 10), "luisg", "pass101", "Valencia", 
                            -1, LocalDate.now(), "987654321098");
                    System.out.println("Error: Deberia haber fallado (numero de empleado invalido)");
                } catch (IllegalArgumentException e) {
                    System.out.println("Prueba de numero de empleado invalido correcta: " + e.getMessage());
                }
            } catch (SQLException e) {
                System.err.println("Error en Prueba de Empleados: " + e.getMessage());
                e.printStackTrace();
            }
            
            // 4. Prueba de Clubes
            try {
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
                
                // Buscar club
                Club clubEncontrado = federacion.buscarClub("Valencia CF Test");
                System.out.println("Club encontrado: " + clubEncontrado);
                
                // Actualizar club
                if (club1 != null) {
                    club1.setFechaFundacion(LocalDate.of(1919, 3, 19));
                    club1.actualizar();
                    System.out.println("Club actualizado: " + federacion.buscarClub("Valencia CF Test"));
                }
                
                // Prueba de error: nombre de club nulo
                try {
                    if (persona1 != null) {
                        federacion.nuevoClub(null, LocalDate.of(2000, 1, 1), persona1);
                    }
                    System.out.println("Error: Deberia haber fallado (nombre nulo)");
                } catch (IllegalArgumentException e) {
                    System.out.println("Prueba de nombre de club nulo correcta: " + e.getMessage());
                }
            } catch (SQLException e) {
                System.err.println("Error en Prueba de Clubes: " + e.getMessage());
                e.printStackTrace();
            }
            
            // 5. Prueba de Categorias
            try {
                System.out.println("\n--- Prueba de Categorias ---");
                senior = federacion.nuevaCategoria("Senior", 1, 100.0);
                senior.guardar();
                System.out.println("Categoria creada: " + senior);
                
                juvenil = federacion.nuevaCategoria("Juvenil", 2, 75.0);
                juvenil.guardar();
                System.out.println("Categoria creada: " + juvenil);
                
                // Listar todas las categorias
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
            } catch (SQLException e) {
                System.err.println("Error en Prueba de Categorias: " + e.getMessage());
                e.printStackTrace();
            }
            
            // 6. Prueba de Grupos
            try {
                System.out.println("\n--- Prueba de Grupos ---");
                if (senior != null) {
                    grupoA = federacion.nuevoGrupo(senior, "Grupo A");
                    grupoA.guardar();
                    System.out.println("Grupo creado: " + grupoA);
                    
                    grupoB = federacion.nuevoGrupo(senior, "Grupo B");
                    grupoB.guardar();
                    System.out.println("Grupo creado: " + grupoB);
                    
                    // Listar grupos por categoria
                    List<Grupo> gruposSenior = federacion.obtenerGrupos(senior);
                    System.out.println("\nGrupos en categoria Senior:");
                    gruposSenior.forEach(System.out::println);
                    
                    // Actualizar grupo
                    if (grupoA != null) {
                        grupoA.setNombre("Grupo A Modificado");
                        grupoA.actualizar();
                        final int grupoAId = grupoA.getId();
                        System.out.println("Grupo actualizado: " + federacion.obtenerGrupos(senior).stream()
                                .filter(g -> g.getId() == grupoAId).findFirst().orElse(null));
                    }
                }
                
                // Prueba de error: categoria nula
                try {
                    federacion.nuevoGrupo(null, "Grupo C");
                    System.out.println("Error: Deberia haber fallado (categoria nula)");
                } catch (IllegalArgumentException e) {
                    System.out.println("Prueba de categoria nula correcta: " + e.getMessage());
                }
            } catch (SQLException e) {
                System.err.println("Error en Prueba de Grupos: " + e.getMessage());
                e.printStackTrace();
            }
            
            // 7. Prueba de Instalaciones
            try {
                System.out.println("\n--- Prueba de Instalaciones ---");
                mestalla = federacion.nuevaInstalacion("Mestalla", "Av. de Suecia, Valencia", 
                        "CESPED_NATURAL");
                mestalla.guardar();
                System.out.println("Instalacion creada: " + mestalla);
                
                ciutat = federacion.nuevaInstalacion("Ciutat de Valencia", "C/ de Sant Vicent, Valencia", 
                        "CESPED_ARTIFICIAL");
                ciutat.guardar();
                System.out.println("Instalacion creada: " + ciutat);
                
                // Actualizar instalacion
                if (mestalla != null) {
                    mestalla.setDireccion("Av. de Suecia 123, Valencia");
                    mestalla.actualizar();
                    System.out.println("Instalacion actualizada: " + federacion.buscarInstalaciones("Mestalla").get(0));
                }
                
                // Prueba de error: superficie invalida
                try {
                    federacion.nuevaInstalacion("Estadio Nuevo", "Calle Falsa, Valencia", "INVALIDO");
                    System.out.println("Error: Deberia haber fallado (superficie invalida)");
                } catch (IllegalArgumentException e) {
                    System.out.println("Prueba de superficie invalida correcta: " + e.getMessage());
                }
            } catch (SQLException e) {
                System.err.println("Error en Prueba de Instalaciones: " + e.getMessage());
                e.printStackTrace();
            }
            
            // 8. Prueba de Equipos
            try {
                System.out.println("\n--- Prueba de Equipos ---");
                if (mestalla != null && grupoA != null && club1 != null) {
                    equipo1 = federacion.nuevoEquipo("A", mestalla, grupoA, club1);
                    equipo1.guardar();
                    System.out.println("Equipo creado: " + equipo1);
                }
                
                if (ciutat != null && grupoB != null && club2 != null) {
                    equipo2 = federacion.nuevoEquipo("B", ciutat, grupoB, club2);
                    equipo2.guardar();
                    System.out.println("Equipo creado: " + equipo2);
                }
            } catch (SQLException e) {
                System.err.println("Error en Prueba de Equipos: " + e.getMessage());
                e.printStackTrace();
            }
            
            // 9. Prueba de Anadir Jugador
            try {
                System.out.println("\n--- Prueba de Anadir Jugador ---");
                if (equipo1 != null && persona1 != null) {
                    EquipoJugador equipoJugador1 = new EquipoJugador(equipo1, persona1, LocalDateTime.now());
                    equipoJugador1.guardar();
                    System.out.println("Jugador anadido al equipo A: " + persona1);
                    
                    // Verificar que el jugador esta en el equipo
                    List<Persona> jugadores = federacion.buscarJugadoresEnEquipo(equipo1);
                    Persona jugadorEncontrado = jugadores.stream()
                            .filter(p -> p.getDni().equals("12345678A"))
                            .findFirst()
                            .orElse(null);
                    System.out.println("Jugador encontrado en equipo A: " + jugadorEncontrado);
                }
                
                // Prueba de error: anadir jugador a equipo inexistente
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
                
                // Prueba de error: anadir jugador duplicado
                try {
                    if (equipo1 != null && persona1 != null) {
                        EquipoJugador equipoJugadorDuplicado = new EquipoJugador(equipo1, persona1, LocalDateTime.now());
                        equipoJugadorDuplicado.guardar();
                    }
                    System.out.println("Error: Deberia haber fallado (jugador duplicado)");
                } catch (SQLException e) {
                    System.out.println("Prueba de jugador duplicado correcta: " + e.getMessage());
                }
            } catch (SQLException e) {
                System.err.println("Error en Prueba de Anadir Jugador: " + e.getMessage());
                e.printStackTrace();
            }
            
            // 10. Prueba de Licencias
            try {
                System.out.println("\n--- Prueba de Licencias ---");
                if (persona1 != null && equipo1 != null) {
                    licencia1 = federacion.nuevaLicencia(persona1, equipo1, false);
                    licencia1.guardar();
                    System.out.println("Licencia con equipo creada: " + licencia1);
                }
                
                if (persona2 != null && equipo2 != null) {
                    licencia2 = federacion.nuevaLicencia(persona2, equipo2, false);
                    licencia2.guardar();
                    System.out.println("Licencia con equipo creada: " + licencia2);
                }
                
                // Actualizar licencia
                if (licencia1 != null) {
                    licencia1.setAbonada(true);
                    licencia1.actualizar();
                    System.out.println("Licencia actualizada: " + licencia1);
                }
                
                // Listar licencias
                if (persona1 != null) {
                    List<Licencia> licencias = federacion.obtenerLicencias(persona1);
                    System.out.println("Licencias de " + persona1.getNombre() + ":");
                    licencias.forEach(System.out::println);
                }
                
                // Prueba de error: licencia duplicada para persona
                try {
                    if (persona1 != null && equipo1 != null) {
                        federacion.nuevaLicencia(persona1, equipo1, false).guardar();
                    }
                    System.out.println("Error: Deberia haber fallado (licencia duplicada)");
                } catch (SQLException e) {
                    System.out.println("Prueba de licencia duplicada correcta: " + e.getMessage());
                }
            } catch (SQLException e) {
                System.err.println("Error en Prueba de Licencias: " + e.getMessage());
                e.printStackTrace();
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
            try {
                System.out.println("\n--- Pruebas de Eliminacion ---");
                // Delete all licenses for persona1 and persona2
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
                
                if (equipo1 != null) {
                    equipo1.eliminar();
                    System.out.println("Equipo1 eliminado");
                }
                
                if (equipo2 != null) {
                    equipo2.eliminar();
                    System.out.println("Equipo2 eliminado");
                }
                
                if (grupoB != null) {
                    grupoB.eliminar();
                    System.out.println("GrupoB eliminado");
                }
                
                if (grupoA != null) {
                    grupoA.eliminar();
                    System.out.println("GrupoA eliminado");
                }
                
                if (juvenil != null) {
                    juvenil.eliminar();
                    System.out.println("Categoria Juvenil eliminada");
                }
                
                if (senior != null) {
                    senior.eliminar();
                    System.out.println("Categoria Senior eliminada");
                }
                
                if (ciutat != null) {
                    ciutat.eliminar();
                    System.out.println("Instalacion Ciutat eliminada");
                }
                
                if (mestalla != null) {
                    mestalla.eliminar();
                    System.out.println("Instalacion Mestalla eliminada");
                }
                
                if (club2 != null) {
                    club2.eliminar();
                    System.out.println("Club2 eliminado");
                }
                
                if (club1 != null) {
                    club1.eliminar();
                    System.out.println("Club1 eliminado");
                }
                
                if (empleado != null) {
                    empleado.eliminar();
                    System.out.println("Empleado eliminado");
                } else {
                    System.out.println("Empleado no creado, omitiendo eliminacion");
                }
                
                if (persona2 != null) {
                    persona2.eliminar();
                    System.out.println("Persona2 eliminada");
                }
                
                if (persona1 != null) {
                    persona1.eliminar();
                    System.out.println("Persona1 eliminada");
                }
            } catch (SQLException e) {
                System.err.println("Error en Pruebas de Eliminacion: " + e.getMessage());
                e.printStackTrace();
            }
            
        } catch (SQLException e) {
            System.err.println("Error general de base de datos: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("Error de validacion: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Ensure tables are cleaned even if tests fail
            try {
                System.out.println("Limpiando base de datos despues de las pruebas...");
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
            System.out.println("Conexión a la base de datos establecida.");
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