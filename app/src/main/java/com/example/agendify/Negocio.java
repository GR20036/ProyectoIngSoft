package com.example.agendify;

public class Negocio {
    private String nombre;
    private String descripcion;
    private String logoUrl;

    public Negocio(String nombre, String descripcion, String logoUrl) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.logoUrl = logoUrl;

    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;

    }



}
