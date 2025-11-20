package com.devst.apoyovirtual;

public class Emocion {
    private String id; // Firebase usa IDs de texto
    private String nombre;
    private String colorHex;
    private String descripcion;

    // Constructor vacío (OBLIGATORIO para Firebase)
    public Emocion() {}

    // Constructor normal
    public Emocion(String nombre, String colorHex, String descripcion) {
        this.nombre = nombre;
        this.colorHex = colorHex;
        this.descripcion = descripcion;
    }

    // Getters y Setters (OBLIGATORIOS)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getColorHex() { return colorHex; }
    public void setColorHex(String colorHex) { this.colorHex = colorHex; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
