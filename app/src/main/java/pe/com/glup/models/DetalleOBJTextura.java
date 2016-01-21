package pe.com.glup.models;

/**
 * Created by Glup on 5/01/16.
 */
public class DetalleOBJTextura {
    private String cod_tienda;
    private String imagen;
    private String nom_obj;
    private String indUserReg;

    public String getCod_tienda() {
        return cod_tienda;
    }

    public void setCod_tienda(String cod_tienda) {
        this.cod_tienda = cod_tienda;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNom_obj() {
        return nom_obj;
    }

    public void setNom_obj(String nom_obj) {
        this.nom_obj = nom_obj;
    }

    public String getIndUserReg() {
        return indUserReg;
    }

    public void setIndUserReg(String indUserReg) {
        this.indUserReg = indUserReg;
    }
}
