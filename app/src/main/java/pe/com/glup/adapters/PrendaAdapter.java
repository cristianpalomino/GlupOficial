package pe.com.glup.adapters;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pe.com.glup.R;
import pe.com.glup.beans.Prenda;
import pe.com.glup.bus.BusHolder;
import pe.com.glup.datasource.DSProbador;
import pe.com.glup.glup.Detalle;
import pe.com.glup.glup.DetalleNew;
import pe.com.glup.glup.Glup;
import pe.com.glup.glup.Principal;
import pe.com.glup.utils.Util_Fonts;
/**
 * Created by Glup on 18/09/15.
 */
public class PrendaAdapter extends BaseAdapter implements View.OnLongClickListener{

    private ArrayList<Prenda> mPrendas;
    private Context context;
    private LayoutInflater inflater;
    private ViewGroup viewGroup;
    private int position;
    private String codPrenda;
    private String tipo;
    private Glup glup;
    static final int SHORT_DELAY = 1000;

    public PrendaAdapter(Context context, ArrayList<Prenda> prendas) {
        this.context = context;
        this.mPrendas = prendas;
        this.inflater = LayoutInflater.from(context);
        BusHolder.getInstance().register(this);
    }

    @Override
    public int getCount() {
        return mPrendas.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return mPrendas.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        final Prenda prenda = mPrendas.get(position);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.item_catalogo, parent, false);

            //convertView.setTag(holder);
        } else {
            //  holder = (Holder) convertView.getTag();
        }
        Log.e("Despues", "codigoPrenda:" + prenda.getCod_prenda() + " ,indProbador:" + prenda.getIndProbador() + " ," +
                "numMeGusta:" + prenda.getNumGusta());

        holder = new Holder();

        holder.marca = (TextView) convertView.findViewById(R.id.item_marca_prenda);
        holder.precio = (TextView) convertView.findViewById(R.id.precio_prenda);
        holder.contado = (TextView) convertView.findViewById(R.id.contador_corazon);
        holder.modelo = (TextView) convertView.findViewById(R.id.modelo_prenda);
        holder.imagen = (ImageView) convertView.findViewById(R.id.item_imagen_prenda);
        holder.check = (CheckBox) convertView.findViewById(R.id.check);
        holder.corazon = (ToggleButton) convertView.findViewById(R.id.corazon_prenda);

        holder.precio.setVisibility(View.GONE);
        holder.contado.setVisibility(View.GONE);



        //notifyDataSetChanged();
        boolean checked = prenda.getIndProbador().equals("1");
        holder.corazon.setChecked(checked);
        //notifyDataSetChanged();


        glup = (Glup) context;

        this.position=position;
        this.viewGroup=parent;
        this.codPrenda=prenda.getCod_prenda();
        this.tipo=prenda.getTipo();


        holder.marca.setText(prenda.getMarca());
        holder.modelo.setText(prenda.getTipo());

        //Log.e("fragmento",this.context.getApplicationContext().);


        holder.marca.setTypeface(Util_Fonts.setBold(context));
        holder.modelo.setTypeface(Util_Fonts.setRegular(context));



        Picasso.with(context).load(prenda.getImagen()).fit().placeholder(R.drawable.progress_animator).centerInside().noFade().into(holder.imagen);

//        boolean checked = prenda.getIndProbador().equals("1");
//        holder.check.setChecked(checked);

        //holder.imagen.setOnLongClickListener(this);


        final String checkUpdated=prenda.getIndProbador();
        //final View finalConvertView = convertView;
        //
        final View finalConvertView = convertView;
        final Prenda finalprenda = prenda;

        final Holder finalHolder = holder;
/*
        holder.corazon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                Log.e("checkChange", String.valueOf(isChecked));
                if (isChecked) {
                    holder.contado.setText(String.valueOf(cont - 1));
                    holder.corazon.setChecked(false);
                } else {
                    holder.contado.setText(String.valueOf(cont + 1));
                    holder.corazon.setChecked(true);
                }
                notifyDataSetChanged();


            }
        });*/
        holder.corazon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int action=0;//1 agregando 2 eliminando
                if (holder.corazon.isChecked()==true) { //los que eran falsos
                    mPrendas.get(position).setIndProbador("1");
                    holder.corazon.setChecked(true);
                    Log.e("corazon:", prenda.getCod_prenda());
                    //Toast.makeText(context, "Se agrego al Probador", SHORT_DELAY).show();
                    action=1;
                } else {
                    mPrendas.get(position).setIndProbador("0");
                    Log.e("sincorazon:", prenda.getCod_prenda());
                    holder.corazon.setChecked(false);
                    //Toast.makeText(context, "Se elimino del Probador", SHORT_DELAY).show();
                    action=2;
                }

                DSProbador dsProbador = new DSProbador(finalConvertView.getContext());
                dsProbador.setIndProbador(finalprenda.getCod_prenda(),action);
                BusHolder.getInstance().post(holder);
            }
        });
        return convertView;
    }

    @Override
    public boolean onLongClick(View v) {
        Log.e("Prenda:", "posicion " + position + " codigo:" +
                codPrenda + " Tipo:" +
                tipo);
        return false;
    }

    public class Holder {
        public TextView marca;
        public TextView contado;
        public TextView modelo;
        public TextView precio;
        public ImageView imagen;
        public CheckBox check;
        public ToggleButton corazon;
    }

    public ArrayList<Prenda> getmPrendas() {
        return mPrendas;
    }

    public void add(Prenda prenda) {
        mPrendas.add(prenda);
        notifyDataSetChanged();
    }
}