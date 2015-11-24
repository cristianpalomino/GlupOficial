package pe.com.glup.models;

/**
 * Created by Glup on 24/09/15.
 */
public class TallaDisponible {
    private String id_talla;
    private String talla;

    public TallaDisponible(String id_talla, String talla) {
        this.id_talla = id_talla;
        this.talla = talla;
    }

    public String getId_talla() {
        return id_talla;
    }

    public String getTalla() {
        return talla;
    }

    @Override
    public String toString() {
        return "TallaDisponible{" +
                "id_talla='" + id_talla + '\'' +
                ", talla='" + talla + '\'' +
                '}';
    }
}
