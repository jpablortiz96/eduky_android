package com.juanpablo.eduky.Estudiante;

/**
 * Created by PERSONAL on 08/12/2017.
 */

public class Usuario {

    private String eCorreo, eNombre;

    public Usuario() {
    }

    public Usuario(String  eNombre, String eCorreo) {
        this.eNombre = eNombre;
        this.eCorreo = eCorreo;
    }

    public String geteCorreo() {
        return eCorreo;
    }

    public void seteCorreo(String eCorreo) {
        this.eCorreo = eCorreo;
    }

    public String geteNombre() {
        return eNombre;
    }

    public void seteNombre(String eNombre) {
        this.eNombre = eNombre;
    }
}
