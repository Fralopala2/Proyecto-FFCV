//package proyectoffcv;
//
//import proyectoffcv.logica.Federacion;
//import entidades.*;
//import java.sql.SQLException;
//import java.time.LocalDate;
//
//public class Main {
//    public static void main(String[] args) {
//        Federacion federacion = Federacion.getInstance();
//
//        try {
//            Persona persona = federacion.nuevaPersona("12345678A", "Juan", "Pérez", "Gómez", 
//            LocalDate.of(1990, 1, 1), "juanp", "pass123", "Madrid");
//            System.out.println("Persona creada: " + persona);
//
//            Club club = federacion.nuevoClub("Club Deportivo", LocalDate.now(), persona);
//            System.out.println("Club creado: " + club);
//
//            Categoria categoria = federacion.nuevaCategoria("Senior", 1, 50.0);
//            System.out.println("Categoría creada: " + categoria);
//
//            Grupo grupo = federacion.nuevoGrupo(categoria, "Grupo A");
//            System.out.println("Grupo creado: " + grupo);
//
//            Instalacion instalacion = federacion.nuevaInstalacion("Estadio Central", "Calle Depalo 666", "CESPED_NATURAL");
//            System.out.println("Instalación creada: " + instalacion);
//
//            Equipo equipo = federacion.nuevoEquipo("A", instalacion, grupo);
//            System.out.println("Equipo creado: " + equipo);
//
//            club.addEquipo(equipo);
//            System.out.println("Equipo añadido al club: " + club);
//
//            Licencia licencia = federacion.nuevaLicencia(persona, equipo);
//            System.out.println("Licencia creada y asignada: " + licencia);
//
//            Club clubBuscado = federacion.buscarClub("Club Deportivo");
//            System.out.println("Club buscado: " + clubBuscado);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}