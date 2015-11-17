package pe.com.glup.interfaces;

import java.util.ArrayList;

import pe.com.glup.beans.DatoUser;
import pe.com.glup.beans.DetalleUser;
import pe.com.glup.beans.Usuario;

/**
 * Created by Glup on 10/09/15.
 */
public interface OnSuccessDetalleUsuario {
        void onSuccess(String success_msg,ArrayList<DetalleUser> detalleuser, ArrayList<DatoUser> datouser);
        void onFailed(String error_msg);
}
