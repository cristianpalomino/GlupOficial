package pe.com.glup.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import pe.com.glup.R;

/**
 * Created by Glup on 30/10/15.
 */
public class GlupDialogNew extends DialogFragment {
    public GlupDialogNew() {
    }

    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstance){
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        View view = inflater.inflate(R.layout.glup_dialog,
                container,false);
        return view;
    }

    public void onResume(){
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(150,150);
        window.setGravity(Gravity.CENTER);
    }
/*
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        return createDialog();
    }

    private Dialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.glup_dialog,null);
        builder.setView(v);
        AlertDialog alertDialog=builder.create();
        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
        params.x = -20;
        params.height = 200;
        params.width = 100;
        params.y = -10;

        alertDialog.getWindow().setAttributes(params);

        return alertDialog;
    }*/
}
