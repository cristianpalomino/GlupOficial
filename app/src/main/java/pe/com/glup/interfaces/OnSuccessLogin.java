package pe.com.glup.interfaces;

import pe.com.glup.beans.Usuario;

/**
 * Created by Glup on 10/07/15.
 */
public interface OnSuccessLogin {

    void onSuccessLogin(boolean status, Usuario usuario, String message);
}
