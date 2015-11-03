package pe.com.glup.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import pe.com.glup.R;

/**
 * Created by Glup on 30/10/15.
 */
public class GlupDialogNew extends DialogFragment {
    private String msj="";
    private Context context=null;
    public GlupDialogNew() {
    }

    public GlupDialogNew(Context context) {
        this.context = context;
    }

    public GlupDialogNew(String msj){
        this.msj=msj;
    }

    public GlupDialogNew(String msj, Context context) {
        this.msj = msj;
        this.context = context;
    }

    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstance){
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        View view = inflater.inflate(R.layout.glup_dialog,
                container,false);
        TextView textView= (TextView) view.findViewById(R.id.message_dialog);
        //if (msj.equals("")){
            textView.setVisibility(View.GONE);
        /*}else {
            textView.setText(msj);
            textView.setVisibility(View.VISIBLE);
        }*/
        return view;
    }

    public void onResume(){
        super.onResume();
        Window window = getDialog().getWindow();
        if (context!=null){
         window.setLayout((int)convertPixelsToDp(500, context),(int)convertPixelsToDp(500,context));
            Log.e("entro","convert to dp"+(int)convertPixelsToDp(500,context));
        }else {
            window.setLayout(200,200);
        }
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
    public  float convertDpToPixel(float dp, Context context){
        float px = dp * (context.getResources().getDisplayMetrics().densityDpi/160);
        return px;
    }

    public float convertPixelsToDp(float px, Context context){
        float dp = px / (context.getResources().getDisplayMetrics().densityDpi /160);
        return dp;
    }
}
