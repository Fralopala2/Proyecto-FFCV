package proyectoffcv;

import proyectoffcv.logica.Federacion;
import proyectoffcv.util.DatabaseConnection;
import entidades.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Federacion federacion = Federacion.getInstance();
        
        // Declarar objetos para pruebas
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

            // 1. Limpiar base de datos
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
            Persona existingEmpleado = federacion.buscaPersona("11223344H");
            if (existingEmpleado == null || !(existingEmpleado instanceof Empleado)) {
                empleado = federacion.nuevoEmpleado("11223344H", "Carlos", "Martinez", "Sanchez",
                        LocalDate.of(1980, 3, 20), "carlosm", "pass789", "Castellon", 
                        numeroEmpleado, LocalDate.now(), "123456789012");
                empleado.guardar();
                System.out.println("Empleado creado: " + empleado);
                
                empleado.setPuesto("Director Tecnico");
                empleado.actualizar();
                System.out.println("Empleado actualizado: " + federacion.buscaPersona("11223344H"));
            } else {
                System.out.println("Empleado con DNI 11223344H ya existe, omitiendo creacion.");
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
                equipo1 = federacion.nuevoEquipo("A", mestalla, grupoA);
                equipo1.setClub(club1); // Asigna club1
                equipo1.guardar();
                System.out.println("Equipo creado: " + equipo1);
            }
            
           if (ciutat != null && grupoB != null && club2 != null) {
                equipo2 = federacion.nuevoEquipo("B", ciutat, grupoB);
                equipo2.setClub(club2); // Asigna club2
                equipo2.guardar();
                System.out.println("Equipo creado: " + equipo2);
            }

            // 9. Prueba de Licencias
            System.out.println("\n--- Prueba de Licencias ---");
            if (persona1 != null && equipo1 != null) {
                licencia1 = federacion.nuevaLicencia(persona1, equipo1);
                federacion.addLicencia(licencia1, equipo1);
                System.out.println("Licencia con equipo creada: " + licencia1);
            }
            
            if (persona2 != null && equipo2 != null) {
                licencia2 = federacion.nuevaLicencia(persona2, equipo2);
                federacion.addLicencia(licencia2, equipo2);
                System.out.println("Licencia con equipo creada: " + licencia2);
            }
            
            if (persona1 != null) {
                List<Licencia> licencias = buscarLicenciasPorPersona(persona1.getDni());
                System.out.println("Licencias de " + persona1.getNombre() + ":");
                licencias.forEach(System.out::println);
            }
            
            if (persona1 != null && equipo1 != null) {
                Licencia licenciaDuplicada = federacion.nuevaLicencia(persona1, equipo1);
                federacion.addLicencia(licenciaDuplicada, equipo1);
                System.out.println("Error: Deberia haber fallado (licencia duplicada)");
            }

            // 10. Prueba de Busquedas
            System.out.println("\n--- Prueba de Busquedas ---");
            List<Persona> personas = federacion.buscaPersonas("Juan", "Perez", "Gomez");
            System.out.println("Personas encontradas (Juan Perez Gomez):");
            personas.forEach(System.out::println);
            List<Instalacion> instalaciones = federacion.buscarInstalaciones("Mestalla");
            System.out.println("\nInstalaciones encontradas (Mestalla):");
            instalaciones.forEach(System.out::println);

            // 11. Pruebas de Eliminacion
            System.out.println("\n--- Pruebas de Eliminacion ---");
            if (licencia1 != null) {
                licencia1.eliminar();
                System.out.println("Licencia eliminada para persona1: " + licencia1);
            }
            if (licencia2 != null) {
                licencia2.eliminar();
                System.out.println("Licencia eliminada para persona2: " + licencia2);
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
    
    private static List<Licencia> buscarLicenciasPorPersona(String dni) throws SQLException {
        List<Licencia> licencias = new ArrayList<>();
        String sql = "SELECT * FROM licencia WHERE persona_dni = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Persona persona = Persona.buscarPorDni(dni);
                Equipo equipo = rs.getInt("equipo_id") > 0 ? Equipo.buscarPorId(rs.getInt("equipo_id")) : null;
                Licencia licencia = new Licencia(
                    rs.getString("numeroLicencia"),
                    persona,
                    equipo,
                    rs.getBoolean("abonada")
                );
                licencias.add(licencia);
            }
        }
        return licencias;
    }
}