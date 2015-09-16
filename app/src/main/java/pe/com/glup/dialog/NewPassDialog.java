package pe.com.glup.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import pe.com.glup.R;
import pe.com.glup.datasource.DSUsuario;
import pe.com.glup.fragments.FCloset;
import pe.com.glup.interfaces.OnSuccessUpdatePass;
import pe.com.glup.interfaces.OnSuccessUpdateUser;

/**
 * Created by Glup on 14/09/15.
 */
public class NewPassDialog extends DialogFragment implements View.OnClickListener{
    private static final String TAG = NewPassDialog.class.getSimpleName();
    private FCloset context;
    private EditText passw,newPass,repeatNewPass;
    private TextView validator;
    private Button confirmation_new_pass;
    private boolean activarBoton;
    public NewPassDialog(FCloset context) {
        this.context=context;

    }
    private  boolean verificarRepeticion;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstance){
        return createDialog();
    }

    private AlertDialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_confirmation_new_pass,null);
        builder.setView(view);
        activarBoton=false;
        passw= (EditText) view.findViewById(R.id.pass_current);
        newPass =(EditText) view.findViewById(R.id.new_pass);
        repeatNewPass=(EditText)view.findViewById(R.id.new_pass_confirmation);
        validator = (TextView) view.findViewById(R.id.validator_rep_pass);
        confirmation_new_pass = (Button) view.findViewById(R.id.btn_change_confirmation);
        confirmation_new_pass.setTextColor(getResources().getColor(R.color.rojo_glup));
        confirmation_new_pass.setOnClickListener(this);
        repeatNewPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e("ontextChange", s.toString() + " " + String.valueOf(start) + " " + String.valueOf(after) + " " + String.valueOf(count));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("ontextChange", s.toString() + " " + String.valueOf(start) + " " + String.valueOf(before) + " " + String.valueOf(count));

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("afterTextChange", s.toString());
                if (s.toString().equals(newPass.getText().toString())) {
                    validator.setText("Contraseñas coinciden");
                    validator.setTextColor(Color.GREEN);
                    validator.setVisibility(View.VISIBLE);
                    verificarRepeticion = true;
                    repeatNewPass.setTextColor(Color.GREEN);
                    //if (!passw.getText().toString().equals(""))
                    confirmation_new_pass.setBackgroundResource(R.drawable.button_selector);
                    confirmation_new_pass.setText("OK");
                    confirmation_new_pass.setTextColor(getResources().getColor(R.color.celeste_glup));
                    confirmation_new_pass.setEnabled(true);

                    Log.e("contras", "son iguales");
                } else {
                    validator.setText("Contraseñas no coinciden");
                    validator.setTextColor(Color.RED);
                    validator.setVisibility(View.VISIBLE);
                    verificarRepeticion = false;
                    repeatNewPass.setTextColor(Color.RED);
                    confirmation_new_pass.setBackgroundResource(R.drawable.button_selector_disable);
                    confirmation_new_pass.setText("X");
                    confirmation_new_pass.setTextColor(getResources().getColor(R.color.rojo_glup));
                    confirmation_new_pass.setEnabled(false);

                    Log.e("contras", "diferentes");
                }
            }
        });

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_change_confirmation:
                Log.e("repeticion",String.valueOf(verificarRepeticion));
                if (verificarRepeticion){
                    ///
                    Log.e("verificarRep",String.valueOf(verificarRepeticion));
                    DSUsuario dsUsuario = new DSUsuario(getActivity());
                    try {
                        dsUsuario.setOnSuccessUpdatePass((OnSuccessUpdatePass) context);
                        dsUsuario.updatePassUsuario(newPass.getText().toString(), passw.getText().toString());

                    }catch (ClassCastException e){
                        Log.e(null,e.toString());
                    }
                    ///
                }
                validator.setVisibility(View.INVISIBLE);
                dismiss();
                break;
        }
    }


    public boolean verificarRep(){
        if (passw.getText().toString().equals(repeatNewPass.getText().toString()))
            return true;
        else
            return false;
    }
}
