package pe.com.glup.beans;

import java.util.ArrayList;

/**
 * Created by Glup on 23/09/15.
 */
public class Tienda {
    private String local;
    private String indUserReg;
    private ArrayList<TallaDisponible> tallaDisponibles;

    public Tienda() {
    }

    public Tienda(String local, String indUserReg, ArrayList<TallaDisponible> tallaDisponibles) {
        this.local = local;
        this.indUserReg = indUserReg;
        this.tallaDisponibles=tallaDisponibles;
    }

    public String getLocal() {
        return local;
    }

    public String getIndUserReg() {
        return indUserReg;
    }

    public ArrayList<TallaDisponible> getTallaDisponibles() {return tallaDisponibles;}

    @Override
    public String toString() {
        return "Tienda{" +
                "local='" + local + '\'' +
                ", indUserReg='" + indUserReg + '\'' +
                '}';
    }

    public void setLocal(String local) {
        this.local = local;
    }
}
