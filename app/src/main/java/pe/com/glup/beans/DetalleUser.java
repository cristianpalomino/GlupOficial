package pe.com.glup.beans;

import java.io.Serializable;

/**
 * Created by Glup on 10/09/15.
 */
public class DetalleUser implements Serializable{
    private String indUserReg;
    private String nomUser;
    private String apeUser;
    private String fecNac;
    private String correoUser;
    private String numTelef;

    public DetalleUser() {
    }

    public DetalleUser(String indUserReg, String nomUser, String apeUser, String numTelef, String fecNac, String correoUser) {
        this.indUserReg = indUserReg;
        this.nomUser = nomUser;
        this.apeUser = apeUser;
        this.numTelef = numTelef;
        this.fecNac = fecNac;
        this.correoUser = correoUser;
    }

    public String getIndUserReg() {
        return indUserReg;
    }

    public String getNomUser() {
        return nomUser;
    }

    public String getFecNac() {
        return fecNac;
    }

    public String getApeUser() {
        return apeUser;
    }

    public String getCorreoUser() {
        return correoUser;
    }

    public String getNumTelef() {
        return numTelef;
    }

    @Override
    public String toString() {
        return "DetalleUser{" +
                "indUserReg='" + indUserReg + '\'' +
                ", nomUser='" + nomUser + '\'' +
                ", apeUser='" + apeUser + '\'' +
                ", fecNac='" + fecNac + '\'' +
                ", correoUser='" + correoUser + '\'' +
                ", numTelef='" + numTelef + '\'' +
                '}';
    }
}