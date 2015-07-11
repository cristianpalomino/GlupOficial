package pe.com.glup.interfaces;

import pe.com.glup.beans.Prenda;
import pe.com.glup.beans.Usuario;

/**
 * Created by Glup on 10/07/15.
 */
public interface OnSuccessRegistro {

    void onSuccessRegistro(boolean status, Usuario usuario,String message);
}
