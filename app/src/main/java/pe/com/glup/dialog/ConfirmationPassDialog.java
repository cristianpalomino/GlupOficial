package pe.com.glup.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import pe.com.glup.R;
import pe.com.glup.datasource.DSUsuario;
import pe.com.glup.fragments.FCloset;
import pe.com.glup.fragments.FClosetProfile;
import pe.com.glup.interfaces.OnPassProfileElements;
import pe.com.glup.interfaces.OnSuccessUpdateUser;

/**
 * Created by Glup on 14/09/15.
 */
public class ConfirmationPassDialog extends DialogFragment implements View.OnClickListener{
    private  static  final String TAG = ConfirmationPassDialog.class.getSimpleName();
    private EditText inputPass;
    private FClosetProfile context;
    private Button btnOk;
    private String nombre,apellido,cumpleanos,correo,telefono,indOp;
    private boolean range=false;

    public ConfirmationPassDialog(){}

    public ConfirmationPassDialog(String indOp,String nombre, String apellido, String cumpleanos, String correo, String telefono, FClosetProfile context) {
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
        inputPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int size=s.toString().length();
                if (size>=6 && size<=12){
                    range=true;
                }else{
                    range=false;
                }
                if (range){
                    btnOk.setEnabled(true);
                }
            }
        });
        btnOk = (Button) v.findViewById(R.id.btn_confirmation);
        //btnOk.setEnabled(false);
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
                    super.dismiss();
                }catch (ClassCastException e){
                    Log.e(null,e.toString());
                }
               super.dismiss();
                //dismiss();
                break;
        }
    }


}
