package pe.com.glup.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import pe.com.glup.R;
import pe.com.glup.datasource.DSUsuario;
import pe.com.glup.fragments.FCloset;
import pe.com.glup.interfaces.OnPassProfileElements;
import pe.com.glup.interfaces.OnSuccessUpdateUser;

/**
 * Created by Glup on 14/09/15.
 */
public class ConfirmationPassDialog extends DialogFragment implements View.OnClickListener{
    private  static  final String TAG = ConfirmationPassDialog.class.getSimpleName();
    private EditText inputPass;
    private FCloset context;
    private Button btnOk;
    private String nombre,apellido,cumpleanos,correo,telefono,indOp;

    public ConfirmationPassDialog(){}

    public ConfirmationPassDialog(String indOp,String nombre, String apellido, String cumpleanos, String correo, String telefono, FCloset context) {
        this.indOp=indOp;
        this.nombre = nombre;
        this.apellido = apellido;
        this.cumpleanos = cumpleanos;
        this.correo = correo;
        this.telefono = telefono;
        this.context=context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Log.e("nombre", nombre);
        return createDialog();
    }

    private AlertDialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= getActivity().getLayoutInflater();
        View v =  inflater.inflate(R.layout.fragment_confirmation_pass,null);
        builder.setView(v);
        inputPass = (EditText) v.findViewById(R.id.pass_confirmation);
        btnOk = (Button) v.findViewById(R.id.btn_confirmation);
        btnOk.setOnClickListener(this);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_confirmation:
                DSUsuario dsUsuario = new DSUsuario(getActivity());

                try{
                   dsUsuario.setOnSuccessUpdateUser((OnSuccessUpdateUser) context);
                   dsUsuario.updateUsuario(indOp, inputPass.getText().toString(), nombre, apellido, cumpleanos, correo, telefono);

                }catch (ClassCastException e){
                    Log.e(null,e.toString());
                }

                dismiss();
                break;
        }
    }


}
