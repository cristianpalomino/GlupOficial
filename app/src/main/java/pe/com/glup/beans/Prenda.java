package pe.com.glup.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Glup on 24/06/15.
 */
public class Prenda implements Serializable {

    private String indUserReg;
    private String cod_prenda;
    private String marca;
    private String tipo;
    private String modelo;
    private String estilo;
    private String imagen;
    private String indProbador;
    private String nombre;
    private String precio;
    private String numGusta;
    private ArrayList<String> talla;

    public Prenda() {
    }

    public Prenda(String indUserReg, String cod_prenda, String marca, String tipo, String modelo, String estilo, String imagen, String indProbador, String nombre, String precio, String numGusta, ArrayList<String> talla) {
        this.indUserReg = indUserReg;
        this.cod_prenda = cod_prenda;
        this.marca = marca;
        this.tipo = tipo;
        this.modelo = modelo;
        this.estilo = estilo;
        this.imagen = imagen;
        this.indProbador = indProbador;
        this.nombre = nombre;
        this.precio = precio;
        this.numGusta = numGusta;
        this.talla = talla;
    }

    public String getIndUserReg() {
        return indUserReg;
    }

    public String getCod_prenda() {
        return cod_prenda;
    }

    public String getMarca() {
        return marca;
    }

    public String getTipo() {
        return tipo;
    }

    public String getModelo() {
        return modelo;
    }

    public String getEstilo() {
        return estilo;
    }

    public String getImagen() {
        return imagen;
    }

    public String getIndProbador() {
        return indProbador;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public ArrayList<String> getTalla() {
        return talla;
    }

    public String getNumGusta() {
        return numGusta;
    }

    @Override
    public String toString() {
        return "Prenda{" +
                "indUserReg='" + indUserReg + '\'' +
                ", cod_prenda='" + cod_prenda + '\'' +
                ", marca='" + marca + '\'' +
                ", tipo='" + tipo + '\'' +
                ", modelo='" + modelo + '\'' +
                ", estilo='" + estilo + '\'' +
                ", imagen='" + imagen + '\'' +
                ", indProbador='" + indProbador + '\'' +
                ", nombre='" + nombre + '\'' +
                ", precio='" + precio + '\'' +
                ", numGusta='" + numGusta + '\'' +
                ", talla=" + talla +
                '}';
    }
}
