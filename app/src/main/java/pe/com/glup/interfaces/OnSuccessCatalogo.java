package pe.com.glup.interfaces;

import java.util.ArrayList;

import pe.com.glup.beans.Prenda;

/**
 * Created by Glup on 24/06/15.
 */
public interface OnSuccessCatalogo {
    void onSuccess(String success_msg,ArrayList<Prenda> prendas);
    void onFailed(String error_msg);
}
