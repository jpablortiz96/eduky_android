package com.juanpablo.eduky.Educador;

/**
 * Created by PERSONAL on 30/12/2017.
 */

public class Educador {

    String correoPro, imagenPro, nomPro, celPro;
    String asigPro;
    String contraPro, ocupaPro;
    String edadPro, apePro;
    String latitudPro, longitudPro;
    String descriPro;

    public Educador() {
    }

    public Educador(String apePro, String asigPro, String celPro, String contraPro, String correoPro, String descriPro ,String edadPro, String imagenPro,String latitudPro, String longitudPro, String nomPro, String ocupaPro) {
        this.apePro = apePro;
        this.asigPro = asigPro;
        this.celPro = celPro;
        this.contraPro = contraPro;
        this.correoPro = correoPro;
        this.descriPro = descriPro;
        this.edadPro = edadPro;
        this.imagenPro = imagenPro;
        this.latitudPro = latitudPro;
        this.longitudPro = longitudPro;
        this.nomPro = nomPro;
        this.ocupaPro = ocupaPro;
    }

    public String getNomPro() {
        return nomPro;
    }

    public void setNomPro(String nomPro) {
        this.nomPro = nomPro;
    }

    public String getApePro() {
        return apePro;
    }

    public void setApePro(String apePro) {
        this.apePro = apePro;
    }

    public String getEdadPro() {
        return edadPro;
    }

    public void setEdadPro(String edadPro) {
        this.edadPro = edadPro;
    }

    public String getCelPro() {
        return celPro;
    }

    public void setCelPro(String celPro) {
        this.celPro = celPro;
    }

    public String getImagenPro() {
        return imagenPro;
    }

    public void setImagenPro(String imagenPro) {
        this.imagenPro = imagenPro;
    }

    public String getOcupaPro() {
        return ocupaPro;
    }

    public void setOcupaPro(String ocupaPro) {
        this.ocupaPro = ocupaPro;
    }

    public String getAsigPro() {
        return asigPro;
    }

    public void setAsigPro(String asigPro) {
        this.asigPro = asigPro;
    }

    public String getCorreoPro() {
        return correoPro;
    }

    public void setCorreoPro(String correoPro) {
        this.correoPro = correoPro;
    }

    public String getContraPro() {
        return contraPro;
    }

    public void setContraPro(String contraPro) {
        this.contraPro = contraPro;
    }

    public String getLatitudPro() {
        return latitudPro;
    }

    public void setLatitudPro(String latitudPro) {
        this.latitudPro = latitudPro;
    }

    public String getLongitudPro() {
        return longitudPro;
    }

    public void setLongitudPro(String longitudPro) {
        this.longitudPro = longitudPro;
    }

    public String getDescriPro() {
        return descriPro;
    }

    public void setDescriPro(String descriPro) {
        this.descriPro = descriPro;
    }

    @Override
    public String toString() {
        return "Educador{" +
                "apePro='" + apePro + '\'' +
                ", asigPro='" + asigPro + '\'' +
                ", celPro='" + celPro + '\'' +
                ", contraPro='" + contraPro + '\'' +
                ", correoPro='" + correoPro + '\'' +
                ", descriPro='" + descriPro + '\'' +
                ", edadPro='" + edadPro + '\'' +
                ", imagenPro='" + imagenPro + '\'' +
                ", latitudPro='" + latitudPro + '\'' +
                ", longitudPro='" + longitudPro + '\'' +
                ", nomPro='" + nomPro + '\'' +
                ", ocupaPro='" + ocupaPro + '\'' +
                '}';
    }
}
