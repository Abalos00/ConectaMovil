package com.example.conectamvil;

public class Usuario {
    private String nombre;
    private String imagenURL;

    // Constructor vacío (puede ser útil en algunas situaciones)
    public Usuario() {
    }

    // Constructor con parámetros para inicializar nombre e imagenURL
    public Usuario(String nombre, String imagenURL) {
        this.nombre = nombre;
        this.imagenURL = imagenURL;
    }

    // Getters y setters

    public String getImagenURL() {
        return imagenURL;
    }

    public void setImagenURL(String imagenURL) {
        this.imagenURL = imagenURL;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Puedes agregar otros métodos o funcionalidades según sea necesario
}
