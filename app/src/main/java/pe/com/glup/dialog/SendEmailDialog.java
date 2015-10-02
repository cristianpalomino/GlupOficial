package pe.com.glup.dialog;

/**
 * Created by Glup on 1/10/15.
 */
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import pe.com.glup.R;
import pe.com.glup.bus.BusHolder;
import pe.com.glup.datasource.DSReserva;

public class SendEmailDialog extends  DialogFragment implements View.OnClickListener{
    private TextView validator;
    private EditText inputCode;
    private Button confirmReserva;
    private String codeFromEmail="";
    private Context context;

    public SendEmailDialog(Context context) {
        this.context=context;
        BusHolder.getInstance().register(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        return  createDialog();
    }

    private AlertDialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (getActivity().getLayoutInflater());
        View v= inflater.inflate(R.layout.dialog_send_email,null);
        builder.setView(v);
        validator = (TextView) v.findViewById(R.id.validator_send_code);
        inputCode = (EditText) v.findViewById(R.id.code_confirm_reserva);
        confirmReserva = (Button) v.findViewById(R.id.btn_for_confirm_reserva);
        confirmReserva.setOnClickListener(this);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.btn_for_confirm_reserva:
                Log.e(null,"click confirm");
                if (codeFromEmail.equals(inputCode.getText().toString())){
                    validator.setVisibility(View.INVISIBLE);
                    Log.e(null,"entro confirm reserva");
                    DSReserva dsReserva = new DSReserva(context);
                    dsReserva.confirmReseva();
                    new SuccessReservaDialog().show(getActivity().getSupportFragmentManager(),
                            SuccessReservaDialog.class.getSimpleName());
                    dismiss();
                }else{
                    Log.e(null,"code confirm reserva diferente");
                    validator.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
    @Subscribe
    public void getcodeEmail(DSReserva.ResponseSendEmail responseSendEmail){
        Log.e(null,"indEmail "+responseSendEmail.indEnvio+" code "+responseSendEmail.cod_confirmacion);
        codeFromEmail = responseSendEmail.cod_confirmacion;
    }
}
