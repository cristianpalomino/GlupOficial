package pe.com.glup.models;

/**
 * Created by Glup on 10/07/15.
 */
public class Usuario {

    private String tag;
    private String sexoUser;
    private String indRegistro;
    private String indCorreo;
    private String codUser;
    private int error;
    private int success;

    public Usuario() {
    }

    public Usuario(String tag, String sexoUser,String indRegistro, String indCorreo, String codUser, int error, int success) {
        this.tag = tag;
        this.sexoUser=sexoUser;
        this.indRegistro = indRegistro;
        this.indCorreo = indCorreo;
        this.codUser = codUser;
        this.error = error;
        this.success = success;
    }

    public String getTag() {
        return tag;
    }

    public String getSexoUser() {return sexoUser;}

    public String getIndRegistro() {
        return indRegistro;
    }

    public String getIndCorreo() {
        return indCorreo;
    }

    public String getCodUser() {
        return codUser;
    }

    public int getError() {
        return error;
    }

    public int getSuccess() {
        return success;
    }
}
