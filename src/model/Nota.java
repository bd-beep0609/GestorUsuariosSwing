package model;

import java.io.Serializable;
import java.util.Date;

public class Nota implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;
    private int id;
    private int estudianteId;
    private int materiaId;
    private double valor;  // Nota de 0 a 10
    private Date fecha;
    private String observacion;

    public Nota(int estudianteId, int materiaId, double valor, String observacion) {
        this.id = nextId++;
        this.estudianteId = estudianteId;
        this.materiaId = materiaId;
        this.valor = valor;
        this.fecha = new Date();
        this.observacion = observacion;
    }

    public Nota(int id, int estudianteId, int materiaId, double valor, Date fecha, String observacion) {
        this.id = id;
        this.estudianteId = estudianteId;
        this.materiaId = materiaId;
        this.valor = valor;
        this.fecha = fecha;
        this.observacion = observacion;
        if (id >= nextId) nextId = id + 1;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEstudianteId() { return estudianteId; }
    public void setEstudianteId(int estudianteId) { this.estudianteId = estudianteId; }

    public int getMateriaId() { return materiaId; }
    public void setMateriaId(int materiaId) { this.materiaId = materiaId; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public static void setNextId(int value) { nextId = value; }
}