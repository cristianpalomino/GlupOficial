package pe.com.glup.models;

/**
 * Created by Glup on 28/09/15.
 */
public class ReservaItem {
    private Tienda tienda;
    private Prenda prenda;

    public ReservaItem() {
    }

    public ReservaItem(Tienda tienda, Prenda prenda) {
        this.tienda = tienda;
        this.prenda = prenda;
    }

    public Tienda getTienda() {
        return tienda;
    }

    public Prenda getPrenda() {
        return prenda;
    }

    public void setTienda(Tienda tienda) {
        this.tienda = tienda;
    }

    public void setPrenda(Prenda prenda) {
        this.prenda = prenda;
    }
}
