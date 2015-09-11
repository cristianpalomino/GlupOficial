package pe.com.glup.beans;

/**
 * Created by Glup on 10/09/15.
 */
public class DetalleUser {
    private String nomUser;
    private String apeUser;
    private String fecNac;
    private String correoUser;
    private String numTelef;

    public DetalleUser() {
    }

    public DetalleUser(String nomUser, String apeUser, String fecNac, String correoUser, String numTelef) {
        this.nomUser = nomUser;
        this.apeUser = apeUser;
        this.fecNac = fecNac;
        this.correoUser = correoUser;
        this.numTelef = numTelef;
    }
}