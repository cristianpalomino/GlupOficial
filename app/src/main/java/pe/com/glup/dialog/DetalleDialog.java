package pe.com.glup.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import pe.com.glup.R;
import pe.com.glup.models.Prenda;
import pe.com.glup.network.DSInfo;
import pe.com.glup.models.interfaces.OnSuccessInfo;
import pe.com.glup.utils.Util_Fonts;

/**
 * Created by cristian on 8/07/15.
 */
public class DetalleDialog extends AlertDialog implements OnSuccessInfo {

    private TextView tienda;
    private TextView marca;
    private TextView tipo;
    private TextView modelo;
    private TextView estilo;
    private TextView precio;



    public void setCodigo_prenda(String codigo_prenda) {
        this.codigo_prenda = codigo_prenda;
    }

    private String codigo_prenda;

    public DetalleDialog(Context context) {
        super(context);
    }

    protected DetalleDialog(Context context, int theme) {
        super(context, theme);
    }

    protected DetalleDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    public void initDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.dialog_detalle, null);
        setView(view);
        //tienda = (TextView) view.findViewById(R.id.tienda);
        marca = (TextView) view.findViewById(R.id.marca);
        tipo = (TextView) view.findViewById(R.id.tipo);
        modelo = (TextView) view.findViewById(R.id.modelo);
        estilo = (TextView) view.findViewById(R.id.estilo);
        precio = (TextView) view.findViewById(R.id.precio);


        //tienda.setTypeface(Util_Fonts.setRegular(getContext()));
        marca.setTypeface(Util_Fonts.setRegular(getContext()));
        tipo.setTypeface(Util_Fonts.setRegular(getContext()));
        modelo.setTypeface(Util_Fonts.setRegular(getContext()));
        precio.setTypeface(Util_Fonts.setRegular(getContext()));
        estilo.setTypeface(Util_Fonts.setRegular(getContext()));

        //*** Se Declara el Servicio ****
            DSInfo dsInfo= new DSInfo(getContext());
            dsInfo.getInfoPrenda(codigo_prenda);
            dsInfo.setOnSuccessInfo(DetalleDialog.this);



        getWindow().setWindowAnimations(R.style.Dialog_Animation_UP_DOWN);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));

    }


   public void onSuccess(Prenda prenda) {
            //** Validacion si en caso la variable llega vacia
            this.precio.setText(""+ (prenda.getPrecio()==null || prenda.getPrecio().trim()=="" ? "Sin Información":prenda.getPrecio()));
            this.marca.setText("" + (prenda.getMarca()==null || prenda.getMarca().trim()=="" ? "Sin Información":prenda.getMarca()));
            this.tipo.setText(""+(prenda.getTipo()==null || prenda.getTipo().trim()=="" ? "Sin Información":prenda.getTipo()));
            this.estilo.setText(""+(prenda.getEstilo()==null || prenda.getEstilo().trim()=="" ? "Sin Información":prenda.getEstilo()));
            this.modelo.setText("" + (prenda.getModelo()==null || prenda.getModelo().trim()=="" ? "Sin Información":prenda.getModelo()));
    }

    // ** Metodo no utilzado
    public void onFailed(String error_msg) {

    }





}