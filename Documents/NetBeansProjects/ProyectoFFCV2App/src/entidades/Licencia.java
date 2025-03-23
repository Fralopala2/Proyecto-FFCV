/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

/**
 *
 * @author NOX
 */

public class Licencia {
    private Persona persona;
    private String numeroLicencia;
    private boolean abonada;

    public Licencia(Persona persona, String numeroLicencia) {
        this.persona = persona;
        this.numeroLicencia = numeroLicencia;
        this.abonada = false;
    }

    // Metodos
    public Persona getPersona() {
        return persona;
    }

    public String getNumeroLicencia() {
        return numeroLicencia;
    }

    public boolean isAbonada() {
        return abonada;
    }

    public void setAbonada(boolean abonada) {
        this.abonada = abonada;
    }
    
    @Override
    public String toString() {
        return "Licencia{numero='" + numeroLicencia + "', persona=" + persona.getDni() + ", abonada=" + abonada + '}';
    }
}