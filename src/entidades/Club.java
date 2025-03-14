/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;
import java.time.*;
/**
 *
 * @author oscpincer
 */
public class Club{
    private String nombre;
    private LocalDate fechaFund;
    
    public Club(String nombre, LocalDate fechaFund, String letra){
        this.nombre = nombre;
        this.fechaFund = fechaFund;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaFund() {
        return fechaFund;
    }

    public void setFechaFund(LocalDate fechaFund) {
        this.fechaFund = fechaFund;
    }
    
    
}
