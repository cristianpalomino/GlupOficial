package pe.com.glup.dialog;

/**
 * Created by Glup on 1/10/15.
 */
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;

import pe.com.glup.bus.BusHolder;
import pe.com.glup.datasource.DSReserva;
import pe.com.glup.glup.Glup;

public class ConfirmationDeleteReserva extends DialogFragment {
    private Context context;
    private String codPrenda;
    private String tagFragment;

    public ConfirmationDeleteReserva(Context context,String codPrenda, String tagFragment){
        this.context=context;
        this.codPrenda=codPrenda;
        this.tagFragment=tagFragment;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        builder.setMessage("¿Confirma eliminar la reserva seleccionada?")
                .setTitle("Confirmacion")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("Dialogos", "Confirmacion Aceptada.");
                        DSReserva dsReserva= new DSReserva(context);
                        dsReserva.eliminarDeReserva(codPrenda);
                        //dsReserva.listarReserva();



                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("Dialogos", "Confirmacion Cancelada.");
                        dialog.cancel();
                    }
                });

        return builder.create();
    }

}