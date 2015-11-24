package pe.com.glup.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import pe.com.glup.R;
import pe.com.glup.managers.bus.BusHolder;
import pe.com.glup.network.DSReserva;

/**
 * Created by Glup on 2/10/15.
 */
public class SuccessReservaDialog extends DialogFragment {
    private TextView code;
    public SuccessReservaDialog(){
        BusHolder.getInstance().register(this);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){return createDialog();}

    private AlertDialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (getActivity().getLayoutInflater());
        View v=inflater.inflate(R.layout.dialog_show_confirm_reserva,null);
        builder.setView(v);
        code = (TextView) v.findViewById(R.id.code);
        return builder.create();
    }
    @Subscribe
    public void getcodeEmail(DSReserva.ResponseConfirmReserva responseConfirmReserva){
        Log.e(null,"code" + responseConfirmReserva.cod_confirmacion);
        code.setText(responseConfirmReserva.cod_confirmacion);
    }

}
