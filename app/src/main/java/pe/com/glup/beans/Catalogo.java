package pe.com.glup.beans;

import java.util.ArrayList;

/**
 * Created by Glup on 25/06/15.
 */
public class Catalogo {

    private String tag;
    private int success;
    private int error;
    private ArrayList<Prenda> prendas;

    public Catalogo(String tag, int success, int error, ArrayList<Prenda> prendas) {
        this.tag = tag;
        this.success = success;
        this.error = error;
        this.prendas = prendas;
    }

    public String getTag() {
        return tag;
    }

    public int getSuccess() {
        return success;
    }

    public int getError() {
        return error;
    }

    public ArrayList<Prenda> getPrendas() {
        return prendas;
    }

    @Override
    public String toString() {
        return "Catalogo{" +
                "tag='" + tag + '\'' +
                ", success=" + success +
                ", error=" + error +
                ", prendas=" + prendas +
                '}';
    }
}
