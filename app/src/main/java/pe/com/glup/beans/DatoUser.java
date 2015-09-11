package pe.com.glup.beans;

import java.io.Serializable;

/**
 * Created by Glup on 10/09/15.
 */
public class DatoUser implements Serializable{
    private String indUserReg;
    private String nomUser;
    private String apeUser;
    private String rutaFoto;
    private String numPrend;

    public DatoUser() {
    }

    public DatoUser(String indUserReg, String nomUser, String apeUser, String rutaFoto, String numPrend) {
        this.indUserReg = indUserReg;
        this.nomUser = nomUser;
        this.apeUser = apeUser;
        this.rutaFoto = rutaFoto;
        this.numPrend = numPrend;
    }

    public String getIndUserReg() {
        return indUserReg;
    }

    public String getNomUser() {
        return nomUser;
    }

    public String getApeUser() {
        return apeUser;
    }

    public String getRutaFoto() {
        return rutaFoto;
    }

    public String getNumPrend() {
        return numPrend;
    }

    @Override
    public String toString() {
        return "DatoUser{" +
                "indUserReg='" + indUserReg + '\'' +
                ", nomUser='" + nomUser + '\'' +
                ", apeUser='" + apeUser + '\'' +
                ", rutaFoto='" + rutaFoto + '\'' +
                ", numPrend='" + numPrend + '\'' +
                '}';
    }
}