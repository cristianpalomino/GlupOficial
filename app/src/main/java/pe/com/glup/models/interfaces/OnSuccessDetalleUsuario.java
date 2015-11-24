package pe.com.glup.models.interfaces;

import java.util.ArrayList;

import pe.com.glup.models.DatoUser;
import pe.com.glup.models.DetalleUser;

/**
 * Created by Glup on 10/09/15.
 */
public interface OnSuccessDetalleUsuario {
        void onSuccess(String success_msg,ArrayList<DetalleUser> detalleuser, ArrayList<DatoUser> datouser);
        void onFailed(String error_msg);
}
