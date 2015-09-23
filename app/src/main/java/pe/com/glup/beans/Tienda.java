package pe.com.glup.beans;

/**
 * Created by Glup on 23/09/15.
 */
public class Tienda {
    private String local;
    private String indUserReg;

    public Tienda(String local, String indUserReg) {
        this.local = local;
        this.indUserReg = indUserReg;
    }

    public String getLocal() {
        return local;
    }

    public String getIndUserReg() {
        return indUserReg;
    }

    @Override
    public String toString() {
        return "Tienda{" +
                "local='" + local + '\'' +
                ", indUserReg='" + indUserReg + '\'' +
                '}';
    }
}
