package pe.com.glup.interfaces;

import java.util.ArrayList;

import pe.com.glup.beans.Prenda;
import pe.com.glup.beans.Usuario;

/**
 * Created by Glup on 24/06/15.
 */
public interface OnSuccessInfo {
    void onSuccess(Prenda prenda);
    void onFailed(String error_msg);
}
