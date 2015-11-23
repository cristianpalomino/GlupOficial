package pe.com.glup.views;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import pe.com.glup.R;

/**
 * Created by Glup on 21/11/15.
 */
public class MessageV2 extends DialogFragment {
    private String msj="";
    public  MessageV2(String msj){
        this.msj=msj;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        return createDialog();
    }

    private AlertDialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= getActivity().getLayoutInflater();
        View v= inflater.inflate(R.layout.dialog_message, null);
        TextView message=(TextView)v.findViewById(R.id.message);
        message.setText(msj);
        builder.setView(v);
        return builder.create();
    }
}
