package model;

import java.io.Serializable;

public class Materia implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;
    private int id;
    private String nombre;
    private String carrera;
    private String anio;
    private String grupo;

    // Constructor para NUEVA materia
    public Materia(String nombre, String carrera, String anio, String grupo) {
        this.id = nextId++;
        this.nombre = nombre;
        this.carrera = carrera;
        this.anio = anio;
        this.grupo = grupo;
    }

    // Constructor para EDITAR materia (con ID)
    public Materia(int id, String nombre, String carrera, String anio, String grupo) {
        this.id = id;
        this.nombre = nombre;
        this.carrera = carrera;
        this.anio = anio;
        this.grupo = grupo;
        if (id >= nextId) nextId = id + 1;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCarrera() { return carrera; }
    public String getAnio() { return anio; }
    public String getGrupo() { return grupo; }

    public String getDisplayName() {
        return carrera + " - " + anio + " Año Gpo " + grupo + " - " + nombre;
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    public static void setNextId(int value) { nextId = value; }
}