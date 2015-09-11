package pe.com.glup.beans;

/**
 * Created by Glup on 10/09/15.
 */
public class DatoUser {
    private String nomUser;
    private String apeUser;
    private String rutaFoto;
    private String numPrend;

    public DatoUser() {
    }

    public DatoUser(String nomUser, String apeUser, String rutaFoto, String numPrend) {
        this.nomUser = nomUser;
        this.apeUser = apeUser;
        this.rutaFoto = rutaFoto;
        this.numPrend = numPrend;
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
}