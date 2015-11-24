package pe.com.glup.models.interfaces;

/**
 * Created by Glup on 24/06/15.
 */
public interface OnSuccessPrenda {
    void onSuccessPrenda(boolean status, int indProb);
    void onFailed(String error_msg);
}
