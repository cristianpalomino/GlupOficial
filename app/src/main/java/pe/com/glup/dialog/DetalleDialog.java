package pe.com.glup.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import pe.com.glup.R;
import pe.com.glup.beans.Prenda;
import pe.com.glup.datasource.DSInfo;
import pe.com.glup.glup.Glup;
import pe.com.glup.interfaces.OnSuccessInfo;
import pe.com.glup.session.Session_Manager;
import pe.com.glup.utils.Util_Fonts;

/**
 * Created by cristian on 8/07/15.
 */
public class DetalleDialog extends AlertDialog {

    private TextView tienda;
    private TextView marca;
    private TextView tipo;
    private TextView modelo;
    private TextView estilo;
    private TextView precio;
    private Prenda prenda;

    public DetalleDialog(Context context) {
        super(context);
        initDialog();
    }

    protected DetalleDialog(Context context, int theme) {
        super(context, theme);
        initDialog();
    }

    protected DetalleDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initDialog();
    }



    private void initDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.dialog_detalle, null);
        setView(view);
        tienda = (TextView) view.findViewById(R.id.tienda);
        marca = (TextView) view.findViewById(R.id.marca);
        tipo = (TextView) view.findViewById(R.id.tipo);
        modelo = (TextView) view.findViewById(R.id.modelo);
        estilo = (TextView) view.findViewById(R.id.estilo);
        precio = (TextView) view.findViewById(R.id.precio);


        tienda.setTypeface(Util_Fonts.setRegular(getContext()));
        marca.setTypeface(Util_Fonts.setRegular(getContext()));
        tipo.setTypeface(Util_Fonts.setRegular(getContext()));
        modelo.setTypeface(Util_Fonts.setRegular(getContext()));
        precio.setTypeface(Util_Fonts.setRegular(getContext()));
        estilo.setTypeface(Util_Fonts.setRegular(getContext()));


        /*
        Aca declaras el DSINFO
         */


        DSInfo dsInfo= new DSInfo(getContext());
        dsInfo.getInfoPrenda("000000000002");
        dsInfo.setOnSuccessInfo((OnSuccessInfo) DetalleDialog.this);

        getWindow().setWindowAnimations(R.style.Dialog_Animation_UP_DOWN);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
    }


   public void onSuccess(ArrayList<Prenda> prenda) {
        this.precio.setText(prenda.get(0).getPrecio());
        this.marca.setText(prenda.get(0).getMarca());
        this.tipo.setText(prenda.get(0).getTipo());
        this.estilo.setText(prenda.get(0).getEstilo());
        this.modelo.setText(prenda.get(0).getModelo());
    }


    public void onFailed(String error_msg) {

    }



}