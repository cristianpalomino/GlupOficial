package pe.com.glup.interfaces;

import java.util.ArrayList;

import pe.com.glup.beans.Prenda;

/**
 * Created by Glup on 24/06/15.
 */
public interface OnSuccessPrenda {
    void onSuccessPrenda(boolean status, int indProb);
    void onFailed(String error_msg);
}
