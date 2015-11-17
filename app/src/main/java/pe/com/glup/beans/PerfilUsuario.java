package pe.com.glup.beans;

import java.util.ArrayList;


/**
 * Created by Glup on 10/09/15.
 */
public class PerfilUsuario {
    private String tag;
    private int success;
    private int error;
    private ArrayList<DetalleUser> detalleuser;
    private ArrayList<DatoUser> datouser;

    public PerfilUsuario(String tag, int success, int error, ArrayList<DetalleUser> detalleuser, ArrayList<DatoUser> datouser) {
        this.tag = tag;
        this.success = success;
        this.error = error;
        this.detalleuser = detalleuser;
        this.datouser = datouser;
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

    public ArrayList<DetalleUser> getDetalleuser() {
        return detalleuser;
    }

    public ArrayList<DatoUser> getDatouser() {
        return datouser;
    }
}
