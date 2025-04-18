/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package entidades;
import proyectoffcv.util.DatabaseConnection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.*;
/**
 *
 * @author david
 */
public class Persona {
   
 private String DNI, nombre, apellido1, apellido2, usuario, password, poblacion;
 private LocalDate fechaNacimiento;
 private static ArrayList<Persona> personas;
        
  public Persona(String DNI, String nombre, String apellido1, String apellido2, String usuario, String password, String poblacion, LocalDate fechanacimiento){
  this.DNI = DNI;
  this.apellido1 = apellido1;
  this.apellido2 = apellido2;
  this.fechaNacimiento = fechanacimiento;
  this.nombre = nombre;
  this.password = password;
  this.poblacion = poblacion;
  this.usuario = usuario;
  this.personas = new ArrayList<>();
  }      

    public String getDNI() {
        return DNI;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getPassword() {
        return password;
    }

    public String getPoblacion() {
        return poblacion;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setDNI(String DNI) {
        
    if(DNI == null || DNI.trim().isEmpty()){
        System.out.println("El campo esta vacío o es nulo");
    }    
      this.DNI = DNI;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public static ArrayList<Persona> getPersonas() {
        return personas;
    }

    public static void setPersonas(ArrayList<Persona> personas) {
        Persona.personas = personas;
    }
    
    
    
    public static Persona nuevaPersona(String dni, String nombre, String apellido1, String apellido2, LocalDate fechaNacimiento, String usuario, String password, String poblacion) throws SQLException {
            if (buscaPersona(dni) == null) {
                Persona persona = new Persona(dni, nombre, apellido1, apellido2, usuario, password, poblacion, fechaNacimiento);
                persona.Persistencia(); // Guardar en la base de datos
                personas.add(persona); // Agregar a la lista estática
                return persona;
            }
            return null;
        }

    
    
        public static Persona buscaPersona(String dni) throws SQLException {
            String sql = "SELECT * FROM persona WHERE dni = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, dni);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return new Persona(
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getString("apellido1"),
                        rs.getString("apellido2"),
                        rs.getString("usuario"),
                        rs.getString("password"),
                        rs.getString("poblacion"),
                        rs.getDate("fechaNacimiento").toLocalDate()
                    );
                }
                return null;
            }
        }
    
        public static ArrayList<Persona> buscaPersonas(String nombre, String apellido1, String apellido2) throws SQLException {
            ArrayList<Persona> busqueda = new ArrayList<>();
            String sql = "SELECT * FROM persona WHERE nombre LIKE ? AND apellido1 LIKE ? AND apellido2 LIKE ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, "%" + nombre + "%");
                ps.setString(2, "%" + apellido1 + "%");
                ps.setString(3, "%" + apellido2 + "%");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    busqueda.add(new Persona(
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getString("apellido1"),
                        rs.getString("apellido2"),
                        rs.getString("usuario"),
                        rs.getString("password"),
                        rs.getString("poblacion"),
                        rs.getDate("fechaNacimiento").toLocalDate()
                    ));
                }
            }
            return busqueda;
        }
        
    
     
     public void Persistencia() throws SQLException {
        String consulta = "INSERT INTO persona (dni, nombre, apellido1, apellido2, fechaNacimiento, usuario, password, poblacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement valor = conn.prepareStatement(consulta)) {
            valor.setString(1, DNI);
            valor.setString(2, nombre);
            valor.setString(3, apellido1);
            valor.setString(4, apellido2);
            valor.setDate(5, java.sql.Date.valueOf(fechaNacimiento));
            valor.setString(6, usuario);
            valor.setString(7, password);
            valor.setString(8, poblacion);

            valor.executeUpdate();
        }
    }

   @Override
    public String toString() {
        return "Nombre: "+ nombre + "\n Primer Apellido: " + apellido1 + "\n Segundo Apellido: " + apellido2 + "\n DNI: " + DNI + "\n Fecha de nacimiento: "+ fechaNacimiento +"\n Poblacion: "+ poblacion +"\n Usuario: "+ usuario +"\n Contraseña: "+ password;
    }   
        
   
   
   
    
    
}
