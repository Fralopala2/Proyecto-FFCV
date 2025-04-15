/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoffcv.logica;

import entidades.*;
import java.time.LocalDate;
import java.util.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jmontanerm
 */
public final class Federacion implements IFederacion {
        private static Federacion instancia;
        private List<Categoria> categorias;
        private List<Empleado> empleados;
        private List<Persona> afiliados;
        private List<Club> clubes;
        private List<Instalacion> instalaciones;
        private Federacion() {
            this.categorias = new ArrayList<>();
            this.empleados = new ArrayList<>();
            this.afiliados = new ArrayList<>();
            this.clubes = new ArrayList<>();
        }

        public static Federacion getInstance() {
            if(instancia == null)
                instancia = new Federacion();
            return instancia;
        }

        @Override
        public Categoria nuevaCategoria(String nombre, int orden, double precioLicencia) {
            try {
                Categoria c = new Categoria(nombre, orden, precioLicencia);
                this.categorias.add(c);
                return c;
            } catch (SQLException ex) {
                throw new InputMismatchException(ex.getMessage());
            }
        }

        @Override
        public Club buscarClub(String nombre) {
            for(Club c : clubes) {
                if(c.getNombre().equals(nombre))
                    return c;
            }
            return null;
        }
}