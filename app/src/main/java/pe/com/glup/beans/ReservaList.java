package pe.com.glup.beans;

import java.util.ArrayList;

/**
 * Created by Glup on 30/09/15.
 */
public class ReservaList {
    private String local;
    private ArrayList<Prenda> datos;

    public ReservaList() {
    }

    public ReservaList(String local, ArrayList<Prenda> datos) {
        this.local = local;
        this.datos = datos;
    }

    public String getLocal() {
        return local;
    }

    public ArrayList<Prenda> getDatos() {
        return datos;
    }
}
