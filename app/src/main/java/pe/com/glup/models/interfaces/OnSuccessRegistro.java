package pe.com.glup.models.interfaces;

import pe.com.glup.models.Usuario;

/**
 * Created by Glup on 10/07/15.
 */
public interface OnSuccessRegistro {

    void onSuccessRegistro(boolean status, Usuario usuario,String message);
}
