package com.juanpablo.eduky.Libro;

/**
 * Created by PERSONAL on 31/03/2018.
 */
public class Book {

    String nombreLibro;
    String imagenLibro;
    String urlLibro;

    public Book() {
    }

    public Book(String imagenLibro, String nombreLibro, String urlLibro) {
        this.imagenLibro = imagenLibro;
        this.nombreLibro = nombreLibro;
        this.urlLibro = urlLibro;
    }

    public String getNombreLibro() {
        return nombreLibro;
    }

    public void setNombreLibro(String nombreLibro) {
        this.nombreLibro = nombreLibro;
    }

    public String getImagenLibro() {
        return imagenLibro;
    }

    public void setImagenLibro(String imagenLibro) {
        this.imagenLibro = imagenLibro;
    }

    public String getUrlLibro() {
        return urlLibro;
    }

    public void setUrlLibro(String urlLibro) {
        this.urlLibro = urlLibro;
    }

    @Override
    public String toString() {
        return "Book{" +
                "imagenLibro='" + imagenLibro + '\'' +
                ", nombreLibro='" + nombreLibro + '\'' +
                ", urlLibro='" + urlLibro + '\'' +
                '}';
    }
}