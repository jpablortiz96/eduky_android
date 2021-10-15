package com.juanpablo.eduky.Audiolibro;

/**
 * Created by PERSONAL on 20/07/2018.
 */

public class AudioLibro {

    String nombreAL, urlAL;

    public AudioLibro() {
    }

    public AudioLibro(String nombreAL, String urlAL) {
        this.nombreAL = nombreAL;
        this.urlAL = urlAL;
    }

    public String getNombreAL() {return nombreAL;}

    public void setNombreAL(String nombreAL) {this.nombreAL = nombreAL;}

    public String getUrlAL() {return urlAL;}

    public void setUrlAL(String urlAL) {this.urlAL = urlAL;}

    @Override
    public String toString() {
        return "AudioLibro{" +
                "nombreAL='" + nombreAL + '\'' +
                ", urlAL='" + urlAL + '\'' +
                '}';
    }

}
