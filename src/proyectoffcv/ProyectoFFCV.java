/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package proyectoffcv;

import entidades.Categoria;
import entidades.Club;
import entidades.Empleado;
import entidades.Grupo;
import entidades.Equipo;
import entidades.Instalacion;
import entidades.Persona;
import java.time.LocalDate;
import proyectoffcv.logica.*;

// /**
//  *
//  * @author jmontanerm
//  */

public class ProyectoFFCV {
    public static void main(String[] args) {
        IFederacion federacion = Federacion.getInstance();

        // Crear una nueva categoria
        Categoria categoria = federacion.nuevaCategoria("Senior", 1, 100.0);
        System.out.println("Categoria creada: " + categoria);

        // Crear un nuevo presidente
        Persona presidente = new Persona("12345678A", "Paco", "Lopez", "Martinez", LocalDate.of(1979, 3, 1), "pacolm", "pass","Xirivella");

        // Crear un nuevo club
        Club club = federacion.nuevoClub("FC Valencia", LocalDate.of(1919, 3, 18), presidente);
        System.out.println("Club creado: " + club);

        // Crear un nuevo empleado
        Empleado empleado = federacion.nuevoEmpleado("87654321Z", "Tobias", "Panchi", "Palmero", LocalDate.of(1985, 5, 15), "tobipan", "pass1234", "Valencia", 1, LocalDate.now(), "123456789");
        System.out.println("Empleado creado: " + empleado);

        // Crear una nueva instalacion
        Instalacion instalacion = federacion.nuevaInstalacion("Estadio Mestalla", "Calle de Luis Casanova, 46010 Valencia", "Campo de futbol");
        System.out.println("Instalacion creada: " + instalacion);

        // Crear un nuevo grupo
        Grupo grupo = federacion.nuevoGrupo(categoria, "Grupo A");
        System.out.println("Grupo creado: " + grupo);

        // Crear un nuevo equipo
        Equipo equipo = federacion.nuevoEquipo("Valencia CF", instalacion, grupo);
        System.out.println("Equipo creado: " + equipo);
    }
}