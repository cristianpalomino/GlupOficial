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
import android.util.Log;

import pe.com.glup.R;
import pe.com.glup.network.DSReserva;

public class ConfirmationDelete extends DialogFragment {
    private Context context;
    private String codPrendaOTicket;
    private String tagFragment;

    public ConfirmationDelete(Context context, String codPrendaOTicket, String tagFragment){
        this.context=context;
        this.codPrendaOTicket=codPrendaOTicket;
        this.tagFragment=tagFragment;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity(), R.style.DeleteReservaDialogTheme);

        String titulo;
        if (tagFragment.equals("FReservaTicket")){
            titulo="¿Confirma eliminar el ticket seleccionado?";
        }else{
            titulo="¿Confirma eliminar la reserva seleccionada?";
        }

        builder.setMessage(titulo)
                .setTitle("Confirmación")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("Dialogos", "Confirmacion Aceptada.");
                        DSReserva dsReserva= new DSReserva(context);
                        if (tagFragment.equals("FReservaTicket")){
                            dsReserva.eliminarTicket(codPrendaOTicket);
                        }else{
                            dsReserva.eliminarDeReserva(codPrendaOTicket);
                        }

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