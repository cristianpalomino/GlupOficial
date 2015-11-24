package pe.com.glup.models.interfaces;

import pe.com.glup.models.Prenda;

/**
 * Created by Glup on 24/06/15.
 */
public interface OnSuccessInfo {
    void onSuccess(Prenda prenda);
    void onFailed(String error_msg);
}
