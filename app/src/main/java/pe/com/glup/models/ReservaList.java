package pe.com.glup.models;

import java.util.ArrayList;

/**
 * Created by Glup on 30/09/15.
 */
public class ReservaList {
    private String marca;
    private String local;
    private ArrayList<Prenda> datos;

    public ReservaList() {
    }

    public ReservaList(String local, ArrayList<Prenda> datos) {
        this.local = local;
        this.datos = datos;
    }

    public ReservaList(String marca, String local, ArrayList<Prenda> datos) {
        this.marca = marca;
        this.local = local;
        this.datos = datos;
    }

    public String getLocal() {
        return local;
    }

    public ArrayList<Prenda> getDatos() {
        return datos;
    }

    public String getMarca() {
        return marca;
    }
}
