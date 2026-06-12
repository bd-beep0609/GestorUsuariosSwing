package model;

import java.io.Serializable;

/**
 * Clase que representa un Estudiante en el Sistema de Notas ESFE
 *
 * @author Bayron
 * @version 4.0 - Contexto ESFE: Carrera Software con niveles 1°, 2° y 4° Articulado
 */
public class Estudiante implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;

    private int id;
    private String carnet;
    private String nombre;
    private String apellido;
    private String carrera;   // "Software"
    private String nivel;      // "1°", "2°", "4° Articulado"
    private String grupo;      // "1", "2", "3", "4", "5"
    private boolean activo;

    /**
     * Constructor para NUEVO estudiante (sin ID, se genera automáticamente)
     */
    public Estudiante(String carnet, String nombre, String apellido,
                      String carrera, String nivel, String grupo, boolean activo) {
        this.id = nextId++;
        this.carnet = carnet;
        this.nombre = nombre;
        this.apellido = apellido;
        this.carrera = carrera;
        this.nivel = nivel;
        this.grupo = grupo;
        this.activo = activo;
    }

    /**
     * Constructor para EDITAR estudiante (con ID existente)
     */
    public Estudiante(int id, String carnet, String nombre, String apellido,
                      String carrera, String nivel, String grupo, boolean activo) {
        this.id = id;
        this.carnet = carnet;
        this.nombre = nombre;
        this.apellido = apellido;
        this.carrera = carrera;
        this.nivel = nivel;
        this.grupo = grupo;
        this.activo = activo;
        if (id >= nextId) nextId = id + 1;
    }

    // ==================== GETTERS Y SETTERS ====================

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCarnet() { return carnet; }
    public void setCarnet(String carnet) { this.carnet = carnet; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getNombreCompleto() { return nombre + " " + apellido; }

    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public String getGrupo() { return grupo; }
    public void setGrupo(String grupo) { this.grupo = grupo; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getActivoText() { return activo ? "Activo" : "Inactivo"; }

    /**
     * Devuelve el curso completo para mostrar en la tabla
     * Ejemplos:
     * - "Software - 1° Año Gpo 2"
     * - "Software - 2° Año Gpo 1"
     * - "Software - 4° Articulado Gpo 3"
     */
    public String getCursoDisplay() {
        if (carrera == null) return "Carrera no definida";
        if (nivel == null || grupo == null) return carrera + " - Datos incompletos";

        if (nivel.equals("4° Articulado")) {
            return carrera + " - " + nivel + " Gpo " + grupo;
        } else {
            return carrera + " - " + nivel + " Año Gpo " + grupo;
        }
    }

    public static void setNextId(int value) { nextId = value; }
    public static int getNextId() { return nextId; }

    @Override
    public String toString() {
        return carnet + " - " + getNombreCompleto() + " (" + getCursoDisplay() + ")";
    }
}